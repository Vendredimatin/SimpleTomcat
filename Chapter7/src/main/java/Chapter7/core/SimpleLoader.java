package Chapter7.core;


import org.apache.catalina.*;

import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @program: SimpleTomcat
 * @description: tomcat加载器类
 * @author: Liu Hanyi
 * @create: 2019-05-15 20:08
 **/

public class SimpleLoader implements Loader, Lifecycle {
    private URLClassLoader loader;
    private Container container;
    private static final String WEB_ROOT = System.getProperty("user.dir")+"/Chapter5/src/main/java/resource";

    public SimpleLoader() {
        String path = "file:" + WEB_ROOT;
        URL newurl = null;
        try {
            newurl = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        loader = new URLClassLoader(new URL[]{newurl});

    }

    @Override
    public ClassLoader getClassLoader() {
        return loader;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public DefaultContext getDefaultContext() {
        return null;
    }

    @Override
    public void setDefaultContext(DefaultContext defaultContext) {

    }

    @Override
    public boolean getDelegate() {
        return false;
    }

    @Override
    public void setDelegate(boolean b) {

    }

    @Override
    public String getInfo() {
        return "a simple loader";
    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void setReloadable(boolean b) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void addRepository(String s) {

    }

    @Override
    public String[] findRepositories() {
        return new String[0];
    }

    @Override
    public boolean modified() {
        return false;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

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
        System.out.println("Starting SimpleLoader");
    }

    @Override
    public void stop() throws LifecycleException {

    }
}
