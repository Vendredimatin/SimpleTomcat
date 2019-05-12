package Chapter2;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @program: Tomcat
 * @description: servlet 处理器类
 * @author: Liu Hanyi
 * @create: 2019-05-07 11:48
 **/

public class ServletProcessor1 {

    public void process(Request request, Response response) {
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        File filpath = new File("");
        String path = "file:" + Constants.WEB_ROOT;
        URL newurl = null;
        try {
            newurl = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        URLClassLoader classLoader = new URLClassLoader(new URL[]{newurl});
        Class myClass = null;
        try {
            myClass = classLoader.loadClass("resource."+servletName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Servlet servlet = null;
        try {
            servlet = (Servlet) myClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
