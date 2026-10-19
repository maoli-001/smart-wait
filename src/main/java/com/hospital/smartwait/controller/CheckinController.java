package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.service.CheckinService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 签到接口（UC-01 患者扫码签到 / UC-12 取消签到）
 *
 * <p>接口清单：
 *  POST   /api/checkin            扫码签到并入队（UC-01）
 *  DELETE /api/checkin/{queueNo}  取消签到/放弃重排（UC-12）
 */
@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    /** // @UseCase UC-01 患者扫码签到 */
    @PostMapping
    public Result<Map<String, Object>> checkin(@RequestBody Map<String, String> body) {
        return Result.ok(checkinService.checkin(
                Long.valueOf(body.get("deptId")),
                Long.valueOf(body.get("doctorId")),
                body.get("patientName")));
    }

    /** // @UseCase UC-12 取消签到 */
    @DeleteMapping("/{queueNo}")
    public Result<Void> cancel(@PathVariable String queueNo) {
        checkinService.cancel(queueNo);
        return Result.ok();
    }
}
