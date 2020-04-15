package org.archer.archermq.common.annotation;


import org.archer.archermq.common.log.LogConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 *
 * 当在方法上添加该注解时，
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
public @interface Log {

    String layer() default LogConstants.MODEL_LAYER;

}
