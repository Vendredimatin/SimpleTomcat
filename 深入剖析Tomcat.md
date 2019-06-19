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
### 5.1 Container接口

1. 共有四种类型的容器，对应不同的概念层次
   1. Engine：表示整个Catalina servlet引擎
   2. Host: 表示包含有一个或多个Context容器的虚拟主机
   3. Context：表示一个Web应用程序，一个Context可以有多个Wrapper
   4. Wrapper：表示一个独立的servlet
2. 可以调用Container接口的addChild（）方法向某容器添加子容器
3. findChild和findChildren方法的来查找某个子容器和所有子容器的某个集合

### 5.2 管道任务

1. 管道包含该servlet容器将要调用的任务。一个阀表示一个具体的执行任务。在servlet容器中，有一个基础阀
2. 当调用容器的invoke（）方法后，容器会将处理工作交由管道完成，而管道会调用其中的第一个阀开始处理
3. 通过引入ValueContext接口来实现阀的便利执行
4. 当连接器调用容器的invoke（）方法后，容器会调用关掉的invoke（）方法
5. ValueContext接口通过invokeNext（）方法来调用下一个阀
6. 管道会调用ValueContext实例的invokeContext（）方法，ValueContext实例会将自身传给每个阀，因此，每个阀都可以调用ValueContext的invokeNext（）方法
7. Value接口的invoke()方法实现如下：
   1.     public void invoke(Request request, Response response, ValueContext valueContext){
          ​    
          }
   2. 

#### 5.2.1 Pipeline 接口

1. servlet容器通过调用Pipeline的invoke（）方法开始调用管道中的阀和基础阀。通过调用addValue（）方法来添加新的阀

#### 5.2.2 Value 接口

1. 阀是Value接口的实例，用来处理接收到的请求。该接口由两个办法，invoke()方法和getInfo（）方法

    public void invoke(Request request, Response response, ValueContext valueContext){
    }

#### 5.2.3 ValueContext 接口

该接口由两个方法，invokeNext（）和getInfo()f方法

#### 5.2.4 Conatined 接口

该接口的实现类可以通过接口中的一个方法至多与一个servlet容器相关联

### 5.3 Wrapper接口

1. Wrapper接口的实现类要负责管理其基础servlet类的servlet生命周期。
2. 比较重要的方法是load（）和allocate（）方法。allocate（）方法会分配一个已经初始化的servlet实例

### 5.4 Context接口

Context接口中比较重要的方法是addWrapper方法和createWrapper方法

### 5.5 Wrapper 应用程序

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

### 5.6 Context应用程序

1. 本章应用程序展示了如何使用一个包含了两个Wrapper实例的Context实例来构建Web应用程序。
2. 当应用程序中有多个Wrapper实例时，需要使用一个映射器，帮助servlet容器选择一个子容器来处理某个指定的请求
3. Mapper接口
   1. map（）方法，返回要处理某个特定请求的子容器的实例
4. SimpleContext类使用SimpleContextMapper类的实例作为其映射器
5. SimpleContext类的invoke（）:
   1.     public void invoke(Request request, Response response){
          ​    pipeline.invoke(request,response);
          }

1. SimplePipelinel类的invoke（）

1.     public void invoke(Request request, Response response){
       ​    (new SimplePipelineValueContext()).invokeNext(request,response);
       }

#### 5.6.1 SimpleContextValue类

1. 是SimpleContextValue的基础阀，最重要的是invoke方法
2. 调用context的map（）得到Wrapper类，然后调用wrapper的invoke方法

 ####　5.6.2 SimpleContextMapper类

1. map()方法，map方法会从request对象中解析处请求的上下文路径，并调用context实例的findServletMapping（）方法来获取一个与该路径相关联的名称，如果找到了这个名称，则它调用Context实例的findChild（）方法来获取一个Wrapper实例

#### 5.6.3 SimpleContext类

1. addServletMapping（）添加一个URL模式/Wrapper实例的名称对
2. findServletMapping（）通过URL模式查找对应的Wrapper实例名称
3. addMapper（）阿紫容器中添加一个映射器
4. findMapper()找到正确的映射器，在SimpleContext中，他会返回默认映射器
5. map() 返回负责处理当前请求的Wrapper实例

#### 5.6.4 Bootstrap2类

1. main()

## Chapter 6 生命周期

1. 通过实现Lifecycle接口，可以达到统一启动／关闭这些组件的效果
2. 实现了Lifecycle接口的组件可以触发一个或多个下面的事件，需要编写相应的事件监听器对这些事件进行响应

### 6.1 Lifecycle接口

1. Catalina在设计上允许一个组件包含其他组建．父组件负责启动／关闭它的子组件，Catalina的启动类只需要启动一个组件就可以将全部应用的组件都启动起来．这种单一启动／关闭机制是通过Lifecycle接口实现的
2. 组件必须提供start()和stop()方法的实现，供其父组件调用
3. 组件中可以注册多个事件监听器来对发生在该组件上的某些事件进行监听．当某个事件发生时，相应的事件监听器会收到通知

### 6.2 LifecycleEvent类

生命周期事件是LifecycleEvent类的实例

### 6.3 LifecycleListener接口

生命周期事件监听器是LifecycleListener接口的实例

当事件监听器监听到相关事件发生时，会调用该方法lifecycleEvent()

### 6.4 LifecycleSupport类

1. LifecycleSupport帮助组件管理监听器，并触发相应的生命周期事件
2. 将所有的生命周期监听器存储在一个名为listeners的数组中，并初始化为一个没有元素的数组对象
3. fireLifecycleEvent()方法会触发一个生命周期时间．首先，它会复制复制监听器数组，然后它会调用数组中每个成员的lifecycleEvent（）方法，并传入要触发的事件

### 6.5 应用程序

#### 6.5.1 SimpleContext类

1. 单一启动／关闭机制，只需要启动最高层级的组件，其余的组件会由各自的父组件去启动．关闭这些组件，也只需要关闭最高层级的组建即可

#### 6.5.2 SimpleContextLifecycleListener类

#### 6.5.３ SimpleLoader类

#### 6.5.4 SimplePipeline类

#### 6.5.5 SimpleWrapper类

1. SimpleWrapper类的start()方法与SimpleContext类的ｓｔａｒｔ（）方法类似，它会启动添加到其中的所有组件，并触发相应的事件

## Chapter 7 日志记录器

### 7.1 Logger接口

1. Logger接口的最后两个log（）方法接收一个日志级别参数。如果参数的日志级别的数字比日志记录器实例中设定的等级低，才会记录该条消息

### 7.2 Tomcat的日志记录器

#### 7.2.1 LoggerBase类

1. LoggerBase类是一个抽象类，它实现了Logger接口中除log（String msg）方法外的全部方法的实现

#### 7.2.2 SystemOutLogger 类

1. 接收到的每条日志信息都会传递给System.out.println()

#### SystemErrLogger 类

1. 将日志信息输出到标准错误，调用System.err.println()

#### 7.2.4 FileLogger类

1. 会将从servlet容器中接收到的日志消息写到一个文件中，并且可以选择是否要为每条信息添加时间戳
2. open（）
   1. 会在指定目录创建一个新的日志文件。创建一个PrintWriter实例
3. close（）
   1. 负责确保将PrintWriter实例中所有的日志消息都写到文件中
4. log（）
   1. 对时间戳进行处理，会比较tsDate与字符串形式的变量date比较，若二者的值不相等，则log（）方法会关闭当前的日志文件，将tsDate的值赋给date，并打开一个新的日志文件





## Chapter 8  载入器

1. servlet容器需要实现一个自定义的载入器，而不能简单地使用系统的类载入器，因为servlet容器不应该完全信任它正在运行的servlet类
2. servlet应该只允许载入WEB-INF/classes目录及其子目录下的类，和从部署的库到ＷＥＢ-INF/lib目录载入类
3. Tomcat需要实现自定义载入器的另一个原因是，为了提供自动重载的功能，当/classes,/lib目录下的类发生变化时，Ｗｅｂ应用程序会重新载入这些类．类载入器使用一个额外的线程来不断地检查servlet类和其他类的文件的时间戳
4. 仓库（repository）和资源(resource)
   1. 仓库表示类载入器会在哪里搜索要载入的类
   2. 资源指的是一个类载入其中的DirContext的对象，它的文件跟根路径指的就是上下文的文件根路径

### 8.1 Java的类载入器

1. ＪＶＭ使用了３中类载入器来载入所需要的类，分别是引导类载入器，扩展类载入器和系统类载入器．其中引导类载入器位于层次结构的最上层
2. 引导类载入器用于引导启动java虚拟机．引导类载入器是使用本地代码实现的，因为它用来载入运行ＪＶＭ所需要的类，以及所有的Java核心类，例如java.lang/java.io
3. 扩展类载入器负责载入标准扩展目录中的类
4. 系统类载入器是默认的类载入器，它会搜索在环境变量CLASSPATH中指明的路径和JVR文件
5. 代理模型的重要用途就是为了解决类载入过程中的安全性问题．如果用户编写了一个名为java.lang.Object的类，它可以访问硬盘中的任意目录．由于ＪＶＭ是信任java.lang.Object类的，这样，它就不会见识这个类的活动．结果是，如果这个自定义的Ｏｂｊｅｃｔ允许载入，安全管理器就这样被轻易地绕过了．
6. 当程序中的某个地方调用了自定义的java.lang.Object类，系统类载入器会将载入任务委托给扩展类载入器，继而交给引导类载入器，引导类载入器搜索其核心库，找到标准的java.lang.Object类，并将之实例化，结果是，自定义的java.lang.Object并没有被载入
7. Tomcat使用自定义类载入器的原因有以下３条
   1. 为了在载入类中指定某些规则
   2. 为了缓存已经载入的类
   3. 为了实现类的预载入，方便使用



### 8.2 Loader接口

1. Ｔｏｍｃａｔ的载入器通常会与一个Context级别的servlet容器相关联．
2. Loader接口使用modified()方法来支持类的自动重载．如果仓库中的一个类或多个类被修改了，它会返回true

### 8.3 Reloader接口

1. 为了支持类的自动重载功能，类载入器需要实现该接口

### 8.4 WebappLoader 类

1. 实现了loader接口，其实例就是Web应用程序中的载入器
2. 也实现了LifeCycle接口，可以由其关联的容器来启动和关闭
3. 还实现了Runnable接口，可以指定一个线程来不断地调用其类载入器的modified（）方法
4. 当调用WebappLoader类的start()方法时，会完成以下几项重要工作：
   1. 创建一个类载入器
   2. 设置仓库
   3. 设置类路径
   4. 设置访问权限
   5. 启动一个新线程来支持自动重载

#### 8.4.1　创建类载入器

crateClassLoader方法

#### 8.4.2 设置仓库

WebappLoader类的start()方法会调用setRepository()方法将仓库添加到其类载入器中，WEB-INF/classes目录被传入到类载入器的addRepository()方法中,而Web-INF/lib目录被传入到类载入器的setJarPath()方法中

#### 8.4.3 设置类路径

#### 8.4.4 设置访问权限

#### 8.4.5　开启新线程执行类的重新载入

1. 为了实现类的自动载入，WebappLoader类使用一个线程周期性地检查每个资源的时间戳，检查是否有文件需要自动重新载入
2. run()方法中使用一个while循环检查
   1. 使线程休眠一段时间，时长由变量checkInterval指定，单位为秒
   2. 调用WebappLoader实例的类载入器的modified()方法检查已经载入的类是否被修改．若没有被修改，则重新执行循环
   3. 若某个已经载入的类被修改了，则调用私有方法notifyContext(),通知与WebappLoader实例关联的Context容器重新载入相关类
3. notifyContext()方法并不会直接调用Context接口的reload方法，它会实例化一个内部类WebappContextNotifier, 然后将其传入一个新建的线程对象，并调用线程对象的start()方法．这样，重载相关类的任务就可以在另一个线程中完成

### 8.5 WebappClassLoader类

1. 会缓存之前已经载入的类来提升新能

#### 8.5.1 类缓存

java.lang.ClassLoader类会维护一个Vector对象，保存已经载入的类，防止这些类在不使用时当作垃圾被回收，这种情况下，缓存是由父类来管理

#### 8.5.2 载入类

遵守如下规则：

1. 因为所有已经载入的类都会缓存起来，所以载入类时要先检查本地缓存
2. 若本地缓存中没有，则检查上一层缓存
3. 若两个缓存中都没有，则使用系统的类载入器进行加载，防止Ｗｅｂ应用程序中的类覆盖J2EE的类
4. 若启用了安全管理其，则检查是否允许载入该类．如果禁止载入该类，则抛出异常
5. 若打开标志位delegate,或者待载入的类是属于包触发器中的包名，则调用父载入器来载入相关类
6. ．．．．Ｐ１２１

## 第九章 Session管理器

1. Session管理器需要与一个Context容器相关联，且必须与一个context容器关联
2. Session管理器负责创建、更新、销毁Session对象，当有请求到来时，要返回一个有效的Session对象
3. servlet实例通过调用HttpServletRequest接口的getSeesion() 方法来获取一个Session对象
4. 默认情况下，Session管理其会将其管理的Session对象存放在内存中。也可以将Session对象进行持久化，存储到文件存储器或者通过JDBC写进数据库中

### 9.1 Session对象

#### 9.1.1 Session接口

1. getManager() 或 setManager（）方法将Session实例和某个Session管理器相关联
2. 可以通过setId（）和getId（）方法来访问session的标识符
3. getLastAccessedTime()
4. setValid()
5. access()
6. expire() 将session对象设置为过期

#### 9.1.2 StandardSession类

1. StandardSession类是Session接口的标准实现
2. 其中getSession() 方法会通过传入一个自身实例来创建StandardSessionFacade类的一个实例，并将其返回
3. expire() P128

#### 9.1.3 StandardSessionFacade类

1. 为了传递一个Session对象给servlet实例，Catalina会实例化StandardSession类，填充该Session对象，然后再将其传给servlet实例。但实际上，Catalina传递的是Session的外观类StandardSessionFacade的实例，该类仅仅实现了HttpSession接口中的方法，这样servlet程序员就不能将HttpSession对象向下转化为StandardSession类型，也阻止了访问一些敏感方法

### 9.2 Manager

1. ManagerBase类有两个直接子类，StandardManager类和PersistentManager类
2. 当Catalina运行时，StandardManager实例会将所有Session对象存储在内存中。关闭时，它会将当前内存中的所有Session对象存储到一个文件中。当再次启动是，又会将这些Session对象重新载入内存
3. PersistentManager会将session对象存储到辅助存储器中。有两个子类，PersistentManager类和DistributedManager类

#### 9.2.1 Manager接口

1. add()方法会将一个Session实例添加到Session池中
2. load()和unload()方法用来将Session对象持久化到辅助存储器中

#### 9.2.2 ManagerBase类

1. Session管理器会将活动的Session对象都存储在一个名为sessions的HashMap变量中
2. findSession（）

#### 9.2.3 StandardManager类

1. 实现了Lifecycle接口
2. 实现了Runnable接口，有一个专门的线程来负责销毁那些失效的session对象
3. processExpire() 方法会遍历由Session管理器管理的所有Session对象，将Session实例的lastAccessedTime属性值与当前时间比较，如果超过了，会调用session接口的expire()来使这个session过期

#### 9.2.4 PersistentManagerBase类

1. 实现了Runnable接口，使用一个专门的线程来执行备份和换出活动的Session对象的任务
2. 换出和备份 P134

#### 9.2.5 PersistenManger类

#### 9.2.6 DistributedManager类

1. 一个节点表示部署的一台Tomcat服务器。在集群环境中，每个节点必须使用DistributedManager实例作为其Session管理器，这样才能支持复制Session对象。
2. 为了实现复制Session对象的目的，当创建或者销毁Session对象时，DistributedManager实例会向其他节点发送消息。此外，集群中的节点也必须能够接收其他节点发送的消息
3. ClusterSender类用于向集群中的其他节点发送消息，ClusterReceiver类用于接收集群中其他节点发送的消息
4. createSession（）使用ClusterSender以字节数组的形式将该Session对象发送到集群中的其他节点
5. 还实现了Runnable，专门的线程检查Session对象是否过期

### 9.3 存储器

#### 9.3.1 StoreBase类

1. 也是使用另外一个线程周期性地检查Session对象
2. processExpires()

#### 9.3.2 FileStore类

将session对象存储到某个文件中

#### 9.3.3 JDBCStore类

存储到数据库中

#### 9.4 应用程序

#### 9.4.2 SimpleWraaperValve

1. 当调用getSession() 方法时，request对象必须调用与Context容器相关联的Session管理器
2. request对象为了能够访问Session管理器，request对象必须能够访问Context容器
3. 为此，SimpleWrapperValve类的invoke()方法中，需要调用setContext()方法，并传入context实例给request
4. invoke()
5. HttpRequestBase类的doGetSession()会调用context接口的getManager()方法来获取Session管理器对象，然后就可以获取到Session对象
6. doGetSession() P125

## 第10章 安全性

servlet容器是通过一个名为验证器的阀来支持安全限制的。在调用Wrapper阀之前，会先调用验证器阀，对当前用户进行身份验证。如果用户输入了正确的用户名和密码，则验证阀会调用后续的阀

### 过程

Bootstrap中通过context.setRealm() 来设置领域对象，领域对象中包括了用户的信息和角色

然后simpleContextConfig类中会通过lifecycle事件，调用authenticatorConfig()方法，authenticatorConfig()方法会检查当前StardardContext对象的管道中的基础阀或附加阀是否是验证其。因为一个Context实例只能有一个验证器。如果没有验证器，则会默认添加一个BasicAuthenticator阀，每次有http请求时，会调用BasicAuthenticator.authenticate()方法，然后会在之中调用realm.authenticate() 返回Principal() 对象，Principal中有hasRole()方法来检查用户是否有权限访问

```java
public boolean HasRole(String role){
    if(role == null)
        return false;
    return (Arrays.binarySearch(roles, role) >= 0);
}
```

Tomcat中对用户角色权限认证的方法也值得借鉴

```java
class User{
	String username;
     String password;
    ArrayList roles = new ArrayList();
    public void addRole(String role){}
    public ArrayListt getRoles(){}
}
```



## Chapter 11 StandardWrapper



### 11. 1 方法调用序列

具体过程：

1. 连接器创建request和response对象
2. 连接器调用StandardContext实例得invoke()方法
3. StandardContext实例得invoke()方法调用其管道对象的invoke()方法。StandardContext中管道对象得基础阀是StandardContextValve类得实例，因此，StandardContext的管道对象会调用StandardContextValve实例的invoke()方法
4. StandardContextValve实例的invoke()方法获取相应的Wrapper实例处理HTTP请求，调用Wrapper实例的invoke（）方法
5. StandardWrapper类是Wrapper接口的标准实现，StandardWrapper实例的invoke()方法会调用其管道对象的invoke()方法
6. StandardWrapper的管道对象中的基础阀是StandardWrapperValve类的实例，因此，会调用StandardWrapperValve的invoke()方法，StandardWrapperValve的invoke()方法会调用Wrapper实例的allocate()方法获取servlet实例
7. allocate()方法调用load()方法载入相应的servlet类，若已经载入，不需要重复载入
8. load()方法调用servlet的init()方法
9. StandardWrapperValve调用servlet的service()方法

### 11.2 SingThreadModel

1. 实现此接口的目的是保证servlet实例一次只处理一个请求。
2. servlet容器维护一个servlet实例池，然后将每个新请求分派给一个空闲的servlet实例。该接口并不能防止servlet访问共享资源造成的同步问题，例如访问类的静态变量
3. 实现了SingleThreadModel接口的servlet类只能保证再同一时刻，只有一个线程在执行该servlet实例的service（）方法
4. ps：实现了该接口的servlet类并不是多线程安全的

### 11.3 StandardWrapper

1. StandardWrapper的主要任务是载入它所代表的servlet类，并进行实例化，调用servlet的service()方法由StandardWrapperValve对象完成
2. 对于没有实现SingThreadModel接口的servlet类，StandardWrapper只会载入该servlet类一次，并对随后的请求都返回该servlet类的同一个实例
3. 为了获得更好的性能，会维护一个STM servlet实例池

#### 11.3.1 分配servlet 实例

1. allocate()方法分为两个部分，为了处理STM servlet和非STM servlet

2. ```java
   if(!singleThreadModel){
       if(instance == null){
           synchronized(this){
               if(instance == null){
                   try{
                       instance = loadServlet();
                   }catch(){
                       
                   }
               }
           }
           
           if(!singleThreadModel){
               if(debug >= 2){
                   countAllocated++;
                   return instance;
               }
           }
       }
   }
   ```

3. 若servlet类是一个STM servlet类，则allocate()会试图从对象池中返回一个servlet实例。变量instancePool是一个Stack类型的实例，保存了所有的STM servlet实例

4.  ```java
   synchronized(instancePool){
       while（countAllocated >= nInstances）{
           if(nInstances < maxInstances){
               try{
                   instancePool.push(loadServlet());
                   nInstances++;
               }
           }else{
               try{
                   instancePool.wait();
               }
           }
       }
       if(debug >= 2){
           log
       }
       countAllocated++;
       return (Servlet) instancePool.pop();
   }
    ```

5. 如果变量nInstances的值大于等于变量maxInstance的值，他会通过调用instancePool栈的wait()方法进入等待状态，直到某个STM servlet实例被放回到栈中

#### 11.3.2 载入servlet类

1. loadServlet()

2. 在实例化servelt类后，loadServlet()方法出发BEFORE_INIT_EVET事件，即调用servlet（）实例的init（）方法

3. ```java
   instanceSupport.fireInstanceEvent(Instance.BEFORE_INIT_EVENT,servlet);
   servlet.init(facade);
   instanceSupport.fireInstanceEvent(Instance.AFTER_INIT_EVENT,servlet);
   ```

4. 如果servlet类是一个STM servlet，则要将该servlet实例添加到servlet实例池中

#### 11.3.3 ServletConfig对象

1. StandardWrapper对象如何获取servletConfig对象？
2. 答案在StandardWrapper类本身，该类不仅是先了Wrapper接口，还实现了ServletConfig接口
3. 使用一个map来存储初始化参数的值

#### 11.3.4 servlet容器的父子关系

#### 11.4 StandardWrapperFacade类

1. 当在StandardWrapper类使用下面的代码创建StandardWrapperFacade类的一个实例，需要将自身实例作为参数传入StandardWrapperFacade的构造函数

2. ```java
   private StandardWrapperFacade facade = new StandardWrapperFacade(this);
   public StandardWrapperFacade(StandardWrapper config){
       super();
       this.config = (ServletConfig) config;
   }
   ```


### StandardWrapperValve类

1. 是StandardWrapper的基础阀，要完成的两个操作

   1. 执行与该servlet实例相关联的全部过滤器

   2. 调用servlet实例的service方法

       

2. StandardWrapperValve的invoke()方法实现中会执行一下几个操作
   1. 调用StandardWrapper实例的allocate（）方法获取其表示的servlet实例
   2. 调用私有方法createFilterChain()，创建过滤器链
   3. 调用过滤器链的doFilter()方法，其中包括调用servlet实例的service()方法
   4. 释放过滤器链
   5. 调用Wrapper实例的deallocate()方法
   6. 如果该servlet类再也不会被使用到，调用unload()方法

### 11.8 ApplicationFilterChain类

1. StandardWrapperValve类的invoke()方法会创建ApplicationFilterChain类的一个实例，并调用其doFilter()方法

2. doFilter()方法会调用过滤器链中第一个过滤器的doFilter()方法。

3. doFilter(ServletRequest,ServletResponse,FilterChain chain);

4. ```java
   doFilter(ServletRequest,ServletResponse,FilterChain chain){
       chain.doFilter(request,response);
   }
   ```

5.  如果某个过滤器是过滤器链中的最后一个过滤器，则会调用被请求的servlet类的service()方法

6. 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

#### 

