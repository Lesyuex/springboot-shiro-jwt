package com.jobeth.perm.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/6/30 16:11
 */
@Data
public class UserDTO implements Serializable {
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
