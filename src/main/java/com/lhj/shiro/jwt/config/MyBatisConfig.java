package com.lhj.shiro.jwt.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {MyBatisConfig.BASE_PACKAGES})
public class MyBatisConfig {

    static final String BASE_PACKAGES = "com.lhj.shiro.jwt.dao";

}
