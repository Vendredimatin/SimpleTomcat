package Chpater5.values;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @program: SimpleTomcat
 * @description: 记录请求头信息的阀类
 * @author: Liu Hanyi
 * @create: 2019-05-15 20:12
 **/

public class HeaderLoggerValve implements Valve, Contained {
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
        System.out.println("Header Logger Valve");
        ServletRequest servletRequest = request.getRequest();
        if (servletRequest instanceof HttpServletRequest){
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            Enumeration headerNames = httpServletRequest.getHeaderNames();
            while (headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement().toString();
                String headerValue = httpServletRequest.getHeader(headerName);
                System.out.println(headerName+":"+headerValue);
            }
        }else
            System.out.println("Not an Http Request");

        System.out.println("--------------------------------------------");
    }
}
