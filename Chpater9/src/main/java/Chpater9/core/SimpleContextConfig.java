package Chpater9.core;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

/**
 * @program: SimpleTomcat
 * @description: StandardContext的监听器类
 * @author: Liu Hanyi
 * @create: 2019-06-04 16:50
 **/

public class SimpleContextConfig implements LifecycleListener {

    public void lifecycleEvent(LifecycleEvent event) {
        if (Lifecycle.START_EVENT.equals(event.getType())) {
            Context context = (Context) event.getLifecycle();
            context.setConfigured(true);
        }
    }
}
