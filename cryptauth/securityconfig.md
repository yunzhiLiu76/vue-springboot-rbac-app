## 📌 Purpose of the Class

This class is the core **Spring Security configuration**. It provides:

- ✅ JWT-based login and authentication  
- ✅ Request-level permission control (which endpoints require login, which don't)  
- ✅ Custom exception handling (authentication failure, access denied)  
- ✅ Cross-Origin Resource Sharing (CORS) configuration  
- ✅ Fully **stateless session** handling (ideal for front-end/back-end separation)

---

## 🧱 Class Structure

```java
@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
```

- `@Configuration`: Marks it as a Spring configuration class  
- `@EnableMethodSecurity`: Enables method-level security annotations like `@Secured`, `@RolesAllowed`

---

### 👇 Injected Components

```java
private final UserAccountServiceImpl userDetailsService;
private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
```

These components serve the following roles:

- **userDetailsService**: Custom service for loading user details (roles, credentials, etc.)
- **jwtAuthenticationEntryPoint**: Handles cases when the user is **not authenticated** (returns 401)
- **jwtAccessDeniedHandler**: Handles cases when the user is **authenticated but lacks permission** (returns 403)

---

## 🔐 JWT Authentication Filter

```java
@Bean
public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(...);
}
```

- Defines your custom `JwtAuthenticationFilter`, which intercepts every request and validates the JWT token from the header.

---

## 🛡️ Core Security Configuration — `filterChain`

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
```

### ✅ Main Configurations:

#### 1. Disable CSRF

```java
http.csrf(AbstractHttpConfigurer::disable)
```

CSRF protection is disabled because this is a stateless REST API using tokens, not cookies/sessions.

---

#### 2. Define Authorization Rules

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    .requestMatchers("/login/**", "/swagger-ui/**", ...).permitAll()
    .anyRequest().authenticated()
)
```

- Allows all OPTIONS preflight requests (for CORS)
- Permits open access to login and Swagger API docs
- All other endpoints require authentication

---

#### 3. Add JWT Filter

```java
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
```

Your JWT filter is inserted into the Spring Security filter chain before the default login filter.

---

#### 4. Exception Handling

```java
.exceptionHandling(e -> e
    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
    .accessDeniedHandler(jwtAccessDeniedHandler)
)
```

- If not logged in → `jwtAuthenticationEntryPoint` returns a 401 error  
- If permission is denied → `jwtAccessDeniedHandler` returns a 403 error

---

#### 5. Stateless Session

```java
.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

This ensures that **no HTTP session is created**. Each request must carry its own authentication token (e.g. JWT).

---

#### 6. Enable CORS

```java
http.cors(c -> c.configurationSource(corsConfigurationSource()));
```

Enables your custom CORS configuration (see below).

---

## 🌐 CORS Configuration

```java
@Bean
public CorsConfigurationSource corsConfigurationSource()
```

Allows front-end applications (Vue, etc.) hosted on different domains/IPs to access your backend API.

- Multiple allowed origins: IPs, localhost, etc.
- All HTTP methods allowed
- All headers allowed
- Credentials allowed (like cookies or Authorization headers)

---

## 🔑 Password Encoder (For Login/Register)

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Uses BCrypt to securely hash user passwords (recommended by Spring Security).

---

## 🧰 AuthenticationManager (Used During Login)

```java
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception
```

- Configures `userDetailsService` and password encoder
- Returns a custom `AuthenticationManager` for validating login credentials

---

## ❌ Ignoring Swagger and Static Resources

```java
@Bean
public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(...);
}
```

These paths will be excluded from Spring Security, allowing access without authentication (e.g., API docs).

---

## ✅ Summary of Features

| Feature                     | Description                                                                 |
|-----------------------------|-----------------------------------------------------------------------------|
| JWT Authentication          | Validates JWT token on every request                                        |
| Custom Exception Handling   | Returns proper JSON for 401/403 errors                                      |
| Stateless Session           | No server session; every request must include a token                       |
| CORS Support                | Allows frontend apps from other origins to access APIs                      |
| Swagger Support             | Allows unauthenticated access to API docs                                   |
| Password Security           | Uses BCrypt hashing to store passwords safely                               |
| Method-Level Security       | Supports `@Secured`, `@RolesAllowed`, and similar annotations               |





## 📌 类的作用

这个类是 Spring Security 的核心配置类，目的是为整个微服务系统提供：

- ✅ 基于 JWT 的登录认证机制  
- ✅ 配置请求权限控制（哪些接口需要登录、哪些不需要）  
- ✅ 自定义异常处理（认证失败、权限不足）  
- ✅ 跨域（CORS）处理  
- ✅ 完全无状态的 Session（适合前后端分离架构）

---

## 🧱 类结构说明

```java
@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
```

- `@Configuration`：这是一个配置类，会被 Spring 扫描加载  
- `@EnableMethodSecurity`：启用方法级别的权限控制（可以在 Controller 或 Service 上使用 `@Secured`, `@RolesAllowed`）

---

### 👇 构造方法中注入了三个依赖：

```java
private final UserAccountServiceImpl userDetailsService;
private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
```

这些组件的作用分别是：

- **userDetailsService**：自定义用户信息加载服务（基于用户名加载权限等信息）
- **jwtAuthenticationEntryPoint**：处理“未登录”情况下的异常（返回 401）
- **jwtAccessDeniedHandler**：处理“没有权限访问”时的异常（返回 403）

---

## 🔐 JWT 认证过滤器配置

```java
@Bean
public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(...);
}
```

- 这个自定义的 `JwtAuthenticationFilter` 会拦截每个请求，从请求头中提取 Token，进行认证校验。

---

## 🧱 核心安全配置：`filterChain`

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
```

### ✅ 主要配置项如下：

#### 1. 关闭 CSRF

```java
http.csrf(AbstractHttpConfigurer::disable)
```

因为我们使用的是 Token，而不是 Session + Cookie，所以 CSRF（跨站请求伪造）防护可以关闭。

---

#### 2. 配置 URL 权限规则

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    .requestMatchers("/login/**", "/swagger-ui/**", ...).permitAll()
    .anyRequest().authenticated()
)
```

- 允许所有的 OPTIONS 请求（解决跨域的预检请求问题）
- 允许 `/login/**` 和 Swagger 文档地址无需认证
- 其他所有接口必须登录才能访问

---

#### 3. 添加 JWT 过滤器

```java
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
```

将自定义的 JWT 过滤器插入到 Spring Security 的认证流程中。

---

#### 4. 异常处理配置

```java
.exceptionHandling(e -> e
    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
    .accessDeniedHandler(jwtAccessDeniedHandler)
)
```

- 登录失败（没有 token）会由 `jwtAuthenticationEntryPoint` 处理，返回 401  
- 登录了但没有权限的请求会由 `jwtAccessDeniedHandler` 处理，返回 403

---

#### 5. 配置为无状态模式

```java
.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

这行配置非常重要，表示 **不使用 HTTP 会话**，每个请求都必须携带认证信息（如 JWT）。

---

#### 6. 启用跨域（CORS）

```java
http.cors(c -> c.configurationSource(corsConfigurationSource()));
```

启用了自定义的跨域配置，前端可以在其他端口访问这个后端接口。

---

## 🌐 跨域配置方法

```java
@Bean
public CorsConfigurationSource corsConfigurationSource()
```

配置了允许以下来源访问：

- `http://localhost:9528`
- `http://8.211.38.135`
- `http://192.168.175.118:9528`

允许所有请求方法（GET、POST、DELETE 等），允许所有请求头，允许带 cookie 或 token。

---

## 🔑 密码加密器（用于注册/登录）

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

使用 Spring 推荐的 BCrypt 算法对密码进行加密（可防止暴力破解）

---

## 🧰 认证管理器（用于登录）

```java
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception
```

- 指定了 `userDetailsService` 和密码加密器
- 这个认证器会被用于用户登录校验（Username + Password）

---

## 🔍 忽略静态资源（如 Swagger 文档）

```java
@Bean
public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(...);
}
```

这些路径将被 Spring Security 忽略，不会进入认证流程。

---

## ✅ 总结功能

| 功能项               | 描述                                                                 |
|----------------------|----------------------------------------------------------------------|
| JWT 登录认证          | 拦截每个请求，校验 JWT 是否有效                                        |
| 自定义异常处理        | 提供 401、403 错误时的 JSON 响应                                        |
| 无状态会话支持        | 每次请求都必须带上 Token，不依赖服务器 Session                           |
| 支持 CORS 跨域        | 前端 Vue 项目等可跨域访问                                               |
| 支持 Swagger 接口文档 | 配置了无需认证可访问文档，便于开发和调试                                  |
| 加密密码              | 注册或登录时密码使用 BCrypt 加密，提升安全性                              |




