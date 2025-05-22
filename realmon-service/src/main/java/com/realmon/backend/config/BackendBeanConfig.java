package com.realmon.backend.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
        "com.realmon.backend",                // Controller, Service, Repository, etc
        "com.realmon.common.model.mapper"     // MapStruct Mapper
})
@EnableJpaRepositories(basePackages = "com.realmon.backend.repository")
@EntityScan(basePackages = "com.realmon.common.model.entity")
public class BackendBeanConfig {
}