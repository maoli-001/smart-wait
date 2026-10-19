package com.hospital.smartwait.service;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.model.QueueEntry;
import com.hospital.smartwait.pattern.observer.CallEvent;
import com.hospital.smartwait.pattern.observer.EventPublisher;
import com.hospital.smartwait.pattern.observer.ScreenNotifier;
import com.hospital.smartwait.pattern.state.QueueState;
import com.hospital.smartwait.repository.QueueEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 排队叫号服务（UC-02 候诊排队叫号）
 *
 * <p>业务规则（对应需求文档）：
 *  BR-4 叫号后规定时间内未到视为过号（超时时长可配置）；
 *  BR-5 过号患者仅可重排一次，重排插入队尾；
 *  BR-6 同一时间仅呼叫一位患者。
 *
 * <p>// @Pattern 状态模式（State）：状态流转由 {@link QueueState} 状态机校验
 * // @Pattern 观察者模式（Observer）：叫号事件通过 {@link EventPublisher} 广播
 */
@Service
public class QueueService {

    /** 过号判定超时（分钟），可配置（BR-4） */
    private static final int MISS_TIMEOUT_MINUTES = 5;

    private final QueueEntryRepository queueEntryRepository;
    private final EventPublisher eventPublisher;
    private final ScreenNotifier screenNotifier;

    public QueueService(QueueEntryRepository queueEntryRepository,
                        EventPublisher eventPublisher,
                        ScreenNotifier screenNotifier) {
        this.queueEntryRepository = queueEntryRepository;
        this.eventPublisher = eventPublisher;
        this.screenNotifier = screenNotifier;
    }

    /**
     * 医生叫下一号（BR-6 同一时间仅呼叫一位）
     *
     * @param doctorId 医生
     * @param roomNo   诊室号
     * @return 被叫号患者信息
     */
    @Transactional
    public QueueEntry call(Long doctorId, String roomNo) {
        // BR-6：若存在已叫号未处理患者，不允许重复叫号
        List<QueueEntry> called = queueEntryRepository
                .findByDoctorIdAndStatusOrderByQueueNoAsc(doctorId, QueueEntry.STATUS_CALLED);
        if (!called.isEmpty()) {
            throw new BizException(4008, "已呼叫患者 " + called.get(0).getQueueNo()
                    + "，请先处理后再叫号");
        }
        List<QueueEntry> waiting = queueEntryRepository
                .findByDoctorIdAndStatusOrderByQueueNoAsc(doctorId, QueueEntry.STATUS_WAITING);
        if (waiting.isEmpty()) {
            throw new BizException(4009, "当前队列为空，暂无可叫号患者");
        }
        QueueEntry entry = waiting.get(0);
        // 状态机：WAITING → CALLED（非法流转会抛异常）
        entry.setStatus(QueueState.valueOf(entry.getStatus()).call().name());
        queueEntryRepository.save(entry);

        // 观察者模式：广播叫号事件（大屏/短信/语音）
        eventPublisher.publish(new CallEvent(
                entry.getQueueNo(), entry.getPatientName(),
                entry.getDeptId(), entry.getDoctorId(), roomNo));
        return entry;
    }

    /** 患者到场确认就诊：CALLED → SEEN */
    @Transactional
    public void seen(String queueNo) {
        QueueEntry entry = getEntry(queueNo);
        entry.setStatus(QueueState.valueOf(entry.getStatus()).seen().name());
        queueEntryRepository.save(entry);
    }

    /** 标记过号：CALLED → MISSED（BR-4） */
    @Transactional
    public void missed(String queueNo) {
        QueueEntry entry = getEntry(queueNo);
        entry.setStatus(QueueState.valueOf(entry.getStatus()).missed().name());
        queueEntryRepository.save(entry);
    }

    /**
     * 过号患者申请重排：MISSED → WAITING（BR-5 仅一次）
     */
    @Transactional
    public QueueEntry reorder(String queueNo) {
        QueueEntry entry = getEntry(queueNo);
        if (entry.getReorderCount() >= 1) {
            throw new BizException(4010, "过号患者仅可重排一次");
        }
        entry.setStatus(QueueState.valueOf(entry.getStatus()).reorder().name());
        entry.setReorderCount(entry.getReorderCount() + 1);
        // 重排插入队尾：直接更新状态，编号不变（BR-5）
        queueEntryRepository.save(entry);
        return entry;
    }

    /** 放弃重排：离场（MISSED → LEFT） */
    @Transactional
    public void leave(String queueNo) {
        QueueEntry entry = getEntry(queueNo);
        entry.setStatus(QueueState.valueOf(entry.getStatus()).leave().name());
        queueEntryRepository.save(entry);
    }

    /** 读取候诊大屏当前叫号缓存（供 BoardController 轮询） */
    public CallEvent latestCall() {
        return screenNotifier.getLatestCall();
    }

    private QueueEntry getEntry(String queueNo) {
        return queueEntryRepository.findByQueueNo(queueNo)
                .orElseThrow(() -> new BizException(4007, "排队记录不存在：" + queueNo));
    }
}
