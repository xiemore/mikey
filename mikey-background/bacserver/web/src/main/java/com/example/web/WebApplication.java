package com.example.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@EnableTransactionManagement
@SpringBootApplication
@MapperScan(basePackages = {"com.example.web.mapper"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


    @Configuration
    public class CrosConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET","HEAD","POST","DELETE","OPTIONS","PUT")
                    .allowCredentials(true)
                    .maxAge(3600)
                    .allowedHeaders("*");
        }
    }
}
