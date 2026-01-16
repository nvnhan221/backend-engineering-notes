# Observability

Monitoring, logging, tracing, and metrics for distributed systems.

## Overview

Observability is the ability to understand the internal state of a system from its external outputs.

## Three Pillars

### Logs

Structured logging for debugging and auditing.

### Metrics

Quantitative measurements over time.

### Traces

Request flows through distributed systems.

## Logging

### Structured Logging

```python
import logging
import json

logger = logging.getLogger(__name__)

logger.info(
    "Order created",
    extra={
        "order_id": order_id,
        "user_id": user_id,
        "amount": amount
    }
)
```

### Log Levels

- **DEBUG** - Detailed information for debugging
- **INFO** - General informational messages
- **WARNING** - Warning messages
- **ERROR** - Error messages
- **CRITICAL** - Critical errors

## Metrics

### Types of Metrics

- **Counter** - Incrementing values
- **Gauge** - Current value
- **Histogram** - Distribution of values
- **Summary** - Quantiles over time

### Example

```python
from prometheus_client import Counter, Histogram

request_count = Counter('requests_total', 'Total requests')
request_duration = Histogram('request_duration_seconds', 'Request duration')
```

## Distributed Tracing

Track requests across service boundaries.

### OpenTelemetry

```python
from opentelemetry import trace

tracer = trace.get_tracer(__name__)

with tracer.start_as_current_span("process_order"):
    # Your code here
    pass
```

## Best Practices

- Use structured logging
- Include correlation IDs
- Set appropriate log levels
- Monitor key metrics
- Trace critical paths
- Set up alerts

---

*Add more observability topics as needed*
