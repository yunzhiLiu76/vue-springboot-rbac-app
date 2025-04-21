# 🚀 Vue-SpringBoot-RBAC-App

前后端分离综合性权限系统 | **Vue2 + Spring Boot + Spring Cloud + JWT + RBAC**

---

## 📦 项目简介 Project Overview

本项目是一套 **前后端分离架构** 的综合性应用系统：

- ✨ **前端**：基于 Vue.js + Vuex + Vue-Router + Element UI 开发 。
- 🚀 **后端**：基于 Spring Boot 3.3.5、Spring Cloud 构建微服务体系，支持服务注册、鉴权、接口级权限控制。
- 🛡️ **权限控制**：采用 JWT + Spring Security，实现基于角色的访问控制模型（RBAC），支持接口级权限。
- 🧠 **架构特性**：分布式服务，组件解耦，适用于中后台管理系统。

---

## 🧭 微服务模块 Microservices

| 模块名 | 描述 |
|--------|------|
| `cryptauth` | 认证与权限服务 |
| `scaffold` | 业务服务（示例） |
| `mqworker` | 消息队列工作者 |
| `gateway` | 网关服务，统一入口 |
| `eurekaserver` | 注册中心 |

---

## ⚙️ 本地运行配置 Local Environment Setup

请打开以下各微服务模块的 `application.properties` 文件，进行如下修改：

```properties
# ✅ 注释掉远程注册中心
# eureka.client.service-url.defaultZone=${EUREKA_DEFAULT_ZONE:http://8.211.38.135:8085/eureka/}

# ✅ 启用本地注册中心
eureka.client.service-url.defaultZone=${EUREKA_DEFAULT_ZONE:http://localhost:8085/eureka/}
```

---

## 🔐 账号信息 & 地址入口

| 项目 | 地址 | 用户名 | 密码 |
|------|------|--------|------|
| 🖥️ 系统登录 | [http://8.211.38.135/](http://8.211.38.135/) | `13800000002` | `123456` |
| GitLab | [http://8.211.40.36:28080/](http://8.211.40.36:28080/) | `root` | `AaBb9213` |
| Harbor | [http://8.211.40.36:8083/](http://8.211.40.36:8083/) | `admin` | `Harbor12345` |
| Nexus | [http://8.211.63.6:8091/](http://8.211.63.6:8091/) | `admin` | `Nexus123` |
| Jenkins | [http://8.211.63.6:8090/](http://8.211.63.6:8090/) | `admin` | `admin123` |
| SonarQube | [http://8.211.40.36:9000/](http://8.211.40.36:9000/) | `admin` |  `123456` |

---

## 📂 微服务代码地址 Git Repositories

| 服务 | Git 地址 |
|------|----------|
| `eurekaserver` | http://8.211.40.36:28080/com.shuangshuan/eurekaserver.git |
| `gateway` | http://8.211.40.36:28080/com.shuangshuan/gateway.git |
| `cryptauth` | http://8.211.40.36:28080/com.shuangshuan/cryptauth.git |
| `scaffold` | http://8.211.40.36:28080/com.shuangshuan/scaffold.git |
| `mqworker` | http://8.211.40.36:28080/com.shuangshuan/mqworker.git |

---

## 📚 Swagger 接口文档

| 模块 | Swagger 地址 |
|------|--------------|
| `scaffold` | [http://8.211.38.135:8084/scaffold/swagger-ui/index.html](http://8.211.38.135:8084/scaffold/swagger-ui/index.html) |
| `cryptauth` | [http://8.211.38.135:8082/cryptauth/swagger-ui/index.html](http://8.211.38.135:8082/cryptauth/swagger-ui/index.html) |

---

## 🧰 工具服务

| 服务 | 地址 | 用户名 | 密码 |
|------|------|--------|------|
| RabbitMQ | [http://101.200.32.138:15672/](http://101.200.32.138:15672/) | `admin` | `admin` |

⚠️ RabbitMQ 用户名和密码暂未写入配置文件，后续计划统一接入配置中心管理。

---

## 🛠️ 技术栈 Technology Stack

### 前端
- Vue.js 2.x
- Vuex / Vue-Router
- Element UI
- Axios
- Store

### 后端
- Spring Boot 3.3.5
- Spring Cloud
- Spring Security + JWT
- MySQL / Redis / Swagger 
- 可扩展支持：RabbitMQ、MongoDB、Elasticsearch（计划中）


## 🧑‍💼 致面试官 | Dear Interviewer

尊敬的面试官您好：

您目前所看到的这个项目，是我们团队从 0 到 1 独立搭建完成的，能够有机会呈现给您，我们感到非常荣幸。我非常期待能加入贵公司，在未来的工作中不断学习、共同成长，并为团队贡献自己的力量。

衷心感谢您对本项目的关注与宝贵时间！


📌 **备注 Reminder：**
> MySQL 数据未部署在安全环境中（如未配置防火墙或安全组限制），存在被攻击风险。若项目接口偶尔连接失败，请尝试本地运行方式或联系作者。



---

# 🚀 Vue-SpringBoot-RBAC-App

A Full-Stack Role-Based Access Control System | **Vue2 + Spring Boot + Spring Cloud + JWT + RBAC**

---

## 📦 Project Overview

This project is a comprehensive **front-end and back-end separated architecture** application system featuring:

- ✨ **Frontend**: Developed using Vue.js + Vuex + Vue-Router + Element UI.
- 🚀 **Backend**: Built with Spring Boot 3.3.5 and Spring Cloud to support microservices including service registration, authentication, and interface-level access control.
- 🛡️ **Access Control**: Utilizes JWT + Spring Security to implement a Role-Based Access Control (RBAC) model with API-level permission control.
- 🧠 **Architecture Highlights**: Distributed services, decoupled components — ideal for admin and enterprise management systems.

---

## 🧭 Microservices

| Module        | Description                  |
|---------------|------------------------------|
| `cryptauth`   | Authentication and auth services |
| `scaffold`    | Business service (example)   |
| `mqworker`    | Message queue worker         |
| `gateway`     | API Gateway, unified entry   |
| `eurekaserver`| Service registry center      |

---

## ⚙️ Local Environment Setup

Open each microservice module’s `application.properties` file and make the following changes:

```properties
# ✅ Comment out the remote Eureka registry
# eureka.client.service-url.defaultZone=${EUREKA_DEFAULT_ZONE:http://8.211.38.135:8085/eureka/}

# ✅ Enable the local Eureka registry
eureka.client.service-url.defaultZone=${EUREKA_DEFAULT_ZONE:http://localhost:8085/eureka/}
```

---

## 🔐 Accounts & Entry Points

| Service        | URL                                   | Username       | Password       |
|----------------|----------------------------------------|----------------|----------------|
| 🖥️ System Login | [http://8.211.38.135/](http://8.211.38.135/) | `13800000002`  | `123456`       |
| GitLab         | [http://8.211.40.36:28080/](http://8.211.40.36:28080/) | `root`         | `AaBb9213`     |
| Harbor         | [http://8.211.40.36:8083/](http://8.211.40.36:8083/)   | `admin`        | `Harbor12345`  |
| Nexus          | [http://8.211.63.6:8091/](http://8.211.63.6:8091/)     | `admin`        | `Nexus123`     |
| Jenkins        | [http://8.211.63.6:8090/](http://8.211.63.6:8090/)     | `admin`        | `admin123`     |
| SonarQube      | [http://8.211.40.36:9000/](http://8.211.40.36:9000/)   | `admin`        | `123456`|

---

## 📂 Git Repositories

| Service       | Git URL                                                                 |
|---------------|-------------------------------------------------------------------------|
| `eurekaserver`| http://8.211.40.36:28080/com.shuangshuan/eurekaserver.git               |
| `gateway`     | http://8.211.40.36:28080/com.shuangshuan/gateway.git                    |
| `cryptauth`   | http://8.211.40.36:28080/com.shuangshuan/cryptauth.git                  |
| `scaffold`    | http://8.211.40.36:28080/com.shuangshuan/scaffold.git                   |
| `mqworker`    | http://8.211.40.36:28080/com.shuangshuan/mqworker.git                   |

---

## 📚 Swagger API Docs

| Module        | Swagger UI URL                                                                 |
|---------------|--------------------------------------------------------------------------------|
| `scaffold`    | [http://8.211.38.135:8084/scaffold/swagger-ui/index.html](http://8.211.38.135:8084/scaffold/swagger-ui/index.html) |
| `cryptauth`   | [http://8.211.38.135:8082/cryptauth/swagger-ui/index.html](http://8.211.38.135:8082/cryptauth/swagger-ui/index.html) |

---

## 🧰 DevOps Tools & Services

| Service    | URL                                           | Username | Password     |
|------------|-----------------------------------------------|----------|--------------|
| RabbitMQ   | [http://101.200.32.138:15672/](http://101.200.32.138:15672/) | `admin`  | `admin`      |

⚠️ Note: RabbitMQ credentials are not yet managed via config center; plan to centralize in future updates.

---

## 🛠️ Technology Stack

### Frontend
- Vue.js 2.x
- Vuex / Vue-Router
- Element UI
- Axios
- Store

### Backend
- Spring Boot 3.3.5
- Spring Cloud
- Spring Security + JWT
- MySQL / Redis / Swagger
- Optional/Planned support: RabbitMQ, MongoDB, Elasticsearch

---

## 🧑‍💼 Dear Interviewer

The project you are reviewing was independently built by our team from scratch. We are honored to present it to you and I personally look forward to the opportunity to join your company — to learn, grow, and contribute as part of your team.

Thank you sincerely for your time and attention!

---

📌 **Reminder:**
> The MySQL database is not deployed in a secure production environment (e.g. firewall or security group restrictions may be missing), which might lead to occasional connection failures. If you encounter issues accessing interfaces, please try running it locally or contact the author.


