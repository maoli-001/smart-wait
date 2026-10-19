package com.hospital.smartwait.service;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.model.Department;
import com.hospital.smartwait.model.Doctor;
import com.hospital.smartwait.model.Schedule;
import com.hospital.smartwait.repository.DepartmentRepository;
import com.hospital.smartwait.repository.DoctorRepository;
import com.hospital.smartwait.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理服务（UC-07 科室信息维护 / UC-08 医生信息维护 / UC-09 号源排班配置）
 *
 * <p>仅科室/系统管理员（ADMIN）角色可调用，权限在 Controller 层校验。
 */
@Service
public class AdminService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final ScheduleRepository scheduleRepository;

    public AdminService(DepartmentRepository departmentRepository,
                        DoctorRepository doctorRepository,
                        ScheduleRepository scheduleRepository) {
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // ---------- 科室信息维护（UC-07） ----------

    public List<Department> listDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new BizException(4013, "科室不存在：" + id));
    }

    public Department createDepartment(String name, String description) {
        return departmentRepository.save(new Department(name, description));
    }

    public Department updateDepartment(Long id, String name, String description) {
        Department dept = getDepartment(id);
        dept.setName(name);
        dept.setDescription(description);
        return departmentRepository.save(dept);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    // ---------- 医生信息维护（UC-08） ----------

    public List<Doctor> listDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> listDoctorsByDept(Long deptId) {
        return doctorRepository.findByDeptId(deptId);
    }

    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new BizException(4014, "医生不存在：" + id));
    }

    public Doctor createDoctor(String name, String title, Long deptId,
                               String username, String password) {
        return doctorRepository.save(new Doctor(name, title, deptId, username, password));
    }

    public Doctor updateDoctor(Long id, String name, String title, Long deptId) {
        Doctor doctor = getDoctor(id);
        doctor.setName(name);
        doctor.setTitle(title);
        doctor.setDeptId(deptId);
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // ---------- 号源排班配置（UC-09） ----------

    public List<Schedule> listSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule createSchedule(Long doctorId, String workDate, int slotCount) {
        if (scheduleRepository.findByDoctorIdAndWorkDate(doctorId, workDate) != null) {
            throw new BizException(4015, "该医生当日已有排班，请改用更新操作");
        }
        return scheduleRepository.save(new Schedule(doctorId, workDate, slotCount));
    }

    public Schedule updateSchedule(Long id, int slotCount) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new BizException(4016, "排班不存在：" + id));
        if (slotCount < schedule.getUsedCount()) {
            throw new BizException(4017, "号源总数不能小于已占用数（已占用 " + schedule.getUsedCount() + "）");
        }
        schedule.setSlotCount(slotCount);
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
