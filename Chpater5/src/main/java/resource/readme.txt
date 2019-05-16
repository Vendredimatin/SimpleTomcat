1.调用过程　http->HttpConnector->HttpProcessor处理请求，创建请求响应，将http请求填充请求对象，调用process()方法,process()中调用this.connector.getContainer().invoke(this.request, this.response);
－＞SimpleWapper.invoke->SimplePipeline.invoke->SimplePipeValveContext.invokeNext->开始调用所有阀，然后在阀中调用invoke传递自己
－＞SimpleWapperValve基础阀，servlet = simpleWrapper.allocate(); servlet.service();
2.SimpleLoader()中采用书中加载仍然有问题，所以采用Ｃhpater2的做法