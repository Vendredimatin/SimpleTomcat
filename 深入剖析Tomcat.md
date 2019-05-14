# 深入剖析Tomcat

## Chapter 4

### 4.1 HTTP 1.1 新特性

1. 持久链接

   1. 当下载了页面后，服务器并不会立即关闭连接，相反，它会等待Ｗｅｂ客户端来请求被该页面所引用的所有资源．这样一来，页面和被页面引用的资源都会使用同一个连接来下载



   1. 块编码
      1. 建立持久

1. 持久连接
   1. 当下载了页面后，服务器并不会立即关闭连接，相反，它会等待Ｗｅｂ客户端来请求被该页面所引用的所有资源．这样一来，页面和被页面引用的资源都会使用同一个连接来下载
2. 块编码
   1. 建立了持久连接之后，服务器可以从多个资源发送字节流，而客户端也可以使用该连接发送多个请求．发送方必须在每个请求或响应中添加content-length头信息，这样接收方才能知道如何解释这些字节信息
   2. 对于每一个块，块的长度后面会有一个回车／换行符（ＣＲ／ＬＦ），然后是具体的数据
3. 状态码１００的使用
   1. 当客户端准备发送一个较长的请求体，而不确定服务端是否会接收时，就可能会发送下面的头信息
   2. Expect:100-continue

### 4.2 Connector接口

1. setContainer()将连接器和某个servlet容器相关连
2. getConatiner()返回与当前连接器关联的ｓｅｒｖｌｅｔ容器
3. createRequest()为引入的ＨＴＴＰ请求创建request对象
4. createResponse()创建一个response对象

### 4.3 HttpConnector类

1. HttpConnector实例维护一个HttpProcessor实例池，避免每次都为新请求创建HttpProcessor
2. private Stack processors = new Stack()
3. httpProcessor实例负责解析ＨＴＴＰ请求头和请求行，填充request对象．
4. run() 主要业务逻辑
5. createProcessor() 获得一个HttpProcessor对象

### 4.4 HttpProcessor类

1. assign(Socket) assign方法会直接返回，而不会等到HttpProcessor实例完成解析，这样HttpConnector才能继续接受传入的ＨＴＴＰ请求，而HttpProcessor实例在自己的线程中完成解析工作

2. HttpProcessor类实现了Runnable接口，每个HttpProcessor实例就可以运行在自己的线程中了．每个HttpConnector对象创建HttpProcessor实例后，会调用其start()方法，启动HttpProcessor的线程

3. run() 获取套接字对象，进行解析，调用recycle()回收

4. recycle()

   1. ```java
      void recycle(HttpProcessor processor){
          processors.push(processor);
      }
      ```

5. await() 会阻塞处理器线程的控制流，直到它从HttpConnector中获取到了新的Ｓｏｃｋｅｔ对象．换句话说，直到HttpConnector对象调用HttpProcessor实例的assign()方法前，都会一直阻塞

6. assign() P60

7. 两个方法处于不同线程之中，他们之间如何沟通交流，使用布尔变量和wait(),notifyAll()

8. 为什么await()方法要使用一个局部变量sockert,而不直接将成员变量返回呢？因为使用局部变量可以在当前Socket对象处理完之前，继续接收下一个Socket对象

9. 为什么await()方法需要调用notifyAll()方法？是为了防止出现另一个Socket对象已经到达，而此时变量avaliable的值还是true,使得连接器线程阻塞在assign方法中，知道处理器线程调用了notifyAll()

10. process()方法，解析连接，解析请求，解析请求头．初始化request,response

### 4.5 Request对象

### 4.6 Response对象

### 4.7 处理请求

### 4.8 简单的Container应用程序

1. SimpleContainer类实现了Container接口，这样就可以与默认连接器关联
2. invoke()方法，invoke()方法会创建一个类载入器，载入相关ｓｅｒｖｌｅｔ类，并调用该servlet类的service()方法
3. 

