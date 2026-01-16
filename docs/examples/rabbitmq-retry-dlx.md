# RabbitMQ Retry with Dead Letter Exchange

Implementing retry logic with RabbitMQ using Dead Letter Exchanges (DLX).

## Overview

This pattern uses RabbitMQ's Dead Letter Exchange feature to implement automatic retries with exponential backoff.

## Architecture

```
Main Queue → Process → Success
              │
              └──► Failure
                    │
                    └──► Retry Queue (with TTL)
                          │
                          └──► After TTL expires
                                │
                                └──► Back to Main Queue
                                      │
                                      └──► Max retries exceeded
                                            │
                                            └──► Dead Letter Queue
```

## Implementation

### Queue Setup

```python
import pika

connection = pika.BlockingConnection(
    pika.ConnectionParameters('localhost')
)
channel = connection.channel()

# Dead Letter Exchange
channel.exchange_declare(
    exchange='dlx',
    exchange_type='direct',
    durable=True
)

# Dead Letter Queue
channel.queue_declare(
    queue='dlq',
    durable=True
)

channel.queue_bind(
    exchange='dlx',
    queue='dlq',
    routing_key='failed'
)

# Retry Exchange
channel.exchange_declare(
    exchange='retry-exchange',
    exchange_type='direct',
    durable=True
)

# Retry Queue (with TTL and DLX)
channel.queue_declare(
    queue='retry-queue',
    durable=True,
    arguments={
        'x-dead-letter-exchange': '',
        'x-dead-letter-routing-key': 'main-queue',
        'x-message-ttl': 5000  # 5 seconds
    }
)

channel.queue_bind(
    exchange='retry-exchange',
    queue='retry-queue',
    routing_key='retry'
)

# Main Queue
channel.queue_declare(
    queue='main-queue',
    durable=True
)
```

### Consumer with Retry Logic

```python
import json
import pika

MAX_RETRIES = 3

def process_message(ch, method, properties, body):
    try:
        data = json.loads(body)
        
        # Simulate processing
        result = process_data(data)
        
        # Acknowledge on success
        ch.basic_ack(delivery_tag=method.delivery_tag)
        
    except Exception as e:
        # Get retry count from headers
        retry_count = properties.headers.get('x-retry-count', 0) if properties.headers else 0
        
        if retry_count < MAX_RETRIES:
            # Increment retry count
            new_headers = properties.headers.copy() if properties.headers else {}
            new_headers['x-retry-count'] = retry_count + 1
            
            # Calculate exponential backoff
            ttl = 1000 * (2 ** retry_count)  # 1s, 2s, 4s, ...
            
            # Update retry queue TTL
            channel.queue_declare(
                queue='retry-queue',
                durable=True,
                arguments={
                    'x-dead-letter-exchange': '',
                    'x-dead-letter-routing-key': 'main-queue',
                    'x-message-ttl': ttl
                }
            )
            
            # Publish to retry queue
            channel.basic_publish(
                exchange='retry-exchange',
                routing_key='retry',
                body=body,
                properties=pika.BasicProperties(
                    headers=new_headers,
                    delivery_mode=2  # Persistent
                )
            )
            
            # Acknowledge original message
            ch.basic_ack(delivery_tag=method.delivery_tag)
        else:
            # Max retries exceeded, send to DLQ
            channel.basic_publish(
                exchange='dlx',
                routing_key='failed',
                body=body,
                properties=pika.BasicProperties(
                    headers=properties.headers,
                    delivery_mode=2
                )
            )
            
            # Acknowledge original message
            ch.basic_ack(delivery_tag=method.delivery_tag)

# Start consuming
channel.basic_qos(prefetch_count=1)
channel.basic_consume(
    queue='main-queue',
    on_message_callback=process_message
)

channel.start_consuming()
```

## Alternative: Multiple Retry Queues

For more control, use separate queues for each retry attempt:

```python
# Retry queues with different TTLs
retry_queues = [
    {'name': 'retry-1', 'ttl': 1000},   # 1 second
    {'name': 'retry-2', 'ttl': 5000},   # 5 seconds
    {'name': 'retry-3', 'ttl': 30000},  # 30 seconds
]

for queue in retry_queues:
    channel.queue_declare(
        queue=queue['name'],
        durable=True,
        arguments={
            'x-dead-letter-exchange': '',
            'x-dead-letter-routing-key': 'main-queue',
            'x-message-ttl': queue['ttl']
        }
    )
```

## Benefits

- ✅ **Automatic retries** - No manual intervention
- ✅ **Exponential backoff** - Reduces load on failing services
- ✅ **Dead letter handling** - Failed messages don't get lost
- ✅ **Configurable** - Adjust retry count and delays

## Monitoring

- Monitor DLQ size
- Track retry rates
- Alert on high failure rates
- Review DLQ messages regularly

## Further Reading

- [RabbitMQ](../messaging/rabbitmq.md)
- [Retries and DLQ](../messaging/retries-dlq.md)
- [Examples Code](https://github.com/nvnhan221/backend-engineering-notes/tree/main/examples/rabbitmq-retry-dlx)

---

*Last updated: {{ git_revision_date_localized }}*
