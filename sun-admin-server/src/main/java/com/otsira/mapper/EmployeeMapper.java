package com.otsira.mapper;

import com.otsira.entity.Employee;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 员工管理的 Dao 层接口
 * @create: 2024/10/19 22:00
 */
public interface EmployeeMapper extends Mapper<Employee> {

    /**
     * 根据用户名查找员工
     * @param username 用户名
     * @return 员工
     */
    @Select("select * from employee where username = #{username}")
    Employee selectByUsername(String username);
}
