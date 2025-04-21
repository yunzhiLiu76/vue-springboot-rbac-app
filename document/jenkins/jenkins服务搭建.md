

确认docker compose已经安装

```
docker compose version
```

在宿主机上创建Volume对应文件夹：

```
mkdir -p /root/jenkins/data
```

启动sonarqube-compose.yaml文件

```
docker compose -f jenkins-compose.yaml up -d
```

确认启动成功

1.查看容器(jenkins)

```
docker ps
```

2.浏览器访问

```
http://8.211.63.6:8090
```

若启动未成功，查看日志

```
docker compose logs -f jenkins（容器名）
```

查看jenkins初始密码：

```
docker exec -it jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

后面需要安装一些插件：

git Parameter(jenkins可以从gitlab上拉取代码)[Git plugin也会同时下载安装）

下面是git Parameter的安装方式，其他插件也是同样安装

还需要安装的插件有：Publish Over SSH

![image-20250220165455543](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220165455543.png)

![image-20250220165509520](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220165509520.png)

然后把宿主机里面的maven和jdk挪到docker

先把maven和jdk解压缩到宿主机/usr/local目录，并进行setting.xml的配置

在宿主机，创建 /root/jenkins/data目录（这个目录是jenkins家目录的映射目录）

```
cd /root/jenkins/data
mv /usr/local/jdk ./
mv /usr/local/maven ./
```

此时jenkins家目录已经有了maven和jdk

![image-20250220175233797](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220175233797.png)

进入之后，配置jdk和maven的家。里面的name 是自己随便起的名字，home目录是jenkins容器内的目录

![image-20250220175352035](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220175352035.png)

![image-20250220175359166](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220175359166.png)

之后再配置Publish Over SSH，方法如下：

![image-20250220175502435](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220175502435.png)

![image-20250220175528135](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220175528135.png)

![image-20250220175541688](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220175541688.png)

这个name也是随便写的

![image-20250220175550397](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220175550397.png)

![image-20250220175610025](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220175610025.png)

最后测试连接通过，点击apply和save.(注意在宿主机创建/usr/local/test目录。此时是以test为例)

之后就是创建一个item,然后在里面配置构建操作，和构建后操作。把代码从git上拉下来，再用maven构建成target，构建后把构建的jar包传给目标服务器。然后再生成镜像，运行镜像。以下是主要步骤：

![](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220193318275.png)

![image-20250220193624106](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220193624106.png)

![image-20250220193343864](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220193343864.png)



![image-20250220193400899](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220193400899.png)

![image-20250220193417102](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220193417102.png)

![image-20250220193430805](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250220193430805.png)

现在jenkins把传过来的jar包做成镜像。

项目里面新建一个docker文件夹，里面写Dockerfile和一个docker-compose.yaml.把它们一起传到服务器这里，制作镜像，运行容器。主要过程如下图：

![image-20250221070256083](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221070256083.png)

cd docker
mv ../target/*.jar ./
docker compose down
docker compose up -d --build

![image-20250221070339773](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221070339773.png)

![image-20250221070348134](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221070348134.png)

![image-20250221070408721](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221070408721.png)

第一行cd 要用绝对路径，不然会出问题。

之后再删除不需要的镜像：删除所有 **dangling（无标签的未使用）镜像**，即构建失败或被替换的临时镜像。

docker image prune -f

![image-20250221070501219](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221070501219.png)

之后，配置jenkins CD操作。根据指定tag构建镜像，运行容器。

核心过程如下：

参数化构建，从指定的分支来拉取代码。这个name是自己写的，这里写tag就可以

![image-20250221094648617](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221094648617.png)

![image-20250221094704796](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221094704796.png)

![image-20250221094718841](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221094718841.png)

这里的构建操作，需要把从指定tag check out代码放到执行maven之前。所以需要按住移动框里面的三横，把从指定tag check out代码移动到maven之前。（移动框框）

!(/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221094733002.png)



![image-20250221094747401](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221094747401.png)

git提交完代码之后，给代码打一个tag，之后就可以按照指定tag来参数化运行代码

![image-20250221095026617](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221095026617.png)

![image-20250221094802685](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221094802685.png)

![image-20250221094816980](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221094816980.png)

下面需要安装sonner-scanner

下载sonner-scanner

```
https://docs.sonarsource.com/sonarqube-server/9.9/analyzing-source-code/scanners/sonarscanner/
```

把sonarscanner上传到jenkins所在服务器的家目录，这里上传到/root目录。

安装解压工具

```
yum -y install unzip
```

解压sonarscanner

```
unzip sonar-scanner-cli-7.0.2.4839-linux-x64.zip
```

为sonar-scanner改名

```
mv sonar-scanner-7.0.2.4839-linux-x64  sonar-scanner
```

进入jenkins的家目录（在宿主机里面的映射）

```
cd /root/jenkins/data
```

把sonar-scanner移动到jenkins家目录

```
mv ~/sonar-scanner ./
```

进入sonar-scanner的conf目录

```
cd sonar-scanner/conf
```

修改sonar-scanner.properties配置文件

```
vi sonar-scanner.properties
```

修改以下部分：（sonar.host.url是sonar服务器的访问地址）

```
sonar.host.url=http://8.211.40.36:9000
sonar.sourceEncoding=UTF-8
```

进入项目工作目录workspace里面的项目目录

```
cd ../../workspace/CryptAuth/
```

执行下面的命令

-Dsonar.sources=./ 检测的代码在当前目录

-Dsonar.projectname=CryptAuth 检测的项目名

-Dsonar.login=squ_94a2db5b7a88d258ba29afedc1b45e2d39d13fc4 当前的登录用户token

-Dsonar.projectKey=CryptAuth 项目标识，我这里和项目名称保持一致

Dsonar.java.binaries=target/ 编译过后的内容在哪里

Dsonar.java.binaries=./target/ 编译过后的内容在哪里

```
 /root/jenkins/data/sonar-scanner/bin/sonar-scanner -Dsonar.sources=./ -Dsonar.projectname=CryptAuth -Dsonar.login=squ_94a2db5b7a88d258ba29afedc1b45e2d39d13fc4 -Dsonar.projectKey=CryptAuth -Dsonar.java.binaries=./target/
```

token在sonnerqube的网站里面生成，如下图：

![image-20250221190545101](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221190545101.png)

上面的sonnerscanner运行之后，在sonerqube网页上就可以看到对应代码的质量分析了。

在jenkins上下载sonarqube Scanner插件，如下：

![image-20250221200407966](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221200407966.png)

进去manager jenkins里面的system,增加sonnaerquebeserver

![image-20250221200514104](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221200514104.png)



name随便写，地址写sonnerquebe访问地址，如下

![image-20250221200620088](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221200620088.png)

配置秘钥token

![image-20250221200648374](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221200648374.png)

![image-20250221200703112](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221200703112.png)

进入manager jenkins,里面的tools，增加sonnerquebe scanner

![image-20250221200917950](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221200917950.png)

![image-20250221200950465](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221200950465.png)

这个名字也是自己起的。运行home是jenkins容器内部家的运行home，之后apply,save.

进入项目的配置，增加构建操作，在maven之后添加

![image-20250221200802166](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221200802166.png)

![image-20250221201134812](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221201134812.png)

这里就配置完了，然后就可以去build项目了，日志中会看到

![image-20250221201226458](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221201226458.png)

如果构建失败，可能是之前生成了隐藏文件，删除workspace里面的隐藏文件，如下图

![image-20250221201308417](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250221201308417.png)

 **Jenkins**容器使用宿主机Docker

构建镜像和发布镜像到harbor都需要使用到docker命令。而在Jenkins容器内部安装Docker官方推荐直

接采用宿主机带的Docker即可。 设置Jenkins容器使用宿主机Docker

在/var/run中有一个docker.sock文件，现在这个docker.sock是所属用户root，所属组是docker，要将他修改为root用户下的root组下的一个文件，并且要让其他组的成员也都有读和写的权限，当这个文件拥有这样的权限后，再去修改我们的数据卷，就可以让我们的docker容器内部去拥有这个文件，并且拥有我们的docker可执行文件，最后再利用我们之前配过的demon.json的一个配置就可以了，这些准备好就可以实现在jenkins容器内部可以使用docker命令，并且可以把它推送到我的habor私有仓库中

第一步  修改这个文件的所属组

```
chown root:root docker.sock
```

第二步  修改它的权限，使其他组也有读和写的权限

```
chmod o+rw docker.sock
```

第三步 跳到之前配置的jenkins目录下，找到目录jenkins_docker，修改数据卷

```
cd /usr/local/docker/jenkins_docker
```

修改jenkins-compose文件，增加数据卷映射

```
volumes:
      - /root/jenkins/data:/var/jenkins_home  # Jenkins 数据存储
      #/var/jenkins_home是jenkins安装好后，jenkins的家目录  jenkins所有的插件，项目，都会在这个目录下操作
      - /var/run/docker.sock:/var/run/docker.sock #映射宿主机里面docker.sock文件到jenkins容器。
      #使得容器内部的jenkins可以使用宿主机的docker
      - /usr/bin/docker:/usr/bin/docker #映射宿主机里面docker命令到jenkins容器
      - /etc/docker/daemon.json:/etc/docker/daemon.json #将宿主机的daemon.json映射到jenkins容器内部
```

重新启动jenkins容器

```
docker compose -f jenkins-compose.yaml down
docker compose -f jenkins-compose.yaml up -d
```

验证jenkins容器可以使用docker命令

进入jenkins容器，执行docker命令

```
docker exec -it jenkins bash
 docker version
```

可以看到docker版本输出，就说明这里已经配置完毕



在jenkins上制作自定义镜像，并且推送给harbor

先删除docker目录下面的docker-compose.yaml，并且推送给gitlab，打上新的tag

删除之前jenkins发送jar包和docker文件夹到目标服务器的构建后操作，增加shell执行，执行以下shell

![image-20250222152528378](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250222152528378.png)

上图命令解释如下

```
mv target/*.jar docker/ #把maven构建后的jar包移动到docker文件夹
docker build -t cryptauth:$tag docker/ #用docker执行构建镜像命令
docker login -u admin -p Harbor12345 8.211.40.36:8083 #登录harbor服务器
docker tag cryptauth:$tag 8.211.40.36:8083/repo/cryptauth:$tag #给刚才的镜像起新的tag
docker push 8.211.40.36:8083/repo/cryptauth:$tag #推送镜像到harbor
```

此时，项目构建之后，harbor上面已经有了新的镜像。

现在要在目标服务器放一个shell脚本，里面来接收jenkins给它传过来的参数，并且jenkins给它运行脚本的命令。

目标服务器的脚本主要做了如下操作：

1. 告知目标服务器拉取哪个镜像
2. 判断当前服务器是否正在运行容器，需要删除
3. 如果目标服务器已经存在当前镜像，需要删除
4. 目标服务器拉取 harbor 上的镜像
5. 将拉取下来的镜像运行成容器

目标服务器的脚本如下：

在root家目录创建脚本

```
deploy.sh
```

脚本内容如下：

```
horbar_addr=$1  #接收第一个参数赋值给horbar_addr（harbor服务器地址）
horbar_repo=$2  #接收第2个参数赋值给horbar_repo（仓库名）
project=$3      #接收第3个参数赋值给project(项目名)
version=$4      #接收第4个参数赋值给version （版本号）
container_port=$5 #接收第5个参数赋值给container_port（容器运行的端口号）
host_port=$6		#接收第6个参数赋值给host_port（容器映射的宿主机端口号）
					

#根据接收的参数，拼装出来镜像名，并且把镜像名赋值给imageName（这个镜像就是目标服务器需要拉取的镜像）
imageName=$horbar_addr/$horbar_repo/$project:$version
#打印镜像名
echo $imageName

#docker ps -a | grep ${project}根据项目名查找容器
#awk '{print $1}' 是一个 awk 命令，主要用于从输入数据中提取 第一列 并输出
#print $1：打印第一列的数据
#这里执行完，输出的是根据项目名找到的容器id,并且把这个容器id赋值containerId
containerId=`docker ps -a | grep ${project} | awk '{print $1}'`
#打印容器id
echo $containerId
#做判断，如果容器id不为空，说明有这个镜像对应的容器在运行
if [ "$containerId" != "" ] ; then
    #那么就先停止容器
    docker stop $containerId
    #再删除容器
    docker rm $containerId
fi

#打印出来根据项目名找到所有的tag
tag=`docker images | grep ${project} | awk '{print $2}'`

#打印这些tag
echo $tag

#检查变量 $tag 是否包含 $version，有的话，会直接执行then里面的代码
if [[ "$tag" =~ "$version" ]] ; then
	#删除镜像
    docker rmi $imageName
fi

#登录harbor服务器
docker login -u admin -p Harbor12345 $horbar_addr
#拉取镜像
docker pull $imageName
#运行容器
docker run -d --network my-network -p $host_port:$container_port --name $project $imageName
#打印SUCCESS
echo "SUCCESS"

```

给所有用户增加`deploy.sh` 文件的可执行权限

```
chmod a+x deploy.sh
```

把这个文件移动到PATH目录，让所有地方都可以执行这个文件

```
echo $PATH
 mv deploy.sh /usr/bin
```

现在配置Jenkins配置，增加2个参数，如图：

![image-20250222164341694](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250222164341694.png)

![image-20250222164353072](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250222164353072.png)

增加构建后操作，如下图：

![image-20250222164423717](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250222164423717.png)

之后就可以构建项目。

去目标服务器看看容器运行起来了没有

现在我们尝试用pipeline来构建项目

pipeline配置从gitlab上面拉取代码

![image-20250223065708553](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223065708553.png)

![image-20250223065724485](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223065724485.png)

checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'http://8.211.40.36:28080/com.shuangshuan/cryptauth.git']])

把pipeline的配置文件上传到gitlab

![image-20250223065831368](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223065831368.png)

必须放在以Jenkinsfile命名的文件中

![image-20250223065850071](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223065850071.png)

增加构建参数tag

![image-20250223065917753](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223065917753.png)

生成maven构建pipeline语法

![image-20250223070009742](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223070009742.png)

sh '/var/jenkins_home/maven/bin/mvn clean package -DskipTests'

此时构建出错：

![image-20250223070037873](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223070037873.png)

![image-20250223070049505](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223070049505.png)

![image-20250223070059908](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223070059908.png)

Maven想用JDK21编译，但是找不到JDK21，或者默认不是JDK21编译

显式指定JDK21的位置，必须和jenkins的配置JDK一致

修改如下：

![image-20250223070210149](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223070210149.png)

![image-20250223070221994](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223070221994.png)

补充一个pipeline的基本结构：

```
pipeline {
//jenkins有集群，这个是集群里面的任何一台jenkins机器都可以执行
    agent any

    // 存放所有任务的合集
    stages {
    //每一阶段的任务
        stage('拉取Git代码') {
            steps {
                echo '拉取Git代码'
            }
        }

        stage('检测代码质量') {
            steps {
                echo '检测代码质量'
            }
        }

        stage('构建代码') {
            steps {
                echo '构建代码'
            }
        }

        stage('制作自定义镜像并发布Harbor') {
            steps {
                echo '制作自定义镜像并发布Harbor'
            }
        }

        stage('基于Harbor部署工程') {
            steps {
                echo '基于Harbor部署工程'
            }
        }
    }
}

```

如果构建之后，没有出现阶段试图，需要安装pipeline stage插件

下面是sonaquebe pipeline:

![image-20250223083430771](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223083430771.png)

```
/var/jenkins_home/sonar-scanner/bin/sonar-scanner -Dsonar.source=./ -Dsonar.projectname=${JOB_NAME} -Dsonar.projectKey=${JOB_NAME} -Dsonar.java.binaries=./target/ -Dsonar.login=7ed175cfc702ed12f0d38ac3bba158e068fec473
```

生成镜像

```
mv ./target/*.jar ./docker/
docker build -t ${JOB_NAME}:${tag} ./docker/
```

![image-20250223083536929](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223083536929.png)

推送到harbor:

jenkinsfile文件增加全局变量

```
environment {
    harborUser = 'admin'
    harborPasswd = 'Harbor12345'
    harborAddress = '192.168.11.102:80'
    harborRepo = 'repo'
}
```

下面的命令生成脚本

```
docker login -u harbor用户名 -p harbor密码 harbor地址
docker tag ${JOB_NAME}:${tag} harbor地址/harbor仓库/${JOB_NAME}:${tag}
docker push harbor地址/harbor仓库/${JOB_NAME}:${tag}
```

![image-20250223083700489](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223083700489.png)

生成的pipeline在粘贴到jenkinsfile的时候，需要把$引用变量的地方从单引号替换成双引号。

如下：

```
sh '''docker login -u ${harborUser} -p ${harborPasswd} ${harborAddress}
docker tag ${JOB_NAME}:${tag} ${harborAddress}/${harborRepo}/${JOB_NAME}:${tag}
docker push ${harborAddress}/${harborRepo}/${JOB_NAME}:${tag}'''
```

在jenkins配置中增加container_port和host_port变量

![image-20250223083907572](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223083907572.png)



```
deploy.sh $harborAddress $harborRepo $JOB_NAME $tag $container_port $host_port
```

让目标服务器执行：

![image-20250223084016051](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223084016051.png)

![image-20250223084022732](/Users/liuyunchao/Library/Application Support/typora-user-images/image-20250223084022732.png)

命令如下：

```
sshPublisher(publishers: [sshPublisherDesc(configName: 'test', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "deploy.sh $harborAddress $harborRepo $JOB_NAME $tag $container_port $host_port", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
```

