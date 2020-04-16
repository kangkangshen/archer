package org.archer.archermq.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;

/**
 *
 */
public class TraceUtil {

    private static final ThreadLocal<String> traceInfoHolder = new ThreadLocal<>();


    /**
     * 基于当前进程id+线程id,调用类+方法，时间，amqp前缀,使用md5生成traceId
     * 原则上，以上基本元素已经足够
     *
     * @return 本次方法调用的traceID
     */
    public static String generateTraceId() {
        StringBuilder stringbuilder = new StringBuilder("amqp.server.impl-archermq");
        //当前进程
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        stringbuilder.append(Optional.ofNullable(runtimeMXBean).map(RuntimeMXBean::getName).orElse(StringUtils.EMPTY));
        //调用线程
        Thread thread = Thread.currentThread();
        stringbuilder.append(Thread.currentThread().getId());

        //这里，generateTraceId方法被调用至少会存在两层方法调用(main+...+generateTraceId)
        String invokedClassName = thread.getStackTrace()[2].getClassName();
        String invokedMethodName = thread.getStackTrace()[2].getMethodName();
        stringbuilder.append(invokedClassName).append(invokedMethodName);

        //当前时间
        stringbuilder.append(System.currentTimeMillis());

        return DigestUtils.md5DigestAsHex(stringbuilder.toString().getBytes());

    }

    public static void saveThraceInfo(String traceId) {
        traceInfoHolder.set(traceId);
    }

    public static String getTraceInfo() {
        return StringUtils.isBlank(traceInfoHolder.get()) ? generateTraceId() : traceInfoHolder.get();
    }

    public static void removeThraceInfo() {
        traceInfoHolder.remove();
    }

}
