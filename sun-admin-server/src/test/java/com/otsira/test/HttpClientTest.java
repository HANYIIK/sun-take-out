package com.otsira.test;

import com.alibaba.fastjson.JSONObject;
import com.otsira.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 测试 aliyun-sdk 包下自带的 httpclient 包能不能用
 * @create: 2024/11/10 21:09
 */
@SpringBootTest
public class HttpClientTest {

    /**
     * 测试 GET 请求
     */
    @Test
    public void testGet() {
        // 创建 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建 HttpGet 对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        // 创建 CloseableHttpResponse 对象
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            // 获取状态码
            int statusCode = response.getStatusLine().getStatusCode();
            // 获取响应体
            HttpEntity entity = response.getEntity();
            // 获取响应体的内容
            String content = EntityUtils.toString(entity, "utf-8");
            // 打印
            System.out.println("状态码：" + statusCode);
            System.out.println("内容：" + content);
            // 关闭
            response.close();
            httpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试 PUT 请求
     */
    @Test
    public void testPost() {
        // 创建 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建 HttpPut 对象
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "123456");

        StringEntity stringEntity;
        try {
            stringEntity = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        stringEntity.setContentEncoding("utf-8");
        stringEntity.setContentType("application/json");
        System.out.println(jsonObject);
        httpPost.setEntity(stringEntity);

        // 创建 CloseableHttpResponse 对象
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            // 获取状态码
            int statusCode = response.getStatusLine().getStatusCode();
            // 获取响应体
            HttpEntity entity = response.getEntity();
            // 获取响应体的内容
            String content = EntityUtils.toString(entity, "utf-8");
            // 打印
            System.out.println("状态码：" + statusCode);
            System.out.println("内容：" + content);
            // 关闭
            response.close();
            httpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试 HttpClientUtil 工具类的 doGet 方法
     * 该方法是对上面的 testGet 方法的封装
     */
    @Test
    public void testHttpClientUtilDoGet() {
        String responseBody = HttpClientUtil.doGet("http://localhost:8080/user/shop/status", null);
        System.out.println(responseBody);
    }

    /**
     * 测试 HttpClientUtil 工具类的 doPost 方法
     * 该方法是对上面的 testPost 方法的封装
     */
    @Test
    public void testHttpClientUtilDoPost() {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("username", "admin");
        requestParam.put("password", "123456");
        String responseBody;
        try {
            responseBody = HttpClientUtil.doPost4Json("http://localhost:8080/admin/employee/login", requestParam);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(responseBody);
    }
}
