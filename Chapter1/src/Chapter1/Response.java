package Chapter1;

import java.io.*;

/**
 * @program: Tomcat
 * @description: 响应类
 * @author: Liu Hanyi
 * @create: 2019-05-02 18:44
 **/

public class Response {
    private OutputStream out;
    private Chapter1.Request request;

    public Response(OutputStream out) {
        this.out = out;
    }

    public void setRequest(Chapter1.Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[1024];
        FileInputStream fips = null;
        StringBuffer sb = new StringBuffer();
        sb.append("http/1.1 200 ok").append("\n\n");

        BufferedWriter bw;
        try {
            File file = new File(Chapter1.HttpServer.WEB_ROOT, request.getUri());
            if (file.exists()) {

                fips = new FileInputStream(file);
                int i = fips.read(bytes, 0, 1024);
                while (i != -1) {
                    sb.append(new String(bytes,0,i));
                    i = fips.read(bytes, 0, 1024);
                }

                bw = new BufferedWriter(new OutputStreamWriter(out));
                bw.write(sb.toString());
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fips != null)
                fips.close();
        }

    }
}
