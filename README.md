# 加解密逻辑漏洞的靶场

苦于找不到现成的, 所以自己写了一个

靶场环境使用了经典的图书管理系统稍作修改, 并增加了几处逻辑漏洞, 具体可以自己找吧!

### 架构:

图书管理系统: springboot + vue

加解密网关: springcloud gateway

数据库: mysql + redis

注册中心: nacos



### 示例:





### Build:

#### 前端:

需要修改vue/src/utils/request.js 中的 baseURL: 值修改为自己的ip

```
#最好使用windows环境
cd vue
npm install
npm run build
```

#### 后端:

```
mvn clean package
```



### Run:

推荐docker运行

先运行nacos

```
docker-compose down
docker-compose up -d nacos
```

后端

```
docker build -t library-service ./library
docker build -t gateway-service ./gateway

docker-compose up -d
```







图书管理系统引用: https://github.com/XinChennn/xc016-library-system