package Chapter2;

/**
 * @program: Tomcat
 * @description: 处理对静态资源的请求
 * @author: Liu Hanyi
 * @create: 2019-05-07 11:48
 **/

public class StaticResourceProcessor {

    public void process(Request request, Response response) {
        response.sendStaticResource();
    }
}
