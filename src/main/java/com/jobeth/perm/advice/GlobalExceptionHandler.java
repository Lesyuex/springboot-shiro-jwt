package com.jobeth.perm.advice;

import com.jobeth.perm.common.enums.ResultEnum;
import com.jobeth.perm.common.exception.ServerException;
import com.jobeth.perm.vo.JsonResultVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/4/30 16:12
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /***
     * 自定义异常
     * @return ResultVO
     */
    @ExceptionHandler(Exception.class)
    public JsonResultVO<Object> exception(Exception e) {
        if (e instanceof ServerException) {
            ServerException serverException = (ServerException) e;
            ResultEnum resultEnum = serverException.getResultEnum();
            return new JsonResultVO<>(resultEnum);
        } else if (e instanceof NoHandlerFoundException) {
            return new JsonResultVO<>(ResultEnum.CHECK_REQUEST_URL);
        } else {
            return new JsonResultVO<>(ResultEnum.INTERNAL_SERVER_ERROR);
        }
    }

}