package com.project.group;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.project.group.dao")

public class ProjectStarter {
    public static void main(String[] args) {
        SpringApplication.run(ProjectStarter.class,args);
    }
}
