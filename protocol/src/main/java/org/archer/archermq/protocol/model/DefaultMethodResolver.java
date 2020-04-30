package org.archer.archermq.protocol.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.constants.Delimiters;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Connection;
import org.archer.archermq.protocol.Server;
import org.archer.archermq.protocol.VirtualHost;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.transport.ChannelException;
import org.archer.archermq.protocol.transport.StandardMethodFrame;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class DefaultMethodResolver implements MethodResolver, ApplicationContextAware,InitializingBean {

    private final Map<String, java.lang.Class<? extends Command<?>>> methodRegistry = Maps.newConcurrentMap();

    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private ApplicationContext springContext;

    @Override
    public boolean support(int classId, int methodId) {
        return methodRegistry.containsKey(generateKey(classId, methodId));
    }

    private String generateKey(int classId, int methodId) {
        return StringUtils.EMPTY + classId + Delimiters.COLON + methodId;
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
            injectContext(clazzInstance, methodFrame);


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
    private void injectContext(Object clazzInstance, StandardMethodFrame rawMethodFrame) {
        if (clazzInstance instanceof FeatureBased) {
            FeatureBased clazz = (FeatureBased) clazzInstance;
            //1.注入当前server
            clazz.addFeature(FeatureKeys.Command.SERVER, springContext.getBean(Server.class));

            //2.注入当前的conn,channel,virtualHost,连接新建立的时候可能还没有conn,channel
            Optional<Channel> channel = Optional.ofNullable(rawMethodFrame.channel());
            Optional<Connection> connection = channel.map(Channel::conn);
            Optional<VirtualHost> virtualHost = connection.map(Connection::virtualHost);
            clazz.addFeature(FeatureKeys.Command.AMQP_CONNECTION, connection.orElse(null));
            clazz.addFeature(FeatureKeys.Command.AMQP_CHANNEL, channel.orElse(null));
            clazz.addFeature(FeatureKeys.Command.VIRTUALHOST, virtualHost.orElse(null));
            clazz.addFeature(FeatureKeys.Command.VIRTUALHOST_NAME, virtualHost.map(VirtualHost::name).orElse(StringUtil.EMPTY_STRING));

            //3.注入当前的tcpChannel,ip,port等信息
            io.netty.channel.Channel tcpChannel = rawMethodFrame.tcpChannel();
            clazz.addFeature(FeatureKeys.Command.TCP_CHANNEL, tcpChannel);
            InetSocketAddress inetAddress = (InetSocketAddress) tcpChannel.remoteAddress();
            clazz.addFeature(FeatureKeys.Command.PEER_IP, inetAddress.getHostName());
            clazz.addFeature(FeatureKeys.Command.PEER_PORT, inetAddress.getPort());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //basic class
        this.register(Basic.classId, 111, Basic.RecoverOk.class);
        this.register(Basic.classId, 100, Basic.RecoverAsync.class);
        this.register(Basic.classId, 110, Basic.Recover.class);
        this.register(Basic.classId, 90, Basic.Reject.class);
        this.register(Basic.classId, 80, Basic.Ack.class);
        this.register(Basic.classId, 72, Basic.GetEmpty.class);
        this.register(Basic.classId, 71, Basic.GetOk.class);
        this.register(Basic.classId, 70, Basic.Get.class);
        this.register(Basic.classId, 60, Basic.Deliver.class);
        this.register(Basic.classId, 50, Basic.Return.class);
        this.register(Basic.classId, 40, Basic.Publish.class);
        this.register(Basic.classId, 31, Basic.CancelOk.class);
        this.register(Basic.classId, 30, Basic.Cancel.class);
        this.register(Basic.classId, 21, Basic.ConsumeOk.class);
        this.register(Basic.classId, 20, Basic.Consume.class);
        this.register(Basic.classId, 11, Basic.QosOk.class);
        this.register(Basic.classId, 10, Basic.Qos.class);

        //channel class
        this.register(org.archer.archermq.protocol.model.Channel.classId, 41, org.archer.archermq.protocol.model.Channel.CloseOk.class);
        this.register(org.archer.archermq.protocol.model.Channel.classId, 40, org.archer.archermq.protocol.model.Channel.Close.class);
        this.register(org.archer.archermq.protocol.model.Channel.classId, 21, org.archer.archermq.protocol.model.Channel.FlowOk.class);
        this.register(org.archer.archermq.protocol.model.Channel.classId, 20, org.archer.archermq.protocol.model.Channel.Flow.class);
        this.register(org.archer.archermq.protocol.model.Channel.classId, 11, org.archer.archermq.protocol.model.Channel.OpenOk.class);
        this.register(org.archer.archermq.protocol.model.Channel.classId, 10, org.archer.archermq.protocol.model.Channel.Open.class);


        //connection class
        this.register(org.archer.archermq.protocol.model.Connection.classId, 51, org.archer.archermq.protocol.model.Connection.CloseOk.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 50, org.archer.archermq.protocol.model.Connection.Close.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 41, org.archer.archermq.protocol.model.Connection.OpenOk.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 40, org.archer.archermq.protocol.model.Connection.Open.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 31, org.archer.archermq.protocol.model.Connection.TuneOk.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 30, org.archer.archermq.protocol.model.Connection.Tune.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 21, org.archer.archermq.protocol.model.Connection.SecureOk.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 20, org.archer.archermq.protocol.model.Connection.Secure.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 11, org.archer.archermq.protocol.model.Connection.StartOk.class);
        this.register(org.archer.archermq.protocol.model.Connection.classId, 10, org.archer.archermq.protocol.model.Connection.Start.class);

        //exchange class
        this.register(Exchange.classId, 21, Exchange.DeleteOk.class);
        this.register(Exchange.classId, 20, Exchange.Delete.class);
        this.register(Exchange.classId, 11, Exchange.DeclareOk.class);
        this.register(Exchange.classId, 10, Exchange.Declare.class);

        //queue class
        this.register(Queue.classId, 51, Queue.UnbindOk.class);
        this.register(Queue.classId, 50, Queue.Unbind.class);
        this.register(Queue.classId, 41, Queue.DeleteOk.class);
        this.register(Queue.classId, 31, Queue.PurgeOk.class);
        this.register(Queue.classId, 30, Queue.Purge.class);
        this.register(Queue.classId, 21, Queue.BindOk.class);
        this.register(Queue.classId, 20, Queue.Bind.class);
        this.register(Queue.classId, 11, Queue.DeclareOk.class);
        this.register(Queue.classId, 10, Queue.Declare.class);

        this.register(Tx.classId, 31, Tx.RollbackOk.class);
        this.register(Tx.classId, 30, Tx.Rollback.class);
        this.register(Tx.classId, 21, Tx.CommitOk.class);
        this.register(Tx.classId, 20, Tx.Commit.class);
        this.register(Tx.classId, 11, Tx.SelectOk.class);
        this.register(Tx.classId, 10, Tx.Select.class);

    }
}
