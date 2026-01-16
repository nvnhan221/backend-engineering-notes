# Consistency Models

Understanding different consistency models in distributed systems.

## Overview

Consistency models define the guarantees about when updates to data become visible to different nodes in a distributed system.

## Strong Consistency

All nodes see the same data at the same time. Updates are atomic and immediately visible.

!!! note "Use Cases"
    - Financial transactions
    - Critical configuration data
    - Leader election

## Eventual Consistency

Given enough time, all nodes will converge to the same state, but temporary inconsistencies are allowed.

!!! tip "Benefits"
    - Better availability
    - Lower latency
    - Higher throughput

## Consistency Levels

### Read Your Writes

After writing, you will always read your own writes.

### Monotonic Reads

If you read a value, subsequent reads will return the same or a newer value.

### Causal Consistency

Causally related operations are seen in the same order by all nodes.

## Choosing a Model

Consider:

- **Latency requirements**: Strong consistency may increase latency
- **Availability needs**: Eventual consistency improves availability
- **Data criticality**: Critical data may need strong consistency

## Examples

=== "Strong Consistency"
    ```python
    # All replicas must agree before returning
    def write(key, value):
        consensus = get_consensus_from_all_replicas()
        if consensus:
            return write_to_all_replicas(key, value)
    ```

=== "Eventual Consistency"
    ```python
    # Write to primary, replicate asynchronously
    def write(key, value):
        write_to_primary(key, value)
        replicate_async(key, value)  # Eventually reaches all replicas
    ```

## Further Reading

- [Distributed Systems Basics](distributed-systems-basics.md)
- [Messaging Patterns](../messaging/index.md)

---

*Last updated: {{ git_revision_date_localized }}*
