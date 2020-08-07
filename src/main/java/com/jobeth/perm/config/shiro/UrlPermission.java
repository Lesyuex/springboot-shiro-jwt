package com.jobeth.perm.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/7/29 17:59
 */
@Slf4j
public class UrlPermission implements Permission {
    private final String perms;

    public UrlPermission(String perms) {
        this.perms = perms;
    }

    @Override
    public boolean implies(Permission permission) {
        if (!(permission instanceof UrlPermission)) {
            return false;
        }
        //鉴权
        UrlPermission urlPermission = (UrlPermission) permission;
        PatternMatcher patternMatcher = new AntPathMatcher();
        return patternMatcher.matches(this.perms, urlPermission.perms);
    }
}
