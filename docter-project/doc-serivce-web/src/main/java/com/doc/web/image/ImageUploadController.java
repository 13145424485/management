package com.doc.web.image;


import com.alibaba.fastjson.JSONObject;
import com.doc.config.minio.MinioUtils;
import com.doc.utils.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload/")
@Slf4j
public class ImageUploadController {

    @Autowired
    MinioUtils minioUtils;


    //类级别的公共路径前缀，接口完整路径 = 前缀 + 方法路径。
   // 最终 URL 是 /api/upload/uploadImage
    //方法级映射，默认支持 GET/POST/PUT/DELETE；
    @RequestMapping("/uploadImage")
    public ResultVo uploadImage(@RequestParam("file") MultipartFile file) {

        log.info("触发上传图片Controller");

        JSONObject images = null;
        try {
            images = minioUtils.uploadFile(file, "gymnasium");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo("上传失败", 500, null);
        }
        return new ResultVo("上传成功", 200, images);


    }


}
