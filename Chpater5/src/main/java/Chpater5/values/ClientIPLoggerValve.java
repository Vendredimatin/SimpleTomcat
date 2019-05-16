package Chpater5.values;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 * @program: SimpleTomcat
 * @description: 记录ip的阀类
 * @author: Liu Hanyi
 * @create: 2019-05-15 20:11
 **/

public class ClientIPLoggerValve implements Valve, Contained {
    protected Container container;

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        valveContext.invokeNext(request,response);
        System.out.println("Client IP Logger valve");
        ServletRequest servletRequest = request.getRequest();
        System.out.println(servletRequest.getRemoteAddr());
        System.out.println("------------------------------------");
    }
}
