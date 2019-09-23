API应该使用HTTP状态码还是全部返回200？

1. 全部返回200，然后通过自定义HTTP response body来定义不同的错误类型。
2. 遵循HTTP的响应码，按不同的错误返回相应的HTTP状态码。

那么哪种方案更好的呢？这个问题在`stackoverflow`、`知乎`、`v2ex`都有大量的讨论帖。在此不争论。

本人其实更倾向于第二种方案。首先HTTP作为通用标准，是更容易遵循的，也更容易理解的，自定义的code在小组，小部门好规范，但是再大就不容易规范了。其次业界大部分的通用框架都是会兼容HTTP标准的。以前做`监控系统`，规范的HTTP响应码，会使收集的Nginx访问日志更具有实际意义。很多`分布式调用链框架`一般也遵循HTTP协议来处理错误，自定义错误码一般就需要自身再做额外的扩展。

本项目就是基于`SpringBoot`的`异常处理机制`来将异常统一转换成相应的HTTP状态码作为响应。同时在异常时返回相应的消息体。

`使用异常而非返回码`   --- 《代码整洁之道》

调用方法中遇到错误立刻抛出异常很方便，而且代码会很整洁，代码逻辑也不会被错误处理搞乱。那些繁琐的状态码转换就交给通用工具去完成吧。我们只关心异常。



所以你只需要做以下事情：



## 一、引入maven包



## 二、EnableAutoExceptionHandler

在SpringBoot启动类上添加`@EnableAutoExceptionHandler`

```
@SpringBootApplication
@EnableAutoExceptionHandler
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```



## 三、抛出异常

```java
throw new NotFoundException(ErrorType.NOT_FOUND);
```



## 四、自定义ErrorType

可以通过实现ErrorType接口来自定义错误类型。建议使用`enum`在定义错误类型。可以参考项目`CommonError`的实现。

```java
public enum CommonError implements ErrorType {
    REQUEST_ERROR("000001", "request error"),
    SERVER_ERROR("000002", "server error")

    ;
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



## 五、选择异常类

**BadRequestException:** 400

**UnauthorizedException**: 401

**ForbiddenException** : 403

**NotFoundException**: 404

**ConflictException:** 409
