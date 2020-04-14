package org.archer.archermq.common.annotation;

import org.archer.archermq.common.log.LogConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * trace记录切面类，当执行关键方法时会追踪调用过程,
 * 默认情况下，会记录调用发生的时间，参数，返回结果，异常
 *
 * @author dongyue
 * @date 2020年04月14日20:47:47
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Trace {
    /**
     * 是否开始记录调用过程，
     *
     * @return 标志是否开始记录
     */
    boolean begin() default false;

    /**
     * 是否结束记录调用过程，生成一条trace记录到指定的存储
     *
     * @return 标志是否结束调用
     */
    boolean end() default false;

    /**
     * 标记当前调用所处层次,包含["model layer","session layer","transport layer"]
     *
     * @return 当前调用所处层次
     */
    String layer() default LogConstants.MODEL_LAYER;

    /**
     * 标记当前调用所属类型,默认是LogConstants.METHOD_INVOKE
     *
     * @see org.archer.archermq.common.log.LogConstants
     * @return 当前调用所属类型
     */
    String type() default LogConstants.METHOD_INVOKE;


}
