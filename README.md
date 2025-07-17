# Spring Exception Handler

[English](./README_EN.md) | 中文

## 项目简介

Spring Exception Handler 是一个基于 Spring Boot 的统一异常处理框架，旨在简化 API 开发中的异常处理逻辑。该框架遵循 HTTP 标准，将异常自动转换为相应的 HTTP 状态码，同时提供统一的错误响应格式。

## 设计理念

### 为什么选择 HTTP 状态码？

在 API 设计中，有两种常见的错误处理方案：

1. **全部返回 200**：通过自定义 HTTP response body 来定义不同的错误类型
2. **遵循 HTTP 响应码**：按不同的错误返回相应的 HTTP 状态码

本项目采用第二种方案，原因如下：

- **标准化**：HTTP 作为通用标准，更容易遵循和理解
- **兼容性**：业界大部分通用框架都兼容 HTTP 标准
- **可观测性**：规范的 HTTP 响应码使 Nginx 访问日志更具实际意义
- **分布式支持**：分布式调用链框架一般遵循 HTTP 协议处理错误

### 使用异常而非返回码

> "使用异常而非返回码" --- 《代码整洁之道》

在方法中遇到错误时立即抛出异常，代码会更加整洁，逻辑也不会被错误处理搞乱。繁琐的状态码转换工作交给通用工具完成，开发者只需关注业务逻辑。

## 功能特性

- ✅ 自动异常处理：基于 Spring Boot 的 `@ControllerAdvice` 机制
- ✅ HTTP 状态码映射：异常自动转换为标准 HTTP 状态码
- ✅ 统一错误格式：标准化的错误响应结构
- ✅ 自定义错误类型：支持通过枚举定义业务错误
- ✅ 详细错误信息：包含错误代码、消息、详情、时间戳等
- ✅ 日志记录：自动记录异常信息便于调试

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.tme.uni</groupId>
    <artifactId>spring-exception-handler</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 启用自动异常处理

在 Spring Boot 启动类上添加 `@EnableAutoExceptionHandler` 注解：

```java
@SpringBootApplication
@EnableAutoExceptionHandler
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3. 抛出异常

在业务代码中直接抛出相应的异常：

```java
// 资源未找到
throw new NotFoundException(ErrorType.NOT_FOUND);

// 请求参数错误
throw new BadRequestException(ErrorType.INVALID_PARAMETER);

// 权限不足
throw new ForbiddenException(ErrorType.ACCESS_DENIED);

// 未授权
throw new UnauthorizedException(ErrorType.UNAUTHORIZED);

// 资源冲突
throw new ConflictException(ErrorType.RESOURCE_CONFLICT);
```

### 4. 自定义错误类型

通过实现 `ErrorType` 接口来自定义错误类型，建议使用枚举：

```java
public enum CommonError implements ErrorType {
    REQUEST_ERROR("000001", "请求参数错误"),
    SERVER_ERROR("000002", "服务器内部错误"),
    INVALID_PARAMETER("000003", "无效的参数"),
    ACCESS_DENIED("000004", "访问被拒绝"),
    UNAUTHORIZED("000005", "未授权访问"),
    RESOURCE_CONFLICT("000006", "资源冲突");

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

## 异常类型与 HTTP 状态码映射

| 异常类 | HTTP 状态码 | 说明 |
|--------|-------------|------|
| `BadRequestException` | 400 | 请求参数错误 |
| `UnauthorizedException` | 401 | 未授权 |
| `ForbiddenException` | 403 | 禁止访问 |
| `NotFoundException` | 404 | 资源未找到 |
| `ConflictException` | 409 | 资源冲突 |
| `BusinessException` | 500 | 业务逻辑错误 |

## 错误响应格式

所有异常都会返回统一的错误响应格式：

```json
{
    "code": "000001",
    "message": "请求参数错误",
    "detail": "具体的错误详情",
    "date": "2024-01-01 12:00:00.000",
    "url": "http://localhost:8080/api/resource"
}
```

字段说明：
- `code`: 错误代码
- `message`: 错误消息
- `detail`: 详细错误信息（可选）
- `date`: 错误发生时间
- `url`: 请求的 URL

## 使用示例

### 控制器示例

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

### 自定义错误类型示例

```java
public enum UserError implements ErrorType {
    USER_NOT_FOUND("100001", "用户不存在"),
    EMAIL_ALREADY_EXISTS("100002", "邮箱已存在"),
    INVALID_USER_ID("100003", "无效的用户ID");

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

## 技术栈

- **Spring Boot**: 2.1.6.RELEASE
- **Java**: 1.8+
- **Lombok**: 简化代码
- **Apache Commons Lang3**: 工具类库

## 许可证

本项目采用 MIT 许可证。

## 贡献

欢迎提交 Issue 和 Pull Request！

## 更新日志

### v1.0.0
- 初始版本发布
- 支持基础异常类型
- 提供统一的错误响应格式
- 支持自定义错误类型
