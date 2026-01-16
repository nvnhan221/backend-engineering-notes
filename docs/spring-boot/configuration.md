# Spring Boot Configuration

Application configuration and properties management.

## Overview

Spring Boot provides flexible configuration through properties files, YAML, environment variables, and more.

## Application Properties

### application.properties

```properties
# Server configuration
server.port=8080
server.servlet.context-path=/api

# Application name
spring.application.name=my-app

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=password

# JPA configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  application:
    name: my-app
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

## Profile-Specific Configuration

### application-dev.properties

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.show-sql=true
logging.level.com.example=DEBUG
```

### application-prod.properties

```properties
spring.datasource.url=jdbc:postgresql://prod-db:5432/mydb
spring.jpa.show-sql=false
logging.level.com.example=INFO
```

### Activating Profiles

```bash
# Command line
java -jar app.jar --spring.profiles.active=prod

# Environment variable
export SPRING_PROFILES_ACTIVE=prod

# application.properties
spring.profiles.active=dev
```

## Configuration Properties

### Using @ConfigurationProperties

```java
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String name;
    private String version;
    private Database database;
    
    // Getters and setters
    
    public static class Database {
        private String host;
        private int port;
        
        // Getters and setters
    }
}
```

### application.properties

```properties
app.name=My Application
app.version=1.0.0
app.database.host=localhost
app.database.port=5432
```

### Using in Code

```java
@Service
public class MyService {
    
    @Autowired
    private AppConfig appConfig;
    
    public void doSomething() {
        String dbHost = appConfig.getDatabase().getHost();
    }
}
```

## Environment Variables

Spring Boot automatically maps environment variables:

```bash
# Set environment variable
export SERVER_PORT=9090
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/mydb
```

## Command Line Arguments

```bash
java -jar app.jar --server.port=9090 --spring.profiles.active=prod
```

## External Configuration

### application.properties in External Directory

```bash
java -jar app.jar --spring.config.location=file:/path/to/config/
```

### Multiple Configuration Files

```properties
spring.config.location=classpath:/application.properties,file:/external/config.properties
```

## Validation

### Validating Configuration

```java
@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class AppConfig {
    
    @NotBlank
    private String name;
    
    @Min(1)
    @Max(65535)
    private int port;
    
    // Getters and setters
}
```

## Best Practices

1. **Use profiles** for different environments
2. **Externalize configuration** for production
3. **Use @ConfigurationProperties** for type-safe configuration
4. **Validate configuration** on startup
5. **Keep sensitive data** in environment variables or secrets

## Further Reading

- [Getting Started](getting-started.md)
- [Data Access](data-access.md)

---

*Last updated: {{ git_revision_date_localized }}*
