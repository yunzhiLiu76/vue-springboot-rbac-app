创建目录，给权限

```
mkdir -p ~/docker/redis/data
cd ~/docker/redis
chmod 777 ~/docker/redis/data
```

运行

```
docker compose -f redis-compose.yml up -d
```

检查容器运行

```
docker ps
```

进入容器，检查是否运行成功

```
docker exec -it redis redis-cli
执行 ping 命令  返回pong   成功
```

