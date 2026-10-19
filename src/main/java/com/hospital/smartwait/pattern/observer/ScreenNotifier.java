package com.hospital.smartwait.pattern.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 候诊大屏通知器（观察者模式：ConcreteObserver 具体观察者）
 *
 * <p>// @Pattern 观察者模式（Observer）
 * // @UseCase UC-02 / UC-03 候诊信息展示
 *
 * <p>收到叫号事件后把"当前叫号信息"缓存到内存，
 * 候诊大屏接口 GET /api/board/status 轮询读取该缓存。
 */
@Component
public class ScreenNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(ScreenNotifier.class);

    /** 最近一次叫号信息（内存缓存，供大屏轮询） */
    private volatile CallEvent latestCall;

    @Override
    public String name() {
        return "候诊大屏";
    }

    @Override
    public void notify(CallEvent event) {
        this.latestCall = event;
        log.info("[{}] 大屏显示：请 {} 号患者 {} 到诊室 {} 就诊",
                name(), event.getQueueNo(), event.getPatientName(), event.getRoomNo());
    }

    /** 供 BoardController 读取当前叫号信息 */
    public CallEvent getLatestCall() {
        return latestCall;
    }
}
