# Spring Exception Handler

English | [中文](./README.md)

## Project Introduction

Spring Exception Handler is a unified exception handling framework based on Spring Boot, designed to simplify exception handling logic in API development. This framework follows HTTP standards and automatically converts exceptions to corresponding HTTP status codes while providing a unified error response format.

## Design Philosophy

### Why Choose HTTP Status Codes?

In API design, there are two common error handling approaches:

1. **Return 200 for all responses**: Define different error types through custom HTTP response body
2. **Follow HTTP response codes**: Return corresponding HTTP status codes for different errors

This project adopts the second approach for the following reasons:

- **Standardization**: HTTP as a universal standard is easier to follow and understand
- **Compatibility**: Most industry frameworks are compatible with HTTP standards
- **Observability**: Standard HTTP response codes make Nginx access logs more meaningful
- **Distributed Support**: Distributed tracing frameworks generally follow HTTP protocol for error handling

### Use Exceptions Instead of Return Codes

> "Use exceptions rather than return codes" --- Clean Code

Throwing exceptions immediately when encountering errors in methods makes the code cleaner, and the logic won't be cluttered by error handling. Let the tedious status code conversion work be handled by common tools. We only need to focus on business logic.

## Features

- ✅ Automatic Exception Handling: Based on Spring Boot's `@ControllerAdvice` mechanism
- ✅ HTTP Status Code Mapping: Exceptions automatically converted to standard HTTP status codes
- ✅ Unified Error Format: Standardized error response structure
- ✅ Custom Error Types: Support for defining business errors through enums
- ✅ Detailed Error Information: Includes error code, message, details, timestamp, etc.
- ✅ Logging: Automatic exception logging for debugging

## Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>com.tme.uni</groupId>
    <artifactId>spring-exception-handler</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Enable Auto Exception Handler

Add `@EnableAutoExceptionHandler` annotation to your Spring Boot startup class:

```java
@SpringBootApplication
@EnableAutoExceptionHandler
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3. Throw Exceptions

Throw appropriate exceptions directly in your business code:

```java
// Resource not found
throw new NotFoundException(ErrorType.NOT_FOUND);

// Bad request parameters
throw new BadRequestException(ErrorType.INVALID_PARAMETER);

// Insufficient permissions
throw new ForbiddenException(ErrorType.ACCESS_DENIED);

// Unauthorized
throw new UnauthorizedException(ErrorType.UNAUTHORIZED);

// Resource conflict
throw new ConflictException(ErrorType.RESOURCE_CONFLICT);
```

### 4. Custom Error Types

Define custom error types by implementing the `ErrorType` interface, preferably using enums:

```java
public enum CommonError implements ErrorType {
    REQUEST_ERROR("000001", "Request parameter error"),
    SERVER_ERROR("000002", "Internal server error"),
    INVALID_PARAMETER("000003", "Invalid parameter"),
    ACCESS_DENIED("000004", "Access denied"),
    UNAUTHORIZED("000005", "Unauthorized access"),
    RESOURCE_CONFLICT("000006", "Resource conflict");

    private String code;
    private String message;

    CommonError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
```

## Exception Types and HTTP Status Code Mapping

| Exception Class | HTTP Status Code | Description |
|-----------------|------------------|-------------|
| `BadRequestException` | 400 | Bad Request |
| `UnauthorizedException` | 401 | Unauthorized |
| `ForbiddenException` | 403 | Forbidden |
| `NotFoundException` | 404 | Not Found |
| `ConflictException` | 409 | Conflict |
| `BusinessException` | 500 | Business Logic Error |

## Error Response Format

All exceptions return a unified error response format:

```json
{
    "code": "000001",
    "message": "Request parameter error",
    "detail": "Specific error details",
    "date": "2024-01-01 12:00:00.000",
    "url": "http://localhost:8080/api/resource"
}
```

Field descriptions:
- `code`: Error code
- `message`: Error message
- `detail`: Detailed error information (optional)
- `date`: Error occurrence time
- `url`: Request URL

## Usage Examples

### Controller Example

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new NotFoundException(CommonError.USER_NOT_FOUND);
        }
        return user;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new ConflictException(CommonError.EMAIL_ALREADY_EXISTS);
        }
        return userService.create(request);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        User user = userService.findById(id);
        if (user == null) {
            throw new NotFoundException(CommonError.USER_NOT_FOUND);
        }
        return userService.update(id, request);
    }
}
```

### Custom Error Type Example

```java
public enum UserError implements ErrorType {
    USER_NOT_FOUND("100001", "User not found"),
    EMAIL_ALREADY_EXISTS("100002", "Email already exists"),
    INVALID_USER_ID("100003", "Invalid user ID");

    private String code;
    private String message;

    UserError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
```

## Technology Stack

- **Spring Boot**: 2.1.6.RELEASE
- **Java**: 1.8+
- **Lombok**: Code simplification
- **Apache Commons Lang3**: Utility library

## License

This project is licensed under the MIT License.

## Contributing

Issues and Pull Requests are welcome!

## Changelog

### v1.0.0
- Initial release
- Support for basic exception types
- Unified error response format
- Support for custom error types 