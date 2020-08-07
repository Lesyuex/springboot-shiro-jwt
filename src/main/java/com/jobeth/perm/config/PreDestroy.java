package com.jobeth.perm.config;

import com.jobeth.perm.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/8/3 11:10
 */
@Slf4j
@Component
public class PreDestroy implements ExitCodeGenerator, DisposableBean {
    private final CommonConfigProperties properties;
    private final RedisService redisService;

    public PreDestroy(RedisService redisService, CommonConfigProperties properties) {
        this.redisService = redisService;
        this.properties = properties;
    }

    @Override
    public void destroy() throws Exception {
        //清除相关keys
        log.info("权限配置清除......");
        redisService.deleteByPrefix(properties.getRedisUrlPermKey() + "*");
    }

    @Override
    public int getExitCode() {
        return 0;
    }
}
