package Chapter1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: Tomcat
 * @description: HTTP服务器类
 * @author: Liu Hanyi
 * @create: 2019-05-02 18:43
 **/

public class HttpServer {
    private static final String SHUTDOWN = "/shutdown";
    private static boolean shutdown = false;
    public static final String WEB_ROOT = System.getProperty("user.dir")+"/Chapter1/web";

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        System.out.println(WEB_ROOT);
        httpServer.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!shutdown) {

            try {
                Socket socket = serverSocket.accept();

                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                Request request = new Request(inputStream);
                request.parse();

                Response response = new Response(outputStream);
                response.setRequest(request);
                response.sendStaticResource();

                socket.close();

                if (request.getUri().equals(SHUTDOWN))
                    shutdown = true;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

        }
    }

}
