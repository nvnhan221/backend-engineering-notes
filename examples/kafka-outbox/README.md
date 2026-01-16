# Kafka Outbox Pattern Example

Complete implementation of the transactional outbox pattern with Kafka.

## Overview

This example demonstrates how to implement the outbox pattern to ensure atomicity between database writes and Kafka message publishing.

## Prerequisites

- Python 3.8+
- PostgreSQL
- Apache Kafka
- Required Python packages (see requirements.txt)

## Setup

1. **Start PostgreSQL**
   ```bash
   docker run -d \
     --name postgres \
     -e POSTGRES_PASSWORD=postgres \
     -e POSTGRES_DB=outbox_example \
     -p 5432:5432 \
     postgres:13
   ```

2. **Start Kafka**
   ```bash
   docker-compose up -d
   ```

3. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

4. **Run database migrations**
   ```bash
   python migrate.py
   ```

## Running the Example

### Start the Outbox Processor

```bash
python outbox_processor.py
```

### Create an Order (Producer)

```bash
python create_order.py --user-id 123 --amount 99.99
```

### Consume Events

```bash
python consumer.py
```

## Project Structure

```
kafka-outbox/
├── README.md
├── requirements.txt
├── migrate.py          # Database migrations
├── models.py           # SQLAlchemy models
├── create_order.py     # Example: Create order with outbox
├── outbox_processor.py # Process outbox events
└── consumer.py         # Kafka consumer example
```

## How It Works

1. Application writes to both `orders` and `outbox` tables in a single transaction
2. Outbox processor polls the `outbox` table for unpublished events
3. Events are published to Kafka
4. Events are marked as published in the database

## Key Features

- ✅ Atomic transactions
- ✅ Guaranteed delivery
- ✅ Idempotent processing
- ✅ Error handling and retries

## Further Reading

See the [documentation](../../docs/examples/kafka-outbox.md) for detailed explanation.
