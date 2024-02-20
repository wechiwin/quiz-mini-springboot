package com.moggi.quizmini;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Author weiqirui
 * @Date 2024/2/19
 */
@SpringBootApplication
@MapperScan(basePackages = "com.moggi.quizmini.mapper")
public class QuizApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(QuizApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }
}
