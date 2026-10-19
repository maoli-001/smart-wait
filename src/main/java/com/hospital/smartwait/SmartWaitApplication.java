package com.hospital.smartwait;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 智能候诊系统启动类
 *
 * <p>系统按分层架构组织：controller（接口层）→ service（业务层）→
 * repository（持久层）→ model（实体层），公共能力位于 common/util/config 包。
 *
 * <p>落地设计模式：
 *  1. {@link com.hospital.smartwait.pattern.observer.EventPublisher}  观察者模式（叫号事件 → 候诊大屏）
 *  2. {@link com.hospital.smartwait.pattern.state.QueueState}         状态模式（排队状态机）
 *  3. {@link com.hospital.smartwait.pattern.factory.ServiceFactory}   工厂模式（按业务类型创建服务）
 *
 * @author 智能候诊系统小组
 */
@SpringBootApplication
public class SmartWaitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartWaitApplication.class, args);
    }
}
