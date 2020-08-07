package com.jobeth.perm.config.shiro;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/7/29 17:44
 */
public class JwtPermissionResolver implements PermissionResolver {

    @Override
    public Permission resolvePermission(String s) {
        return new UrlPermission(s);
    }
}
