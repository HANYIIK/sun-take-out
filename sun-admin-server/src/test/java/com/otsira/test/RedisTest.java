package com.otsira.test;

import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 测试 Redis 的连接情况
 * @create: 2024/11/09 21:13
 */
@SuppressWarnings({"rawtypes", "unchecked"})
// @SpringBootTest
public class RedisTest {
    // @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString() {
        // 四中操作: set get setex setnx
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 1. setex: 有效期为 1 分钟的验证码
        valueOperations.set("code", "231452", 1, TimeUnit.MINUTES);

        // 2. setnx: 如果 key 不存在则设置值, 存在则不设置
        valueOperations.setIfAbsent("lock", "1");
        // 设置不进去, 因为 key 已经存在
        valueOperations.setIfAbsent("lock", "0");

        System.out.println(valueOperations.get("code"));
        // 输出为 1
        System.out.println(valueOperations.get("lock"));
    }

    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("user", "name", "otsira");
        hashOperations.put("user", "password", "123456");
        System.out.println(hashOperations.get("user", "name"));
        System.out.println(hashOperations.get("user", "password"));
    }

    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush("list", "otsira");
        listOperations.rightPush("list", "hanyiik");
        Objects.requireNonNull(listOperations.range("list", 0, -1)).forEach(System.out::println);
    }

    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("set", "otsira");
        setOperations.add("set", "hanyiik");
        // set 中的元素不允许重复
        setOperations.add("set", "hanyiik");
        System.out.println(setOperations.members("set"));
    }

    @Test
    public void testZSet() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("zset", "otsira", 1);
        zSetOperations.add("zset", "hanyiik", 2);
        zSetOperations.add("zset", "yiix", 3);
        System.out.println(zSetOperations.rangeWithScores("zset", 0, -1));
    }

    @Test
    public void remove() {
        redisTemplate.delete("name");
        redisTemplate.delete("test");
        redisTemplate.delete("user");
        redisTemplate.delete("list");
        redisTemplate.delete("set");
        redisTemplate.delete("zset");
    }
}
