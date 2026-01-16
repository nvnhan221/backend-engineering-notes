# Java Best Practices

Coding standards and best practices for Java backend development.

## Code Style

### Naming Conventions

```java
// Classes: PascalCase
public class UserService { }

// Methods and variables: camelCase
public void getUserById() { }
private String userName;

// Constants: UPPER_SNAKE_CASE
public static final int MAX_RETRIES = 3;
```

### Package Structure

```
com.example.project
├── controller
├── service
├── repository
├── model
└── config
```

## Exception Handling

### Use Specific Exceptions

```java
// Good
try {
    // code
} catch (FileNotFoundException e) {
    // handle specific exception
}

// Bad
try {
    // code
} catch (Exception e) {
    // too generic
}
```

### Don't Swallow Exceptions

```java
// Bad
try {
    processData();
} catch (Exception e) {
    // Silent failure
}

// Good
try {
    processData();
} catch (Exception e) {
    logger.error("Failed to process data", e);
    throw new ProcessingException("Processing failed", e);
}
```

## Resource Management

### Try-With-Resources

```java
// Automatic resource management
try (FileInputStream fis = new FileInputStream("file.txt");
     BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
} // Resources automatically closed
```

## Immutability

### Immutable Classes

```java
public final class User {
    private final String name;
    private final int age;
    
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
}
```

## Dependency Injection

### Constructor Injection

```java
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

## Logging

### Use Proper Logging

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public void processUser(String userId) {
        logger.info("Processing user: {}", userId);
        try {
            // process
        } catch (Exception e) {
            logger.error("Failed to process user: {}", userId, e);
        }
    }
}
```

## Performance

### Avoid String Concatenation in Loops

```java
// Bad
String result = "";
for (String item : items) {
    result += item;
}

// Good
StringBuilder sb = new StringBuilder();
for (String item : items) {
    sb.append(item);
}
String result = sb.toString();
```

### Use Collections Efficiently

```java
// Specify initial capacity for large collections
List<String> list = new ArrayList<>(1000);
Map<String, Integer> map = new HashMap<>(1000);
```

## Security

### Input Validation

```java
public void processInput(String input) {
    if (input == null || input.trim().isEmpty()) {
        throw new IllegalArgumentException("Input cannot be empty");
    }
    // process
}
```

### Avoid SQL Injection

```java
// Use PreparedStatement
String sql = "SELECT * FROM users WHERE id = ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setString(1, userId);
```

## Testing

### Unit Tests

```java
@Test
public void testUserCreation() {
    User user = new User("John", 25);
    assertEquals("John", user.getName());
    assertEquals(25, user.getAge());
}
```

## Documentation

### JavaDoc

```java
/**
 * Creates a new user with the specified name and age.
 *
 * @param name the user's name, must not be null
 * @param age the user's age, must be positive
 * @return the created user
 * @throws IllegalArgumentException if name is null or age is negative
 */
public User createUser(String name, int age) {
    // implementation
}
```

## Further Reading

- [Java Basics](java-basics.md)
- [Collections](collections.md)
- [Concurrency](concurrency.md)

---

*Last updated: {{ git_revision_date_localized }}*
