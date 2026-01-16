# Messaging

Message queues and event-driven architectures.

## Overview

This section covers messaging systems, patterns, and best practices for building event-driven applications.

## Topics

- [Kafka](kafka.md) - Apache Kafka fundamentals and patterns
- [RabbitMQ](rabbitmq.md) - RabbitMQ usage and patterns
- [Retries and DLQ](retries-dlq.md) - Handling failures and dead letter queues

## When to Use Messaging

Messaging is ideal for:

- **Asynchronous processing** - Decouple producers and consumers
- **Event-driven architecture** - React to events across services
- **Load leveling** - Smooth out traffic spikes
- **Reliability** - Guaranteed message delivery

## Common Patterns

### Publish-Subscribe

Multiple consumers receive the same message.

### Point-to-Point

One message is consumed by exactly one consumer.

### Request-Reply

Synchronous request-response pattern over messaging.

## Further Reading

- [Examples](../examples/index.md) - Practical examples
- [Foundations](../foundations/index.md) - Core concepts

---

*Add more messaging topics as needed*
