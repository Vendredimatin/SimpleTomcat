package Chapter1;

import java.io.*;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws IOException {

        //创建与指定host，ip绑定的socket
        Socket socket = new Socket("127.0.0.1",8080);
        //从socket获取输入输出流
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        //发出HTTP request请求，填写请求头部，此处实体为空
        //PrintWriter类是向输出流格式化输出数据的类，若制定自动刷新，则其println，printf，format等方法时会刷新生效
        Boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(),autoflush);
        out.println("GET /index.html HTTP/1.1");
        out.println("Host:localhost:8080");
        out.println();

        //读取response
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        boolean loop = true;
        StringBuffer sb = new StringBuffer(8096);
        while(loop){
            if(in.ready()){
                int i = 0;
                while(i!=-1){
                    i=in.read();
                    sb.append((char)i);
                }
                loop = false;
            }
        }
        System.out.println(sb.toString());
        socket.close();
    }
}
