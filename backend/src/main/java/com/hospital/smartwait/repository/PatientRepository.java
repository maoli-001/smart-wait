package com.hospital.smartwait.repository;

import com.hospital.smartwait.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 患者持久层（UC-01）
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /** 按用户名查询（UC-10 登录） */
    Patient findByUsername(String username);
}
