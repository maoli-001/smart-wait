package com.hospital.smartwait.service;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.model.Doctor;
import com.hospital.smartwait.model.Patient;
import com.hospital.smartwait.repository.DoctorRepository;
import com.hospital.smartwait.repository.PatientRepository;
import com.hospital.smartwait.util.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务（UC-10 用户登录认证）
 *
 * <p>支持三种角色：患者（PATIENT）、医生（DOCTOR）、科室/系统管理员（ADMIN）。
 * 管理员账号为内置 admin / admin123。
 */
@Service
public class AuthService {

    /** 角色常量 */
    public static final String ROLE_PATIENT = "PATIENT";
    public static final String ROLE_DOCTOR = "DOCTOR";
    public static final String ROLE_ADMIN = "ADMIN";

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AuthService(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * 登录并签发 token
     *
     * @param role     角色（PATIENT / DOCTOR / ADMIN）
     * @param username 用户名
     * @param password 口令
     * @return token 与用户信息
     */
    public Map<String, Object> login(String role, String username, String password) {
        String name;
        switch (role) {
            case ROLE_PATIENT:
                Patient patient = patientRepository.findByUsername(username);
                if (patient == null || !patient.getPassword().equals(password)) {
                    throw new BizException(4002, "用户名或密码错误");
                }
                name = patient.getName();
                break;
            case ROLE_DOCTOR:
                Doctor doctor = doctorRepository.findByUsername(username);
                if (doctor == null || !doctor.getPassword().equals(password)) {
                    throw new BizException(4002, "用户名或密码错误");
                }
                name = doctor.getName();
                break;
            case ROLE_ADMIN:
                if (!"admin".equals(username) || !"admin123".equals(password)) {
                    throw new BizException(4002, "用户名或密码错误");
                }
                name = "系统管理员";
                break;
            default:
                throw new BizException(4003, "未知角色：" + role);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("token", TokenUtil.issue(role));
        data.put("role", role);
        data.put("name", name);
        return data;
    }

    /** 注销：吊销 token */
    public void logout(String token) {
        TokenUtil.revoke(token);
    }
}
