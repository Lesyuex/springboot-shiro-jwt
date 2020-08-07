package com.jobeth.perm.common.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/8/5 9:50
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {

    /**
     * Spring上下文
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHelper.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <B> B getBean(String beanName) {
        return (B) applicationContext.getBean(beanName);
    }

    public static <B> B getBean(Class<B> clazz) {
        return applicationContext.getBean(clazz);
    }
}
