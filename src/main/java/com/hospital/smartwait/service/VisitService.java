package com.hospital.smartwait.service;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.model.QueueEntry;
import com.hospital.smartwait.model.VisitRecord;
import com.hospital.smartwait.pattern.state.QueueState;
import com.hospital.smartwait.repository.QueueEntryRepository;
import com.hospital.smartwait.repository.VisitRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 就诊服务（UC-04 医生接诊 / UC-05 填写诊断记录 / UC-06 开医嘱单）
 */
@Service
public class VisitService {

    private final VisitRecordRepository visitRecordRepository;
    private final QueueEntryRepository queueEntryRepository;

    public VisitService(VisitRecordRepository visitRecordRepository,
                        QueueEntryRepository queueEntryRepository) {
        this.visitRecordRepository = visitRecordRepository;
        this.queueEntryRepository = queueEntryRepository;
    }

    /** 开始接诊（UC-04）：基于已叫号患者创建就诊记录，并将队列状态流转为已就诊 */
    @Transactional
    public VisitRecord start(String queueNo, Long doctorId) {
        QueueEntry entry = queueEntryRepository.findByQueueNo(queueNo)
                .orElseThrow(() -> new BizException(4007, "排队记录不存在：" + queueNo));
        if (!QueueEntry.STATUS_CALLED.equals(entry.getStatus())) {
            throw new BizException(4011, "患者当前状态不可接诊（需为已叫号）");
        }
        // 状态机：CALLED → SEEN（患者已进入诊室，释放叫号位）
        entry.setStatus(QueueState.valueOf(entry.getStatus()).seen().name());
        queueEntryRepository.save(entry);
        VisitRecord record = new VisitRecord(queueNo, entry.getPatientName(), doctorId);
        return visitRecordRepository.save(record);
    }

    /** 结束接诊（UC-04） */
    @Transactional
    public VisitRecord end(Long visitId) {
        VisitRecord record = getRecord(visitId);
        record.setEndTime(LocalDateTime.now());
        return visitRecordRepository.save(record);
    }

    /** 保存诊断记录（UC-05） */
    @Transactional
    public VisitRecord saveDiagnosis(Long visitId, String diagnosis) {
        VisitRecord record = getRecord(visitId);
        record.setDiagnosis(diagnosis);
        return visitRecordRepository.save(record);
    }

    /** 开医嘱单（UC-06） */
    @Transactional
    public VisitRecord savePrescription(Long visitId, String prescription) {
        VisitRecord record = getRecord(visitId);
        record.setPrescription(prescription);
        return visitRecordRepository.save(record);
    }

    private VisitRecord getRecord(Long visitId) {
        return visitRecordRepository.findById(visitId)
                .orElseThrow(() -> new BizException(4012, "就诊记录不存在：" + visitId));
    }
}
