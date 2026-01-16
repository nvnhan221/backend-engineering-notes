# Java Basics

Core Java concepts and syntax for backend development.

## Overview

This guide covers fundamental Java concepts essential for backend development.

## Variables and Data Types

### Primitive Types

```java
// Numeric types
int age = 25;
long population = 7_000_000_000L;
double price = 99.99;
float discount = 0.15f;

// Boolean
boolean isActive = true;

// Character
char grade = 'A';
```

### Reference Types

```java
// Strings
String name = "John Doe";
String message = new String("Hello");

// Arrays
int[] numbers = {1, 2, 3, 4, 5};
String[] names = new String[10];
```

## Classes and Objects

### Basic Class

```java
public class User {
    private String name;
    private int age;
    
    // Constructor
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // Method
    public boolean isAdult() {
        return age >= 18;
    }
}
```

### Object Instantiation

```java
User user = new User("John", 25);
System.out.println(user.getName()); // John
System.out.println(user.isAdult()); // true
```

## Control Flow

### If-Else

```java
int score = 85;

if (score >= 90) {
    System.out.println("Grade: A");
} else if (score >= 80) {
    System.out.println("Grade: B");
} else {
    System.out.println("Grade: C");
}
```

### Switch

```java
String status = "ACTIVE";

switch (status) {
    case "ACTIVE":
        System.out.println("User is active");
        break;
    case "INACTIVE":
        System.out.println("User is inactive");
        break;
    default:
        System.out.println("Unknown status");
}
```

### Loops

```java
// For loop
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}

// Enhanced for loop
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
    System.out.println(num);
}

// While loop
int count = 0;
while (count < 5) {
    System.out.println(count);
    count++;
}
```

## Exception Handling

### Try-Catch

```java
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Division by zero: " + e.getMessage());
} catch (Exception e) {
    System.out.println("General error: " + e.getMessage());
} finally {
    System.out.println("This always executes");
}
```

### Throwing Exceptions

```java
public void validateAge(int age) throws IllegalArgumentException {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
}
```

## Interfaces and Abstract Classes

### Interface

```java
public interface PaymentProcessor {
    void processPayment(double amount);
    boolean isAvailable();
}

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        // Implementation
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}
```

### Abstract Class

```java
public abstract class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public abstract void makeSound();
    
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}
```

## Generics

```java
// Generic class
public class Box<T> {
    private T content;
    
    public void setContent(T content) {
        this.content = content;
    }
    
    public T getContent() {
        return content;
    }
}

// Usage
Box<String> stringBox = new Box<>();
stringBox.setContent("Hello");
String value = stringBox.getContent();
```

## Annotations

```java
@Override
public String toString() {
    return "User: " + name;
}

@Deprecated
public void oldMethod() {
    // This method is deprecated
}

@SuppressWarnings("unchecked")
public void methodWithWarning() {
    // Suppress compiler warnings
}
```

## Best Practices

1. **Use meaningful variable names**
2. **Follow naming conventions** (camelCase for variables, PascalCase for classes)
3. **Always handle exceptions**
4. **Use access modifiers appropriately** (private, protected, public)
5. **Document your code** with JavaDoc

## Further Reading

- [Collections](collections.md)
- [Concurrency](concurrency.md)
- [Streams API](streams.md)

---

*Last updated: {{ git_revision_date_localized }}*
