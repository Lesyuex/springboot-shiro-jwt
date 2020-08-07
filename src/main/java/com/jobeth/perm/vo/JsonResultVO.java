package com.jobeth.perm.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jobeth.perm.common.enums.ResultEnum;
import lombok.Data;

/**
 * 功能描述
 *
 * @author Jobeth
 * @date Created by IntelliJ IDEA on 12:12 2020/4/10
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResultVO<T> {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 信息
     */
    private String msg;

    private T data;

    public JsonResultVO() {
        this.code = 200;
        this.msg = "操作成功！";
    }

    public JsonResultVO(T data) {
        this.code = 200;
        this.msg = "操作成功！";
        this.data = data;
    }

    public JsonResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResultVO(Integer code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public JsonResultVO(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
    }

    public JsonResultVO(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
        this.data = data;
    }
}
