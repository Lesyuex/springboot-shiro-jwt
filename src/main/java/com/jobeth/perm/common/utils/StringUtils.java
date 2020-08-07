package com.jobeth.perm.common.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * String字符串工具类
 *
 * @author JyrpoKoo
 * @version [版本号 2019/8/8]
 * @date Created by IntelliJ IDEA in 2019/8/8 13:03
 */
public class StringUtils {


    /**
     * 为空对象或空字符串
     *
     * @param val 对象
     * @return boolean值
     */
    public static boolean isNullOrEmpty(String val) {
        return val == null || val.length() == 0;
    }

    /**
     * 不为空对象并且不为空字符串
     *
     * @param val 对象
     * @return boolean值
     */
    public static boolean notNullAndEmpty(String val) {
        return !isNullOrEmpty(val);
    }

    /**
     * 返回字符串，如果为空对象或空字符串，返回默认值defaultValue
     *
     * @param val          对象
     * @param defaultValue 默认值
     * @return 字符串
     */
    public static String getValue(String val, String defaultValue) {
        return isNullOrEmpty(val) ? defaultValue : val;
    }

    /**
     * 判断是否为正整数
     *
     * @param val val
     * @return boolean
     */
    public static boolean isNumber(String val) {
        if (isNullOrEmpty(val)) {
            return false;
        }
        for (int i = 0; i < val.length(); i++) {
            if (!Character.isDigit(val.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将首字符转换为小写
     *
     * @param val val
     * @return String
     */
    public static String toLowerCaseFirstOne(String val) {
        if (isNullOrEmpty(val)) {
            return val;
        } else {
            if (Character.isLowerCase(val.charAt(0))) {
                return val;
            } else {
                return Character.toLowerCase(val.charAt(0)) + val.substring(1);
            }
        }
    }

    /**
     * 将首字符转换为大写
     *
     * @param val val
     * @return String
     */
    public static String toUpperCaseFirstOne(String val) {
        if (isNullOrEmpty(val)) {
            return val;
        } else {
            if (Character.isUpperCase(val.charAt(0))) {
                return val;
            } else {
                return Character.toUpperCase(val.charAt(0)) + val.substring(1);
            }
        }
    }


    /**
     * 字符串转换成驼峰格式（如:user_name => userName）
     *
     * @param param 原字符串
     * @return 驼峰格式字符串
     */
    public static String toJavaField(String param) {
        if (isNullOrEmpty(param)) {
            return param;
        }
        StringBuilder sb = new StringBuilder();
        int len = param.length();
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 字符串转换成固定格式（如:userName => USER_NAME ）
     *
     * @param s 字段名
     * @return 字符串
     */
    public static String toDataBaseField(String s) {
        if (isNullOrEmpty(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        //转换格式
        for (int i = 0; i < len; i++) {
            if (Character.isUpperCase(s.charAt(i))) {
                sb.append("_");
            }
            sb.append(Character.toUpperCase(s.charAt(i)));
        }
        return sb.toString();
    }


    /**
     * 将键值对的字符串  x=1&key=value  转为Map<String,String>的Map类型数据
     *
     * @param param 键值对字符串
     * @return Map<String, String>
     */
    public static Map<String, String> keyValueStrToMap(String param) {
        Map<String, String> map = new HashMap<>(16);
        if (param != null) {
            //切割字符串
            String[] str = param.split("&");
            //取key和value存入Map中
            for (String s : str) {
                String value = "";
                String[] a = s.split("=");
                String key = a[0] + "";
                if (a.length > 1) {
                    value += a[1];
                }
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 将Map<String, String>转为http请求表单参数，
     *
     * @param map map
     * @return 表单参数
     */
    public static String generateUrlParams(Map<String, String> map) {
        String params = "";
        if (map != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuilder.append(entry.getKey());
                stringBuilder.append("=");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("&");
            }
            params = stringBuilder.toString();
            params = params.substring(0, params.length() - 1);
        }
        return params;
    }

    /**
     * 将Map<String, String>转为用URLEncoder对参数进行编码的http请求表单参数，
     *
     * @param map map
     * @return 表单参数
     * @throws UnsupportedEncodingException 异常
     */
    public static String generateUrlEncoderParam(Map<String, String> map) throws UnsupportedEncodingException {
        String params = null;
        if (map != null) {
            StringBuilder query = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                query.append(entry.getKey());
                query.append('=');
                query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                query.append('&');
            }
            params = query.toString();
            params = params.substring(0, params.length() - 1);
        }
        return params;
    }

    /**
     * 根据字段名返回set方法(如：userName => setUserName)
     *
     * @param filedName 字段名
     * @return set方法
     */
    public static String methodNameSet(String filedName) {
        //将字段名的首字符换成大写
        return "set" + filedName.replaceFirst(filedName.substring(0, 1), filedName.substring(0, 1).toUpperCase());
    }

    /**
     * 根据字段名返回get方法(如：userName => getUserName)
     *
     * @param filedName 字段名
     * @return get方法
     */
    public static String methodNameGet(String filedName) {
        //将字段名的首字符换成大写
        return "get" + filedName.replaceFirst(filedName.substring(0, 1), filedName.substring(0, 1).toUpperCase());
    }

}
