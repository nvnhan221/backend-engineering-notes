# Spring Boot REST APIs

Building RESTful APIs with Spring Boot.

## Overview

Spring Boot provides excellent support for building REST APIs through Spring MVC.

## Basic REST Controller

```java
package com.example.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

## Request Mapping

### Path Variables

```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}

@GetMapping("/users/{userId}/orders/{orderId}")
public Order getOrder(
    @PathVariable Long userId,
    @PathVariable Long orderId
) {
    return orderService.findByUserAndOrder(userId, orderId);
}
```

### Request Parameters

```java
@GetMapping("/users")
public List<User> getUsers(
    @RequestParam(required = false) String name,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
) {
    return userService.findByName(name, page, size);
}
```

### Request Body

```java
@PostMapping("/users")
public User createUser(@RequestBody User user) {
    return userService.save(user);
}
```

## Response Status

```java
@PostMapping("/users")
@ResponseStatus(HttpStatus.CREATED)
public User createUser(@RequestBody User user) {
    return userService.save(user);
}

@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
}
```

## Exception Handling

### Global Exception Handler

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
        UserNotFoundException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            "USER_NOT_FOUND",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            "Invalid input"
        );
        return ResponseEntity.badRequest().body(error);
    }
}
```

## Validation

### Using Bean Validation

```java
public class User {
    @NotNull
    @NotBlank
    private String name;
    
    @Email
    private String email;
    
    @Min(18)
    @Max(100)
    private int age;
}

@PostMapping("/users")
public User createUser(@Valid @RequestBody User user) {
    return userService.save(user);
}
```

## Content Negotiation

```java
@GetMapping(value = "/users/{id}", produces = {
    MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_XML_VALUE
})
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}
```

## CORS Configuration

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

## Best Practices

1. **Use @RestController** for REST endpoints
2. **Follow RESTful conventions** for URLs and HTTP methods
3. **Return appropriate status codes**
4. **Handle exceptions globally**
5. **Validate input data**
6. **Use DTOs** for request/response objects

## Further Reading

- [Data Access](data-access.md)
- [Security](security.md)
- [Testing](testing.md)

---

*Last updated: {{ git_revision_date_localized }}*
