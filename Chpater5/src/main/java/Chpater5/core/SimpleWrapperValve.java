package Chpater5.core;

import org.apache.catalina.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: SimpleTomcat
 * @description: Wrapper的基础阀类
 * @author: Liu Hanyi
 * @create: 2019-05-15 20:10
 **/

public class SimpleWrapperValve implements Valve, Contained {
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
        //基础阀不需要再接着传递，调用需要调用的ｓｅｒｖｌｅｔ方法
        SimpleWrapper simpleWrapper = (SimpleWrapper) getContainer();
        ServletRequest sreq = request.getRequest();
        ServletResponse sres = response.getResponse();
        Servlet servlet = null;

        HttpServletRequest hreq = null;
        if (sreq instanceof HttpServletRequest)
            hreq = (HttpServletRequest) sreq;
        HttpServletResponse hres = null;
        if (sres instanceof HttpServletResponse)
            hres = (HttpServletResponse) sres;

        // Allocate a servlet instance to process this request
        // Allocate a servlet instance to process this request
        try {
            servlet = simpleWrapper.allocate();
            if (hres!=null && hreq!=null) {
                servlet.service(hreq, hres);
            }
            else {
                servlet.service(sreq, sres);
            }
        }
        catch (ServletException e) {
        }
    }
}
