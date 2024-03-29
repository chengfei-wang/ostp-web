package top.ostp.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import top.ostp.web.mapper.BookMapper;
import top.ostp.web.mapper.BookOrderMapper;
import top.ostp.web.mapper.CourseOpenMapper;
import top.ostp.web.mapper.StudentMapper;
import top.ostp.web.model.Book;
import top.ostp.web.model.CourseOpen;
import top.ostp.web.model.Student;
import top.ostp.web.model.StudentBookOrder;
import top.ostp.web.model.common.ApiResponse;
import top.ostp.web.model.common.Responses;
import top.ostp.web.model.complex.BookAdvance;
import top.ostp.web.model.complex.SearchParams;
import top.ostp.web.model.complex.SearchParams2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    BookMapper bookMapper;
    CourseOpenMapper courseOpenMapper;
    BookOrderMapper bookOrderMapper;
    StudentMapper studentMapper;

    @Autowired
    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Autowired
    public void setCourseOpenMapper(CourseOpenMapper courseOpenMapper){
        this.courseOpenMapper = courseOpenMapper;
    }

    @Autowired
    public void setBookOrderMapper(BookOrderMapper bookOrderMapper){
        this.bookOrderMapper = bookOrderMapper;
    }

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper){
        this.studentMapper = studentMapper;
    }

    public ApiResponse<Object> insert(Book book) {
        try {
            int result = bookMapper.insert(book);
            return Responses.ok();
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        }
        return Responses.fail();
    }

    public ApiResponse<Book> selectByISBN(String isbn) {
        Book book = bookMapper.selectByISBN(isbn);
        if (book != null) {
            return Responses.ok(book);
        } else {
            return Responses.fail("ISBN不存在");
        }
    }

    public ApiResponse<Object> deleteByISBN(String isbn) {
        int result = bookMapper.deleteByISBN(isbn);
        if (result == 1) {
            return Responses.ok();
        } else {
            return Responses.fail("删除失败");
        }

    }

    public ApiResponse<Object> updateBook(Book book) {
        int result = bookMapper.updateByISBN(book);
        if (result == 1) {
            return Responses.ok();
        } else {
            return Responses.fail("更新失败");
        }
    }

    public ApiResponse<List<Book>> fuzzyQuery(String name) {
        return Responses.ok(bookMapper.fuzzyQuery(name));
    }

    public ApiResponse<List<Book>> selectAll() {
        return Responses.ok(bookMapper.selectAll());
    }

    public List<BookAdvance> searchOfStudent(SearchParams params) {
        return bookMapper.searchOfStudent(params)
                .stream().map((book)-> bookAdviceOfStudent(params.toSearchParams2(book))).collect(Collectors.toList());
    }

    public BookAdvance bookAdviceOfStudent(SearchParams2 params2) {
        Book book = bookMapper.selectByISBN(params2.getIsbn());
        if (book == null) {
            return null;
        } else {
            return new BookAdvance(book, courseOpenMapper.selectByBookAndStudent(params2.getIsbn(), params2.getPersonId()), bookOrderMapper.searchOfStudent(params2)).calculateStudent();
        }
    }

    public List<BookAdvance> searchOfTeacher(SearchParams params) {
        var books = bookMapper.searchOfTeacher(params);
        return books.stream().map((book)-> bookAdviceOfTeacher(params.toSearchParams2(book))).collect(Collectors.toList());
    }

    public BookAdvance bookAdviceOfTeacher(SearchParams2 params2){
        Book book = bookMapper.selectByISBN(params2.getIsbn());
        if (book == null){
            return null;
        } else {
            return new BookAdvance(book, courseOpenMapper.selectByBookAndTeacher(params2.getIsbn(), params2.getPersonId()), new LinkedList<>()).calculateTeacher(params2);
        }
    }

    public List<BookAdvance> searchOfSuAdmin(SearchParams params) {
        return bookMapper.searchOfSuAdmin(params)
                .stream().map((book)-> bookAdviceOfSuAdmin(params.toSearchParams2(book))).collect(Collectors.toList());
    }

    public List<BookAdvance> searchOfCollegeAdmin(SearchParams params) {
        return bookMapper.searchOfCollegeAdmin(params)
                .stream().map((book)-> bookAdviceOfCollegeAdmin(params.toSearchParams2(book))).collect(Collectors.toList());
    }

    public BookAdvance bookAdviceOfSuAdmin(SearchParams2 params2) {
        Book book = bookMapper.selectByISBN(params2.getIsbn());
        if (book == null){
            return null;
        } else {
            return new BookAdvance(book, courseOpenMapper.selectByBook(book), new LinkedList<>());
        }
    }

    public BookAdvance bookAdviceOfCollegeAdmin(SearchParams2 params2){
        Book book = bookMapper.selectByISBN(params2.getIsbn());
        if (book == null){
            return null;
        } else {
            return new BookAdvance(book, courseOpenMapper.selectByBookAndCollegeAdmin(params2), new LinkedList<>());
        }
    }

    public ApiResponse<?> orderBookStu(SearchParams2 params2){
        // 获取增强模型
        BookAdvance bookAdvance = bookAdviceOfStudent(params2);
        if (bookAdvance == null) {
            return Responses.fail("没有这本书");
        }
        if (bookAdvance.getCourseOpens().isEmpty()) {
            return Responses.fail("你无法订阅这本书，因为没有相关的开课");
        }
        Optional<StudentBookOrder> order = bookAdvance.getOrders().stream().findAny();

        // 增加额外的逻辑
        Student student = studentMapper.queryMoney(params2.getPersonId());
        if (order.isEmpty()) {
            if (student.getBalance() < bookAdvance.getBook().getPrice()) {
                return Responses.fail("余额不足");
            }
            studentMapper.changeMoney(student, (int)-bookAdvance.getBook().getPrice());
            bookOrderMapper.addBookOrder(params2.getPersonId(), params2.getIsbn(), bookAdvance.getBook().getPrice().intValue(), params2.getYear() , params2.getSemester());
        } else {
            studentMapper.changeMoney(student, bookAdvance.getBook().getPrice().intValue());
            bookOrderMapper.deleteBookOrder((int)order.get().getId());
        }
        return Responses.ok("订阅成功");
    }

    public ApiResponse<List<StudentBookOrder>> orderListByStudentYearSemester(String student, int year, int semester) {
        if (student == null) {
            return Responses.fail(new ArrayList<>());
        }
        return Responses.ok(bookOrderMapper.selectByYearSemesterAndStudent(student, year, semester));
    }

    public ApiResponse<?> orderBookTeacher(SearchParams2 params2) {
        BookAdvance bookAdvance = bookAdviceOfTeacher(params2);
        if (bookAdvance == null){
            return Responses.fail("没有该书本");
        }
        if (bookAdvance.getOrderState() == 1) {
            return Responses.fail("你已经领取了该本书");
        }
        CourseOpen courseOpen = bookAdvance.selectTeacher(params2);
        if (courseOpen == null){
            return Responses.fail("无法领取书籍，因为没有相关的开课");
        }
        // 设置为领取状态
        courseOpenMapper.requestBook((int)courseOpen.getId());
        return Responses.ok();
    }


}
