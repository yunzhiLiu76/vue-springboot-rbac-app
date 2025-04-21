### **安装 Nginx**

```

sudo yum install nginx -y
```

### **1.4 启动并设置 Nginx 开机自启**

```

sudo systemctl start nginx
sudo systemctl enable nginx
```

### **1.5 检查 Nginx 是否运行**

```

sudo systemctl status nginx
```

如果 Nginx 运行正常，打开浏览器访问 `http://服务器IP`，你应该能看到 **Welcome to Nginx!** 页面。

第二步：部署前端项目

```
 npm run build:prod
 之后生成dist文件夹，里面的就是需要部署的东西
```

默认的 Nginx 站点目录是 `/usr/share/nginx/html`，你可以选择直接覆盖或新建站点目录：

```

# 删除默认的 Nginx 页面
sudo rm -rf /usr/share/nginx/html/*

# 复制前端项目到 Nginx 目录
sudo cp -r dist/* /usr/share/nginx/html/  # Vue/Angular
sudo cp -r build/* /usr/share/nginx/html/  # React
```

## **第三步：配置 Nginx**

### **3.1 编辑 Nginx 配置文件**

```

sudo nano /etc/nginx/nginx.conf
```

在 `server` 块中找到 `location /`，并修改如下：

```

解决404问题，无论访问什么都返回首页
location / {
        try_files $uri /index.html;
    }

```

### **测试 Nginx 配置**

```

sudo nginx -t
```

如果输出 `syntax is ok` 和 `test is successful`，说明配置正确。

### **3.3 重新启动 Nginx**

```

sudo systemctl restart nginx
```