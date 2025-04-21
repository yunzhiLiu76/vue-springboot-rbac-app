### **`/root/es/data` 可能会遇到权限问题**

- **Elasticsearch 以 `elasticsearch` 用户（UID 1000）运行**，但 `/root/es/data` 可能默认是 `root` 权限，Elasticsearch **可能无法写入数据**。

- 如果容器启动时报 **`Permission denied`** 或 `failed to obtain lock`，需要 **手动修改权限**。

  修改方法：

  ```
  sudo mkdir -p /root/es/data
  sudo chown -R 1000:1000 /root/es/data
  sudo chmod -R 777 /root/es/data  # 给予写入权限
  mkdir -p /root/es/config
  touch /root/es/config/elasticsearch.yml
chown -R 1000:1000 /root/es/config
  chmod -R 644 /root/es/config/elasticsearch.yml
```
  
Elasticsearch **需要 `vm.max_map_count` 至少为 `262144`**，否则可能会在运行时出现 **内存映射错误**。要 **永久生效**，你需要正确修改系统参数。
  
  修改 `/etc/sysctl.conf`
  
```
  vi /etc/sysctl.conf
```
  
  **在文件末尾添加**：
  
```
  vm.max_map_count=262144
```
  
  **保存并生效**：
  
```
  sudo sysctl -p
```
  
  验证是否生效
  
```
  sysctl vm.max_map_count
```
  
  使用 `docker stats` 监控 CPU 和内存
  
```
  docker stats elasticsearch
```
  
- **`CPU %`**：如果 CPU 限制生效，你会发现 **不会超过 `1.00`（表示 1 核心）**
  
- **`MEM USAGE / LIMIT`**：应该看到 `LIMIT` 是 `1.00GB`（如果你限制了 `1GB`）
  
    示例输出（资源限制成功）
  
  ```
  CONTAINER ID   NAME            CPU %   MEM USAGE / LIMIT   NET I/O   BLOCK I/O   PIDS
3f1c2a8e9c2d   elasticsearch   80.5%   900MiB / 1GiB       1.2MB / 1.5MB   0B / 0B   30
  
  ```
  
  如果 `MEM LIMIT` 不是 `1GiB`，说明 `docker-compose.yaml` 里的限制没生效

Elasticsearch 默认监听 **9200 端口**，可以用 `curl` 测试：(这里建议浏览器直接访问http://192.168.175.164:9200)

```
curl -X GET "http://192.168.175.164:9200/"

```

**如果 Elasticsearch 运行成功，返回类似 JSON 响应：**

```
{
  "name": "elasticsearch",
  "cluster_name": "docker-cluster",
  "version": {
    "number": "8.5.1",
    "build_flavor": "default"
  },
  "tagline": "You Know, for Search"
}

```

或者在浏览器输入

192.168.175.164:9200

输入用户名：elastic

密码：

如果你忘记了 `elastic` 用户的密码，你可以 **重置密码**：

**进入 Elasticsearch 容器**：

```
docker exec -it elasticsearch /bin/bash
```

然后执行

```
bin/elasticsearch-reset-password -u elastic
```

示例输出：

```
New password for user 'elastic' is: MyNewSecurePassword123
```

这个新密码就是 `elastic` 用户的密码！

查看监听端口

```
ss -tulnp | grep 9200
```

