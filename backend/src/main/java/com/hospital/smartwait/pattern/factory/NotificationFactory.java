package com.hospital.smartwait.pattern.factory;

import com.hospital.smartwait.pattern.observer.Notifier;
import com.hospital.smartwait.pattern.observer.ScreenNotifier;
import com.hospital.smartwait.pattern.observer.SmsNotifier;
import com.hospital.smartwait.pattern.observer.VoiceNotifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * 通知器工厂（工厂模式：Factory）
 *
 * <p>// @Pattern 工厂模式（Factory Method / Simple Factory）
 * // @UseCase UC-03a 接收叫号提示 / UC-02 候诊排队叫号
 *
 * <p>根据通知类型从 Spring 容器中挑选对应的 {@link Notifier} 观察者实例，
 * 使创建/挑选逻辑集中：新增通知方式只需新增一个 Notifier Bean，
 * 发布器与业务代码无需修改（开闭原则）。
 */
@Component
public class NotificationFactory {

    /** 通知类型常量 */
    public static final String TYPE_SCREEN = "SCREEN";
    public static final String TYPE_SMS = "SMS";
    public static final String TYPE_VOICE = "VOICE";

    /** 容器中所有观察者 Bean */
    private final List<Notifier> notifiers;

    public NotificationFactory(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    /**
     * 按类型返回对应的通知器
     *
     * @param type 通知类型（SCREEN / SMS / VOICE）
     * @return 对应通知器实例
     */
    public Notifier create(String type) {
        Predicate<Notifier> predicate;
        switch (type) {
            case TYPE_SCREEN:
                predicate = n -> n instanceof ScreenNotifier;
                break;
            case TYPE_SMS:
                predicate = n -> n instanceof SmsNotifier;
                break;
            case TYPE_VOICE:
                predicate = n -> n instanceof VoiceNotifier;
                break;
            default:
                throw new IllegalArgumentException("未知的通知类型：" + type);
        }
        return notifiers.stream().filter(predicate).findFirst()
                .orElseThrow(() -> new IllegalStateException("未找到通知器：" + type));
    }
}
