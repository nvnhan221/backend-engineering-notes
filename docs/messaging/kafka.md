# Kafka

Apache Kafka for event streaming and messaging.

## Overview

Apache Kafka is a distributed event streaming platform capable of handling trillions of events per day. It's designed to handle high-throughput, low-latency real-time data feeds.

## Key Concepts

### Topics

Topics are categories or feeds to which records are published. A topic is identified by its name.

### Partitions

Topics are split into partitions for parallelism and scalability. Each partition is an ordered, immutable sequence of records.

### Producers

Applications that publish data to topics. Producers choose which partition to send data to.

### Consumers

Applications that read data from topics. Consumers read from one or more partitions.

### Consumer Groups

Multiple consumers working together to consume a topic. Each consumer in a group reads from a different partition.

### Brokers

Kafka cluster consists of one or more servers (brokers). Each broker can handle terabytes of data.

### Replication

Partitions are replicated across multiple brokers for fault tolerance.

## Installation

### Using Docker

```bash
docker run -d \
  --name kafka \
  -p 9092:9092 \
  -e KAFKA_ZOOKEEPER_CONNECT=localhost:2181 \
  apache/kafka:latest
```

### Using Docker Compose

```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
```

### Local Installation

1. Download Kafka from [kafka.apache.org](https://kafka.apache.org/downloads)
2. Extract and navigate to the directory
3. Start Zookeeper:
   ```bash
   bin/zookeeper-server-start.sh config/zookeeper.properties
   ```
4. Start Kafka:
   ```bash
   bin/kafka-server-start.sh config/server.properties
   ```

## Architecture

### Cluster Architecture

```
┌─────────────┐
│  Producer   │
└──────┬──────┘
       │
       ▼
┌─────────────────┐
│  Kafka Broker   │──┐
│  (Partition 0)  │  │
└─────────────────┘  │
                     │ Replication
┌─────────────────┐  │
│  Kafka Broker   │──┘
│  (Partition 1)  │
└──────┬──────────┘
       │
       ▼
┌─────────────┐
│  Consumer   │
│   Group     │
└─────────────┘
```

### Partitioning

- Messages with the same key go to the same partition
- Ordering is guaranteed within a partition
- Parallelism is achieved through multiple partitions

### Replication Factor

- **Replication Factor = 1**: No redundancy (not recommended for production)
- **Replication Factor = 3**: Standard for production (can tolerate 2 broker failures)

### In-Sync Replicas (ISR)

ISR are replicas that are caught up with the leader. Only ISR can become leaders.

## Basic Usage

### Producer Example (Python)

```python
from kafka import KafkaProducer
import json

producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

producer.send('my-topic', {'key': 'value'})
producer.flush()
```

### Consumer Example (Python)

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

### Java Examples

See [Kafka Java Examples](../examples/kafka-java.md) for complete Java implementations.

## Advanced Concepts

### Message Keys

Using keys ensures messages with the same key go to the same partition:

```python
producer.send('my-topic', key=b'user-123', value={'action': 'login'})
```

### Consumer Offsets

- Kafka stores the offset of the last consumed message
- Consumers can start from `earliest` or `latest`
- Manual offset management for exactly-once processing

### Consumer Groups

```python
# Multiple consumers in the same group share partitions
consumer1 = KafkaConsumer('my-topic', group_id='my-group')
consumer2 = KafkaConsumer('my-topic', group_id='my-group')
# Each consumer reads from different partitions
```

### Exactly-Once Semantics

- Enable idempotent producer
- Use transactional producer
- Read committed messages only

## Configuration

### Producer Configuration

```python
producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    acks='all',  # Wait for all replicas to acknowledge
    retries=3,
    max_in_flight_requests_per_connection=1,
    enable_idempotence=True,  # Exactly-once semantics
    compression_type='snappy',
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)
```

### Consumer Configuration

```python
consumer = KafkaConsumer(
    'my-topic',
    bootstrap_servers=['localhost:9092'],
    group_id='my-group',
    auto_offset_reset='earliest',  # or 'latest'
    enable_auto_commit=False,  # Manual commit for reliability
    max_poll_records=100,
    session_timeout_ms=30000,
    heartbeat_interval_ms=10000
)
```

### Important Settings

| Setting | Description | Recommended Value |
|---------|-------------|-------------------|
| `acks` | Producer acknowledgment | `all` for reliability |
| `retries` | Number of retries | `3` or higher |
| `enable_idempotence` | Exactly-once semantics | `True` |
| `auto_offset_reset` | Where to start reading | `earliest` or `latest` |
| `enable_auto_commit` | Auto commit offsets | `False` for reliability |

## Best Practices

### Partitioning Strategy

- Use meaningful keys for partitioning
- Balance partition count with consumer parallelism
- Consider partition size limits (recommended: < 2GB per partition)
- More partitions = more parallelism but more overhead

### Producer Best Practices

- Use idempotent producer for exactly-once
- Set appropriate `acks` based on requirements
- Use compression (snappy, gzip, lz4)
- Batch messages for better throughput
- Handle errors and retries properly

### Consumer Best Practices

- Use consumer groups for parallel processing
- Commit offsets manually for reliability
- Handle rebalancing gracefully
- Monitor consumer lag
- Set appropriate `max_poll_records`

### Error Handling

- Implement retry logic with exponential backoff
- Use dead letter topics for failed messages
- Monitor consumer lag
- Handle deserialization errors
- Log errors for debugging

## Monitoring

### Key Metrics

- **Throughput**: Messages per second
- **Latency**: End-to-end message latency
- **Consumer Lag**: Delay between producer and consumer
- **Broker Metrics**: CPU, memory, disk I/O
- **Partition Count**: Number of partitions per topic

### Tools

- **Kafka Manager** - Web-based management
- **Confluent Control Center** - Enterprise monitoring
- **Kafka Exporter** - Prometheus metrics
- **Burrow** - Consumer lag monitoring

## Performance Tuning

### Producer Tuning

```python
producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    batch_size=16384,  # Increase batch size
    linger_ms=10,  # Wait for batch
    compression_type='snappy',
    buffer_memory=33554432  # 32MB buffer
)
```

### Consumer Tuning

```python
consumer = KafkaConsumer(
    'my-topic',
    bootstrap_servers=['localhost:9092'],
    fetch_min_bytes=1,
    fetch_max_wait_ms=500,
    max_partition_fetch_bytes=1048576  # 1MB per partition
)
```

## Use Cases

- **Event Sourcing** - Store all events as a log
- **Stream Processing** - Real-time data processing
- **Log Aggregation** - Centralized logging
- **Metrics Collection** - Collecting metrics from multiple sources
- **Commit Log** - Database replication
- **Message Queue** - Decoupling producers and consumers

## Common Patterns

### Outbox Pattern

Ensure database writes and Kafka publishes happen atomically. See [Kafka Outbox Pattern](../examples/kafka-outbox.md).

### CQRS

Command Query Responsibility Segregation using Kafka as event store.

### Event Sourcing

Store all state changes as events in Kafka.

## Troubleshooting

### Consumer Lag

- Increase consumer instances
- Optimize processing logic
- Check network latency
- Review partition count

### Message Loss

- Set `acks='all'` in producer
- Use replication factor >= 3
- Monitor ISR status
- Check broker disk space

### Performance Issues

- Increase partition count
- Tune batch sizes
- Use compression
- Optimize serialization

## Further Reading

- [Kafka Java Examples](../examples/kafka-java.md) - Complete Java implementations
- [Kafka Outbox Pattern](../examples/kafka-outbox.md) - Transactional outbox pattern
- [Retries and DLQ](retries-dlq.md) - Error handling patterns

## Resources

- [Official Documentation](https://kafka.apache.org/documentation/)
- [Confluent Documentation](https://docs.confluent.io/)
- [Kafka Best Practices](https://kafka.apache.org/documentation/#bestPractices)

---

*Last updated: {{ git_revision_date_localized }}*
