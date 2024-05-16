# 加解密逻辑漏洞的靶场

苦于找不到现成的, 所以自己写了一个

靶场环境使用了经典的图书管理系统稍作修改, 并增加了几处逻辑漏洞

漏洞都很简单, 重点是对抗加解密

### 架构:

图书管理系统: springboot + vue

加解密网关: springcloud gateway

数据库: mysql + redis

网关： nginx

### 加解密方式:

默认使用的是AES-128  可以根据实际需求修改

具体的 加解密类 

##### 后端 :

​	gateway  ---  utils/AESUtil

##### 前端

​	vue  ---  utils/request.js 

### 示例:





### Build:

#### 前端:

前端已经有构建好的 在vue/html1 文件夹中, 不会构建的可以不需要构建

#### 后端:

在项目根目录运行: 

```
mvn clean package
```



### Run:

推荐docker运行

```
docker build -t library-service ./library
docker build -t gateway-service ./gateway

docker-compose up -d
```







图书管理系统引用: https://github.com/XinChennn/xc016-library-system