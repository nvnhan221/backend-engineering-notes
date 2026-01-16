# Java Collections

Working with Java Collections Framework for data structures.

## Overview

The Java Collections Framework provides a set of classes and interfaces for storing and manipulating groups of objects.

## List

### ArrayList

```java
List<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
names.add("Charlie");

// Access elements
String first = names.get(0); // Alice

// Iterate
for (String name : names) {
    System.out.println(name);
}
```

### LinkedList

```java
List<Integer> numbers = new LinkedList<>();
numbers.add(1);
numbers.add(2);
numbers.add(3);

// Better for frequent insertions/deletions
numbers.add(1, 10); // Insert at index 1
```

## Set

### HashSet

```java
Set<String> uniqueNames = new HashSet<>();
uniqueNames.add("Alice");
uniqueNames.add("Bob");
uniqueNames.add("Alice"); // Duplicate, won't be added

// Check existence
boolean exists = uniqueNames.contains("Alice"); // true
```

### TreeSet

```java
Set<Integer> sortedNumbers = new TreeSet<>();
sortedNumbers.add(3);
sortedNumbers.add(1);
sortedNumbers.add(2);
// Automatically sorted: [1, 2, 3]
```

## Map

### HashMap

```java
Map<String, Integer> ages = new HashMap<>();
ages.put("Alice", 25);
ages.put("Bob", 30);
ages.put("Charlie", 28);

// Get value
int aliceAge = ages.get("Alice"); // 25

// Check key existence
boolean hasKey = ages.containsKey("Alice"); // true

// Iterate
for (Map.Entry<String, Integer> entry : ages.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

### TreeMap

```java
Map<String, Integer> sortedMap = new TreeMap<>();
sortedMap.put("Charlie", 28);
sortedMap.put("Alice", 25);
sortedMap.put("Bob", 30);
// Automatically sorted by key
```

## Queue

### PriorityQueue

```java
Queue<Integer> priorityQueue = new PriorityQueue<>();
priorityQueue.offer(5);
priorityQueue.offer(1);
priorityQueue.offer(3);

// Poll returns smallest element
int first = priorityQueue.poll(); // 1
int second = priorityQueue.poll(); // 3
```

## Best Practices

### Choose the Right Collection

- **ArrayList**: Random access, frequent reads
- **LinkedList**: Frequent insertions/deletions
- **HashSet**: Unique elements, fast lookup
- **TreeSet**: Sorted unique elements
- **HashMap**: Key-value pairs, fast lookup
- **TreeMap**: Sorted key-value pairs

### Thread-Safe Collections

```java
// Synchronized collections
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// Concurrent collections (better performance)
ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
```

## Common Operations

### Filtering

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
```

### Mapping

```java
List<String> names = Arrays.asList("alice", "bob", "charlie");
List<String> upperCase = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

## Further Reading

- [Streams API](streams.md)
- [Concurrency](concurrency.md)

---

*Last updated: {{ git_revision_date_localized }}*
