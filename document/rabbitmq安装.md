步骤 1.1：添加 PackageCloud 仓库并安装 Erlang

```
 #添加 PackageCloud 仓库
curl -s https://packagecloud.io/install/repositories/rabbitmq/erlang/script.rpm.sh | sudo bash

# 安装 Erlang
sudo yum install -y erlang-23.3.4.11-1.el7.x86_64
```

上述命令将添加 RabbitMQ 官方的 PackageCloud 仓库，并安装适用于 CentOS 7 的 Erlang 版本。

步骤 2.1：添加 PackageCloud 仓库并安装 RabbitMQ

```
# 添加 PackageCloud 仓库
curl -s https://packagecloud.io/install/repositories/rabbitmq/rabbitmq-server/script.rpm.sh | sudo bash

# 安装 RabbitMQ
sudo yum install -y rabbitmq-server-3.9.16-1.el7.noarch
```

3. 启动并配置 RabbitMQ
  步骤 3.1：启动 RabbitMQ 服务并设置开机自启

  ```
  # 启动 RabbitMQ 服务
  sudo systemctl start rabbitmq-server
  
  # 设置开机自启
  sudo systemctl enable rabbitmq-server
  ```

  步骤 3.2：启用管理插件
  RabbitMQ 自带的管理插件提供了图形化界面，默认未启用。启用该插件后，可以通过浏览器访问管理界面。

  ```
  # 启用管理插件
  sudo rabbitmq-plugins enable rabbitmq_management
  
  # 重启 RabbitMQ 服务以应用更改
  sudo systemctl restart rabbitmq-server
  ```

  步骤 3.3：配置防火墙以允许访问管理界面==默认已关闭

  步骤 3.4：访问 RabbitMQ 管理界面
  在浏览器中访问 http://<服务器IP>:15672/。默认的用户名和密码均为 guest。请注意，出于安全考虑，guest 用户默认只能本地访问。如果需要远程访问，建议创建新的管理员用户

  ```
  # 添加新用户
  sudo rabbitmqctl add_user your_username your_password
  
  # 设置用户角色为管理员
  sudo rabbitmqctl set_user_tags your_username administrator
  
  # 为用户设置权限
  sudo rabbitmqctl set_permissions -p / your_username ".*" ".*" ".*"
  ```

  完成上述步骤后，即可使用新创建的用户通过浏览器远程访问 RabbitMQ 管理界面。