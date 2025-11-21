package com;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
public class DOCServiceWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(DOCServiceWebApplication.class,args);
    }
}
