package com.hospital.smartwait.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 排队记录实体（UC-01/UC-02 排队叫号）
 *
 * <p>status 取值与 {@link com.hospital.smartwait.pattern.state.QueueState} 状态机对应：
 * WAITING（候诊）→ CALLED（叫号中）→ SEEN（已就诊） / MISSED（过号）→ LEFT（离场）
 */
@Entity
@Table(name = "queue_entry")
public class QueueEntry {

    /** 候诊中 */
    public static final String STATUS_WAITING = "WAITING";
    /** 已叫号 */
    public static final String STATUS_CALLED = "CALLED";
    /** 已就诊 */
    public static final String STATUS_SEEN = "SEEN";
    /** 过号 */
    public static final String STATUS_MISSED = "MISSED";
    /** 离场（取消签到/放弃重排） */
    public static final String STATUS_LEFT = "LEFT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 排队编号（同一天内递增） */
    @Column(nullable = false, unique = true)
    private String queueNo;

    /** 患者姓名 */
    @Column(nullable = false)
    private String patientName;

    /** 科室 */
    @Column(nullable = false)
    private Long deptId;

    /** 医生 */
    @Column(nullable = false)
    private Long doctorId;

    /** 排队状态（见状态常量） */
    @Column(nullable = false)
    private String status;

    /** 过号后可重排次数（业务规则 BR-5 仅一次） */
    @Column(nullable = false)
    private int reorderCount;

    /** 签到时间 */
    @Column(nullable = false)
    private LocalDateTime createTime;

    public QueueEntry() {
    }

    public QueueEntry(String queueNo, String patientName, Long deptId, Long doctorId) {
        this.queueNo = queueNo;
        this.patientName = patientName;
        this.deptId = deptId;
        this.doctorId = doctorId;
        this.status = STATUS_WAITING;
        this.reorderCount = 0;
        this.createTime = LocalDateTime.now();
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReorderCount() {
        return reorderCount;
    }

    public void setReorderCount(int reorderCount) {
        this.reorderCount = reorderCount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
