package top.ostp.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ostp.web.model.SecondHandFind;
import top.ostp.web.model.annotations.AuthAdmin;
import top.ostp.web.model.annotations.AuthStudent;
import top.ostp.web.model.annotations.AuthTeacher;
import top.ostp.web.model.common.ApiResponse;
import top.ostp.web.service.SecondHandFindService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SecondHandFindController {
    SecondHandFindService secondHandFindService;

    @Autowired
    public void setSecondHandFindService(SecondHandFindService secondHandFindService) {
        this.secondHandFindService = secondHandFindService;
    }

    /**
     * 查询所有的二手书的订单
     *
     * @return 查询的结果
     */
    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(path = "/second/find/select/all")
    @ResponseBody
    public ApiResponse<Object> selectAll() {
        return secondHandFindService.selectAll();
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(path = "/second/find/select/{id}")
    @ResponseBody
    public ApiResponse<Object> select(@PathVariable String id) {
        return secondHandFindService.select(id);

    }

    @AuthStudent
    @PostMapping(path = "/second/find/selectByStudent")
    @ResponseBody
    public ApiResponse<List<SecondHandFind>> selectByStudentId(String person) {
        return secondHandFindService.selectByStudentId(person);
    }

    @AuthAdmin
    @AuthStudent
    @AuthTeacher
    @PostMapping(path = "/second/find/selectByBook")
    @ResponseBody
    public ApiResponse<List<SecondHandFind>> selectByBook(String isbn) {
        return secondHandFindService.selectByBook(isbn);
    }

    @AuthStudent
    @PostMapping(value = "/second/find/insert")
    @ResponseBody
    public ApiResponse<Object> insert(@RequestParam("person") String person, @RequestParam("book") String book, int price, int exchange, int status) {
        return secondHandFindService.insert(person, book, price, exchange, status);
    }

    /**
     * 取消一个find，要求其status为0
     */
    @AuthStudent
    @PostMapping(value = "/second/find/cancel")
    @ResponseBody
    public ApiResponse<Object> cancel(String id, HttpServletRequest request) {
        String studentId = (String) request.getSession().getAttribute("username");
        return secondHandFindService.cancel(id, studentId);
    }

    /**
     * 改变一个订单的状态，要求其status不为0，主要用于确认订单和取消确认订单
     */
    @AuthStudent
    @PostMapping(value = "/second/find/changeStatusOk")
    @ResponseBody
    public ApiResponse<Object> changeStatusOk(String id, HttpServletRequest request) {
        String studentId = (String) request.getSession().getAttribute("username");
        return secondHandFindService.changeStatusOk(id, studentId);
    }

    /**
     * 返回可交换的且状态为0的订单
     *
     * @param otherId 交换方的id
     * @param selfId  自己的id
     * @return 返回可交换的且状态为0的订单
     */
    @AuthStudent
    @PostMapping("/second/find/other_exchange/list")
    @ResponseBody
    public ApiResponse<List<SecondHandFind>> otherExchangeList(String otherId, String selfId) {
        return secondHandFindService.selectByStudentIdNotExchanged(otherId, selfId);
    }
    /**
     *发起一个交换，并更新相应的状态
     * @param otherId 交换方的id
     * @param selfId  自己的id
     * @param otherFindId 他人的所有交换书的id
     * @param otherPublishId 他人的所有发布书的id
     * @return 操作结果
     */

    @AuthStudent
    @PostMapping("/second/find/post_exchange")
    @ResponseBody
    public ApiResponse<Object> postExchange(String otherId, String selfId, String otherFindId, String otherPublishId) {
        return secondHandFindService.postExchange(otherId, selfId, otherFindId, otherPublishId);
    }

}
