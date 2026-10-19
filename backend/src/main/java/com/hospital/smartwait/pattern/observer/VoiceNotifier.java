package com.hospital.smartwait.pattern.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 语音播报通知器（观察者模式：ConcreteObserver 具体观察者）
 *
 * <p>// @Pattern 观察者模式（Observer）
 * // @UseCase UC-03a 接收叫号提示
 *
 * <p>课程演示中以日志模拟语音播报，不实际接入语音合成服务。
 */
@Component
public class VoiceNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(VoiceNotifier.class);

    @Override
    public String name() {
        return "语音播报";
    }

    @Override
    public void notify(CallEvent event) {
        log.info("[{}] 语音播报：请 {} 号 {} 患者到诊室 {}",
                name(), event.getQueueNo(), event.getPatientName(), event.getRoomNo());
    }
}
