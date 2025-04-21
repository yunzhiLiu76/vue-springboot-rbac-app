- **MySQL 数据目录** 持久化到 `/root/mysql/data`
- **MySQL 配置文件** 挂载到 `/root/mysql/mysql-config/my.cnf`
- **网络模式** 设置为 `bridge`

为了让 MySQL **正确运行且无权限问题**，需要 **调整宿主机目录权限**，确保 **Docker 内的 `mysql` 进程能访问这些目录**

1.确保 MySQL 数据目录 `/root/mysql/data` 有正确权限

```
sudo mkdir -p /root/mysql/data
sudo chown -R 999:999 /root/mysql/data
sudo chmod -R 755 /root/mysql/data
```

**解释**：

- `mkdir -p /root/mysql/data` → 确保目录存在
- `chown -R 999:999 /root/mysql/data` → **MySQL 容器的用户 ID `999`（默认 `mysql` 用户）有权限访问**
- `chmod -R 755 /root/mysql/data` → **确保 `mysql` 用户有读写权限**

2.确保 MySQL 配置文件 `/root/mysql/mysql-config/my.cnf` 可读

```
sudo mkdir -p /root/mysql/mysql-config
sudo touch /root/mysql/mysql-config/my.cnf
sudo chown 999:999 /root/mysql/mysql-config/my.cnf
sudo chmod 644 /root/mysql/mysql-config/my.cnf
```

**解释**：

- `mkdir -p /root/mysql/mysql-config` → 确保目录存在
- `touch /root/mysql/mysql-config/my.cnf` → 确保 `my.cnf` 存在
- `chown 999:999 /root/mysql/mysql-config/my.cnf` → **确保 MySQL 容器内的用户 `999` 可以读取 `my.cnf`**
- `chmod 644 /root/mysql/mysql-config/my.cnf` → **设置 `my.cnf` 为 644（只读），防止修改错误**

确保 `3306` 端口未被占用

**确保 `docker` 组的用户能正常管理 MySQL 容器**

如果你的 **Jenkins** 或 **其他用户** 需要管理 MySQL 容器，确保它们属于 `docker` 组：

执行启动命令：

```
docker compose -f mysql-compose.yaml up -d
```







Mysql-compose.yaml如下

```

services:
  mysql:   # 必须在 services 下面！
    image: mysql:8.0
    container_name: mysql
    restart: always
    # 指定 默认的身份验证插件 为 mysql_native_password，用于管理 MySQL 用户的认证方式。
    # 从 MySQL 8.0 开始，默认的用户认证插件从 mysql_native_password 改为了 caching_sha2_password。
    # 但是，某些旧版本的 MySQL 客户端（如 Python mysql-connector、PHP mysqli 等）不支持 caching_sha2_password，会导致连接 MySQL 时出现 认证错误：
    # 但是我的应用支持 caching_sha2_password（如新版 mysql-connector-java、新版 PHP），可以不设置此参数。
    # 我的连接工具HeidiSQL也支持caching_sha2_password
     command: --default-authentication-plugin=mysql_native_password
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: shuan
    ports:
      - "3306:3306"
    volumes:
      - /root/mysql/data:/var/lib/mysql
      - /root/mysql/mysql-config/my.cnf:/etc/mysql/my.cnf
    networks:
      - mysql_network

volumes:
  mysql_data:

networks:
  mysql_network:
    driver: bridge

```

`docker-compose.yaml` 里使用

command: --default-authentication-plugin=mysql_native_password

这个命令 **只会影响 MySQL 新创建的用户**，但 **不会修改已有的 `root` 账户的身份验证方式**。

**MySQL 8.0 默认使用 `caching_sha2_password`**，所以 `root` 账户可能仍然使用 **`caching_sha2_password`**，而不是 `mysql_native_password`。

**解决方案：手动修改 `root` 账户的身份验证方式** 进入 MySQL 容器：

```
docker exec -it mysql mysql -u root -p
```

然后执行：

```
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
FLUSH PRIVILEGES;

```

