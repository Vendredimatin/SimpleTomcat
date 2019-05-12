package Chapter3.http;

import java.net.Socket;

/**
 * @program: Tomcat
 * @description: 处理Ｈｔｔｐ请求,创建request,response，并传递给相应的processor,解析http请求，填充HttpRequest
 * @author: Liu Hanyi
 * @create: 2019-05-10 10:07
 **/

public class HttpProcessor {
    public HttpProcessor(HttpConnector httpConnector) {

    }

    public void process(Socket socket) {
    }

    public void parseRequest(){}
}
