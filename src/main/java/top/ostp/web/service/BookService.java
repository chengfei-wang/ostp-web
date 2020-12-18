package top.ostp.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import top.ostp.web.mapper.BookMapper;
import top.ostp.web.model.Book;
import top.ostp.web.model.common.ApiResponse;
import top.ostp.web.model.common.Responses;

@Service
public class BookService {
    BookMapper bookMapper;

    @Autowired
    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
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
        return Responses.ok(bookMapper.selectByISBN(isbn));
    }
}