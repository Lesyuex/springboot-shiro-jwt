package com.jobeth.perm.common.enums;

import lombok.Getter;

/**
 * 异常枚举类
 *
 * @author JyrpoKoo
 * @version [版本号 2019/12/28]
 * @date Created by IntelliJ IDEA on 23:50 2019/12/28
 */
@Getter
public enum ResultEnum {
    /**
     * 正常
     */
    NO_ERROR(200, "操作成功！"),
    /**
     * 操作失败
     */
    ERROR(400, "操作失败！"),
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    /**
     * 请求参数错误
     */
    REQUEST_PARAMETER_ERROR(501, "请求参数错误"),
    /**
     * 未登录
     */
    USER_NOT_LOGIN(504, "未登录"),
    /**
     * 登录失败
     */
    USER_LOGIN_FAIL(505, "用户不存在或密码不一致"),
    /**
     * Token过期或者无效
     */
    USER_TOKEN_INVALID(502, "用户Token过期或无效"),
    /**
     * 没有权限
     */
    USER_ACCESS_DENIED(507, "无权限访问"),

    /**
     * 系统无此资源
     */
    SERVER_NO_THIS_SOURCE(508, "系统无此资源"),
    /**
     * 访问地址有误
     */
    CHECK_REQUEST_URL(998, "检查访问地址"),
    /**
     * 未知
     */
    UNKNOWN(999, "未知");


    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
