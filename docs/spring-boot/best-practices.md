# Spring Boot Best Practices

Best practices for building production-ready Spring Boot applications.

## Project Structure

```
com.example.project
├── controller/     # REST controllers
├── service/        # Business logic
├── repository/     # Data access
├── entity/         # JPA entities
├── dto/            # Data transfer objects
├── config/         # Configuration classes
├── exception/      # Exception handling
└── Application.java
```

## Layered Architecture

### Controller Layer

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
}
```

### Service Layer

```java
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        return convertToDTO(user);
    }
}
```

## Dependency Injection

### Constructor Injection (Recommended)

```java
@Service
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

## Exception Handling

### Custom Exceptions

```java
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
```

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
}
```

## Logging

```java
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public User createUser(User user) {
        logger.info("Creating user: {}", user.getEmail());
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Failed to create user: {}", user.getEmail(), e);
            throw e;
        }
    }
}
```

## Testing

### Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void testFindById() {
        User user = new User("John", "john@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        UserDTO result = userService.findById(1L);
        
        assertEquals("John", result.getName());
    }
}
```

### Integration Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

## Security Best Practices

1. **Use HTTPS** in production
2. **Validate all inputs**
3. **Use parameterized queries** to prevent SQL injection
4. **Implement authentication and authorization**
5. **Keep dependencies updated**

## Performance

1. **Use connection pooling**
2. **Enable caching** where appropriate
3. **Use pagination** for large datasets
4. **Optimize database queries**
5. **Monitor application metrics**

## Configuration

1. **Externalize configuration** for different environments
2. **Use profiles** for environment-specific settings
3. **Keep sensitive data** in environment variables
4. **Validate configuration** on startup

## Further Reading

- [Getting Started](getting-started.md)
- [REST APIs](rest-apis.md)
- [Data Access](data-access.md)
- [Security](security.md)

---

*Last updated: {{ git_revision_date_localized }}*
