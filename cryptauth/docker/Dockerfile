# 选择基础镜像（这里使用 Eclipse Temurin JDK 21）
FROM eclipse-temurin:21-jdk AS build

# 复制项目文件到容器
COPY cryptauth.jar /usr/local

# 设置工作目录
WORKDIR /usr/local

# 运行 Java 应用（可选，具体根据你的项目结构调整）
CMD java -jar  cryptauth.jar
