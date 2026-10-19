package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.model.Department;
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
 * 科室接口（UC-07 科室信息维护 / UC-11 查看科室与医生）
 *
 * <p>接口清单：
 *  GET    /api/depts         科室列表（患者端可查，UC-11）
 *  GET    /api/depts/{id}    科室详情
 *  POST   /api/depts         新增科室（ADMIN）
 *  PUT    /api/depts/{id}    修改科室（ADMIN）
 *  DELETE /api/depts/{id}    删除科室（ADMIN）
 */
@RestController
@RequestMapping("/api/depts")
public class DeptController {

    private final AdminService adminService;

    public DeptController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** // @UseCase UC-11 查看科室 */
    @GetMapping
    public Result<List<Department>> list() {
        return Result.ok(adminService.listDepartments());
    }

    /** // @UseCase UC-07 科室详情 */
    @GetMapping("/{id}")
    public Result<Department> get(@PathVariable Long id) {
        return Result.ok(adminService.getDepartment(id));
    }

    /** // @UseCase UC-07 新增科室 */
    @PostMapping
    public Result<Department> create(@RequestBody Map<String, String> body,
                                     HttpServletRequest request) {
        requireAdmin(request);
        return Result.ok(adminService.createDepartment(body.get("name"), body.get("description")));
    }

    /** // @UseCase UC-07 修改科室 */
    @PutMapping("/{id}")
    public Result<Department> update(@PathVariable Long id,
                                     @RequestBody Map<String, String> body,
                                     HttpServletRequest request) {
        requireAdmin(request);
        return Result.ok(adminService.updateDepartment(id, body.get("name"), body.get("description")));
    }

    /** // @UseCase UC-07 删除科室 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        requireAdmin(request);
        adminService.deleteDepartment(id);
        return Result.ok();
    }

    private void requireAdmin(HttpServletRequest request) {
        if (!AuthService.ROLE_ADMIN.equals(request.getAttribute("role"))) {
            throw new BizException(403, "无权限：仅科室管理员可执行该操作");
        }
    }
}
