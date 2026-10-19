package com.hospital.smartwait.pattern.observer;

import com.hospital.smartwait.pattern.factory.NotificationFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 事件发布器（观察者模式：Subject 主题）
 *
 * <p>// @Pattern 观察者模式（Observer）
 * // @UseCase UC-02 候诊排队叫号
 *
 * <p>维护观察者列表，负责注册与广播叫号事件。观察者实例由
 * {@link NotificationFactory}（工厂模式）创建，两种模式在此联动。
 */
@Component
public class EventPublisher {

    /** 已注册的观察者（通知器）列表 */
    private final List<Notifier> notifiers;

    public EventPublisher(NotificationFactory factory) {
        // 工厂模式：按类型创建三个观察者并注册
        this.notifiers = List.of(
                factory.create("SCREEN"),
                factory.create("SMS"),
                factory.create("VOICE")
        );
    }

    /** 广播叫号事件给所有观察者 */
    public void publish(CallEvent event) {
        for (Notifier notifier : notifiers) {
            notifier.notify(event);
        }
    }
}
