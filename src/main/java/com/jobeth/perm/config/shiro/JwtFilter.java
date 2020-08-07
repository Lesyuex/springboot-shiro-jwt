package com.jobeth.perm.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jobeth.perm.common.enums.ResultEnum;

import com.jobeth.perm.common.exception.ServerException;
import com.jobeth.perm.common.utils.JwtUtil;
import com.jobeth.perm.common.utils.ResponseUtil;

import com.jobeth.perm.config.CommonConfigProperties;
import com.jobeth.perm.po.Permission;
import com.jobeth.perm.service.PermissionService;
import com.jobeth.perm.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;


import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


/**
 * JwtFilter继承shiro AuthenticatingFilter重写认证方法
 *
 * @author Jobeth
 * @since 2020/6/30 16:41
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    private final RedisService redisService;
    private final PermissionService permissionService;
    private final CommonConfigProperties properties;
    PatternMatcher patternMatcher = new AntPathMatcher();

    public JwtFilter(RedisService redisService, PermissionService permissionService, CommonConfigProperties properties) {
        this.redisService = redisService;
        this.permissionService = permissionService;
        this.properties = properties;
    }


    /**
     * 判断用户是否想要登录
     *
     * @param request  request
     * @param response response
     * @return boolean
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //从header中获取token
        String token = httpServletRequest.getHeader(properties.getJwtTokenName());
        return token != null;
    }

    /**
     * 验证用户
     *
     * @param request     request
     * @param response    response
     * @param mappedValue mappedValue
     * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (!isLoginAttempt(httpServletRequest, httpServletResponse)) {
            ResponseUtil.writeJson(response, ResultEnum.USER_TOKEN_INVALID);
            return false;
        }
        return executeLogin(request, response);
    }

    /**
     * 创建用户的验证凭证
     *
     * @param servletRequest  servletRequest
     * @param servletResponse servletResponse
     * @return AuthenticationToken
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //从header中获取token
        String token = httpServletRequest.getHeader(properties.getJwtTokenName());
        return new JwtToken(token);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        return false;
    }


    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        //生成Token认证的信息
        JwtToken jwtToken = (JwtToken) createToken(request, response);
        Subject subject = getSubject(request, response);
        try {
            subject.login(jwtToken);
            log.info("【 userId:{} 】- 凭证正确，认证成功", jwtToken.getUserId());
            if (JwtUtil.needRefresh(jwtToken.getRealToken())) {
                //生成新token
                String userId = jwtToken.getUserId().toString();
                String newToken = properties.getJwtTokenPrefix() + JwtUtil.generateToken(userId);
                //存进redis
                String redisKey = properties.getJwtTokenPrefix() + userId;
                redisService.setExpire(redisKey, newToken, properties.getJwtExpiration());
                log.info("【 userId:{} 】- 凭证刷新，更新完成", userId);
            }
            return onLoginSuccess(jwtToken, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(jwtToken, e, request, response);
        }
    }


    @Override
    protected boolean onLoginSuccess(AuthenticationToken authenticationToken, Subject subject, ServletRequest request, ServletResponse response) {
        //判断是否刷新Token
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String userId = jwtToken.getUserId().toString();
        try {
            //鉴权
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String requestSource = httpServletRequest.getRequestURI();
            String systemSourcePerm = getSystemSourcePerm(jwtToken, requestSource);
            subject.checkPermission(systemSourcePerm);
            log.info("【 userId:{} 】- 拥有权限，授权成功", userId);
            return true;
        } catch (Exception e) {
            log.error("【 userId:{} 】- 无此权限，授权失败 -【{}】", userId, e.getMessage());
            ResponseUtil.writeJson(response, ResultEnum.USER_ACCESS_DENIED);
            return false;
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        JwtToken jwtToken = (JwtToken) token;
        log.error("【 userId:{} 】- 凭证有误，认证失败- [{}]", jwtToken.getUserId(), e.getMessage());
        ResponseUtil.writeJson(response, ResultEnum.USER_TOKEN_INVALID);
        return false;
    }

    /**
     * 获取请求资源的对应权限
     *
     * @param requestSource requestSource
     * @return systemSourcePerm
     */
    @SuppressWarnings("unchecked")
    protected String getSystemSourcePerm(AuthenticationToken token, String requestSource) {
        String systemSource = null;
        String systemSourcePerm = null;
        JwtToken jwtToken = (JwtToken) token;
        try {
            //从redis取url对应的匹配权限
            if (redisService.exists(properties.getRedisUrlPermKey())) {
                LinkedHashMap<String, String> permMap = (LinkedHashMap<String, String>) redisService.get(properties.getRedisUrlPermKey());
                Set<String> permList = permMap.keySet();
                for (String pattern : permList) {
                    if (patternUrl(pattern, requestSource)) {
                        systemSource = pattern;
                        systemSourcePerm = permMap.get(pattern);
                        break;
                    }
                }
            }
            //没有在Redis拿到数据，从数据库取
            if (systemSourcePerm == null) {
                LambdaQueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
                        .lambda().eq(Permission::getType, 0)
                        .eq(Permission::getStatus, 0)
                        .groupBy(Permission::getUrl)
                        .orderByDesc(Permission::getSortId);
                List<Permission> permissionList = permissionService.list(wrapper);
                for (Permission permission : permissionList) {
                    if (patternUrl(permission.getUrl(), requestSource)) {
                        systemSource = permission.getUrl();
                        systemSourcePerm = permission.getPermission();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("【 userId:{} 】-匹配资源权限发生内部异常-", jwtToken.getUserId(), e);
        }
        if (systemSourcePerm == null) {
            throw new ServerException(ResultEnum.SERVER_NO_THIS_SOURCE.getMessage());
        }
        log.info("【 系统资源 [{}] 匹配请求资源 [{}] 成功，所需权限 => [{}] 】", systemSource, requestSource, systemSourcePerm);
        return systemSourcePerm;
    }

    private boolean patternUrl(String systemSource, String requestSource) {
        return patternMatcher.matches(systemSource, requestSource);
    }
}
