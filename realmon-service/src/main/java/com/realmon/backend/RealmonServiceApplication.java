package com.realmon.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EntityScan(basePackages = {
//        "com.realmon.backend.entity",
//        "com.realmon.common.model.entity"
//})
//@EnableJpaRepositories(basePackages = {
//        "com.realmon.backend.repository"
//})
//@ComponentScan(basePackages = {
//        "com.realmon.backend",
//        "com.realmon.common"
//})
public class RealmonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealmonServiceApplication.class, args);
    }

}
