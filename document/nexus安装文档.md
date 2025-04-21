查找镜像

```
docker search nexus
```

创建宿主机volume

```
mkdir -p /root/nexus-data
```

为目录增加权限

```
chmod -R 777 /root/nexus-data
```

docker安装

```
docker run -d -p 8091:8081 --name nexus \
  -v /root/nexus-data:/nexus-data \
  sonatype/nexus3
```

查看容器

```
docker ps 
```

浏览器访问地址

```
http://8.211.63.6:8091
```

安装完成后，默认的管理员密码存储在：

```
cat /root/nexus-data/admin.password
```

使用这个密码登录 Nexus Web 界面（默认用户名 `admin`）

### 注意注意：多次上传不上去是因为blob文件夹没有写权限