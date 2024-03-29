package top.ostp.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ostp.web.model.SecondHandPublish;
import top.ostp.web.model.annotations.AuthAdmin;
import top.ostp.web.model.annotations.AuthStudent;
import top.ostp.web.model.annotations.AuthTeacher;
import top.ostp.web.model.common.ApiResponse;
import top.ostp.web.service.SecondHandPublishService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller(value = "/secondHandPublish")
public class SecondHandPublishController {
    SecondHandPublishService secondHandPublishService;

    @Autowired
    public void setSecondHandPublishService(SecondHandPublishService secondHandPublishService) {
        this.secondHandPublishService = secondHandPublishService;
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(value = "/second/publish/insert")
    @ResponseBody
    public ApiResponse<Object> insertSecondHandPublish(@RequestParam("person") String person, @RequestParam("book") String book, int price, int exchange) {
        return secondHandPublishService.insert(person, book, price, exchange);
    }

    @Deprecated()
    @AuthAdmin
    @PostMapping(value = "/second/publish/delete")
    @ResponseBody
    public ApiResponse<Object> deleteSecondHandPublish(String id) {
        return secondHandPublishService.deleteByOrderId(id);
    }

    @AuthAdmin
    @PostMapping(value = "/second/publish/update")
    @ResponseBody
    public ApiResponse<Object> updateSecondHandPublish(String id, @RequestParam("person") String person, @RequestParam("book") String book, int price, int exchange, int status) {
        return secondHandPublishService.update(id, person, book, price, exchange, status);
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(value = "/second/publish/select/all")
    @ResponseBody
    public ApiResponse<List<SecondHandPublish>> selectAllCollege() {
        return secondHandPublishService.selectAll();
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(value = "/second/publish/select/{id}")
    @ResponseBody
    public ApiResponse<SecondHandPublish> selectSecondHandPublishByOrderId(@PathVariable String id) {
        return secondHandPublishService.selectPublishByOrderId(id);
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(value = "/second/publish/selectByISBN")
    @ResponseBody
    public ApiResponse<List<SecondHandPublish>> selectSecondHanPublishByISBN(String isbn) {
        return secondHandPublishService.selectPublishByISBN(isbn);
    }

    @AuthStudent
    @PostMapping(value = "/second/publish/selectByStudent")
    @ResponseBody
    public ApiResponse<List<SecondHandPublish>> selectSecondHandPublishByStudentId(String id) {
        return secondHandPublishService.selectPublishByStudentId(id);
    }

    @AuthStudent
    @PostMapping("/second/publish/buyList")
    @ResponseBody
    public ApiResponse<List<SecondHandPublish>> searchBuyList(String name, String publisher, HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("username");
        return secondHandPublishService.searchBuyList(name, id, publisher);
    }

    @AuthStudent
    @PostMapping("/second/publish/exchangeList")
    @ResponseBody
    public ApiResponse<List<SecondHandPublish>> searchExchangeList(String name, String publisher, HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("username");
        return secondHandPublishService.searchExchangeList(name, id, publisher);
    }

    /**
     * 购买一本书籍
     *
     * @param id 购买的订单号
     * @return 返回值
     */
    @AuthStudent
    @PostMapping("/second/publish/purchase")
    @ResponseBody
    public ApiResponse<Object> purchase(String id, HttpServletRequest request) {
        String studentId = (String) request.getSession().getAttribute("username");
        return secondHandPublishService.purchase(id, studentId);
    }

    /**
     * 取消一个publish，要求其status为0
     */
    @AuthStudent
    @PostMapping("/second/publish/cancel")
    @ResponseBody
    public ApiResponse<Object> cancel(String id, HttpServletRequest request) {
        String studentId = (String) request.getSession().getAttribute("username");
        return secondHandPublishService.cancel(id, studentId);
    }

    /**
     * 改变一个订单的状态，要求其status不为0，主要用于确认订单和取消确认订单
     */
    @AuthStudent
    @PostMapping(value = "/second/publish/changeStatusOk")
    @ResponseBody
    public ApiResponse<Object> changeStatusOk(String id, HttpServletRequest request) {
        String studentId = (String) request.getSession().getAttribute("username");
        return secondHandPublishService.changeStatusOk(id, studentId);
    }
}
