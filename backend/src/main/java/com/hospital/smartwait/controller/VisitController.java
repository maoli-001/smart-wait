package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.model.VisitRecord;
import com.hospital.smartwait.service.AuthService;
import com.hospital.smartwait.service.VisitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 就诊接口（UC-04 医生接诊 / UC-05 填写诊断记录 / UC-06 开医嘱单）
 *
 * <p>接口清单：
 *  POST /api/visit/start                       开始接诊（UC-04）
 *  POST /api/visit/end                         结束接诊（UC-04）
 *  POST /api/visit/{visitId}/diagnosis         保存诊断记录（UC-05）
 *  POST /api/visit/{visitId}/prescription      开医嘱单（UC-06）
 */
@RestController
@RequestMapping("/api/visit")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    /** // @UseCase UC-04 开始接诊 */
    @PostMapping("/start")
    public Result<VisitRecord> start(@RequestBody Map<String, String> body,
                                     HttpServletRequest request) {
        requireDoctor(request);
        return Result.ok(visitService.start(body.get("queueNo"), Long.valueOf(body.get("doctorId"))));
    }

    /** // @UseCase UC-04 结束接诊 */
    @PostMapping("/end")
    public Result<VisitRecord> end(@RequestBody Map<String, String> body) {
        return Result.ok(visitService.end(Long.valueOf(body.get("visitId"))));
    }

    /** // @UseCase UC-05 保存诊断记录 */
    @PostMapping("/{visitId}/diagnosis")
    public Result<VisitRecord> diagnosis(@PathVariable Long visitId,
                                         @RequestBody Map<String, String> body) {
        return Result.ok(visitService.saveDiagnosis(visitId, body.get("diagnosis")));
    }

    /** // @UseCase UC-06 开医嘱单 */
    @PostMapping("/{visitId}/prescription")
    public Result<VisitRecord> prescription(@PathVariable Long visitId,
                                            @RequestBody Map<String, String> body) {
        return Result.ok(visitService.savePrescription(visitId, body.get("prescription")));
    }

    private void requireDoctor(HttpServletRequest request) {
        if (!AuthService.ROLE_DOCTOR.equals(request.getAttribute("role"))) {
            throw new BizException(403, "无权限：仅医生可执行该操作");
        }
    }
}
