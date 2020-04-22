package org.archer.archermq.common.aspects;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 日志记录切面
 * todo dongyue：暂不支持对constructor打日志
 *
 * @author dongyue
 * @date 2020年04月14日19:22:11
 */
@Aspect
@Component
public class LogInspectAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogConstants.SYS_ERR);

    @Pointcut("@annotation(org.archer.archermq.common.annotation.Log)")
    public void logInspectAspect() {
    }

    @SuppressWarnings("unchecked")
    @Before("logInspectAspect()")
    public void logOnBefore(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        LogInfo logInfo = BizLogUtil.start();

        //1.先分析layer和type
        logInfo.setLayer(logAnnotation.layer());
        boolean isSetterMethod = isSetterMethod(method);
        logInfo.setType(isSetterMethod ? LogConstants.CONFIG_CHANGED : LogConstants.METHOD_INVOKE);

        if (LogConstants.CONFIG_CHANGED.equals(logInfo.getType())) {
            //config change
            Field field = Objects.requireNonNull(getSetterField(method));
            boolean prevAccess = field.isAccessible();
            field.setAccessible(true);
            Object configItem = null;
            try {
                configItem = field.get(point.getTarget());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                field.setAccessible(prevAccess);
            }
            logInfo.addContent(LogConstants.CONFIG_ITEM_NAME,methodSignature.getParameterNames()[0]);
            logInfo.addContent(LogConstants.CONFIG_ITEM_BEFORE,Objects.isNull(configItem)? StringUtils.EMPTY:configItem.toString());
            logInfo.addContent(LogConstants.CONFIG_ITEM_AFTER,point.getArgs()[0].toString());
        } else if (LogConstants.METHOD_INVOKE.equals(logInfo.getType())) {
            //method invoke
            logInfo.addContent(LogConstants.CLASS_NAME,method.getDeclaringClass().getName());
            logInfo.addContent(LogConstants.METHOD_NAME,method.getName());
            logInfo.addContent(LogConstants.ARGS_VAL,handleInvokeArgs(methodSignature.getParameterNames(),point.getArgs()));
        }

    }

    @AfterReturning(value = "logInspectAspect()",returning = "result")
    public void logOnAfterReturning(JoinPoint point,Object result){
        LogInfo logInfo = BizLogUtil.get();
        logInfo.setResult(JSON.toJSONString(result));
        BizLogUtil.record(logInfo,logger);
    }

    @AfterThrowing(value = "logInspectAspect()",throwing = "e")
    public void logOnAfterThrowing(JoinPoint point,Throwable e){
        LogInfo logInfo = Objects.requireNonNull(BizLogUtil.get());
        logInfo.setType(LogConstants.EXCEPTION_THROW);
        logInfo.addContent(LogConstants.EXCEPTION_STACK,JSON.toJSONString(e.getStackTrace()));
        BizLogUtil.record(logInfo,logger);
    }





    /**
     * 判定某方法是不是setter方法
     * 0.判断当前方法是不是以set开头
     * 1.判断当前方法是不是仅有一个参数且参数类型为基础数据类型
     * 2.判断当前类是否存在对应的field
     *
     * @param method 当前方法
     */
    private boolean isSetterMethod(Method method) {
        if (Objects.isNull(method)) {
            return false;
        }
        String methodName = method.getName();
        boolean isSetter = methodName.startsWith("set");
        isSetter &= method.getParameterCount() == 1;
        isSetter &= isPrimaryDataType(method.getParameters()[0].getType());

        isSetter &= Objects.nonNull(getSetterField(method));
        return isSetter;
    }

    private boolean isPrimaryDataType(Class<?> clazz) {
        Set<Class<?>> primaryTypes = Sets.newHashSet(
                Boolean.TYPE,
                Byte.TYPE,
                Short.TYPE,
                Character.TYPE,
                Integer.TYPE,
                Long.TYPE,
                Float.TYPE,
                Double.TYPE,
                String.class
        );
        return primaryTypes.contains(clazz);
    }

    private Field getSetterField(Method setterMethod) {
        String methodName = setterMethod.getName();
        try {
            return setterMethod.getDeclaringClass().getField(methodName.substring(3));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String handleInvokeArgs(String[]argNames,Object[] args){
        Map<String,Object> tmpMap = Maps.newHashMap();
        if(Objects.isNull(argNames)){
            return StringUtils.EMPTY;
        }
        for(int i = 0; i<argNames.length;i++){
            tmpMap.put(argNames[i],args[i]);
        }
        return JSON.toJSONString(tmpMap);
    }

}
