package com.otsira.mapper;

import com.otsira.entity.*;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户 mapper 层接口
 * @create: 2024/10/26 17:00
 */
public interface UserMapper extends Mapper<User> {
    @Select("select * from user where openid = #{openid}")
    User queryUserByOpenid(String openid);
}
