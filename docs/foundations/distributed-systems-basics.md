# Distributed Systems Basics

Fundamental concepts for understanding and building distributed systems.

## What is a Distributed System?

A distributed system is a collection of independent computers that appear to users as a single coherent system.

## Key Characteristics

### Scalability

Distributed systems can scale horizontally by adding more nodes.

!!! tip "Horizontal vs Vertical Scaling"
    - **Horizontal**: Add more machines
    - **Vertical**: Add more resources to existing machines

### Fault Tolerance

Systems should continue operating even when some components fail.

### Consistency

Different consistency models trade off between performance and correctness.

## Common Challenges

### Network Partitions

When network links fail, nodes may be unable to communicate.

### Clock Synchronization

Distributed systems often need to coordinate time across nodes.

### Consensus

Agreeing on a value or decision across multiple nodes.

## CAP Theorem

The CAP theorem states that a distributed system can guarantee at most two of:

- **Consistency**: All nodes see the same data simultaneously
- **Availability**: System remains operational
- **Partition Tolerance**: System continues despite network failures

## Further Reading

- [Consistency Models](consistency-models.md)
- [Idempotency](idempotency.md)

---

*Last updated: {{ git_revision_date_localized }}*
