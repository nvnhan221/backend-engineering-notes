# Kafka Outbox Pattern

Implementing the transactional outbox pattern with Kafka.

## Overview

The outbox pattern ensures that database changes and message publishing happen atomically, preventing data inconsistencies.

## Problem

When you need to:
1. Save data to database
2. Publish event to Kafka

If step 2 fails, you have inconsistent state.

## Solution: Outbox Pattern

Store events in an outbox table within the same database transaction, then publish them asynchronously.

## Architecture

```
┌─────────────┐
│ Application │
└──────┬──────┘
       │
       ├──► Write to main table
       │
       └──► Write to outbox table
            │
            └──► Outbox Processor
                 │
                 └──► Publish to Kafka
```

## Implementation

### Database Schema

```sql
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE outbox (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id UUID NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    published BOOLEAN DEFAULT FALSE
);
```

### Application Code

```python
from sqlalchemy import create_engine, Column, String, JSON, Boolean
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import json
import uuid

Base = declarative_base()

class Outbox(Base):
    __tablename__ = 'outbox'
    
    id = Column(String, primary_key=True)
    aggregate_id = Column(String, nullable=False)
    aggregate_type = Column(String, nullable=False)
    event_type = Column(String, nullable=False)
    payload = Column(JSON, nullable=False)
    published = Column(Boolean, default=False)

def create_order(user_id, amount):
    engine = create_engine('postgresql://...')
    Session = sessionmaker(bind=engine)
    session = Session()
    
    try:
        # Create order
        order_id = str(uuid.uuid4())
        order = Order(id=order_id, user_id=user_id, amount=amount)
        session.add(order)
        
        # Create outbox event
        outbox_event = Outbox(
            id=str(uuid.uuid4()),
            aggregate_id=order_id,
            aggregate_type='Order',
            event_type='OrderCreated',
            payload={
                'order_id': order_id,
                'user_id': user_id,
                'amount': str(amount)
            }
        )
        session.add(outbox_event)
        
        # Commit transaction
        session.commit()
    except Exception as e:
        session.rollback()
        raise

# Outbox Processor
def process_outbox():
    session = Session()
    unpublished = session.query(Outbox).filter(
        Outbox.published == False
    ).limit(100).all()
    
    for event in unpublished:
        try:
            # Publish to Kafka
            kafka_producer.send('orders', value=event.payload)
            
            # Mark as published
            event.published = True
            session.commit()
        except Exception as e:
            session.rollback()
            # Will retry on next run
```

## Benefits

- ✅ **Atomicity** - Database and event in same transaction
- ✅ **Reliability** - Events never lost
- ✅ **Consistency** - No partial updates

## Considerations

- Outbox processor must be reliable
- Consider idempotency for consumers
- Monitor outbox table size

## Further Reading

- [Kafka](../messaging/kafka.md)
- [Idempotency](../foundations/idempotency.md)
- [Examples Code](https://github.com/nvnhan221/backend-engineering-notes/tree/main/examples/kafka-outbox)

---

*Last updated: {{ git_revision_date_localized }}*
