package Chapter2;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

/**
 * @program: Tomcat
 * @description: 响应类
 * @author: Liu Hanyi
 * @create: 2019-05-07 11:29
 **/

public class Response implements ServletResponse {
    OutputStream out;
    Request request;
    private static final int BUFFER_SIZE = 1024;
    PrintWriter writer;

    public Response(OutputStream out) {
        this.out = out;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() {
        String uri = request.getUri();
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        BufferedWriter bw;
        StringBuffer buffer = new StringBuffer();
        buffer.append("http/1.1 200 ok").append("\n\n");

        try {
            File file = new File(Constants.WEB_ROOT, uri);
            if (file.exists()) {
                fis = new FileInputStream(file);
                int c = 0;
                while ((c = fis.read(bytes,0,BUFFER_SIZE)) != -1){
                    buffer.append(new String(bytes,0,c));
                }

                bw = new BufferedWriter(new OutputStreamWriter(out));
                bw.write(buffer.toString());
                bw.flush();
                bw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis !=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        writer = new PrintWriter(out, true);
        return writer;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
