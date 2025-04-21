一：安装

下载gitlab安装包

```
wget https://packages.gitlab.com/gitlab/gitlab-ce/packages/el/7/gitlab-ce-17.6.4-ce.0.el7.x86_64.rpm/download -O gitlab-ce-17.6.4-ce.0.el7.x86_64.rpm

```

安装该版本依赖perl

```
sudo yum install -y perl
```

安装：

```
rpm -i gitlab-ce-17.6.4-ce.0.el7.x86_64.rpm
```

编辑 /etc/gitlab/gitlab.rb 文件

```
vi /etc/gitlab/gitlab.rb

external_url 'http://8.211.40.36:28080'# 修改 external_url 访问路径
gitlab_rails['time_zone'] = 'Europe/Berlin'
puma['worker_processes'] = 2
sidekiq['max_concurrency'] = 8
postgresql['shared_buffers'] = "128MB"
postgresql['max_worker_processes'] = 4
prometheus_monitoring['enable'] = false
```

更新配置并重启

```
gitlab-ctl reconfigure
gitlab-ctl restart
```

## 二：配置

#### 1.查看默认密码

```
cat /etc/gitlab/initial_root_password
```

登录后修改默认密码 > 右上角头像 > Perferences > Password

#### 2.修改系统配置

点击右下角 > Admin

Settings > General > Account and limit > 取消 Gravatar enabled > Save changes

#### 3.关闭用户注册功能

Settings > General > Sign-up restrictions > 取消 Sign-up enabled > Save changes

#### 4.开启 webhook 外部访问

Settings > Network > Outbound requests > Allow requests to the local network from web hooks and services 勾选

#### 5.设置语言为中文（全局）(可选)

Settings > Preferences > Localization > Default language > 选择简体中文 > Save changes

#### 6.设置当前用户语言为中文（可选）

右上角用户头像 > Preferences > Localization > Language > 选择简体中文 > Save changes

## 三：配置 Secret

创建 gitlab 默认用户名密码 secret

这里需要我在k8s集群master上配置，这是是TODO。

```
echo root > ./username
echo wolfcode > password
kubectl create secret generic git-user-pass --from-file=./username --from-file=./password -n kube-devops
```

## 四：为项目配置 Webhook

进入项目点击侧边栏设置 > Webhooks 进入配置即可

URL：在 jenkins 创建 pipeline 项目后
触发来源：
	推送事件：表示收到新的推送代码就会触发
	标签推送事件：新标签推送才会触发
	评论：根据评论决定触发
	合并请求事件：创建、更新或合并请求触发

添加成功后，可以在下方点击测试按钮查看 jenkins 是否成功触发构建操作

## 五：卸载

1.停止服务

```
gitlab-ctl stop
```

2.卸载 rpm 软件（注意安装的软件版本是 ce 还是 ee）

```
rpm -e gitlab-ce
```

3.查看进程

```
ps -ef|grep gitlab 

干掉第一个 runsvdir -P /opt/gitlab/service log 进程
```

4.删除 gitlab 残余文件

```
find / -name *gitlab* | xargs rm -rf
find / -name gitlab | xargs rm -rf
```

