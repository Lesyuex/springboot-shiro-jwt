package com.jobeth.perm;

import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class PermApplicationTests {


    public static void main(String[] args) {

        PatternMatcher patternMatcher = new AntPathMatcher();
        boolean matches = patternMatcher.matches("/sys/**", "/sys/test");
        System.out.println(matches);
        System.out.println(patternMatcher.matches("/sys/**", "/sys/test/get/1"));


        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        Set<String> strings = map.keySet();
        for (String string : strings) {
            System.out.println(string + " => " + map.get(string));
        }


        map.forEach((key, value) -> {
            System.out.println(key + " => " + value);
        });


        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {

            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println(key + " => " + value);
        }
    }
}
