# Kafka Java Examples

Complete, runnable Kafka examples using Java with the official Kafka client library.

## Prerequisites

- Java 8 or higher
- Maven 3.6+ (or Gradle)
- Kafka cluster running (see setup below)

## Quick Start

### 1. Start Kafka

Using Docker:

```bash
docker-compose up -d
```

Or start Kafka manually:

```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh config/server.properties
```

### 2. Create Topic

```bash
kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 3 \
  --topic my-topic
```

### 3. Build Project

```bash
mvn clean compile
```

### 4. Run Examples

```bash
# Run producer
mvn exec:java -Dexec.mainClass="com.example.kafka.SimpleProducer"

# Run consumer (in another terminal)
mvn exec:java -Dexec.mainClass="com.example.kafka.SimpleConsumer"
```

## Project Structure

```
kafka-java/
├── README.md
├── pom.xml
├── docker-compose.yml
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── kafka/
                        ├── SimpleProducer.java
                        ├── ProducerWithCallback.java
                        ├── SimpleConsumer.java
                        ├── ManualCommitConsumer.java
                        ├── ConsumerGroupExample.java
                        ├── PartitionedProducer.java
                        └── ProducerWithErrorHandling.java
```

## Examples Included

### Producers

1. **SimpleProducer** - Basic producer sending messages
2. **ProducerWithCallback** - Producer with async callback
3. **PartitionedProducer** - Producer with partition control
4. **ProducerWithErrorHandling** - Error handling and retries

### Consumers

1. **SimpleConsumer** - Basic consumer reading messages
2. **ManualCommitConsumer** - Consumer with manual offset commit
3. **ConsumerGroupExample** - Multiple consumers in a group

## Configuration

### Producer Configuration

Key settings for production:

- `acks=all` - Wait for all replicas
- `retries=3` - Retry failed sends
- `enable.idempotence=true` - Exactly-once semantics
- `compression.type=snappy` - Compress messages

### Consumer Configuration

Key settings for production:

- `enable.auto.commit=false` - Manual commit for reliability
- `auto.offset.reset=earliest` - Start from beginning
- `max.poll.records=100` - Batch size

## Best Practices

1. **Use Idempotent Producer** - Prevents duplicate messages
2. **Manual Commit** - Commit after processing, not before
3. **Error Handling** - Always handle exceptions
4. **Consumer Groups** - Use groups for parallel processing
5. **Monitoring** - Monitor consumer lag and throughput

## Troubleshooting

### Consumer Not Receiving Messages

- Check consumer group ID
- Verify topic exists
- Check `auto.offset.reset` setting
- Ensure consumer is subscribed to correct topic

### Producer Errors

- Check broker connectivity
- Verify topic exists
- Check serialization classes
- Review error messages in callback

## Further Reading

- [Kafka Java Documentation](../docs/examples/kafka-java.md) - Detailed guide
- [Kafka Guide](../docs/messaging/kafka.md) - Complete Kafka documentation
