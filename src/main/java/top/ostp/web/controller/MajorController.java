package top.ostp.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ostp.web.model.Major;
import top.ostp.web.model.annotations.AuthAdmin;
import top.ostp.web.model.annotations.AuthStudent;
import top.ostp.web.model.annotations.AuthTeacher;
import top.ostp.web.model.annotations.NoAuthority;
import top.ostp.web.model.common.ApiResponse;
import top.ostp.web.model.complex.MajorAdvance;
import top.ostp.web.service.MajorService;

import java.util.List;

@Controller
public class MajorController {
    MajorService majorService;

    @Autowired
    public void setMajorService(MajorService majorService) {
        this.majorService = majorService;
    }

    @AuthAdmin
    @NoAuthority
    @PostMapping(path = "/major/insert")
    @ResponseBody
    public ApiResponse<Object> insert(String name, String college, String year) {
        return majorService.insert(name, college, year);
    }

    /**
     * 根据学院返回该学院所有的专业
     *
     * @param id 学院的id
     * @return List<MajorAdvice> 专业集合
     */
    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping("/major/fetch_all")
    @ResponseBody
    public ApiResponse<List<MajorAdvance>> selectAllByCollegeId(int id) {
        return majorService.selectAllByCollegeId(id);
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping("/major/get")
    @ResponseBody
    public ApiResponse<Object> get(int id) {
        return majorService.selectById(id);
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(path = {"/major/all", "/major/list"})
    @ResponseBody
    public ApiResponse<Object> selectAll() {
        return majorService.selectAll();
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(path = {"/major/by/year"})
    @ResponseBody
    public ApiResponse<Object> selectByYear(int year) {
        return majorService.selectByYear(year);
    }

    @AuthAdmin
    @PostMapping(path = "/major/update")
    @ResponseBody
    public ApiResponse<Object> update(Major major) {
        return majorService.update(major);
    }

    @AuthAdmin
    @PostMapping(path = "/major/delete")
    @ResponseBody
    public ApiResponse<Object> delete(Major major) {
        return majorService.delete(major);
    }

    @AuthAdmin
    @PostMapping("/major/fuzzy")
    @ResponseBody
    public ApiResponse<List<Major>> fuzzyByWithCollegeByName(int college, String name) {
        return majorService.fuzzyByWithCollegeByName(college, name);
    }
}
