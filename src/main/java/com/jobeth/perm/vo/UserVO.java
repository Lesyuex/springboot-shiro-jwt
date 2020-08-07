package com.jobeth.perm.vo;

import lombok.Data;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/6/30 16:08
 */
@Data
public class UserVO {
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 盐
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

}
