### 修改主机名

```
hostnamectl set-hostname 
```

### 关闭防火墙与SELinux

```
systemctl stop firewalld
systemctl disable firewalld

setenforce 0
vim /etc/selinux/config
SELINUX=disable
```

### 配置yum源

第一步：使用cd命令切换到/etc/yum.repos.d目录

```powershell
# cd /etc/yum.repos.d/
```

第二步：对所有的仓库文件进行备份

```powershell
# tar -zcf repo.tar.gz *.repo
```

第三步：删除所有的以.repo结尾的仓库文件

```
# rm -rf *.repo
```

第四步：根据官网提示，配置阿里云的YUM源

```powershell
# mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup

# wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.tencent.com/repo/centos7_base.repo

# yum clean all
# yum makecache
```

### 扩展：EPEL源配置

什么是EPEL源？

答：EPEL是对官网源的一个扩展。

CentOS-Base.repo文件 => 基础源（官方在带的YUM源）

epel.repo文件 => 扩展源，是对官方YUM源的一个补充

```powershell
# wget -O /etc/yum.repos.d/epel.repo http://mirrors.cloud.tencent.com/repo/epel-7.repo
# yum clean all
# yum makecache
```

比如：会跑的小火车就是在epel源中

### 时间同步

```
yum install ntpdate -y
ntpdate time.windows.com
```

### 打开swarm

```
sudo swapoff -a # 先关闭所有 swap sudo rm -f /swapfile # 删除旧的 swap 文件
重新创建 4GB swap 文件（确保它不是稀疏文件）
sudo dd if=/dev/zero of=/swapfile bs=1M count=4096
设置正确的权限
sudo chmod 600 /swapfile
格式化为 Swap
sudo mkswap /swapfile
启用 Swap
sudo swapon /swapfile
```

