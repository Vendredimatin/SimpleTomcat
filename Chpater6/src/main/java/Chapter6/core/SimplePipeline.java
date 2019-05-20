package Chapter6.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @program: SimpleTomcat
 * @description: Tomcat 管道类
 * @author: Liu Hanyi
 * @create: 2019-05-15 20:09
 **/

@SuppressWarnings("Duplicates")
public class SimplePipeline implements Pipeline, Lifecycle {

    protected Valve basic;
    protected Container container;
    protected Valve[] valves = new Valve[0];


    public SimplePipeline(Container container) {
        setContainer(container);
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Valve getBasic() {
        return basic;
    }

    @Override
    public void setBasic(Valve valve) {
        this.basic = valve;
        ((Contained)valve).setContainer(container);
    }

    @Override
    public void addValve(Valve valve) {
        if (valve instanceof Contained)
            ((Contained)valve).setContainer(container);

        //在阀队列中加入阀
        synchronized (valves) {
            Valve results[] = new Valve[valves.length +1];
            System.arraycopy(valves, 0, results, 0, valves.length);
            results[valves.length] = valve;
            valves = results;
        }
    }

    @Override
    public Valve[] getValves() {
        return valves;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        new SimplePipelineValveContext().invokeNext(request,response);
    }

    @Override
    public void removeValve(Valve valve) {

    }

    @Override
    public void addLifecycleListener(LifecycleListener lifecycleListener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener lifecycleListener) {

    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("Starting SimplePipeline");
    }

    @Override
    public void stop() throws LifecycleException {

    }

    // this class is copied from org.apache.catalina.core.StandardPipeline class's
    // StandardPipelineValveContext inner class.
    protected class SimplePipelineValveContext implements ValveContext {

        protected int stage = 0;

        public String getInfo() {
            return null;
        }

        public void invokeNext(Request request, Response response)
                throws IOException, ServletException {
            int subscript = stage;
            stage = stage + 1;
            // Invoke the requested Valve for the current request thread
            if (subscript < valves.length) {
                valves[subscript].invoke(request, response, this);
            }
            else if ((subscript == valves.length) && (basic != null)) {
                basic.invoke(request, response, this);
            }
            else {
                throw new ServletException("No valve");
            }
        }
    } // end of inner class
}
