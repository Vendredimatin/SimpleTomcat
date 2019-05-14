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

## Chapter5
5.1 Container接口

1. 共有四种类型的容器，对应不同的概念层次
   1. Engine：表示整个Catalina servlet引擎
   2. Host: 表示包含有一个或多个Context容器的虚拟主机
   3. Context：表示一个Web应用程序，一个Context可以有多个Wrapper
   4. Wrapper：表示一个独立的servlet
2. 可以调用Container接口的addChild（）方法向某容器添加子容器
3. findChild和findChildren方法的来查找某个子容器和所有子容器的某个集合

5.2 管道任务

1. 管道包含该servlet容器将要调用的任务。一个阀表示一个具体的执行任务。在servlet容器中，有一个基础阀
2. 当调用容器的invoke（）方法后，容器会将处理工作交由管道完成，而管道会调用其中的第一个阀开始处理
3. 通过引入ValueContext接口来实现阀的便利执行
4. 当连接器调用容器的invoke（）方法后，容器会调用关掉的invoke（）方法
5. ValueContext接口通过invokeNext（）方法来调用下一个阀
6. 管道会调用ValueContext实例的invokeContext（）方法，ValueContext实例会将自身传给每个阀，因此，每个阀都可以调用ValueContext的invokeNext（）方法
7. Value接口的invoke()方法实现如下：
   1.     public void invoke(Request request, Response response, ValueContext valueContext){
              
          }
   2. 

5.2.1 Pipeline 接口

1. servlet容器通过调用Pipeline的invoke（）方法开始调用管道中的阀和基础阀。通过调用addValue（）方法来添加新的阀

5.2.2 Value 接口

1. 阀是Value接口的实例，用来处理接收到的请求。该接口由两个办法，invoke()方法和getInfo（）方法

    public void invoke(Request request, Response response, ValueContext valueContext){
    }

5.2.3 ValueContext 接口

该接口由两个方法，invokeNext（）和getInfo()f方法

5.2.4 Conatined 接口

该接口的实现类可以通过接口中的一个方法至多与一个servlet容器相关联

5.3 Wrapper接口

1. Wrapper接口的实现类要负责管理其基础servlet类的servlet生命周期。
2. 比较重要的方法是load（）和allocate（）方法。allocate（）方法会分配一个已经初始化的servlet实例

5.4 Context接口

Context接口中比较重要的方法是addWrapper方法和createWrapper方法

5.5 Wrapper 应用程序

1. 在servlet容器中载入相关servlet类的工作由Loader接口的实例完成

SimpleLoader类

1. SimpleLoader类的构造函数会初始化类加载器，供SimpleWrapper实例使用

SimplePipeline类

1. 实现了Pipeline接口，该类中最重要的方法是invoke（）方法，该方法包含了一个内部类SimplePipelineValueContext，该类实现了ValueContext接口

SimpleWrapper类

1. allocate（）
2. load（）
3. getLoader（）
4. 实例变量Loader、Container parent、Pipeline

SimpleWrapperValue类

1. 是一个基础阀，专门用于处理SimpleWrapper的请求，实现了Value接口和Contained接口
2. invoke（）方法
3. 不需要调用传递给它的ValueContext实例的invokeNext方法
4. invoke（）方法会调用SimpleWrapper的allocate（）方法来获取该Wrapper表示的servlet实例，然后调用该servlet实例的service方法

ClientIPLoggerValue类

1. 表示的阀用来将客户端的IP地址输出到控制台
2. invoke()

HeaderLoggerValue类

1. 把请求头信息输出到控制台
2. invoke（）

Bootstrap1类

1. main（）

5.6 Context应用程序

1. 本章应用程序展示了如何使用一个包含了两个Wrapper实例的Context实例来构建Web应用程序。
2. 当应用程序中有多个Wrapper实例时，需要使用一个映射器，帮助servlet容器选择一个子容器来处理某个指定的请求
3. Mapper接口
   1. map（）方法，返回要处理某个特定请求的子容器的实例
4. SimpleContext类使用SimpleContextMapper类的实例作为其映射器
5. SimpleContext类的invoke（）:
   1.     public void invoke(Request request, Response response){
              pipeline.invoke(request,response);
          }

1. SimplePipelinel类的invoke（）

1.     public void invoke(Request request, Response response){
           (new SimplePipelineValueContext()).invokeNext(request,response);
       }

5.6.1 SimpleContextValue类

1. 是SimpleContextValue的基础阀，最重要的是invoke方法
2. 调用context的map（）得到Wrapper类，然后调用wrapper的invoke方法

5.6.2 SimpleContextMapper类

1. map()方法，map方法会从request对象中解析处请求的上下文路径，并调用context实例的findServletMapping（）方法来获取一个与该路径相关联的名称，如果找到了这个名称，则它调用Context实例的findChild（）方法来获取一个Wrapper实例

5.6.3 SimpleContext类

1. addServletMapping（）添加一个URL模式/Wrapper实例的名称对
2. findServletMapping（）通过URL模式查找对应的Wrapper实例名称
3. addMapper（）阿紫容器中添加一个映射器
4. findMapper()找到正确的映射器，在SimpleContext中，他会返回默认映射器
5. map() 返回负责处理当前请求的Wrapper实例

5.6.4 Bootstrap2类

1. main()
