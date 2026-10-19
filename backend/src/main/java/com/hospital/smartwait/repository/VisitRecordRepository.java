package com.hospital.smartwait.repository;

import com.hospital.smartwait.model.VisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 就诊记录持久层（UC-04/05/06）
 */
public interface VisitRecordRepository extends JpaRepository<VisitRecord, Long> {
}
