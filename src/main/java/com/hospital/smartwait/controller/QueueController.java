package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.model.QueueEntry;
import com.hospital.smartwait.service.AuthService;
import com.hospital.smartwait.service.QueueService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 排队叫号接口（UC-02 候诊排队叫号）
 *
 * <p>接口清单：
 *  POST /api/queue/call      医生叫下一号（UC-02）
 *  POST /api/queue/reorder   过号患者申请重排（UC-02）
 *
 * <p>另提供过号/离场处理（POST /api/queue/missed、/api/queue/leave），
 * 便于完整演示过号-重排流程。
 */
@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    /** // @UseCase UC-02 医生叫下一号 */
    @PostMapping("/call")
    public Result<QueueEntry> call(@RequestBody Map<String, String> body,
                                   HttpServletRequest request) {
        requireDoctor(request);
        return Result.ok(queueService.call(
                Long.valueOf(body.get("doctorId")), body.get("roomNo")));
    }

    /** // @UseCase UC-02 过号患者申请重排 */
    @PostMapping("/reorder")
    public Result<QueueEntry> reorder(@RequestBody Map<String, String> body) {
        return Result.ok(queueService.reorder(body.get("queueNo")));
    }

    /** // @UseCase UC-02 患者到场确认就诊（CALLED → SEEN） */
    @PostMapping("/seen")
    public Result<Void> seen(@RequestBody Map<String, String> body) {
        queueService.seen(body.get("queueNo"));
        return Result.ok();
    }

    /** // @UseCase UC-02 标记过号（CALLED → MISSED） */
    @PostMapping("/missed")
    public Result<Void> missed(@RequestBody Map<String, String> body) {
        queueService.missed(body.get("queueNo"));
        return Result.ok();
    }

    /** // @UseCase UC-02 放弃重排离场（MISSED → LEFT） */
    @PostMapping("/leave")
    public Result<Void> leave(@RequestBody Map<String, String> body) {
        queueService.leave(body.get("queueNo"));
        return Result.ok();
    }

    private void requireDoctor(HttpServletRequest request) {
        if (!AuthService.ROLE_DOCTOR.equals(request.getAttribute("role"))) {
            throw new BizException(403, "无权限：仅医生可执行该操作");
        }
    }
}
