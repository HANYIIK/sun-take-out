package com.otsira.controller;

import com.otsira.client.UserClient;
import com.otsira.constant.JwtClaimsConstant;
import com.otsira.constant.MessageConstant;
import com.otsira.constant.StatusConstant;
import com.otsira.dto.EmployeeEditPasswordDTO;
import com.otsira.dto.EmployeeInfoDTO;
import com.otsira.dto.EmployeeLoginDTO;
import com.otsira.entity.Employee;
import com.otsira.entity.User;
import com.otsira.properties.JwtProperties;
import com.otsira.result.Page;
import com.otsira.result.Result;
import com.otsira.service.EmployeeService;
import com.otsira.util.EmployeeContext;
import com.otsira.util.JwtUtil;
import com.otsira.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
* @program: sun-take-out
* @author: HANYIIK
* @description: 员工管理的 Controller 层
* @create: 2024/10/19 18:30
*/
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工管理相关接口")
@Slf4j
public class EmployeeController {
    private EmployeeService employeeService;
    private JwtProperties jwtProperties;
    private UserClient userClient;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Autowired
    public void setUserClient(@Qualifier("userClient") UserClient userClient) {
        this.userClient = userClient;
    }

    /**
     * @param: 无
     * @return 所有员工的 List<Employee> Json
     * @description: 获取所有员工信息
     */
    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<Page> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             // 员工姓名的模糊查询
                             @RequestParam(value = "name", required = false) String name) {
        log.info("管理员 {} 正在请求获取所有员工信息, 当前页 {}, 一页 {} 条数据", EmployeeContext.getEmpId(), page, pageSize);
        Page pageResult = employeeService.queryPage(page, pageSize, name);
        return Result.success(pageResult);
    }

    /**
    * @param: EmployeeLoginDTO 前端传输过来的封装类（用户名和密码）
    * @return Result(code, msg, data), 其中 data 为返回给前端的员工登录信息 EmployeeLoginVO（id, 账户, 姓名, token）
    * @description: 登录
    */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录: id-{}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.generateToken(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        log.info("员工登录成功，生成的 token 为: {}", token);
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }

    /**
     * @param: 无
     * @return 返回给前端的 Json（没有 msg 和 data，只有一个 code = 1，表示操作成功）
     * @description: 退出登录
     */
    @PostMapping("logout")
    @ApiOperation("员工退出登录")
    public Result<String> logout() {
        log.info("员工退出登录: id-{}", EmployeeContext.getEmpId());
        List<User> users = userClient.findAll();
        log.info("用户端微服务返回的用户信息：{}", users);
        return Result.success();
    }

    /**
     * @param: EmployeeInfoDTO 前端传输过来的封装类（id 可为空, 用户名, 姓名, 电话, 性别, 身份证号）
     * @return Result(code, msg, data), 其中 data 为返回给前端的员工添加信息 EmployeeInsertVO
     * @description: 新增员工
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result<Object> insert(@RequestBody EmployeeInfoDTO employeeInfoDTO) {
        log.info("管理员 id-{} 新增了一个员工：{}", EmployeeContext.getEmpId(), employeeInfoDTO);
        int insert = employeeService.insert(employeeInfoDTO);
        if (insert == 1) {
            return Result.success();
        } else {
            return Result.error(MessageConstant.SYSTEM_ERROR);
        }
    }

    /**
     * @param: id 员工 id
     * @return 返回给前端的 Result(code, msg, data)
     * @description: 禁用/启用员工
     */
    @PostMapping("status/{status}")
    @ApiOperation("禁用/启用员工")
    public Result<Object> updateStatus(@PathVariable Integer status,
                                       @RequestParam Long id) {
        EmployeeInfoDTO employeeInfoDTO = EmployeeInfoDTO.builder()
                .id(id)
                .status(status)
                .build();
        log.info("管理员 id-{} 正在请求{}员工 id-{}", EmployeeContext.getEmpId(), status.equals(StatusConstant.ENABLE) ? "启用" : "禁用", employeeInfoDTO);
        int update = employeeService.updateStatus(employeeInfoDTO);
        if (update == 1) {
            return Result.success();
        } else {
            return Result.error(MessageConstant.SYSTEM_ERROR);
        }
    }

    /**
     * @param: id - 需要被修改的员工 id
     * @return 返回给前端的 Result(code, msg, data)
     * @description: 编辑员工信息
     */
    @GetMapping("/{id}")
    @ApiOperation("请求编辑员工信息")
    public Result<Employee> edit(@PathVariable Long id) {
        log.info("管理员 id-{} 请求编辑员工 id-{}", EmployeeContext.getEmpId(), id);
        Employee employee = employeeService.queryById(id);
        return Result.success(employee);
    }

    /**
     * 提交编辑后的员工信息
     * @param employeeInfoDTO 前端传输过来的封装类（id, 用户名, 姓名, 电话, 性别, 身份证号）
     * @return 返回给前端的 Result(code, msg, data)
     */
    @PutMapping
    @ApiOperation("提交编辑后的员工信息")
    public Result<Object> update(@RequestBody EmployeeInfoDTO employeeInfoDTO) {
        log.info("管理员 id-{} 正在请求提交编辑后的员工信息: {}", EmployeeContext.getEmpId(), employeeInfoDTO);
        if (employeeService.update(employeeInfoDTO) > 0) {
            return Result.success();
        }
        return Result.error(MessageConstant.SYSTEM_ERROR);
    }

    @PutMapping("/editPassword")
    @ApiOperation("修改员工的登录密码")
    public Result<Object> editPassword(@RequestBody EmployeeEditPasswordDTO employeeEditPasswordDTO) {
        log.info("管理员 id-{} 正在请求修改登录密码", EmployeeContext.getEmpId());
        if (employeeService.editPassword(employeeEditPasswordDTO) <= 0) {
            return Result.error("修改密码失败!");
        }
        return Result.success();
    }
}
