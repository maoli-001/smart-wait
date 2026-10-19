package com.hospital.smartwait.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 就诊记录实体（UC-04 医生接诊 / UC-05 填写诊断记录 / UC-06 开医嘱单）
 */
@Entity
@Table(name = "visit_record")
public class VisitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 对应排队编号 */
    @Column(nullable = false)
    private String queueNo;

    /** 患者姓名 */
    @Column(nullable = false)
    private String patientName;

    /** 接诊医生 */
    @Column(nullable = false)
    private Long doctorId;

    /** 诊断记录（UC-05） */
    @Column(length = 2000)
    private String diagnosis;

    /** 医嘱单内容（UC-06） */
    @Column(length = 2000)
    private String prescription;

    /** 接诊开始时间 */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /** 接诊结束时间 */
    private LocalDateTime endTime;

    public VisitRecord() {
    }

    public VisitRecord(String queueNo, String patientName, Long doctorId) {
        this.queueNo = queueNo;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.startTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
