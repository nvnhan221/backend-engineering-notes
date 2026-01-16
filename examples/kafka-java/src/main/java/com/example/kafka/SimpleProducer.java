package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Simple Kafka Producer Example
 * 
 * This example demonstrates how to create a basic Kafka producer
 * and send messages to a topic.
 */
public class SimpleProducer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "my-topic";

    public static void main(String[] args) {
        // Configure producer properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create producer
        Producer<String, String> producer = new KafkaProducer<>(props);

        try {
            // Send messages
            for (int i = 0; i < 10; i++) {
                String key = "key-" + i;
                String value = "message-" + i;
                
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC_NAME,
                    key,
                    value
                );
                
                producer.send(record);
                System.out.println("Sent: key=" + key + ", value=" + value);
            }
            
            // Flush to ensure all messages are sent
            producer.flush();
            System.out.println("All messages sent successfully");
            
        } catch (Exception e) {
            System.err.println("Error sending messages: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close producer
            producer.close();
        }
    }
}
