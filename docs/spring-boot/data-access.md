# Spring Boot Data Access

Working with databases using Spring Data JPA.

## Overview

Spring Data JPA simplifies database access by providing repository abstractions.

## Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Database driver -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Or PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Entity

```java
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true)
    private String email;
    
    // Getters and setters
}
```

## Repository

```java
package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByNameContaining(String name);
}
```

## Service Layer

```java
package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
```

## Custom Queries

### Using @Query

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmailAddress(String email);
    
    @Query("SELECT u FROM User u WHERE u.age > ?1")
    List<User> findUsersOlderThan(int age);
    
    @Query(value = "SELECT * FROM users WHERE name LIKE %?1%", 
           nativeQuery = true)
    List<User> findByNameLike(String name);
}
```

### Query Methods

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Find by attribute
    User findByEmail(String email);
    
    // Find with conditions
    List<User> findByAgeGreaterThan(int age);
    List<User> findByNameContaining(String name);
    
    // Multiple conditions
    List<User> findByNameAndAge(String name, int age);
    
    // Sorting
    List<User> findByAgeOrderByNameAsc(int age);
}
```

## Database Configuration

### application.properties

```properties
# H2 Database (for development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### PostgreSQL Configuration

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Transactions

```java
@Service
@Transactional
public class UserService {
    
    public User createUserWithOrders(User user, List<Order> orders) {
        User savedUser = userRepository.save(user);
        orders.forEach(order -> {
            order.setUser(savedUser);
            orderRepository.save(order);
        });
        return savedUser;
    }
}
```

## Pagination and Sorting

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByAge(int age, Pageable pageable);
    List<User> findByName(String name, Sort sort);
}

// Usage
Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
Page<User> users = userRepository.findByAge(25, pageable);
```

## Best Practices

1. **Use repositories** for data access
2. **Keep entities simple** - use DTOs for transfer
3. **Use transactions** for multi-step operations
4. **Handle exceptions** properly
5. **Use pagination** for large datasets

## Further Reading

- [REST APIs](rest-apis.md)
- [Configuration](configuration.md)
- [Testing](testing.md)

---

*Last updated: {{ git_revision_date_localized }}*
