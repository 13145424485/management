package com.doc.config.minio;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * MinIO 配置类
 * Spring 启动 → 把 yml 里 minio.* 绑定到 MinioProperties 对象。
 *
 * Spring 发现 @Bean 方法 → 执行它：
 * 取出 minioProp 中的 endpoint、ak、sk → 创建 MinioClient → 注册成 Bean。
 * 业务类里直接
 * @Autowired
 * private MinioClient minioClient;
 * 就能开箱即用，上传、下载、删文件统统搞定，而再也看不到 new MinioClient(...) 的硬编码。
 */
@Configuration
@EnableConfigurationProperties(MinioProp.class)
public class MinioConfig {
    @Autowired
    private MinioProp minioProp;
    @Bean
    //标注在方法上，表示“该方法的返回值要交给 Spring 容器管理”，默认单例、懒加载=false。
    public MinioClient minioClient() throws Exception {
        return MinioClient.builder().endpoint(minioProp.getEndpoint())
                .credentials(minioProp.getAccesskey(), minioProp.getSecretKey()).build();
    }
}
