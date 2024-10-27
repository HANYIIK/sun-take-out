package com.otsira.controller;

import com.otsira.client.UserClient;
import com.otsira.constant.JwtClaimsConstant;
import com.otsira.dto.EmployeeLoginDTO;
import com.otsira.entity.Employee;
import com.otsira.entity.User;
import com.otsira.properties.JwtProperties;
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
    @ApiOperation("获取所有员工信息")
    @GetMapping("/page")
    public Result<List<Employee>> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("管理员 {} 正在请求获取所有员工信息, 当前页 {}, 一页 {} 条数据", EmployeeContext.getEmpId(), page, pageSize);
        // todo: 分页查询所有员工信息
        return Result.success(null);
    }

    /**
    * @param: EmployeeLoginDTO 前端传输过来的封装类（用户名和密码）
    * @return 返回给前端的员工登录信息 Json（id, 账户, 姓名, token）
    * @description: 登录
    */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.generateToken(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        log.info("员工登录成功，生成的 token 为：{}", token);
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
        log.info("员工退出登录：{}", EmployeeContext.getEmpId());
        List<User> users = userClient.findAll();
        log.info("用户端微服务返回的用户信息：{}", users);
        return Result.success();
    }
}
