package com.hospital.smartwait.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 患者实体（UC-01 患者扫码签到）
 */
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 患者姓名 */
    @Column(nullable = false)
    private String name;

    /** 手机号 */
    private String phone;

    /** 登录用户名（UC-10） */
    @Column(nullable = false, unique = true)
    private String username;

    /** 登录口令（课程演示为明文，生产环境需加密存储） */
    @Column(nullable = false)
    private String password;

    public Patient() {
    }

    public Patient(String name, String phone, String username, String password) {
        this.name = name;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
