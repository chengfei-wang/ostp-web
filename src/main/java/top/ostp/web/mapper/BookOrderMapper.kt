package top.ostp.web.mapper

import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Repository
import top.ostp.web.model.Book
import top.ostp.web.model.StudentBookOrder
import top.ostp.web.model.complex.SearchParams2

@Mapper
@Repository
interface BookOrderMapper {

    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @ResultType(StudentBookOrder::class)
    @Select("select id, student, book, price, year, semester, received from student_book_order")
    fun list(): List<StudentBookOrder>

    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @ResultType(StudentBookOrder::class)
    @Select("select id, student, book, price, year, semester, received from student_book_order where year = #{year} and semester = #{semester}")
    fun selectByYearSemester(
        @Param("year") year: Int,
        @Param("semester") semester: Int,
    ): List<StudentBookOrder>


    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @ResultType(StudentBookOrder::class)
    @Select("select * from student_book_order where student = #{student} and year = #{year} and semester = #{semester}")
    fun selectByYearSemesterAndStudent(
        @Param("student") student: String,
        @Param("year") year: Int,
        @Param("semester") semester: Int,
    ): List<StudentBookOrder>


    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @ResultType(StudentBookOrder::class)
    @Select("""
select *
from student_book_order
where year = #{year}
  and semester = #{semester}
  and student in (select id from student where clazz = #{clazz})
""")
    fun selectByYearSemesterAndClazz(
        @Param("clazz") clazz: Long,
        @Param("year") year: Int,
        @Param("semester") semester: Int,
    ): List<StudentBookOrder>

    @Update("""
update student_book_order
set received=1
where year = #{year}
  and semester = #{semester}
  and student in (select id from student where clazz = #{clazz})
""")
    fun setReceivedByClazz(
        @Param("clazz") clazz: Long,
        @Param("year") year: Int,
        @Param("semester") semester: Int,
    ): Int

    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @ResultType(StudentBookOrder::class)
    @Select("select * from student_book_order where book = #{isbn}")
    fun selectByBook(book: Book): List<StudentBookOrder>

    @Deprecated("已迁移到SearchOfStudent")
    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @ResultType(StudentBookOrder::class)
    @Select("select * from student_book_order where book = #{isbn} and student = #{studentId}")
    fun selectByBookAndStudent(
        @Param("isbn") isbn: String,
        @Param("studentId") studentId: String
    ,
    ): List<StudentBookOrder>

    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @ResultType(StudentBookOrder::class)
    @Select("select * from student_book_order where book = #{isbn} and student = #{personId} and year = #{year} and semester = #{semester}")
    fun searchOfStudent(params2: SearchParams2): List<StudentBookOrder>


    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @ResultType(StudentBookOrder::class)
    @Select("select * from student_book_order where student = #{student} and year = #{year} and book = #{book}")
    fun selectByYearSemesterAndBook(
        @Param("book") book: String,
        @Param("year") year: Int,
        @Param("semester") semester: Int,
    ): List<StudentBookOrder>


    @Results(
        value = [
            Result(
                property = "student", column = "student",
                one = One(select = "top.ostp.web.mapper.StudentMapper.selectStudentById")
            ),
            Result(
                property = "book", column = "book",
                one = One(select = "top.ostp.web.mapper.BookMapper.selectByISBN")
            )
        ]
    )
    @Select("select * from student_book_order where id = #{id}")
    fun selectById(order: Int): StudentBookOrder?

    @Delete("delete from student_book_order where id = #{order}")
    fun deleteBookOrder(order: Int): Int

    @Insert("insert into student_book_order (student, book, price, year, semester, received) values (#{student}, #{book}, #{price}, #{year}, #{semester}, 0)")
    fun addBookOrder(
        @Param("student") student: String,
        @Param("book") book: String,
        @Param("price") price: Int,
        @Param("year") year: Int,
        @Param("semester") semester: Int,
    ): Int

    @Update("update student_book_order set received = 1 where id = #{order}")
    fun received(order: Int): Int


}