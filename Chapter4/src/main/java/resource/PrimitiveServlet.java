package resource;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: Tomcat
 * @description: 简单的Servlet
 * @author: Liu Hanyi
 * @create: 2019-05-07 14:20
 **/

public class PrimitiveServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }


    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("from service");
        PrintWriter out = servletResponse.getWriter();
        out.println("http/1.1 200 ok\n");

        out.println("Roses are red");
/*        StringBuffer buffer = new StringBuffer();
        buffer.append("http/1.1 200 ok").append("\n\n");
        buffer.append("Hello. Roses are red");
        Response response = (Response) servletResponse;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.out));
        bw.write(buffer.toString());
        bw.flush();

        bw.close();*/

        out.print("Violets are blue");

        System.out.println("out complete");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
