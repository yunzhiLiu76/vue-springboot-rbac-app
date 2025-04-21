确认docker compose已经安装

```
docker compose version
```

启动sonarqube-compose.yaml文件

```
docker compose -f sonarqube-compose.yaml up -d
```

确认启动成功

1.查看容器(一个是sonarqube，一个是postgres)

```
docker ps
```

2.浏览器访问

```
http://你的ip:9000
```

若启动未成功，查看日志

```
docker compose logs -f sonarqube（容器名）
```

报错：

![image-20250219065554948](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250219065554948.png)

解决办法：

编辑系统配置文件

```
sudo vim /etc/sysctl.conf
```

添加以下行

```
vm.max_map_count=262144
```

保存并使其生效

```
sudo sysctl -p
```

生效后，会自动恢复，不需要手动重启。

