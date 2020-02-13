package org.summerframework.ddd.core;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryWrapper implements BeanFactoryPostProcessor {

    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public synchronized void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (BeanFactoryWrapper.beanFactory == null) {
            BeanFactoryWrapper.beanFactory = beanFactory;
        }
    }

    /**
     * 获取指定关键字的Bean
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return beanFactory.getBean(name, requiredType);
    }

    /**
     * 获取默认的Bean
     */
    public static <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    /**
     * 获取所有注册的Bean
     */
    public static <T> Map<String, T> getBeans(Class<T> requiredType) {
        return ((ListableBeanFactory) beanFactory).getBeansOfType(requiredType);
    }

    /**
     * 获取指定关键字的Bean
     */
    public static Object getBean(String name, Object... args) {
        return beanFactory.getBean(name, args);
    }

}
