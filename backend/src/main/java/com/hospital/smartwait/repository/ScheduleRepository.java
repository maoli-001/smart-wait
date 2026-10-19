package com.hospital.smartwait.repository;

import com.hospital.smartwait.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 号源排班持久层（UC-09）
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /** 按医生+日期查询号源（UC-01 签到校验） */
    Schedule findByDoctorIdAndWorkDate(Long doctorId, String workDate);

    /** 按医生查询排班 */
    List<Schedule> findByDoctorId(Long doctorId);
}
