# Testing Guide for Petralib Constructor

This guide explains how to run tests for the petralib-constructor project.

## Prerequisites

- Java 15 or higher (JDK 17 recommended)
- Gradle (included via wrapper)

## Test Configuration

The project uses:
- **JUnit 5** for test execution
- **Spring Boot Test** for integration testing
- **Mockito** for mocking dependencies
- **H2 in-memory database** for testing (no PostgreSQL required)

## Running Tests

### Run All Tests
```bash
cd petralib-constructor
./gradlew test
```

### Run Tests with Coverage
```bash
./gradlew test jacocoTestReport
```

### Run Specific Test Class
```bash
./gradlew test --tests "com.petralib.auth.rest.AuthControllerTest"
```

### Run Tests in Continuous Mode (watch mode)
```bash
./gradlew test --continuous
```

## Test Structure

### Integration Tests
Located in `src/test/java/com/petralib/`:
- **AuthControllerTest** - Tests authentication endpoints
- **BlockRestControllerTest** - Tests block management endpoints
- **TypeRestControllerTest** - Tests type management endpoints

### Unit Tests
- **BlockServiceTest** - Tests block service business logic

### Application Tests
- **ConstructorApplicationTest** - Tests Spring context loading

## Test Configuration

Tests use a separate configuration file: `src/test/resources/application-test.yml`
- Uses H2 in-memory database
- Separate JWT secret for testing
- Reduced logging levels

## Writing New Tests

### Integration Test Example
```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class MyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/endpoint"))
            .andExpect(status().isOk());
    }
}
```

### Unit Test Example
```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {
    @Mock
    private MyRepository repository;
    
    @InjectMocks
    private MyService service;
    
    @Test
    void testMethod() {
        // Test implementation
    }
}
```

## Test Coverage

Current test coverage includes:
- ✅ Authentication endpoints (login, logout)
- ✅ Block management endpoints (CRUD operations)
- ✅ Type management endpoints (CRUD operations)
- ✅ Block service business logic
- ✅ Spring context loading

## Troubleshooting

### Tests Fail with Database Connection Error
- Ensure you're using the `test` profile
- Check that H2 dependency is in `build.gradle`

### Tests Fail with Security Errors
- Use `@WithMockUser` annotation for authenticated endpoints
- Mock `AuthenticationManager` and `JwtTokenProvider` for auth tests

### Tests Fail with NullPointerException
- Ensure all dependencies are properly mocked
- Check that test data is correctly initialized in `@BeforeEach`

## Next Steps

To expand test coverage:
1. Add tests for remaining controllers (Project, Scenario, Service)
2. Add tests for mapper classes
3. Add tests for repository layer
4. Add tests for security configuration
5. Add tests for error handling
