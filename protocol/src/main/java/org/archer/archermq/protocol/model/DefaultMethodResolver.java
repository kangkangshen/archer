package org.archer.archermq.protocol.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.archer.archermq.common.constants.Delimiters;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.transport.ChannelException;
import org.archer.archermq.protocol.transport.StandardMethodFrame;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class DefaultMethodResolver implements MethodResolver, ApplicationContextAware {

    private final Map<String, java.lang.Class<? extends Command<?>>> methodRegistry = Maps.newConcurrentMap();

    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private ApplicationContext springContext;

    @Override
    public boolean support(int classId, int methodId) {
        return methodRegistry.containsKey(generateKey(classId, methodId));
    }

    private String generateKey(int classId, int methodId) {
        return classId + Delimiters.COLON + methodId;
    }

    @Override
    public void register(int classId, int methodId, java.lang.Class<? extends Command<?>> methodType) {
        methodRegistry.put(generateKey(classId, methodId), methodType);
    }

    @Override
    public Command<?> route(StandardMethodFrame methodFrame) {
        Assert.notNull(methodFrame, ExceptionMessages.buildExceptionMsgWithTemplate("methodFrame is null"));
        java.lang.Class<? extends Command<?>> commandClazz = methodRegistry.get(generateKey(methodFrame.getRawClassId(), methodFrame.getRawMethodId()));
        Assert.notNull(commandClazz, "commandClazz is null");
        String commandClazzName = commandClazz.getName();
        String[] clazzNames = StringUtils.split(commandClazzName, Delimiters.DOLLAR);
        Assert.isTrue(Objects.nonNull(clazzNames) && clazzNames.length == 2, "cannot parse the command class");
        String classClazzName = clazzNames[0];

        try {
            java.lang.Class<?> classClazz = java.lang.Class.forName(classClazzName);
            Object clazzInstance = classClazz.newInstance();
            injectContext(clazzInstance);


            Constructor<?> targetCmdConstructor = null;
            String[] targetCmdConstructorArgNames = null;
            Object[] targetCmdConstructorArgs = null;
            Constructor<?>[] constructors = commandClazz.getConstructors();
            Map<String, Object> frameArgs = methodFrame.getArgs();
            frameArgs.put("this$0", clazzInstance);
            Set<String> frameArgNames = frameArgs.keySet();
            for (Constructor<?> constructor : constructors) {
                String[] tmp = parameterNameDiscoverer.getParameterNames(constructor);
                //内部类默认添加了this$0这个默认的外部类成员变量因此tmp必不为空 todo dongyue
                Set<String> cmdArgNames = Sets.newHashSet(tmp);

                if (cmdArgNames.equals(frameArgNames)) {
                    //因为使用的可能是json传输的字符串表，可能参数顺序会打乱，需要重新组织参数顺序
                    targetCmdConstructor = constructor;
                    targetCmdConstructorArgNames = tmp;
                    targetCmdConstructorArgs = new Object[tmp.length];
                    for (int i = 0; i < targetCmdConstructorArgNames.length; i++) {
                        targetCmdConstructorArgs[i] = frameArgs.get(targetCmdConstructorArgNames[i]);
                    }
                    break;
                }
            }
            if (Objects.isNull(targetCmdConstructor)) {
                throw new NullPointerException("cannot find suitable constructor");
            }
            return (Command<?>) targetCmdConstructor.newInstance(targetCmdConstructorArgs);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ChannelException(ExceptionMessages.SystemErrors.INSTANTIATION_ERR);
        }
    }

    /**
     * 注入上下文信息
     *
     * @param clazzInstance 当前command所属的类的实例，用以充当该command的上下文信息库
     */
    private void injectContext(Object clazzInstance) {


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;
    }
}
