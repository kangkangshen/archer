package org.archer.archermq.config.cfgcenter.impl;

import org.archer.archermq.config.cfgcenter.annotations.ConfigInstance;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConfigInstanceInspector implements BeanPostProcessor {

    @Autowired
    private ConfigReLoader configReLoader;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazzType = bean.getClass();
        ConfigInstance configInstanceAnnotation = clazzType.getAnnotation(ConfigInstance.class);
        if(Objects.nonNull(configInstanceAnnotation)){

        }

        return bean;
    }
}
