事件监听机制是这样的:
LifecycleListener是监听器，监听每一个对象发生的事件
而事件是如何触发的呢？事件是在相应的对象的start(),stop()方法中触发的，通过调用LifecycleSupport的fireLifecycleEvent触发的
每一个对象中都有一个LifecycleSupport对象，里面管理着监听该对象的所有监听器，当事件发生时，fireLifecycleEvent()会调用监听器的lifecycleEvent()方法
start()->LifecycleSupport.fireLifecycleEvent()->LifecycleListener.lifecycleEvent()