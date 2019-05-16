package Chpater5.startup;

import Chpater5.core.SimpleLoader;
import Chpater5.core.SimpleWrapper;
import Chpater5.values.ClientIPLoggerValve;
import Chpater5.values.HeaderLoggerValve;
import org.apache.catalina.Loader;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.http.HttpConnector;

/**
 * @program: SimpleTomcat
 * @description: 启动类
 * @author: Liu Hanyi
 * @create: 2019-05-15 20:14
 **/

public class Bootstrap1 {
    public static void main(String[] args) {

        HttpConnector connector = new HttpConnector();
        Wrapper wrapper = new SimpleWrapper();
        wrapper.setServletClass("ModernServlet");
        Loader loader = new SimpleLoader();
        Valve valve1 = new ClientIPLoggerValve();
        Valve valve2 = new HeaderLoggerValve();

        wrapper.setLoader(loader);
        ((Pipeline)wrapper).addValve(valve1);
        ((Pipeline)wrapper).addValve(valve2);

        connector.setContainer(wrapper);

        try {
            connector.initialize();
            connector.start();

            // make the application wait until we press a key.
            System.in.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
