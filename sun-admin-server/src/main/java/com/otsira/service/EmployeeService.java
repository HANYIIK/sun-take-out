package com.otsira.service;

import com.otsira.dto.EmployeeLoginDTO;
import com.otsira.entity.Employee;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 员工管理的 Service 层
 * @create: 2024/10/19 21:31
 */
public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
}
