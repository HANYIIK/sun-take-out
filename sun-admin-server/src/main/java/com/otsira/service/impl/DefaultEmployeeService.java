package com.otsira.service.impl;

import com.otsira.constant.MessageConstant;
import com.otsira.constant.StatusConstant;
import com.otsira.dto.EmployeeLoginDTO;
import com.otsira.entity.Employee;
import com.otsira.exception.AccountLockedException;
import com.otsira.exception.AccountNotFoundException;
import com.otsira.exception.PasswordErrorException;
import com.otsira.mapper.EmployeeMapper;
import com.otsira.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 员工管理 Service 层接口的默认实现类
 * @create: 2024/10/19 21:58
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class DefaultEmployeeService implements EmployeeService {
    private EmployeeMapper employeeMapper;

    @Autowired
    public void setEmployeeMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        Employee employee = employeeMapper.selectByUsername(username);
        if (employee == null) {
            // 不存在该账户
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码需要进行 MD5 加密后与数据库中的密码(密文)对比
        password = DigestUtils.md5Hex(password.getBytes());
        log.info("加密后的密码：{}", password);
        if (!password.equals(employee.getPassword())) {
            // 密码输入错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        return employee;
    }
}
