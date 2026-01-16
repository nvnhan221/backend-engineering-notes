# Spring Boot Testing

Testing Spring Boot applications with JUnit and Mockito.

## Overview

Spring Boot provides excellent testing support through Spring Test and various testing libraries.

## Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

## Unit Testing

### Service Layer Test

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void testCreateUser() {
        User user = new User("John", "john@example.com");
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        User result = userService.createUser(user);
        
        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(userRepository).save(user);
    }
    
    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(1L);
        });
    }
}
```

## Integration Testing

### Controller Integration Test

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testGetUser() throws Exception {
        User user = new User("John", "john@example.com");
        userRepository.save(user);
        
        mockMvc.perform(get("/api/users/{id}", user.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }
    
    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO("Jane", "jane@example.com");
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Jane"));
    }
}
```

## Repository Testing

```java
@DataJpaTest
class UserRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testFindByEmail() {
        User user = new User("John", "john@example.com");
        entityManager.persist(user);
        entityManager.flush();
        
        User found = userRepository.findByEmail("john@example.com");
        
        assertNotNull(found);
        assertEquals("John", found.getName());
    }
}
```

## Web Layer Testing

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        UserDTO user = new UserDTO("John", "john@example.com");
        when(userService.findById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

## Test Configuration

### application-test.properties

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### Using Test Profile

```java
@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {
    // Tests run with test profile
}
```

## Best Practices

1. **Test in isolation** - Each test should be independent
2. **Use meaningful test names** - Describe what is being tested
3. **Follow AAA pattern** - Arrange, Act, Assert
4. **Mock external dependencies**
5. **Test both success and failure cases**

## Further Reading

- [Getting Started](getting-started.md)
- [REST APIs](rest-apis.md)
- [Best Practices](best-practices.md)

---

*Last updated: {{ git_revision_date_localized }}*
