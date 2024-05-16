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

1. 请求体加密
  默认使用的是AES-128  可以根据实际需求修改
2. RequestId
  为了防止重放攻击, 客户端生成随机RequestId 服务端接收后保存至Redis中, 如果再次接收到此RequestID, 则视为非法请求
3. 时间戳
   添加时间戳的超时时间, 一旦超时, 原始数据包失效
4. 签名
   将 requestId + 原始请求体 + 时间戳 合并生成哈希值  从而保证以上参数的有效性  


具体的 加解密类 

##### 后端 :

​	gateway  ---  utils/AESUtil
![image](https://github.com/0ctDay/encrypt-decrypt-vuls/assets/100889450/0ce8917d-21ee-4817-9c6f-7781a2822636)


##### 前端

​	vue  ---  utils/request.js 
![image](https://github.com/0ctDay/encrypt-decrypt-vuls/assets/100889450/fa870f57-a2cd-49bc-b6ed-f4d7d44cfe64)

### 示例:

http://39.98.108.20:8085/

![image](https://github.com/0ctDay/encrypt-decrypt-vuls/assets/100889450/6b31806f-b2e0-4228-a848-4ba2887098cf)



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
