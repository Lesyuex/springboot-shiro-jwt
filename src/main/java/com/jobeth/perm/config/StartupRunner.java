package com.jobeth.perm.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jobeth.perm.po.Permission;
import com.jobeth.perm.service.PermissionService;
import com.jobeth.perm.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

import java.util.List;


/**
 * 通过order值的大小来决定启动的顺序
 *
 * @author Jobeth
 * @since 2020/8/2 19:19
 */
@Component
@Order(1)
@Slf4j
public class StartupRunner implements CommandLineRunner {

    private final PermissionService permissionService;
    private final RedisService redisService;
    private final CommonConfigProperties properties;

    public StartupRunner(PermissionService permissionService, RedisService redisService, CommonConfigProperties properties) {
        this.permissionService = permissionService;
        this.redisService = redisService;
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(String... args) {
        //清除相关keys
        log.info("【 权限配置清除...... 】");
        redisService.deleteByPrefix(properties.getRedisUrlPermKey() + "*");
        ///加载权限配置
        LambdaQueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
                .lambda().eq(Permission::getType, 0)
                .eq(Permission::getStatus, 0)
                .groupBy(Permission::getUrl)
                .orderByDesc(Permission::getSortId);
        List<Permission> list = permissionService.list(wrapper);
        //保证顺序鉴权
        if (!list.isEmpty()) {
            ///LinkedHashMap<String, String> urlPerm = (LinkedHashMap<String, String>) list.stream().collect(Collectors.toMap(Permission::getUrl, Permission::getPermission));
            LinkedHashMap<String, String> urlPermMap = new LinkedHashMap<>(100);
            list.forEach(permission -> {
                if (permission.getUrl() != null && permission.getPermission() != null) {
                    urlPermMap.put(permission.getUrl(), permission.getPermission());
                }
            });
            log.info("【 权限配置加载...... 】");
            redisService.setExpire(properties.getRedisUrlPermKey(), urlPermMap, 1000 * 60 * 60 * 12L);
        }
    }
}
