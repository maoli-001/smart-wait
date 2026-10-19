package com.hospital.smartwait.repository;

import com.hospital.smartwait.model.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 排队记录持久层（UC-01/UC-02）
 */
public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {

    /** 某医生当天候诊中（按编号正序 = 到达顺序，BR-2） */
    List<QueueEntry> findByDoctorIdAndStatusOrderByQueueNoAsc(Long doctorId, String status);

    /** 某科室当天候诊中（候诊大屏按科室展示） */
    List<QueueEntry> findByDeptIdAndStatusOrderByQueueNoAsc(Long deptId, String status);

    /** 按编号查询 */
    Optional<QueueEntry> findByQueueNo(String queueNo);

    /** 某患者是否已签到（同一时段只能签到一次，BR-1） */
    boolean existsByPatientNameAndStatusIn(String patientName, List<String> statuses);

    /** 某医生当天最大排队编号（用于生成新编号） */
    Optional<QueueEntry> findTopByDoctorIdOrderByQueueNoDesc(Long doctorId);
}
