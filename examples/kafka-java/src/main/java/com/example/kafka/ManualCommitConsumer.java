package com.example.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * Manual Commit Consumer Example
 * 
 * This example demonstrates how to manually commit offsets
 * after processing messages for better reliability.
 */
public class ManualCommitConsumer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "my-topic";
    private static final String GROUP_ID = "manual-commit-group";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Manual commit
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));

        try {
            System.out.println("Consumer started. Waiting for messages...");
            
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        // Process message
                        processMessage(record);
                        
                        // Commit offset after successful processing
                        consumer.commitSync();
                        System.out.println("Committed offset: " + record.offset());
                        
                    } catch (Exception e) {
                        System.err.println("Error processing message: " + e.getMessage());
                        // Don't commit on error - message will be reprocessed
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }

    private static void processMessage(ConsumerRecord<String, String> record) {
        System.out.printf("Processing: Topic=%s, Partition=%d, Offset=%d, Key=%s, Value=%s%n",
            record.topic(),
            record.partition(),
            record.offset(),
            record.key(),
            record.value()
        );
        
        // Simulate processing
        // Your business logic here
    }
}
