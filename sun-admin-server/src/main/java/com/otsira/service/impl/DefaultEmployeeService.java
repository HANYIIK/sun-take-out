package com.otsira.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsira.annotation.AutoFill;
import com.otsira.constant.MessageConstant;
import com.otsira.constant.PasswordConstant;
import com.otsira.constant.StatusConstant;
import com.otsira.dto.EmployeeInfoDTO;
import com.otsira.dto.EmployeeLoginDTO;
import com.otsira.entity.Employee;
import com.otsira.enumeration.OperationType;
import com.otsira.exception.AccountLockedException;
import com.otsira.exception.AccountNotFoundException;
import com.otsira.exception.PasswordErrorException;
import com.otsira.exception.UserNameConflictException;
import com.otsira.mapper.EmployeeMapper;
import com.otsira.result.Page;
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
    private ObjectMapper objectMapper;

    @Autowired
    public void setEmployeeMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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

    /**
     * @description: 新增员工
     * @param employeeInfoDTO 前端传回来的新增员工信息(用户名，姓名，性别，电话，身份证号)
     * @return 受影响的行数
     */
    @Override
    @AutoFill(OperationType.INSERT)
    public int insert(EmployeeInfoDTO employeeInfoDTO) {
        // 首先，要校验 username 是否重复
        if (employeeMapper.selectByUsername(employeeInfoDTO.getUsername()) != null) {
            // 重复，抛异常
            throw new UserNameConflictException(MessageConstant.USERNAME_CONFLICT);
        }
        // 没有重复，继续添加
        Employee employee = objectMapper.convertValue(employeeInfoDTO, Employee.class);
        employee.setStatus(StatusConstant.DISABLE);
        // 设置默认密码（md5加密处理）
        employee.setPassword(DigestUtils.md5Hex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        return employeeMapper.insert(employee);
    }

    /**
     * @description: 更新员工信息
     * @param employeeInfoDTO 前端传回来的更新员工信息(用户名，姓名，性别，电话，身份证号)
     * @return 受影响的行数
     */
    @Override
    @AutoFill(OperationType.UPDATE)
    public int update(EmployeeInfoDTO employeeInfoDTO) {
        // 首先，要校验 username 是否重复
        Employee editingEmployee = employeeMapper.selectByPrimaryKey(employeeInfoDTO.getId());
        String oldUsername = editingEmployee.getUsername();
        if (!employeeInfoDTO.getUsername().equals(oldUsername)) {
            // 用户名被更改，需要校验与其他用户名是否重复
            if (employeeMapper.selectByUsername(employeeInfoDTO.getUsername()) != null) {
                // 重复，抛异常
                throw new UserNameConflictException(MessageConstant.USERNAME_CONFLICT);
            }
        }
        // 没有重复或用户名没有被更改，继续修改
        // 之前密码被改成"******", 需要将其置空，避免数据库中的密码被修改成"******"
        employeeInfoDTO.setPassword(null);
        Employee employee = objectMapper.convertValue(employeeInfoDTO, Employee.class);
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    @Override
    @AutoFill(OperationType.UPDATE)
    public int updateStatus(EmployeeInfoDTO employeeInfoDTO) {
        if (employeeMapper.selectByPrimaryKey(employeeInfoDTO.getId()) == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        Employee employee = objectMapper.convertValue(employeeInfoDTO, Employee.class);
        // 更新数据库
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    /**
     * @description: 分页查询员工信息
     * @param page 当前页
     * @param pageSize 每页显示的条数
     * @param name 员工姓名
     * @return 分页查询结果(total - 总记录数, records - 当前页的记录 List<Employee>)
     */
    @Override
    public Page queryPage(Integer page, Integer pageSize, String name) {
        if (name == null) {
            name = "";
        }
        Integer total = employeeMapper.queryPageCount(name);
        Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer start = (page - 1) * pageSize;
        List<Employee> records = employeeMapper.queryPage(start, pageSize, name);
        return Page.builder()
                .total(total)
                .records(records)
                .build();
    }

    @Override
    public Employee queryById(Long id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 隐藏密码, 防止前台通过浏览器控制台看到密码
        employee.setPassword("******");
        return employee;
    }
}
