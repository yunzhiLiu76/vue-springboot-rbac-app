CentOS 7（使用 yum 进行安装）

###### # step 1: 安装必要的一些系统工具

```
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```

###### # Step 2: 添加docker软件源信息

```
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

###### Step 3: 更新并安装Docker-CE

```
sudo yum makecache fast
sudo yum -y install docker-ce
```

###### Step 4: 开启Docker服务

```
sudo service docker start
```

###### Step 5: 查看Docker版本

```
docker version
```

###### Step 6: 把Docker做成开机自启

```
sudo systemctl enable docker
```

###### Step 7: 查看Docker状态

```
systemctl status docker
```

###### 修改 `exec-opts` 以使用 `systemd`

```
sudo vim /etc/docker/daemon.json
```

```

{
  "exec-opts": ["native.cgroupdriver=systemd"]
}

```

修改完配置文件，重启docker

```
sudo systemctl restart docker
```

这样会重新加载 `daemon.json` 配置并重启 Docker 进程