下载 harbor 安装包

```
wget  https://github.com/goharbor/harbor/releases/download/v2.12.2/harbor-offline-installer-v2.12.2.tgz
```

解压 Harbor 安装包到 `/usr/local` 目录

```
tar -zxvf harbor-offline-installer-v2.12.2.tgz -C /usr/local/
```

进入 Harbor 目录

```
cd /usr/local/harbor
```

**修改配置文件** Harbor 提供了默认的 `harbor.yml` 配置文件，你需要先进行必要的修改：

```
cp harbor.yml.tmpl harbor.yml
vim harbor.yml
```

主要需要修改：

- `hostname`: 设置为你的服务器 IP 或域名

- `https`: 配置证书路径（如果使用 HTTPS）不需要的话把https相关的配置注释上

- 修改http端口：8083：

- **安装 Harbor** 运行以下命令进行 Harbor 安装

  ```
  sudo ./install.sh
  ```

  验证 Harbor

  查看 Harbor 容器状态

  ```
  sudo docker ps
  ```

  访问 Harbor Web UI： 在浏览器中访问：

  ```
  http://<your-server-ip>
  ```

  默认用户名：`admin`
  默认密码：`Harbor12345`

  配置secret(在k8s中创建 TODO)

  ```
  # 创建 harbor 访问账号密码（需要将下访问的配置信息改成你自己的）
  kubectl create secret docker-registry harbor-secret --docker-server=192.168.113.122:8858 --docker-username=admin --docker-password=wolfcode -n kube-devops
  ```

  修改docker配置文件

  ```
  vi /etc/docker/daemon.json
  ```

  ```
  {
    "exec-opts": ["native.cgroupdriver=systemd"],
    "insecure-registries": ["8.211.40.36:8083"]
  }
  ```

  给镜像打tag

  ```
  docker tag 6ac34c78291f  8.211.40.36:8083/repo/cryptauth:v1.0.0
  ```

  登录harbor

  ```
  docker login -u admin -p Harbor12345 8.211.40.36:8083
  ```

  推送镜像

  ```
  docker push 8.211.40.36:8083/repo/cryptauth:v1.0.0
  ```

  以上手动推送镜像到docker成功

