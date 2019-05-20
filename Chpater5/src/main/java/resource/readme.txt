1.调用过程　http->HttpConnector->HttpProcessor处理请求，创建请求响应，将http请求填充请求对象，调用process()方法,process()中调用this.connector.getContainer().invoke(this.request, this.response);
－＞SimpleWapper.invoke->SimplePipeline.invoke->SimplePipeValveContext.invokeNext->开始调用所有阀，然后在阀中调用invoke传递自己
－＞SimpleWapperValve基础阀，servlet = simpleWrapper.allocate(); servlet.service();
2.SimpleLoader()中采用书中加载仍然有问题，所以采用Ｃhpater2的做法

应用程序２
是这样映射的：context中有两个hashmap servletMapping和children children存放wrapper实例，每一个Wrapper中的name,servletClass表明了这个wrapper是哪个servlet
context的基础阀调用context.map(request, true);，而map方法中根据协议使用相应的mapper,然后调用mapper.map(request, update)得到相应的wrapper
mapper中的map方法会调用context.findChild(name); 从children中得到相应name实例
servletMapping 存的是路径和name的对应