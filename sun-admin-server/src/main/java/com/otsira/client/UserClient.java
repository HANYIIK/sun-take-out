package com.otsira.client;

import com.otsira.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用于调用用户端微服务的 openFeign 接口
 * @create: 2024/10/26 17:17
 */
@FeignClient(value = "sun-user", fallback = UserClientFallback.class, qualifiers = "userClient")
public interface UserClient {
    @GetMapping("/user/user/findAllUsers")
    @ResponseBody
    List<User> findAll();

    @GetMapping("/user/user/findNewAndTotalUserNum")
    @ResponseBody
    List<Integer> findNewAndTotalUserNum(@RequestParam("begin") String begin,
                                         @RequestParam("end") String end);
}
