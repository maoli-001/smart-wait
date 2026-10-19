package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.model.Doctor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 医生接口（UC-08 医生信息维护 / UC-11 查看科室与医生）
 *
 * <p>接口清单：
 *  GET    /api/doctors?deptId=   按科室查询医生及号源（UC-11）
 *  POST   /api/doctors           新增医生（ADMIN，UC-08）
 *  PUT    /api/doctors/{id}      修改医生（ADMIN，UC-08）
 *  DELETE /api/doctors/{id}      删除医生（ADMIN，UC-08）
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final AdminService adminService;

    public DoctorController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** // @UseCase UC-11 查看医生（可按科室过滤） */
    @GetMapping
    public Result<List<Doctor>> list(@RequestParam(required = false) Long deptId) {
        if (deptId == null) {
            return Result.ok(adminService.listDoctors());
        }
        return Result.ok(adminService.listDoctorsByDept(deptId));
    }

    /** // @UseCase UC-08 新增医生 */
    @PostMapping
    public Result<Doctor> create(@RequestBody Map<String, String> body,
                                 HttpServletRequest request) {
        requireAdmin(request);
        return Result.ok(adminService.createDoctor(
                body.get("name"), body.get("title"),
                Long.valueOf(body.get("deptId")),
                body.get("username"), body.get("password")));
    }

    /** // @UseCase UC-08 修改医生 */
    @PutMapping("/{id}")
    public Result<Doctor> update(@PathVariable Long id,
                                 @RequestBody Map<String, String> body,
                                 HttpServletRequest request) {
        requireAdmin(request);
        return Result.ok(adminService.updateDoctor(
                id, body.get("name"), body.get("title"), Long.valueOf(body.get("deptId"))));
    }

    /** // @UseCase UC-08 删除医生 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        requireAdmin(request);
        adminService.deleteDoctor(id);
        return Result.ok();
    }

    private void requireAdmin(HttpServletRequest request) {
        if (!AuthService.ROLE_ADMIN.equals(request.getAttribute("role"))) {
            throw new BizException(403, "无权限：仅科室管理员可执行该操作");
        }
    }
}
