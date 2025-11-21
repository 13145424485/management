package com.doc.config.minio;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProp {
    private String endpoint;
    private String accesskey;
    private String secretKey;
}
