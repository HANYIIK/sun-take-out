package com.otsira.service;

import com.otsira.dto.EmployeeInfoDTO;
import com.otsira.dto.EmployeeLoginDTO;
import com.otsira.entity.Employee;
import com.otsira.result.Page;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 员工管理的 Service 层
 * @create: 2024/10/19 21:31
 */
public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    int insert(EmployeeInfoDTO employeeInfoDTO);
    int update(EmployeeInfoDTO employeeInfoDTO);
    Page queryPage(Integer page, Integer pageSize, String name);
    int updateStatus(EmployeeInfoDTO employeeInfoDTO);
    Employee queryById(Long id);
}
