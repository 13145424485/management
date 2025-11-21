package com.doc.config.minio;

import com.alibaba.fastjson.JSONObject;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Component
public class MinioUtils {
    @Autowired
    private MinioClient client;
    @Autowired
    private MinioProp minioProp;

    /**
     * 创建bucket
     */
    public void createBucket(String bucketName) {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
        try {
            if (client.bucketExists(bucketExistsArgs))
                return;
            client.makeBucket(makeBucketArgs);
        } catch (Exception e) {
            log.error("创建桶失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * @param file 文件
     * @param bucketName 存储桶
     * @return
     */
    public JSONObject uploadFile(MultipartFile file, String bucketName) throws Exception {
        JSONObject res = new JSONObject();
        res.put("code", 0);
        // 判断上传文件是否为空
        if (null == file || 0 == file.getSize()) {
            res.put("msg", "上传文件不能为空");
            return res;
        }
        // 判断存储桶是否存在
        createBucket(bucketName);
        // 文件名
        String originalFilename = file.getOriginalFilename();
        // 新的文件名 = 存储桶名称_时间戳.后缀名
        String fileName = bucketName + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
        // 开始上传
        InputStream inputStream = file.getInputStream();
        PutObjectArgs args = PutObjectArgs.builder().bucket(bucketName).object(fileName)
                .stream(inputStream,inputStream.available(),-1).build();
        client.putObject(args);
        res.put("code", 1);
        //getEndpoint前缀 + bucketName 桶名称 + fileName文件名称
        res.put("msg", minioProp.getEndpoint() + "/" + bucketName + "/" + fileName);
        return res;
    }
}