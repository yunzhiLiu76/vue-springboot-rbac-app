---

## 🌍 Overview

This is a **Spring Cloud Gateway configuration class**, where you define routing logic. The main goal is:

Intercept all `/scaffold/**` requests, validate them through the `cryptauth` (CA Authentication) service first, and only forward them to the business service if authentication succeeds.

---

## 📦 Structure

```java
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, DiscoveryClient discoveryClient) {
        ...
    }
}
```

- It's a Spring `@Configuration` class.
- Defines a custom route locator using `RouteLocatorBuilder`.
- Also injects `DiscoveryClient` to dynamically fetch service instances (via Eureka or similar).

---

## 🛣️ Two Routes Defined

### 1. `cryptauth_service`

```java
.route("cryptauth_service", r -> r.path("/cryptauth/**")
        .uri("lb://cryptauth"))
```

- Matches requests starting with `/cryptauth/**`
- Simply **forwards** them using load balancing to the service registered as `cryptauth`.

---

### 2. `scaffold_service` (The Main Logic)

```java
.route("scaffold_service", r -> r.path("/scaffold/**")
        .filters(f -> f.filter((exchange, chain) -> {
            ...
        }))
        .uri("lb://scaffold"))
```

This is the core feature:

- Matches requests like `/scaffold/**`
- Adds a **custom inline filter** to perform pre-authentication
- Finally, forwards to the `scaffold` service if authentication succeeds

---

## 🔐 What the Filter Does

Before calling the business API, it makes a separate call to the `cryptauth` service for authorization.

### Step-by-step:

#### 1. Get `cryptauth` instances from Discovery Client

```java
List<ServiceInstance> instances = discoveryClient.getInstances("cryptauth");
```

- Dynamically retrieves available service instances.
- Ensures you get real-time service addresses.

#### 2. Build the actual authentication URL

```java
String authUrl = caUrl + "/cryptauth" + exchange.getRequest().getURI().getPath();
```

Example:

- Incoming request: `/scaffold/api/v1/user`
- Converted auth request: `http://<cryptauth_instance>/cryptauth/scaffold/api/v1/user`

#### 3. Use `WebClient` to make the call

```java
WebClient.create().get()
    .uri(authUrl)
    .headers(headers -> headers.addAll(exchange.getRequest().getHeaders()))
```

- Reuses the original request headers (especially Authorization tokens)
- Sends an async, non-blocking GET request to the auth service

#### 4. Handle the response

```java
if (response.statusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED) ||
    response.statusCode().isSameCodeAs(HttpStatus.FORBIDDEN))
```

- If the response is `401` or `403` (Unauthorized/Forbidden):
  - It builds a JSON response body with a code and error message
  - Sends that back directly to the client, skipping the actual business request

Example JSON response:

```json
{
  "code": 401,
  "message": "Unauthorized - Token Invalid or Expired"
}
```

- If the response is OK (authenticated), it continues the original request:

```java
return chain.filter(exchange);
```

---

## ✅ Summary Table

| Route Pattern       | Auth Required? | Forwards To        |
|---------------------|----------------|--------------------|
| `/cryptauth/**`     | ❌ No           | `cryptauth` service |
| `/scaffold/**`      | ✅ Yes          | `scaffold` service  |

---

## 💡 Highlights

- Uses **Eureka + Spring LoadBalancer** (`lb://`) to discover services dynamically
- Custom filter integrates **authorization logic** directly into the Gateway layer
- Async non-blocking call with **WebClient**
- Returns JSON error responses with standard status codes
- Easily extensible for additional business logic



这段代码是一个 Spring Cloud Gateway 的配置类，核心目标是：

> **在请求 `scaffold` 服务之前，先通过 `cryptauth` 服务进行认证（CA认证），只有认证通过才允许访问业务服务。**

---

### 🌍 文件结构解析

```java
@Configuration
public class GatewayConfig {
    ...
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, DiscoveryClient discoveryClient) {
        ...
    }
}
```

这是一个 Spring 的配置类，通过 `@Bean` 注册了一个自定义的 `RouteLocator`，定义了网关的路由规则。

---

## 🛣️ 定义了两个路由

### 1. `cryptauth_service`
```java
.route("cryptauth_service", r -> r.path("/cryptauth/**")
        .uri("lb://cryptauth"))
```

- 匹配请求路径：`/cryptauth/**`
- 直接负载均衡转发到注册中心中名为 `cryptauth` 的服务

---

### 2. `scaffold_service`
```java
.route("scaffold_service", r -> r.path("/scaffold/**")
        .filters(f -> f.filter((exchange, chain) -> {
            ...
        }))
        .uri("lb://scaffold"))
```

这是重点：

- 匹配请求路径：`/scaffold/**`
- **添加了一个自定义的认证过滤器**
- 最终目标是转发到：`lb://scaffold`（业务服务）

---

## 🔐 过滤器做了什么？

在请求 `scaffold` 服务前，先调用 `cryptauth` 服务做认证：

### 步骤分析：

#### 1. 获取 `cryptauth` 的地址
```java
List<ServiceInstance> instances = discoveryClient.getInstances("cryptauth");
```
通过 `DiscoveryClient` 动态获取 `cryptauth` 服务的实例（支持多节点）。

#### 2. 构建认证地址
```java
String authUrl = caUrl + "/cryptauth" + exchange.getRequest().getURI().getPath();
```
比如原始请求是：
```
/scaffold/api/v1/user
```
认证请求就会变成：
```
http://<cryptauth 实例>/cryptauth/scaffold/api/v1/user
```

#### 3. 发起认证请求
```java
WebClient.create().get()
    .uri(authUrl)
    .headers(headers -> headers.addAll(exchange.getRequest().getHeaders()))
```
- 复用原请求的 Header（比如 Token）
- 向认证服务发送 GET 请求

#### 4. 响应处理逻辑
```java
if (response.statusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED) ||
    response.statusCode().isSameCodeAs(HttpStatus.FORBIDDEN))
```

- 如果认证失败（401 或 403）：
  - 返回 JSON 错误响应（包含 code 和 message）

```java
Map<String, Object> errorResponse = Map.of(
    "code", 401,
    "message", "Unauthorized - Token Invalid or Expired"
);
```

- 如果认证通过，则继续执行原始请求：
```java
return chain.filter(exchange);
```

---

## 📦 总体功能总结

| 请求路径 | 认证？ | 转发到 |
|----------|--------|--------|
| `/cryptauth/**` | ❌ 直接转发 | `cryptauth` 服务 |
| `/scaffold/**` | ✅ 先认证再转发 | `scaffold` 服务（业务） |

---

## ✅ 特点/亮点

- 动态获取服务地址，适配微服务集群
- 使用 `WebClient` 做认证，异步非阻塞
- 支持标准 HTTP 状态码 + JSON 响应
- 自定义过滤器灵活插入认证逻辑
- `lb://` 结合 Eureka 做服务发现和负载均衡





