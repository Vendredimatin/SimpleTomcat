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
 * @description: 改进的ServletProcessor
 * @author: Liu Hanyi
 * @create: 2019-05-07 15:33
 **/

@SuppressWarnings("Duplicates")
public class ServletProcessor2 {
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
        ResponseFacade responseFacade = new ResponseFacade(response);
        RequestFacade requestFacade = new RequestFacade(request);
        try {
            servlet = (Servlet) myClass.newInstance();
            servlet.service((ServletRequest) requestFacade, (ServletResponse) responseFacade);
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
