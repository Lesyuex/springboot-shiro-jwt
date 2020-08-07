package com.jobeth.perm.common.utils;

import com.jobeth.perm.common.Constant;
import com.jobeth.perm.common.helper.SpringContextHelper;
import com.jobeth.perm.config.CommonConfigProperties;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author Jobeth
 * @date Created by IntelliJ IDEA on 11:29 2020/4/10
 */
@Slf4j
public class JwtUtil {

    private static final CommonConfigProperties PROPERTIES = SpringContextHelper.getBean(CommonConfigProperties.class);

    private JwtUtil() {
    }

    public static String generateToken(String userId) {
        //登录成功生成生成Token
        Map<String, Object> claim = new HashMap<>(6);
        claim.put("userId", userId);
        //失效时间
        String token = null;
        Date date = new Date(Clock.systemDefaultZone().millis() + PROPERTIES.getJwtExpiration());
        try {
            token = Jwts.builder()
                    // 创建payload的私有声明
                    .setClaims(claim)
                    // 放入用户名和用户ID
                    .setId(Constant.JWT_ID)
                    .setSubject(userId)
                    // 失效时间
                    .setExpiration(date)
                    // 签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, PROPERTIES.getJwtSecret())
                    .compact();
            log.info("用户Token生成成功,失效时间：{}", date);
        } catch (Exception e) {
            log.error("用户Token生成失败-", e);
        }
        return token;
    }


    /**
     * 解析token
     *
     * @param token token
     * @return Claims
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(PROPERTIES.getJwtSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取token中的数据，不需要解密
     *
     * @param token token
     * @param field field
     * @param clazz clazz
     * @param <T>   T
     * @return T
     */
    public static <T> T getField(String token, String field, Class<T> clazz) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.get(field, clazz);
        } else {
            return null;
        }
    }

    /**
     * 是否刷新token
     *
     * @param token token
     * @return boolean
     */
    public static boolean needRefresh(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            return (expiration.getTime() - Clock.systemDefaultZone().millis()) <= PROPERTIES.getJwtRefreshTime();
        }
        return false;
    }

}
