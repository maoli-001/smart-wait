package com.hospital.smartwait.pattern.observer;

/**
 * 观察者接口（观察者模式：Observer）
 *
 * <p>所有关心"叫号事件"的组件实现本接口，并注册到
 * {@link EventPublisher}（Subject），收到事件后执行各自的通知逻辑。
 */
public interface Notifier {

    /** 观察者名称（用于日志与调试） */
    String name();

    /** 处理叫号事件 */
    void notify(CallEvent event);
}
