package top.ostp.web.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import top.ostp.web.model.Student;

@Mapper
public interface StudentMapper {
    @Insert("insert into student(id, name, clazz, password, balance, email) VALUES (#{id},#{name},#{clazz.id},#{password},#{balance},#{email})")
    int insert(Student student);

    @Update("update student set name=#{name} ,clazz=#{clazz.id},password=#{password},balance=#{balance},email=#{email} where id=#{id}")
    int update(Student student);

    @Delete("delete from student where id=#{id}")
    int delete(Student student);

    @Update("update student set balance=balance+#{money} where id=#{student.id}")
    int changeMoney(@Param("student") Student student, @Param("money") int money);

    @Select("select * from student where id=#{id} limit 1")
    @Results(
            value = {
                    @Result(
                            property = "clazz", column = "clazz",
                            one = @One(select = "top.ostp.web.mapper.ClazzMapper.selectById", fetchType = FetchType.EAGER)
                    )
            }
    )
    Student selectStudentById(String id);
}