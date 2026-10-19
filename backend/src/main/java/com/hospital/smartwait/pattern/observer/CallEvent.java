package com.hospital.smartwait.pattern.observer;

/**
 * 叫号事件对象（观察者模式：Subject 发布的事件）
 *
 * <p>// @Pattern 观察者模式（Observer）
 * // @UseCase UC-02 候诊排队叫号
 *
 * <p>当系统完成一次叫号时，{@link EventPublisher} 将该事件发布给
 * 所有注册的观察者（候诊大屏、短信、语音等），实现"叫号→通知"的解耦。
 */
public class CallEvent {

    /** 被叫号排队编号 */
    private final String queueNo;

    /** 患者姓名 */
    private final String patientName;

    /** 科室 */
    private final Long deptId;

    /** 医生 */
    private final Long doctorId;

    /** 诊室号 */
    private final String roomNo;

    public CallEvent(String queueNo, String patientName, Long deptId, Long doctorId, String roomNo) {
        this.queueNo = queueNo;
        this.patientName = patientName;
        this.deptId = deptId;
        this.doctorId = doctorId;
        this.roomNo = roomNo;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    @Override
    public String toString() {
        return "CallEvent{queueNo='" + queueNo + "', patientName='" + patientName
                + "', deptId=" + deptId + ", doctorId=" + doctorId
                + ", roomNo='" + roomNo + "'}";
    }
}
