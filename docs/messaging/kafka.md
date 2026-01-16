# Kafka

Apache Kafka for event streaming and messaging.

## Overview

Apache Kafka is a distributed event streaming platform capable of handling trillions of events per day.

## Key Concepts

### Topics

Topics are categories or feeds to which records are published.

### Partitions

Topics are split into partitions for parallelism and scalability.

### Producers

Applications that publish data to topics.

### Consumers

Applications that read data from topics.

### Consumer Groups

Multiple consumers working together to consume a topic.

## Basic Usage

### Producer Example

```python
from kafka import KafkaProducer

producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

producer.send('my-topic', {'key': 'value'})
producer.flush()
```

### Consumer Example

```python
from kafka import KafkaConsumer
import json

consumer = KafkaConsumer(
    'my-topic',
    bootstrap_servers=['localhost:9092'],
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
    group_id='my-group'
)

for message in consumer:
    print(f"Received: {message.value}")
```

## Best Practices

### Partitioning Strategy

- Use meaningful keys for partitioning
- Balance partition count with consumer parallelism
- Consider partition size limits

### Consumer Configuration

```python
consumer = KafkaConsumer(
    'my-topic',
    bootstrap_servers=['localhost:9092'],
    group_id='my-group',
    auto_offset_reset='earliest',  # or 'latest'
    enable_auto_commit=False,  # Manual commit for reliability
    max_poll_records=100
)
```

### Error Handling

- Implement retry logic with exponential backoff
- Use dead letter topics for failed messages
- Monitor consumer lag

## Use Cases

- **Event Sourcing** - Store all events as a log
- **Stream Processing** - Real-time data processing
- **Log Aggregation** - Centralized logging
- **Metrics Collection** - Collecting metrics from multiple sources

## Further Reading

- [Kafka Outbox Pattern](../examples/kafka-outbox.md)
- [Retries and DLQ](retries-dlq.md)

---

*Last updated: {{ git_revision_date_localized }}*
