package top.ostp.web.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.ostp.web.mapper.BookMapper
import top.ostp.web.mapper.CourseMapper
import top.ostp.web.mapper.CourseOpenMapper
import top.ostp.web.mapper.TeacherMapper
import top.ostp.web.model.CourseOpen
import top.ostp.web.model.common.ApiResponse
import top.ostp.web.model.common.Responses

@Service
class CourseOpenService {
    @Autowired
    lateinit var courseOpenMapper: CourseOpenMapper

    @Autowired
    lateinit var courseMapper: CourseMapper

    @Autowired
    lateinit var teacherMapper: TeacherMapper

    @Autowired
    lateinit var bookMapper: BookMapper


    fun insert(course: String, year: Int, semester: Int, book: String, teacher: String): ApiResponse<String> {
        val c = courseMapper.selectById(course)
        val b = bookMapper.selectByISBN(book)
        val t = teacherMapper.selectById(teacher)
        if (c == null || b == null || t == null || year < 0 || semester !in arrayOf(3, 6, 9)) {
            return Responses.fail("参数错误")
        }

        val courseOpen = CourseOpen(c, year, semester, b, t)

        return when (courseOpenMapper.insert(courseOpen)) {
            1 -> Responses.ok("插入成功")
            else -> Responses.fail("插入失败")
        }
    }
}