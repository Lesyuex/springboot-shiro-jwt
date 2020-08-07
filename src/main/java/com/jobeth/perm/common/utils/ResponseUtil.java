package com.jobeth.perm.common.utils;

import com.jobeth.perm.common.enums.ResultEnum;
import com.jobeth.perm.vo.JsonResultVO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author JyrpoKoo
 * @version [版本号 2019/10/25]
 * @date Created by IntelliJ IDEA on 14:59 2019/10/25
 */
@Slf4j
public class ResponseUtil {
    private ResponseUtil() {
    }

    /**
     * 根据枚举类型输出结果
     *
     * @param response   response
     * @param resultEnum resultEnum
     */
    public static void writeJson(ServletResponse response, ResultEnum resultEnum) {
        JsonResultVO<Object> result = buildResult(resultEnum);
        writeJson(response, result);
    }

    /**
     * 响应Json数据
     *
     * @param response response
     * @param object   object
     */
    public static void writeJson(ServletResponse response, Object object) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JsonUtil.objectToJson(object) + "");
            log.info("【 ResponseJson - {} 】", JsonUtil.objectToJson(object));
        } catch (Exception e) {
            log.error("【 ResponseJson - 发生错误- {} 】", e.getMessage());
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 操作正常结果
     *
     * @return 操作正常结果
     */
    public static <T> JsonResultVO<T> success() {
        return buildResult(ResultEnum.NO_ERROR);
    }

    /**
     * 根据枚举类型输出结果
     *
     * @return 结果
     */
    public static <T> JsonResultVO<T> buildResult(ResultEnum resultEnum, T data) {
        JsonResultVO<T> jsonResultVO = new JsonResultVO<>();
        jsonResultVO.setCode(resultEnum.getCode());
        jsonResultVO.setMsg(resultEnum.getMessage());
        jsonResultVO.setData(data);
        return jsonResultVO;
    }

    /**
     * 操作失败结果
     *
     * @return 操作失败结果
     */
    public static <T> JsonResultVO<T> fail() {
        return fail(ResultEnum.ERROR);
    }

    /**
     * 根据枚举类型输出结果
     *
     * @return 结果
     */
    public static <T> JsonResultVO<T> buildResult(ResultEnum resultEnum) {
        return buildResult(resultEnum, null);
    }

    /**
     * 操作失败结果
     *
     * @return 操作失败结果
     */
    public static <T> JsonResultVO<T> fail(ResultEnum resultEnum) {
        return buildResult(resultEnum);
    }


}



