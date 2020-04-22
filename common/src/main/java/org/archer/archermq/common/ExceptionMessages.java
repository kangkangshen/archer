package org.archer.archermq.common;


import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 异常信息常量类
 *
 * @author dongyue
 * @date 2020年04月14日16:37:08
 */
public class ExceptionMessages {


    /**
     * 基于占位符的异常信息生成方法
     *
     * @param exceptionMsgTemplate 带占位符的异常信息模板，使用#作为占位符
     * @param msg                  用于替换占位符的值
     * @return 新的异常信息信息
     * @throws IllegalArgumentException msg.length和exceptionMsgTemplate占位符个数不匹配或者exceptionMsgTemplate
     *                                  为blank,或者msg为empty
     */
    public static String buildExceptionMsgWithTemplate(String exceptionMsgTemplate, String... msg) {
        if (StringUtils.isNotBlank(exceptionMsgTemplate) && msg.length > 0) {
            String[] splits = exceptionMsgTemplate.split("#");
            if (splits.length == msg.length + 1) {
                StringBuilder stringBuilder = new StringBuilder(splits[0]);
                for (int i = 0; i < msg.length; i++) {
                    stringBuilder.append(msg[i]).append(splits[i + 1]);
                }
                return stringBuilder.toString();
            }
            throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException();
    }

}
