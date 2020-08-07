package com.jobeth.perm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/6/30 15:45
 */
@Service
@Slf4j
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 保存key - value
     *
     * @param key key
     * @param val val
     * @return boolean
     */
    public boolean set(String key, Object val) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, val);
        return true;

    }

    /**
     * 根据key设置有效时间
     *
     * @param key        key
     * @param expireTime time
     */
    public void setExpire(String key, Object value, long expireTime) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        Date date = new Date(Clock.systemDefaultZone().millis() + expireTime);
        log.info("【key => {} 数据存入redis缓存成功,失效时间：{}】", key, date);
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key key
     * @return boolean
     */
    public boolean exists(String key) {
        if (key == null) {
            return false;
        }
        return redisTemplate.hasKey(key);
    }


    /**
     * 读取缓存
     *
     * @param key key
     * @return Object
     */
    public Object get(String key) {
        Object result;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 删除对应的value
     *
     * @param key key
     */
    public boolean remove(String key) {
        if (exists(key)) {
            return redisTemplate.delete(key);
        }
        return false;

    }

    /**
     * redisTemplate删除迷糊匹配的key的缓存
     */
    public void deleteByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix);
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
        log.info("【清除Redis keys 数据成功 => {}】", keys);
    }
}
