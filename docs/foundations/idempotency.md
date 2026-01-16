# Idempotency

Designing operations that can be safely retried.

## What is Idempotency?

An idempotent operation produces the same result regardless of how many times it's executed.

## Why Idempotency Matters

In distributed systems, operations may be retried due to:

- Network failures
- Timeouts
- Client retries
- Message queue redelivery

## Idempotent Operations

### HTTP Methods

- `GET` - Always idempotent
- `PUT` - Idempotent (replacing resource)
- `DELETE` - Idempotent
- `POST` - **Not** idempotent by default

### Database Operations

```sql
-- Idempotent: Multiple executions have same effect
UPDATE users SET status = 'active' WHERE id = 1;

-- Not idempotent: Each execution increments
UPDATE users SET count = count + 1 WHERE id = 1;
```

## Implementing Idempotency

### Idempotency Keys

Use unique keys to track operations:

```python
def process_payment(idempotency_key, amount):
    # Check if already processed
    if is_processed(idempotency_key):
        return get_previous_result(idempotency_key)
    
    # Process payment
    result = charge_card(amount)
    
    # Store result
    store_result(idempotency_key, result)
    return result
```

### Idempotency in APIs

```python
@app.post("/api/orders")
def create_order(request: OrderRequest):
    idempotency_key = request.headers.get("Idempotency-Key")
    
    if idempotency_key:
        existing = get_order_by_key(idempotency_key)
        if existing:
            return existing
    
    order = create_new_order(request, idempotency_key)
    return order
```

## Best Practices

1. **Always use idempotency keys** for critical operations
2. **Store results** to return same response on retry
3. **Set expiration** for idempotency keys
4. **Document idempotent endpoints** clearly

## Common Patterns

- **Idempotency tokens** in request headers
- **Deduplication tables** in databases
- **Idempotency middleware** in API frameworks

## Further Reading

- [Distributed Systems Basics](distributed-systems-basics.md)
- [Retries and DLQ](../messaging/retries-dlq.md)

---

*Last updated: {{ git_revision_date_localized }}*
