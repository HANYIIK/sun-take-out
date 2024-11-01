package com.otsira.util;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.otsira.properties.AliyunOssProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 阿里云文件上传的工具包
 * @create: 2024/11/01 19:55
 */
@AllArgsConstructor
@Slf4j
public class AliyunOssUtil {
    private AliyunOssProperties aliyunOssProperties;

    public String uploadFile(MultipartFile file) {
        log.info("文件开始上传...");
        String endpoint = aliyunOssProperties.getEndpoint();
        // 确保已设置环境变量 OSS_ACCESS_KEY_ID 和 OSS_ACCESS_KEY_SECRET
        EnvironmentVariableCredentialsProvider credentialsProvider;
        try {
            credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            log.info("获取环境变量中的授权码和密钥时发生异常。");
            throw new RuntimeException(e);
        }
        String bucketName = aliyunOssProperties.getBucketName();

        // 获取文件的原始名称和扩展名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = null;
        if (originalFilename != null) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成唯一的文件名
        String uniqueFileName = UUID.randomUUID() + fileExtension;
        String objectName = aliyunOssProperties.getFileDir() + uniqueFileName;
        String region = aliyunOssProperties.getRegion();


        // 创建 OSSClient 实例
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
        try {
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求
            ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            log.info("捕获到 OSSException，这意味着您的请求已到达 OSS，但由于某种原因被拒绝并返回了错误响应。");
            log.info("Error Code:{}", oe.getErrorCode());
            log.info("Request ID:{}", oe.getRequestId());
            log.info("Host ID:{}", oe.getHostId());
            throw new RuntimeException(oe);
        } catch (ClientException ce) {
            log.info("捕获到 ClientException，这意味着客户端在尝试与 OSS 通信时遇到了严重的内部问题，例如无法访问网络。");
            throw new RuntimeException(ce);
        } catch (IOException e) {
            log.info("分片文件对象在获取文件输入流时发生异常: {}", "multipartFile.getInputStream()");
            throw new RuntimeException(e);
        } finally {
            ossClient.shutdown();
        }
        log.info("文件上传成功!");
        // 返回 oss 访问链接
        return aliyunOssProperties.getUrlPrefix() + objectName;
    }
}
