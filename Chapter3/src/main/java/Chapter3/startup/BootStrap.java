package Chapter3.startup;

import Chapter3.http.HttpConnector;

/**
 * Hello world!
 */
public class BootStrap {
    public static void main(String[] args) {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.start();
        System.out.println("服务器启动!");
    }
}
