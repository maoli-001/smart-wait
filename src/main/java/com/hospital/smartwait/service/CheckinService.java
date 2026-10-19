package com.hospital.smartwait.service;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.model.QueueEntry;
import com.hospital.smartwait.model.Schedule;
import com.hospital.smartwait.pattern.state.QueueState;
import com.hospital.smartwait.repository.QueueEntryRepository;
import com.hospital.smartwait.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签到服务（UC-01 患者扫码签到 / UC-12 取消签到）
 *
 * <p>业务规则（对应需求文档）：
 *  BR-1 同一患者同一时段只能签到一次；
 *  BR-2 排队编号按签到到达顺序自动生成；
 *  BR-3 签到后未就诊不得重复扫码。
 */
@Service
public class CheckinService {

    private final QueueEntryRepository queueEntryRepository;
    private final ScheduleRepository scheduleRepository;

    public CheckinService(QueueEntryRepository queueEntryRepository,
                          ScheduleRepository scheduleRepository) {
        this.queueEntryRepository = queueEntryRepository;
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 扫码签到并入队
     *
     * @param deptId      科室
     * @param doctorId    医生
     * @param patientName 患者姓名
     * @return 排队编号与当前排队人数
     */
    @Transactional
    public Map<String, Object> checkin(Long deptId, Long doctorId, String patientName) {
        // BR-3 校验号源是否开放（需求文档 4a：号源已满）
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        Schedule schedule = scheduleRepository.findByDoctorIdAndWorkDate(doctorId, today);
        if (schedule == null) {
            throw new BizException(4004, "该医生今天未排班，暂不能签到");
        }
        if (schedule.getUsedCount() >= schedule.getSlotCount()) {
            throw new BizException(4005, "当前时段号源已满，请选择其他医生或时段");
        }
        // BR-1 同一患者同一时段只能签到一次
        List<String> active = List.of(QueueEntry.STATUS_WAITING, QueueEntry.STATUS_CALLED);
        if (queueEntryRepository.existsByPatientNameAndStatusIn(patientName, active)) {
            throw new BizException(4006, "您已签到，请勿重复签到");
        }
        // BR-2 排队编号按到达顺序生成：医生ID-序号
        String seq = "001";
        var last = queueEntryRepository.findTopByDoctorIdOrderByQueueNoDesc(doctorId);
        if (last.isPresent()) {
            String lastNo = last.get().getQueueNo();
            String num = lastNo.substring(lastNo.lastIndexOf('-') + 1);
            seq = String.format("%03d", Integer.parseInt(num) + 1);
        }
        String queueNo = doctorId + "-" + seq;

        QueueEntry entry = new QueueEntry(queueNo, patientName, deptId, doctorId);
        queueEntryRepository.save(entry);
        schedule.setUsedCount(schedule.getUsedCount() + 1);
        scheduleRepository.save(schedule);

        long waitingCount = queueEntryRepository
                .findByDoctorIdAndStatusOrderByQueueNoAsc(doctorId, QueueEntry.STATUS_WAITING)
                .size();

        Map<String, Object> data = new HashMap<>();
        data.put("queueNo", queueNo);
        data.put("waitingCount", waitingCount);
        return data;
    }

    /** 取消签到（UC-12）：仅候诊中可取消，流转至离场 */
    @Transactional
    public void cancel(String queueNo) {
        QueueEntry entry = getEntry(queueNo);
        entry.setStatus(QueueState.valueOf(entry.getStatus()).leave().name());
        queueEntryRepository.save(entry);
    }

    private QueueEntry getEntry(String queueNo) {
        return queueEntryRepository.findByQueueNo(queueNo)
                .orElseThrow(() -> new BizException(4007, "排队记录不存在：" + queueNo));
    }
}
