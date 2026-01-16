# Java Streams API

Functional programming with Java Streams.

## Overview

The Streams API provides a functional approach to processing collections of data.

## Creating Streams

### From Collections

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
Stream<String> stream = names.stream();
```

### From Arrays

```java
String[] names = {"Alice", "Bob", "Charlie"};
Stream<String> stream = Arrays.stream(names);
```

### Using Stream.of()

```java
Stream<String> stream = Stream.of("Alice", "Bob", "Charlie");
```

### Infinite Streams

```java
Stream<Integer> numbers = Stream.iterate(0, n -> n + 1)
    .limit(10);
```

## Intermediate Operations

### Filter

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
// Result: [2, 4, 6]
```

### Map

```java
List<String> names = Arrays.asList("alice", "bob", "charlie");
List<String> upperCase = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// Result: [ALICE, BOB, CHARLIE]
```

### FlatMap

```java
List<List<Integer>> lists = Arrays.asList(
    Arrays.asList(1, 2),
    Arrays.asList(3, 4),
    Arrays.asList(5, 6)
);

List<Integer> flattened = lists.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4, 5, 6]
```

### Distinct

```java
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 4);
List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4]
```

### Sorted

```java
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
List<String> sorted = names.stream()
    .sorted()
    .collect(Collectors.toList());
// Result: [Alice, Bob, Charlie]
```

## Terminal Operations

### Collect

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
List<String> result = names.stream()
    .filter(s -> s.startsWith("A"))
    .collect(Collectors.toList());
```

### ForEach

```java
names.stream()
    .forEach(System.out::println);
```

### Reduce

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream()
    .reduce(0, Integer::sum);
// Result: 15
```

### Count

```java
long count = names.stream()
    .filter(s -> s.length() > 3)
    .count();
```

### AnyMatch, AllMatch, NoneMatch

```java
boolean hasLongName = names.stream()
    .anyMatch(s -> s.length() > 10);

boolean allLong = names.stream()
    .allMatch(s -> s.length() > 3);

boolean noneLong = names.stream()
    .noneMatch(s -> s.length() > 10);
```

## Collectors

### ToList, ToSet

```java
List<String> list = names.stream()
    .collect(Collectors.toList());

Set<String> set = names.stream()
    .collect(Collectors.toSet());
```

### ToMap

```java
Map<String, Integer> map = names.stream()
    .collect(Collectors.toMap(
        name -> name,
        String::length
    ));
```

### GroupingBy

```java
Map<Integer, List<String>> grouped = names.stream()
    .collect(Collectors.groupingBy(String::length));
```

### Joining

```java
String joined = names.stream()
    .collect(Collectors.joining(", "));
// Result: "Alice, Bob, Charlie"
```

## Parallel Streams

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
int sum = numbers.parallelStream()
    .mapToInt(Integer::intValue)
    .sum();
```

!!! warning "When to Use Parallel Streams"
    - Use for large datasets
    - Operations must be stateless
    - Avoid for I/O operations
    - Consider overhead vs. benefit

## Best Practices

1. **Use method references** when possible
2. **Chain operations** for readability
3. **Avoid side effects** in stream operations
4. **Use parallel streams** carefully
5. **Prefer streams** over loops for complex transformations

## Further Reading

- [Collections](collections.md)
- [Concurrency](concurrency.md)

---

*Last updated: {{ git_revision_date_localized }}*
