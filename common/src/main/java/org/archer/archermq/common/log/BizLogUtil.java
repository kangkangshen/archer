package org.archer.archermq.common.log;


import com.alibaba.fastjson.JSON;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Objects;

/**
 * 本应用逻辑日志记录工具类
 * todo dongyue
 *
 * @author dongyue
 * @date 2020年04月14日17:09:47
 */
public class BizLogUtil {
    private final static ThreadLocal<LogInfo> logInfoHolder = new ThreadLocal<>();

    public static LogInfo start() {
        LogInfo logInfo = logInfoHolder.get();
        if (Objects.isNull(logInfo)) {
            logInfo = new LogInfo();
            logInfoHolder.set(logInfo);
        }
        logInfo.setCreateTime(new Date());
        return logInfo;
    }

    public static LogInfo setLayer(String layer) {
        LogInfo logInfo = logInfoHolder.get();
        if (Objects.isNull(logInfo)) {
            logInfo = new LogInfo();
            logInfoHolder.set(logInfo);
        }
        logInfo.setLayer(layer);
        return logInfo;
    }


    public static LogInfo setContent(String content) {
        LogInfo logInfo = logInfoHolder.get();
        if (Objects.isNull(logInfo)) {
            logInfo = new LogInfo();
            logInfoHolder.set(logInfo);
        }
        logInfo.setContent(content);
        return logInfo;
    }

    public static void end() {
        logInfoHolder.get().write();
        logInfoHolder.remove();
    }

    public static void recordException(Throwable e) {
        LogInfo logInfo = logInfoHolder.get();
        if (Objects.isNull(logInfo)) {
            logInfo = new LogInfo();
            logInfo.setCreateTime(new Date());
            logInfoHolder.set(logInfo);
        }
        logInfo.setType(LogConstants.EXCEPTION_THROW);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw,true);
        e.printStackTrace(pw);
        String stackTraceString = sw.getBuffer().toString();
        logInfo.addContent(LogConstants.EXCEPTION_STACK, stackTraceString);
    }

    public static void recordInstanceCreated(Object o){
        LogInfo logInfo = logInfoHolder.get();
        if (Objects.isNull(logInfo)) {
            logInfo = new LogInfo();
            logInfo.setCreateTime(new Date());
            logInfoHolder.set(logInfo);
        }

        logInfo.setType(LogConstants.INSTANCE_CREATED);
        logInfo.addContent(LogConstants.INSTANCE,o.toString());

    }

    public static void recordInstanceDestroyed(Object o){
        LogInfo logInfo = logInfoHolder.get();
        if (Objects.isNull(logInfo)) {
            logInfo = new LogInfo();
            logInfo.setCreateTime(new Date());
            logInfoHolder.set(logInfo);
        }

        logInfo.setType(LogConstants.INSTANCE_DESTROYED);
        logInfo.addContent(LogConstants.INSTANCE,o.toString());
    }

    public static LogInfo get() {
        return logInfoHolder.get();
    }

}
