创建文件夹，并进入

```
mkdir -p ~/docker/mongo
cd ~/docker/mongo
```

启动容器：

```
docker compose -f mongo-compose.yml up -d
```

验证是否启动成功

```
docker ps
```

查看日志：

```
docker logs -f mongodb
```

进入容器：

```
docker exec -it mongodb mongosh
use admin
db.auth("admin", "admin")
如果返回 1，说明认证成功。
```

