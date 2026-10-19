package com.hospital.smartwait.repository;

import com.hospital.smartwait.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 科室持久层（UC-07）
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
