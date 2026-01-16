package com.example.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Producer with Callback Example
 * 
 * This example demonstrates how to use callbacks to handle
 * asynchronous message sending and check for errors.
 */
public class ProducerWithCallback {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "my-topic";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // Reliability settings
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Wait for all replicas
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Exactly-once semantics

        Producer<String, String> producer = new KafkaProducer<>(props);

        try {
            for (int i = 0; i < 10; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC_NAME,
                    "key-" + i,
                    "Hello Kafka! Message " + i
                );

                // Send with callback
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception == null) {
                            System.out.println("Successfully sent to topic: " + metadata.topic() +
                                             ", partition: " + metadata.partition() +
                                             ", offset: " + metadata.offset() +
                                             ", timestamp: " + metadata.timestamp());
                        } else {
                            System.err.println("Error sending message: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
            
            producer.flush();
            System.out.println("All messages sent");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
