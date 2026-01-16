# RabbitMQ Retry with Dead Letter Exchange Example

Complete implementation of retry logic with RabbitMQ using Dead Letter Exchanges.

## Overview

This example demonstrates how to implement automatic retries with exponential backoff using RabbitMQ's Dead Letter Exchange (DLX) feature.

## Prerequisites

- Python 3.8+
- RabbitMQ server
- pika library

## Setup

1. **Start RabbitMQ**
   ```bash
   docker run -d \
     --name rabbitmq \
     -p 5672:5672 \
     -p 15672:15672 \
     rabbitmq:3-management
   ```

2. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

3. **Access RabbitMQ Management UI**
   ```
   http://localhost:15672
   Default credentials: guest/guest
   ```

## Running the Example

### Setup Queues

```bash
python setup_queues.py
```

This script creates:
- Main queue
- Retry queue with TTL
- Dead letter exchange and queue

### Start Consumer

```bash
python consumer.py
```

### Send Test Messages

```bash
python producer.py
```

## Project Structure

```
rabbitmq-retry-dlx/
├── README.md
├── requirements.txt
├── setup_queues.py    # Queue configuration
├── producer.py        # Message producer
└── consumer.py        # Consumer with retry logic
```

## How It Works

1. Messages are consumed from the main queue
2. On failure, messages are sent to retry queue with TTL
3. After TTL expires, messages return to main queue
4. After max retries, messages go to dead letter queue

## Configuration

Edit `consumer.py` to adjust:
- `MAX_RETRIES` - Maximum retry attempts
- `BASE_TTL` - Base time-to-live for retries
- Retry backoff strategy

## Monitoring

Monitor queues in RabbitMQ Management UI:
- Main queue size
- Retry queue size
- Dead letter queue size

## Further Reading

See the [documentation](../../docs/examples/rabbitmq-retry-dlx.md) for detailed explanation.
