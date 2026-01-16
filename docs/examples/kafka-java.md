# Kafka Java Examples

Complete Kafka examples using Java with the official Kafka client library.

## Overview

This guide provides complete, production-ready examples for using Apache Kafka with Java, including producers, consumers, and consumer groups.

## Prerequisites

- Java 8 or higher
- Maven 3.6+
- Kafka cluster running (local or remote)

## Project Setup

### Maven Dependencies

Add to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>3.5.0</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.7</version>
    </dependency>
</dependencies>
```

### Gradle Dependencies

Add to your `build.gradle`:

```gradle
dependencies {
    implementation 'org.apache.kafka:kafka-clients:3.5.0'
    implementation 'org.slf4j:slf4j-simple:2.0.7'
}
```

## Basic Producer

### Simple Producer

```java
package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class SimpleProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(props);

        try {
            for (int i = 0; i < 10; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    "my-topic",
                    "key-" + i,
                    "value-" + i
                );
                producer.send(record);
                System.out.println("Sent: " + record);
            }
        } finally {
            producer.close();
        }
    }
}
```

### Producer with Callback

```java
package com.example.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

public class ProducerWithCallback {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Wait for all replicas
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Exactly-once

        Producer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record = new ProducerRecord<>(
            "my-topic",
            "key-1",
            "Hello Kafka!"
        );

        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println("Successfully sent to topic: " + metadata.topic() +
                                     ", partition: " + metadata.partition() +
                                     ", offset: " + metadata.offset());
                } else {
                    System.err.println("Error sending message: " + exception.getMessage());
                }
            }
        });

        producer.flush();
        producer.close();
    }
}
```

### Producer with Custom Serializer

```java
package com.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonSerializer<T> implements Serializer<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configuration if needed
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing JSON", e);
        }
    }

    @Override
    public void close() {
        // Cleanup if needed
    }
}
```

## Basic Consumer

### Simple Consumer

```java
package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class SimpleConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("my-topic"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Topic: %s, Partition: %d, Offset: %d, Key: %s, Value: %s%n",
                        record.topic(),
                        record.partition(),
                        record.offset(),
                        record.key(),
                        record.value()
                    );
                }
            }
        } finally {
            consumer.close();
        }
    }
}
```

### Consumer with Manual Commit

```java
package com.example.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ManualCommitConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "manual-commit-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Manual commit
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("my-topic"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        // Process message
                        processMessage(record);
                        
                        // Commit offset after successful processing
                        consumer.commitSync();
                    } catch (Exception e) {
                        System.err.println("Error processing message: " + e.getMessage());
                        // Don't commit on error - message will be reprocessed
                    }
                }
            }
        } finally {
            consumer.close();
        }
    }

    private static void processMessage(ConsumerRecord<String, String> record) {
        System.out.println("Processing: " + record.value());
        // Your processing logic here
    }
}
```

## Consumer Group Example

### Multiple Consumers in Group

```java
package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerGroupExample {
    public static void main(String[] args) {
        String groupId = "my-consumer-group";
        String topic = "my-topic";
        
        // Consumer 1
        new Thread(() -> startConsumer(groupId, topic, "consumer-1")).start();
        
        // Consumer 2
        new Thread(() -> startConsumer(groupId, topic, "consumer-2")).start();
        
        // Consumer 3
        new Thread(() -> startConsumer(groupId, topic, "consumer-3")).start();
    }

    private static void startConsumer(String groupId, String topic, String consumerId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("[%s] Topic: %s, Partition: %d, Value: %s%n",
                        consumerId,
                        record.topic(),
                        record.partition(),
                        record.value()
                    );
                }
            }
        } finally {
            consumer.close();
        }
    }
}
```

## Advanced Producer

### Producer with Partitions

```java
package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class PartitionedProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(props);

        // Send to specific partition
        ProducerRecord<String, String> record1 = new ProducerRecord<>(
            "my-topic",
            0,  // Partition 0
            "key-1",
            "value-1"
        );

        // Send with key (Kafka will hash key to determine partition)
        ProducerRecord<String, String> record2 = new ProducerRecord<>(
            "my-topic",
            "user-123",  // Key - same key goes to same partition
            "user-action"
        );

        producer.send(record1);
        producer.send(record2);
        producer.flush();
        producer.close();
    }
}
```

### Producer with Custom Partitioner

```java
package com.example.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                        Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        // Custom partitioning logic
        if (key == null) {
            return 0; // Default partition for null keys
        }

        String keyString = key.toString();
        // Example: partition by first character
        return Math.abs(keyString.charAt(0)) % numPartitions;
    }

    @Override
    public void close() {
        // Cleanup
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // Configuration
    }
}
```

## Error Handling

### Producer with Error Handling

```java
package com.example.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerWithErrorHandling {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);

        Producer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record = new ProducerRecord<>(
            "my-topic",
            "key",
            "value"
        );

        try {
            // Synchronous send with error handling
            RecordMetadata metadata = producer.send(record).get();
            System.out.println("Sent successfully: " + metadata);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error sending message: " + e.getMessage());
            // Handle error - retry, log, send to DLQ, etc.
        } finally {
            producer.close();
        }
    }
}
```

## Best Practices

### Producer Configuration

```java
Properties props = new Properties();
props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

// Reliability
props.put(ProducerConfig.ACKS_CONFIG, "all");
props.put(ProducerConfig.RETRIES_CONFIG, 3);
props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

// Performance
props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
```

### Consumer Configuration

```java
Properties props = new Properties();
props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

// Reliability
props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

// Performance
props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
```

## Running the Examples

See the [complete Java example project](../../examples/kafka-java/) for runnable code.

## Further Reading

- [Kafka Documentation](../messaging/kafka.md) - Complete Kafka guide
- [Kafka Outbox Pattern](kafka-outbox.md) - Transactional outbox pattern

---

*Last updated: {{ git_revision_date_localized }}*
