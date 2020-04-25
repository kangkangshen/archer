package org.archer.archermq.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext springApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springApplicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return springApplicationContext;
    }
}
