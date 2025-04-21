## 🧩 What Does This Class Do?

This class is a custom **JWT authentication and authorization filter**. It extends `OncePerRequestFilter`, meaning it only executes **once per HTTP request**.

Its main responsibility:

> **Intercept every HTTP request, extract the JWT token from the header, validate it, and set the authentication context for Spring Security.**

---

## 🧱 Injected Components

```java
@Value("${server.servlet.context-path}")
private String contextPathProperty;
```

- Reads the application's context path from `application.yml` or `application.properties`.

```java
private final UserAccountServiceImpl userDetailsService;
private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
```

- `userDetailsService`: Loads user info and permissions.
- `jwtAuthenticationEntryPoint`: Handles **unauthenticated** errors (401).
- `jwtAccessDeniedHandler`: Handles **access denied** errors (403).

---

## 🔁 Core Method: `doFilterInternal`

This method processes **every incoming HTTP request** and handles token-based authentication.

---

### ✅ Case 1: Skip Authentication for Swagger and Login Paths

```java
if (request.getRequestURI().startsWith("/cryptauth/login") || isSwaggerRequest(request)) {
    filterChain.doFilter(request, response);
    return;
}
```

- If the request is for login or Swagger docs, skip authentication and continue the filter chain.

---

### ✅ Case 2: Parse and Validate the Authorization Header (JWT)

```java
String token = request.getHeader("Authorization");

if (token != null && token.startsWith("Bearer ")) {
```

- Retrieves the `Authorization` header.
- Extracts the token by removing the `"Bearer "` prefix.

---

### 🔍 Token Handling and Permission Checking

#### Step 1: Extract Username

```java
String username = JwtUtil.extractUserName(token);
```

Parses the username from the token.

---

#### Step 2: Load User Info + Validate Token

```java
UserDetails userDetails = userDetailsService.loadUserByUsername(username);
if (JwtUtil.validateToken(token, userDetails)) {
```

- Loads user roles/authorities from the database.
- Validates the token (e.g., expiration, signature, and user match).

---

#### Step 3: Permission Check (Path-Based)

```java
if (!hasPermission(userDetails, request)) {
    jwtAccessDeniedHandler.handle(...);
    return;
}
```

- Calls the `hasPermission()` method to verify if the user has the right to access the requested URL.
- Permissions are based on authorities (e.g., URL prefixes).

---

#### Step 4: Set Authentication in SecurityContext

```java
UsernamePasswordAuthenticationToken authenticationToken = 
    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

SecurityContextHolder.getContext().setAuthentication(authenticationToken);
```

- Creates an authentication object and puts it into the Spring Security context.

---

### ⚠️ Exception Handling for Token Errors

- `ExpiredJwtException` → Token expired → respond with 401
- `JwtException` or `IllegalArgumentException` → Invalid token → respond with 401
- No token → respond with 401

Handled via:

```java
jwtAuthenticationEntryPoint.commence(request, response, new AuthenticationException(...))
```

This sends a unified JSON error response.

---

### 🧪 Special Route Handling: Return "CA success" for Gateway Check

```java
if (pathSegments[1].equals("cryptauth") && pathSegments[2].startsWith("scaffold")) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write("CA success");
    return;
}
```

- If the path is `/cryptauth/scaffold/**`, it's a request from the **API Gateway for CA validation**.
- Simply return HTTP 200 OK with a `"CA success"` message, no further processing needed.

---

## 🔧 Utility Methods Explained

### 1. `isSwaggerRequest`

Checks if the current request is a Swagger-related endpoint and should be excluded from authentication.

---

### 2. `hasPermission`

```java
return userDetails.getAuthorities().stream()
    .anyMatch(grantedAuthority -> strippedPath.startsWith(grantedAuthority.getAuthority()));
```

- Gets the user’s authorities and checks if any match the requested URL (after removing context path).
- Enables simple **path-based access control**.

---

### 3. `stripContextPath`

Removes the app’s context path (e.g., `/cryptauth`) from the beginning of the request URI for permission comparison.

---

## ✅ Summary Table

| Feature | Description |
|--------|-------------|
| JWT Parsing | Extracts and validates JWT tokens from Authorization headers |
| User Authentication | Loads user details from DB and validates the token |
| Permission Check | Verifies if user has permission to access the requested URL |
| Unified Exception Handling | Handles expired, invalid, or missing tokens with 401 |
| Swagger Whitelist | Skips authentication for Swagger endpoints |
| Gateway Support | For `/cryptauth/scaffold/**` requests, returns early with 200 OK if auth passed |

 😄


这段代码系统中用于处理 **JWT 登录认证和权限校验** 的核心过滤器 —— `JwtAuthenticationFilter`。它继承自 `OncePerRequestFilter`，意味着**每个 HTTP 请求只会被执行一次**。


## 🧩 这个类是做什么的？

它的主要职责是：

> **在用户访问受保护资源之前，从请求头中解析 JWT Token，校验合法性，并设置 Spring Security 上下文，确保用户身份认证通过。**

---

## 🧱 主要字段注入

```java
@Value("${server.servlet.context-path}")
private String contextPathProperty;
```

- 获取配置文件中的 `context-path`，用于后续权限校验中的路径剥离。

```java
private final UserAccountServiceImpl userDetailsService;
private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
```

- 用户信息服务：加载用户角色、权限。
- 自定义异常处理器：
  - `jwtAuthenticationEntryPoint` → 用于处理未登录
  - `jwtAccessDeniedHandler` → 用于处理权限不足

---

## 🔁 核心方法：`doFilterInternal`

这个方法会拦截每一次 HTTP 请求，并处理 Token 验证逻辑。

---

### ✅ 情况 1：跳过 Swagger 和登录路径的拦截

```java
if (request.getRequestURI().startsWith("/cryptauth/login") || isSwaggerRequest(request)) {
    filterChain.doFilter(request, response);
    return;
}
```

- 如果访问的是登录接口或 Swagger 文档，则直接放行，不进行认证处理。

---

### ✅ 情况 2：处理 Authorization 头部（JWT）

```java
String token = request.getHeader("Authorization");

if (token != null && token.startsWith("Bearer ")) {
```

- 从请求头中获取 `Bearer` 类型的 Token
- 去除 `"Bearer "` 前缀，提取真实 Token

---

### 🔍 核心处理流程（Token 校验 + 权限校验）

#### 第一步：提取用户名

```java
String username = JwtUtil.extractUserName(token);
```

从 Token 中解析用户名。

---

#### 第二步：加载用户信息 + 验证 Token

```java
UserDetails userDetails = userDetailsService.loadUserByUsername(username);
if (JwtUtil.validateToken(token, userDetails)) {
```

- 加载用户权限信息
- 校验 Token 是否有效（是否过期、是否匹配）

---

#### 第三步：权限校验（路径级）

```java
if (!hasPermission(userDetails, request)) {
    jwtAccessDeniedHandler.handle(...);
    return;
}
```

- 使用自定义的 `hasPermission` 方法判断用户是否有访问当前请求路径的权限
- 权限来源：用户拥有的 `GrantedAuthority`（即角色或资源路径）

---

#### 第四步：设置 Spring Security 认证信息

```java
UsernamePasswordAuthenticationToken authenticationToken = 
    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

SecurityContextHolder.getContext().setAuthentication(authenticationToken);
```

- 构造一个认证通过的 Token
- 存入 Security 上下文，供后续使用

---

### ⚠️ 错误处理：Token 异常捕获

- `ExpiredJwtException` → Token 过期，返回 401
- `JwtException` / `IllegalArgumentException` → Token 不合法，返回 401
- 没有 Token → 返回 401

通过：

```java
jwtAuthenticationEntryPoint.commence(request, response, new AuthenticationException(...))
```

调用自定义处理器返回统一 JSON 错误响应。

---

### 🧪 特殊路径检测：认证通过后直接返回 "CA success"

```java
if (pathSegments[1].equals("cryptauth") && pathSegments[2].startsWith("scaffold")) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write("CA success");
    return;
}
```

- 如果路径是 `/cryptauth/scaffold/**`，表示这是网关转发来的 CA 校验请求，认证通过就直接返回 200，无需继续转发。

---

## 🔧 工具方法说明

### 1. `isSwaggerRequest`

判断当前请求是不是 Swagger 文档相关的内容（用于免登录访问）

---

### 2. `hasPermission`

```java
return userDetails.getAuthorities().stream()
    .anyMatch(grantedAuthority -> strippedPath.startsWith(grantedAuthority.getAuthority()));
```

- 把用户的权限拿出来，判断是否匹配当前请求路径（用于路径级权限控制）

---

### 3. `stripContextPath`

去除请求路径前缀（如 `/cryptauth`），方便做权限匹配时使用相对路径。

---

## ✅ 总结

| 功能点 | 描述 |
|--------|------|
| JWT Token 解析 | 从请求头中提取并解析 Token |
| 用户认证 | 从数据库加载用户信息，并验证 Token 是否有效 |
| 权限控制 | 判断用户是否有权限访问当前路径 |
| 异常处理 | 统一处理过期、无效 Token，以及未授权访问等异常 |
| Swagger 免认证 | 自动跳过文档接口的 Token 校验 |
| 特殊路径响应 | `/cryptauth/scaffold/**` 认证通过直接返回 "CA success" |



