package Chapter2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class HttpServer1
{
    public static void main( String[] args )
    {
        HttpServer1 httpServer1 = new HttpServer1();
        httpServer1.await();
    }

    public void await(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080,1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket socket = null;
        InputStream input = null;
        OutputStream output = null;

        while (true){
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                //创建Ｒｅｑｕｅｓｔ和Ｒｅｓｐｏｎｓｅ
                Request request = new Request(input);
                request.parse();

                Response response = new Response(output);
                response.setRequest(request);

                System.out.println("访问："+request.getUri());
                if (request.getUri().startsWith("/servlet/")){
                    ServletProcessor1 processor1 = new ServletProcessor1();
                    processor1.process(request,response);
                }else{
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request,response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
