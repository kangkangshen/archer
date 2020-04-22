package org.archer.archermq.common.aspects;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.annotation.Trace;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.archer.archermq.common.utils.HashUtil;
import org.archer.archermq.common.utils.TraceUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * 简单的轨迹记录切面实现
 *
 * @author dongyue
 * @date 2020年04月14日19:28:34
 */
@Aspect
@Component
public class TraceInspectAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogConstants.TRACE);


    @Pointcut("@annotation(org.archer.archermq.common.annotation.Trace)")
    public void traceInspectAspect(){
    }

    @Before("traceInspectAspect()")
    public void traceOnBefore(JoinPoint point){

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method currMethod = methodSignature.getMethod();
        Trace currTrace = currMethod.getDeclaredAnnotation(Trace.class);

        LogInfo logInfo = currTrace.begin()?BizLogUtil.start():BizLogUtil.get();

        //1. layer,type 分析
        logInfo.setLayer(currTrace.layer());
        logInfo.setType(currTrace.type());


        //2. class,method 分析
        logInfo.addContent(LogConstants.CLASS_NAME,currMethod.getDeclaringClass().getName());
        logInfo.addContent(LogConstants.METHOD_NAME,currMethod.getName());

        //3. args分析
        logInfo.addContent(LogConstants.ARGS_VAL,handleInvokeArgs(methodSignature.getParameterNames(),point.getArgs()));


        //4.根据调用信息，生成traceId
        logInfo.setTraceId(TraceUtil.getTraceInfo());

    }

    @AfterReturning(value = "traceInspectAspect()",returning = "result")
    public void traceOnAfterReturning(JoinPoint point,Object result){
        //批量更新至数据库等存储设置
        LogInfo logInfo = Objects.requireNonNull(BizLogUtil.get());
        logInfo.setResult(JSON.toJSONString(result));
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method currMethod = methodSignature.getMethod();
        Trace currTrace = currMethod.getDeclaredAnnotation(Trace.class);
        if(currTrace.end()){
            BizLogUtil.record(logInfo,logger);
        }
    }

    @AfterThrowing(value = "traceInspectAspect()",throwing = "e")
    public void traceOnAfterThrowing(JoinPoint point, Exception e){
        LogInfo logInfo = Objects.requireNonNull(BizLogUtil.get());
        logInfo.setType(LogConstants.EXCEPTION_THROW);
        logInfo.addContent(LogConstants.EXCEPTION_STACK,JSON.toJSONString(e.getStackTrace()));
        BizLogUtil.record(logInfo,logger);
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
