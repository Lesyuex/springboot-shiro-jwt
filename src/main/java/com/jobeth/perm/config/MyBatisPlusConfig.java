package com.jobeth.perm.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatisPlus配置类
 * Configuration 声明为配置类
 * EnableTransactionManagement 声明事务被Spring管理
 * MapperScan 指定要变成实现类的接口所在的包
 *
 * @author Jobeth
 * @since 2020/6/30 13:00
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.jobeth.perm.mapper")
public class MyBatisPlusConfig {
    /**
     * 分页插件Bean
     *
     * @return 分页插件Bean
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
