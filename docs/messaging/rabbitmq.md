# RabbitMQ

RabbitMQ message broker patterns and best practices.

## Overview

RabbitMQ is a message broker that implements the Advanced Message Queuing Protocol (AMQP).

## Key Concepts

### Exchanges

Exchanges receive messages and route them to queues.

### Queues

Queues store messages until consumed.

### Bindings

Bindings connect exchanges to queues with routing rules.

### Routing Keys

Keys used to route messages from exchanges to queues.

## Exchange Types

### Direct Exchange

Routes messages to queues based on exact routing key match.

### Topic Exchange

Routes messages using pattern matching on routing keys.

### Fanout Exchange

Broadcasts messages to all bound queues.

### Headers Exchange

Routes based on message headers instead of routing keys.

## Basic Usage

### Producer Example

```python
import pika

connection = pika.BlockingConnection(
    pika.ConnectionParameters('localhost')
)
channel = connection.channel()

channel.queue_declare(queue='hello')

channel.basic_publish(
    exchange='',
    routing_key='hello',
    body='Hello World!'
)

connection.close()
```

### Consumer Example

```python
import pika

connection = pika.BlockingConnection(
    pika.ConnectionParameters('localhost')
)
channel = connection.channel()

channel.queue_declare(queue='hello')

def callback(ch, method, properties, body):
    print(f"Received: {body.decode()}")

channel.basic_consume(
    queue='hello',
    on_message_callback=callback,
    auto_ack=True
)

channel.start_consuming()
```

## Best Practices

### Message Acknowledgment

```python
def callback(ch, method, properties, body):
    try:
        process_message(body)
        ch.basic_ack(delivery_tag=method.delivery_tag)
    except Exception as e:
        ch.basic_nack(delivery_tag=method.delivery_tag, requeue=True)
```

### Durable Queues

```python
channel.queue_declare(
    queue='my-queue',
    durable=True  # Survives broker restart
)
```

### Prefetch Count

```python
channel.basic_qos(prefetch_count=1)  # Fair dispatch
```

## Use Cases

- **Task Queues** - Background job processing
- **Work Queues** - Distributing work across workers
- **Pub/Sub** - Broadcasting messages
- **RPC** - Request-reply pattern

## Further Reading

- [Retries and DLQ](retries-dlq.md)
- [RabbitMQ Retry DLX Pattern](../examples/rabbitmq-retry-dlx.md)

---

*Last updated: {{ git_revision_date_localized }}*
