package com.otsira.controller;

import com.otsira.result.Result;
import com.otsira.util.AliyunOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 通用请求处理功能（文件上传等）
 * @create: 2024/11/01 18:20
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用功能相关接口")
@Slf4j
public class CommonController {
    private AliyunOssUtil aliyunOssUtil;

    @Autowired
    public void setAliyunOssUtil(AliyunOssUtil aliyunOssUtil) {
        this.aliyunOssUtil = aliyunOssUtil;
    }

    /**
     * @depription: 文件上传
     * @param file MultipartFile 文件对象
     * @return Result<String> 包括 code, msg, data(String) 阿里云 OSS 文件请求的 url 地址
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> fileUpload(MultipartFile file) {
        String url = aliyunOssUtil.uploadFile(file);
        if (url == null) {
            return Result.error("文件上传失败");
        }
        return Result.success(url);
    }
}
