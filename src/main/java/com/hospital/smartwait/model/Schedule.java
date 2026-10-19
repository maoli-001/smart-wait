package com.hospital.smartwait.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 号源排班实体（UC-09 号源排班配置）
 */
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 医生 */
    @Column(nullable = false)
    private Long doctorId;

    /** 排班日期（yyyy-MM-dd） */
    @Column(nullable = false)
    private String workDate;

    /** 号源总数 */
    @Column(nullable = false)
    private int slotCount;

    /** 已占用号源数 */
    @Column(nullable = false)
    private int usedCount;

    public Schedule() {
    }

    public Schedule(Long doctorId, String workDate, int slotCount) {
        this.doctorId = doctorId;
        this.workDate = workDate;
        this.slotCount = slotCount;
        this.usedCount = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }
}
