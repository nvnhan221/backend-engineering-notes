package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * Consumer Group Example
 * 
 * This example demonstrates how multiple consumers in the same
 * consumer group share partitions for parallel processing.
 */
public class ConsumerGroupExample {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "my-topic";
    private static final String GROUP_ID = "my-consumer-group";

    public static void main(String[] args) {
        // Start multiple consumers in the same group
        // Each consumer will be assigned different partitions
        
        new Thread(() -> startConsumer("consumer-1")).start();
        new Thread(() -> startConsumer("consumer-2")).start();
        new Thread(() -> startConsumer("consumer-3")).start();
        
        // Keep main thread alive
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void startConsumer(String consumerId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));

        try {
            System.out.println("[" + consumerId + "] Consumer started. Waiting for messages...");
            
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("[%s] Topic: %s, Partition: %d, Offset: %d, Value: %s%n",
                        consumerId,
                        record.topic(),
                        record.partition(),
                        record.offset(),
                        record.value()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("[" + consumerId + "] Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
