package Chapter3.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: Tomcat
 * @description: tomcat连接器,等待Ｈｔｔｐ请求
 * @author: Liu Hanyi
 * @create: 2019-05-10 10:06
 **/

public class HttpConnector implements Runnable{
    boolean stopped;
    private String scheme = "http";

    public String getScheme(){
        return scheme;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.run();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port,1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!stopped) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            HttpProcessor httpProcessor = new HttpProcessor(this);
            httpProcessor.process(socket);
        }


    }
}
