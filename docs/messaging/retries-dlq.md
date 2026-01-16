# Retries and Dead Letter Queues

Handling message processing failures with retries and dead letter queues.

## Overview

When message processing fails, we need strategies to handle retries and eventually move messages to a dead letter queue (DLQ) for manual inspection.

## Retry Strategies

### Exponential Backoff

Gradually increase delay between retries:

```python
import time
from functools import wraps

def exponential_backoff(max_retries=5):
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            for attempt in range(max_retries):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    if attempt == max_retries - 1:
                        raise
                    delay = 2 ** attempt
                    time.sleep(delay)
            return None
        return wrapper
    return decorator
```

### Fixed Delay

Retry with constant delay between attempts.

### Immediate Retry

Retry immediately (use with caution).

## Dead Letter Queues

Messages that fail after all retries are moved to a DLQ.

### RabbitMQ DLX

```python
# Declare DLX
channel.exchange_declare(
    exchange='dlx',
    exchange_type='direct'
)

# Declare DLQ
channel.queue_declare(
    queue='dlq',
    durable=True
)

# Bind DLQ to DLX
channel.queue_bind(
    exchange='dlx',
    queue='dlq',
    routing_key='failed'
)

# Declare main queue with DLX
channel.queue_declare(
    queue='main-queue',
    durable=True,
    arguments={
        'x-dead-letter-exchange': 'dlx',
        'x-dead-letter-routing-key': 'failed',
        'x-message-ttl': 60000  # 60 seconds
    }
)
```

### Kafka Dead Letter Topic

```python
from kafka import KafkaProducer, KafkaConsumer

# Producer for DLQ
dlq_producer = KafkaProducer(
    bootstrap_servers=['localhost:9092']
)

# Consumer with DLQ handling
consumer = KafkaConsumer(
    'main-topic',
    bootstrap_servers=['localhost:9092'],
    group_id='my-group'
)

for message in consumer:
    try:
        process_message(message.value)
        consumer.commit()
    except Exception as e:
        retry_count = get_retry_count(message)
        if retry_count >= MAX_RETRIES:
            # Send to DLQ
            dlq_producer.send('dlq-topic', message.value)
        else:
            # Retry logic
            retry_message(message, retry_count + 1)
```

## Best Practices

### Retry Limits

Set maximum retry attempts to prevent infinite loops.

### Monitoring

Monitor DLQ size and investigate failed messages regularly.

### Alerting

Set up alerts when DLQ grows beyond threshold.

### Manual Review

Regularly review DLQ messages to identify patterns.

## Patterns

### Outbox Pattern

Use transactional outbox to ensure message delivery.

### Saga Pattern

Handle distributed transactions with compensating actions.

## Further Reading

- [Kafka Outbox Pattern](../examples/kafka-outbox.md)
- [RabbitMQ Retry DLX Pattern](../examples/rabbitmq-retry-dlx.md)
- [Idempotency](../foundations/idempotency.md)

---

*Last updated: {{ git_revision_date_localized }}*
