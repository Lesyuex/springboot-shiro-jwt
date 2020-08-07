package com.jobeth.perm.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author Jobeth
 * @date Created by IntelliJ IDEA on 11:34 2020/4/10
 */
public class JsonUtil {

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("username", "123");
        map.put("username2", "");
        map.put("testNull", null);
        System.out.println(objectToJson(map));
    }

    /**
     * 将对象转换成json字符串。
     *
     * @param data data
     * @return String
     */
    public static String objectToJson(Object data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return T
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonData, beanType);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将json数据转换成pojo对象list
     *
     * @param json     json
     * @param beanType beanType
     * @return List<beanType>
     */
    public static <T> List<T> jsonToList(String json, Class<T> beanType) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            return null;
        }
    }
}

