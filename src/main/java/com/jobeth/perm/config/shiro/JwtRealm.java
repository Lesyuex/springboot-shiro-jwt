package com.jobeth.perm.config.shiro;

import com.jobeth.perm.common.enums.ResultEnum;
import com.jobeth.perm.common.utils.JsonUtil;
import com.jobeth.perm.common.utils.JwtUtil;
import com.jobeth.perm.config.CommonConfigProperties;
import com.jobeth.perm.dto.PermissionDTO;
import com.jobeth.perm.po.Permission;
import com.jobeth.perm.service.PermissionService;
import com.jobeth.perm.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/6/30 15:16
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private RedisService redisService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private CommonConfigProperties properties;

    /**
     * 是否支持JwtToken认证
     *
     * @param token token
     * @return boolean
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 鉴权
     *
     * @param principalCollection principalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Long userId = Long.parseLong((String) principalCollection.getPrimaryPrincipal());
        List<String> permissionList;
        //redis取
        String userPermRedisKey = properties.getRedisUrlPermKey() + "_" + userId;
        if (redisService.exists(userPermRedisKey)) {
            permissionList = (List<String>) redisService.get(userPermRedisKey);
        } else {
            //从数据库读取资源
            PermissionDTO permissionDTO = PermissionDTO.builder().type(0).status(0).userId(userId).build();
            List<Permission> permissions = permissionService.listByPermissionDTO(permissionDTO);
            if (permissions == null || permissions.isEmpty()) {
                throw new AuthorizationException(ResultEnum.USER_ACCESS_DENIED.getMessage());
            }
            //组装权限数据
            permissionList = permissions.stream().map(Permission::getPermission).collect(Collectors.toList());
            redisService.setExpire(userPermRedisKey, permissionList, properties.getJwtExpiration());
        }
        if (permissionList == null || permissionList.isEmpty()) {
            throw new AuthorizationException(ResultEnum.USER_ACCESS_DENIED.getMessage());
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(permissionList);
        return authorizationInfo;
    }

    /**
     * 认证
     *
     * @param authenticationToken 登录信息
     * @return AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = (String) jwtToken.getCredentials();
        String[] strings = token.split(properties.getJwtTokenPrefix());
        if (strings.length != 2) {
            throw new AuthenticationException("用户Token无效");
        }
        jwtToken.setRealToken(strings[1]);
        String userId = JwtUtil.getField(jwtToken.getRealToken(), "userId", String.class);
        if (userId == null) {
            throw new AuthenticationException("用户Token过期");
        }
        jwtToken.setUserId(Long.parseLong(userId));
        //从redis中找
        String redisKey = properties.getJwtTokenPrefix() + userId;
        //token无效或者过期
        if (!redisService.exists(redisKey)) {
            throw new AuthenticationException("用户Redis Token 失效");
        }
        String redisToken = (String) redisService.get(redisKey);
        //token无效或者过期
        if (!token.equals(redisToken)) {
            throw new AuthenticationException("用户Token与Redis Token不一致");
        }
        //返回登录凭证 用于鉴权
        return new SimpleAuthenticationInfo(userId, token, this.getName());
    }
}
