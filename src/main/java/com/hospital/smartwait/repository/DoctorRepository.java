package com.hospital.smartwait.repository;

import com.hospital.smartwait.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 医生持久层（UC-08）
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /** 按科室查询医生（UC-11 患者端查看医生） */
    List<Doctor> findByDeptId(Long deptId);

    /** 按用户名查询（UC-10 登录） */
    Doctor findByUsername(String username);
}
