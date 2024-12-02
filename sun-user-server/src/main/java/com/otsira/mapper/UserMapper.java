package com.otsira.mapper;

import com.otsira.entity.*;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户 mapper 层接口
 * @create: 2024/10/26 17:00
 */
public interface UserMapper extends Mapper<User> {
    @Select("select * from user where openid = #{openid}")
    User queryUserByOpenid(String openid);

    @Select("SELECT COUNT(*) FROM user WHERE create_time BETWEEN #{beginTime} AND #{endTime}")
    Integer sumNewUserNumByDate(LocalDateTime beginTime, LocalDateTime endTime);

    @Select("SELECT COUNT(*) FROM user WHERE create_time <= #{time}")
    Integer sunTotalUserNumByDate(LocalDateTime time);
}
