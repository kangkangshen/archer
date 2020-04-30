package org.archer.archermq.config;

import org.archer.archermq.common.Namespace;
import org.archer.archermq.common.utils.NameSpaceUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class CommonConfigs {


    @Bean
    public ConversionService conversionService(){
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(String.class, Namespace.class, NameSpaceUtil::namespace);
        return conversionService;
    }
}
