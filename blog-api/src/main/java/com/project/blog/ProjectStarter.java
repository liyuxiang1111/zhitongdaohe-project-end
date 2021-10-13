package com.project.blog;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.project.blog.dao")

public class ProjectStarter {
    public static void main(String[] args) {
        SpringApplication.run(ProjectStarter.class,args);
    }
}
