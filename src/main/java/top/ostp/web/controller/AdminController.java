package top.ostp.web.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.ostp.web.model.Admin;
import top.ostp.web.model.annotations.AuthAdmin;
import top.ostp.web.model.annotations.AuthStudent;
import top.ostp.web.model.annotations.AuthTeacher;
import top.ostp.web.model.annotations.NoAuthority;
import top.ostp.web.model.common.ApiResponse;
import top.ostp.web.service.AdminService;


@RestController
public class AdminController {
    AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/admin/insert")
    @ResponseBody
    @AuthAdmin
    public ApiResponse<Object> insert(Admin admin) {
        return adminService.insert(admin);
    }

    @PostMapping(value = "/admin/select")
    @ResponseBody
    @AuthAdmin
    @AuthTeacher
    @AuthStudent
    public ApiResponse<Object> select(String id) {
        return adminService.select(id);
    }

    @PostMapping(value = "/admin/update")
    @ResponseBody
    @AuthAdmin
    public ApiResponse<Object> update(Admin admin) {
        return adminService.update(admin);
    }

    @PostMapping(value = "/admin/delete")
    @ResponseBody
    @AuthAdmin
    public ApiResponse<Object> delete(Admin admin) {
        return adminService.delete(admin);
    }

    @PostMapping(value = "/admin/update/password")
    @ResponseBody
    @AuthAdmin
    public ApiResponse<Object> updatePassword(String id, String password0, String password) {
        return adminService.updatePassword(new Admin(id, password0, 1, null), password);
    }
}