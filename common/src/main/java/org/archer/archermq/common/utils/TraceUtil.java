package org.archer.archermq.common.utils;


import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class TraceUtil {

    private static final ThreadLocal<String> traceInfoHolder = new ThreadLocal<>();


    public static String generateTraceId(){
        return null;
    }

    public static void saveThraceInfo(String traceId){
        traceInfoHolder.set(traceId);
    }

    public static String getThreadInfo(){
        return StringUtils.isBlank(traceInfoHolder.get())?generateTraceId():traceInfoHolder.get();
    }

    public static void removeThraceInfo(){
        traceInfoHolder.remove();
    }

}
