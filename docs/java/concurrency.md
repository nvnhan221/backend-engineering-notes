# Java Concurrency

Multithreading and concurrent programming in Java.

## Overview

Java provides robust support for concurrent programming through threads, executors, and concurrent collections.

## Threads

### Creating Threads

```java
// Extend Thread class
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running");
    }
}

MyThread thread = new MyThread();
thread.start();
```

### Implement Runnable

```java
// Implement Runnable interface
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running");
    }
}

Thread thread = new Thread(new MyRunnable());
thread.start();
```

### Lambda Expression

```java
Thread thread = new Thread(() -> {
    System.out.println("Lambda thread running");
});
thread.start();
```

## ExecutorService

### Basic Usage

```java
ExecutorService executor = Executors.newFixedThreadPool(5);

// Submit tasks
executor.submit(() -> {
    System.out.println("Task executed");
});

// Shutdown
executor.shutdown();
```

### Future

```java
ExecutorService executor = Executors.newFixedThreadPool(5);

Future<String> future = executor.submit(() -> {
    Thread.sleep(1000);
    return "Result";
});

// Get result (blocks until complete)
String result = future.get();
```

## Synchronization

### Synchronized Methods

```java
public class Counter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

### Synchronized Blocks

```java
public void increment() {
    synchronized (this) {
        count++;
    }
}
```

### ReentrantLock

```java
private final ReentrantLock lock = new ReentrantLock();

public void increment() {
    lock.lock();
    try {
        count++;
    } finally {
        lock.unlock();
    }
}
```

## Concurrent Collections

### ConcurrentHashMap

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("key", 1);

// Thread-safe operations
map.compute("key", (k, v) -> v + 1);
```

### BlockingQueue

```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>();

// Producer
queue.put("item");

// Consumer
String item = queue.take(); // Blocks if empty
```

## CompletableFuture

### Async Operations

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Result";
});

future.thenAccept(result -> {
    System.out.println("Got: " + result);
});
```

### Chaining

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")
    .thenApply(String::toUpperCase);

String result = future.get(); // "HELLO WORLD"
```

## Best Practices

1. **Use ExecutorService** instead of creating threads manually
2. **Prefer concurrent collections** for thread-safe operations
3. **Avoid shared mutable state** when possible
4. **Use CompletableFuture** for async operations
5. **Always shutdown ExecutorService**

## Common Patterns

### Producer-Consumer

```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>();

// Producer
new Thread(() -> {
    queue.put("item");
}).start();

// Consumer
new Thread(() -> {
    String item = queue.take();
    // Process item
}).start();
```

## Further Reading

- [Collections](collections.md)
- [Streams API](streams.md)

---

*Last updated: {{ git_revision_date_localized }}*
