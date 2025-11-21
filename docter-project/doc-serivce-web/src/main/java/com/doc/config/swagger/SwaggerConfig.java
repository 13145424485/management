package com.doc.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig{

    /**
     * 创建Docket类型的对象,并使用Spring容器管理
     * Docket是Swagger中的全局配置对象
     * @return
     */
    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(swaggerDemoApiInfo())
                .enable(true) // 启用Swagger
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.doc.web"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/"); // 添加路径映射
    }

    private ApiInfo swaggerDemoApiInfo(){
        return new ApiInfoBuilder()
                .contact(new Contact("JAVA学习社区", "https://www.qq.com",
                        "eric@qq.com")) //配置文档主体内容
                //文档标题
                .title("API")
                //文档描述
                .description("运动系统的API文档")
                //文档版本
                .version("1.0.0")
                .build();
    }
}
