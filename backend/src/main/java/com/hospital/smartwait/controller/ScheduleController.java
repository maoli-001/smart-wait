package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.model.Schedule;
import com.hospital.smartwait.service.AdminService;
import com.hospital.smartwait.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 号源排班接口（UC-09 号源排班配置）
 *
 * <p>接口清单：
 *  GET    /api/schedules          排班列表（ADMIN）
 *  POST   /api/schedules          新增排班（ADMIN）
 *  PUT    /api/schedules/{id}     修改号源总数（ADMIN）
 *  DELETE /api/schedules/{id}     删除排班（ADMIN）
 */
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final AdminService adminService;

    public ScheduleController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** // @UseCase UC-09 排班列表 */
    @GetMapping
    public Result<List<Schedule>> list(HttpServletRequest request) {
        requireAdmin(request);
        return Result.ok(adminService.listSchedules());
    }

    /** // @UseCase UC-09 新增排班 */
    @PostMapping
    public Result<Schedule> create(@RequestBody Map<String, String> body,
                                   HttpServletRequest request) {
        requireAdmin(request);
        return Result.ok(adminService.createSchedule(
                Long.valueOf(body.get("doctorId")),
                body.get("workDate"),
                Integer.parseInt(body.get("slotCount"))));
    }

    /** // @UseCase UC-09 修改号源总数 */
    @PutMapping("/{id}")
    public Result<Schedule> update(@PathVariable Long id,
                                   @RequestBody Map<String, String> body,
                                   HttpServletRequest request) {
        requireAdmin(request);
        return Result.ok(adminService.updateSchedule(id, Integer.parseInt(body.get("slotCount"))));
    }

    /** // @UseCase UC-09 删除排班 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        requireAdmin(request);
        adminService.deleteSchedule(id);
        return Result.ok();
    }

    private void requireAdmin(HttpServletRequest request) {
        if (!AuthService.ROLE_ADMIN.equals(request.getAttribute("role"))) {
            throw new BizException(403, "无权限：仅科室管理员可执行该操作");
        }
    }
}
