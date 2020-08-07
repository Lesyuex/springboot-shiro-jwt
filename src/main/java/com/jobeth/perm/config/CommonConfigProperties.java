package com.jobeth.perm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/6/30 16:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "common")
public class CommonConfigProperties {

    /**
     * JWT密钥KEY
     */
    public String jwtSecret;
    /**
     * JWTTokenKey
     */
    public String jwtTokenName;
    /**
     * JWTToken前缀字符
     */
    public String jwtTokenPrefix;
    /**
     * JWT过期时间
     */
    public Integer jwtExpiration;
    /**
     * JWT-token刷新的时间
     */
    public Integer jwtRefreshTime;
    /**
     * redis 权限配置信息key
     */
    public String redisUrlPermKey;

    /**
     * 不需要认证的接口
     */
    public String antMatcher;

}

