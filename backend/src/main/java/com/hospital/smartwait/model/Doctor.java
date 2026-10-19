package com.hospital.smartwait.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 医生实体（UC-08 医生信息维护）
 */
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 医生姓名 */
    @Column(nullable = false)
    private String name;

    /** 职称 */
    private String title;

    /** 所属科室 */
    @Column(nullable = false)
    private Long deptId;

    /** 登录用户名（UC-10） */
    @Column(nullable = false, unique = true)
    private String username;

    /** 登录口令（课程演示为明文，生产环境需加密存储） */
    @Column(nullable = false)
    private String password;

    public Doctor() {
    }

    public Doctor(String name, String title, Long deptId, String username, String password) {
        this.name = name;
        this.title = title;
        this.deptId = deptId;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
