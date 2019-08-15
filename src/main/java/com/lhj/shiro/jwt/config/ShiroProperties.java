package com.lhj.shiro.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * shiro配置
 * @author LHJ
 */
@Configuration
@ConfigurationProperties(prefix = "shiro")
@Data
public class ShiroProperties {

    /**
     * 可匿名访问的url
     * 通过英文,号分隔
     */
    private String anonUrl;

    /**
     * AES对称加密密钥
     */
    private String secret;

}
