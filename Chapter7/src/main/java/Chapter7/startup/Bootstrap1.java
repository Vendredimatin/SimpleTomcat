package Chapter7.startup;

import Chapter7.core.*;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.logger.FileLogger;

/**
 * @program: SimpleTomcat
 * @description: 启动类
 * @author: Liu Hanyi
 * @create: 2019-05-15 20:14
 **/

@SuppressWarnings("Duplicates")
public class Bootstrap1 {
    public static void main(String[] args) {

        Connector connector = new HttpConnector();
        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new SimpleContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);

        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        LifecycleListener listener = new SimpleContextLifecycleListener();
        ((Lifecycle) context).addLifecycleListener(listener);
        context.addMapper(mapper);
        Loader loader = new SimpleLoader();
        context.setLoader(loader);
        // context.addServletMapping(pattern, name);
        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");

        // ------ add logger --------
        System.setProperty("catalina.base", System.getProperty("user.dir"));
        FileLogger logger = new FileLogger();
        logger.setPrefix("FileLog_");
        logger.setSuffix(".txt");
        logger.setTimestamp(true);
        logger.setDirectory("resource");
        context.setLogger(logger);

        //---------------------------

        connector.setContainer(context);
        try {
            connector.initialize();
            ((Lifecycle) connector).start();
            ((Lifecycle) context).start();

            // make the application wait until we press a key.
            System.in.read();
            ((Lifecycle) context).stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
