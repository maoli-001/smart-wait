package com.hospital.smartwait.pattern.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 短信通知器（观察者模式：ConcreteObserver 具体观察者）
 *
 * <p>// @Pattern 观察者模式（Observer）
 * // @UseCase UC-03a 接收叫号提示
 *
 * <p>课程演示中以日志模拟短信发送，不实际接入短信网关。
 */
@Component
public class SmsNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(SmsNotifier.class);

    @Override
    public String name() {
        return "短信通知";
    }

    @Override
    public void notify(CallEvent event) {
        log.info("[{}] 向患者 {}（{}号）发送短信：请您前往诊室 {}",
                name(), event.getPatientName(), event.getQueueNo(), event.getRoomNo());
    }
}
