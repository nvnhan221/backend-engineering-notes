# Spring Boot Getting Started

Setting up and running your first Spring Boot application.

## Prerequisites

- Java 8 or higher
- Maven 3.6+ or Gradle 6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## Creating a Spring Boot Project

### Using Spring Initializr

1. Visit https://start.spring.io
2. Select:
   - Project: Maven or Gradle
   - Language: Java
   - Spring Boot version: Latest stable
   - Dependencies: Spring Web
3. Click "Generate" and download the project

### Using Spring CLI

```bash
spring init --dependencies=web my-spring-app
```

### Using Maven

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.0</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       └── Application.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/example/
            └── ApplicationTests.java
```

## Main Application Class

```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Creating a REST Controller

```java
package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}
```

## Running the Application

### Using Maven

```bash
./mvnw spring-boot:run
```

### Using Gradle

```bash
./gradlew bootRun
```

### Using IDE

Run the `main` method in `Application.java`

## Testing the Application

```bash
curl http://localhost:8080/hello
# Response: Hello, Spring Boot!
```

## Application Properties

Create `src/main/resources/application.properties`:

```properties
# Server configuration
server.port=8080
server.servlet.context-path=/api

# Application name
spring.application.name=my-app
```

## Common Dependencies

### Web Application

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Database (JPA)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Security

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## Next Steps

- [REST APIs](rest-apis.md) - Build RESTful APIs
- [Data Access](data-access.md) - Connect to databases
- [Configuration](configuration.md) - Configure your application

---

*Last updated: {{ git_revision_date_localized }}*
