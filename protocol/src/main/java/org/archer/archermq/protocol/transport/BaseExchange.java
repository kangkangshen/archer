package org.archer.archermq.protocol.transport;

import com.google.common.collect.Lists;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.register.DistributedRegistrar;
import org.archer.archermq.protocol.register.Registrar;
import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Exchange;
import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.ExchangeTypeEnum;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public abstract class BaseExchange extends BaseLifeCycleSupport implements Exchange {

    private final String name;

    private final ExchangeTypeEnum exchangeType;

    private final String exchangeId;

    private final boolean durable;

    protected final Registrar<String/*bindingKey*/, List<MessageQueue>> msgQueueRegistry = new DistributedRegistrar<>();

    public BaseExchange(String name, Integer rawType) {
        this(name,rawType,name,false);
    }

    public BaseExchange(String name, Integer rawType, String exchangeId,boolean durable) {
        this.name = name;
        this.exchangeId = exchangeId;
        this.durable = durable;
        ExchangeTypeEnum exchangeType = ExchangeTypeEnum.getByVal(rawType);
        Assert.notNull(exchangeType, ExceptionMessages.buildExceptionMsgWithTemplate("exchangeType # not support or wrong.", String.valueOf(rawType)));
        this.exchangeType = exchangeType;
        updateCurrState(LifeCyclePhases.Exchange.CREATE,LifeCyclePhases.Status.FINISH);
        triggerEvent();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ExchangeTypeEnum type() {
        return exchangeType;
    }

    @Override
    public String exchangeId() {
        return exchangeId;
    }

    public boolean isDurable() {
        return durable;
    }

    @Override
    public void acceptMsgQueueBinding(MessageQueue msgQueue, String routingKey) {
        BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.INSTANCE_REGISTER);
        try{
            if(CollectionUtils.isEmpty(msgQueueRegistry.get(routingKey))){
                msgQueueRegistry.register(routingKey, Lists.newArrayList(msgQueue));
            }else{
                List<MessageQueue> msgQueues = Lists.newArrayList(msgQueueRegistry.get(routingKey));
                msgQueues.add(msgQueue);
                msgQueueRegistry.register(routingKey,msgQueues);
            }

        }catch (Exception e){
            BizLogUtil.recordException(e);
            msgQueueRegistry.remove(routingKey);
        }
        finally {
            BizLogUtil.end();
        }
    }

    @Override
    public void discardMsgQueueBinding(MessageQueue msgQueue, String routingKey) {
        BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.INSTANCE_REGISTER);
        try{
            List<MessageQueue> preRegistered = msgQueueRegistry.get(routingKey);
            if(!CollectionUtils.isEmpty(preRegistered)){
                preRegistered.remove(msgQueue);
            }
        }catch (Exception e){
            BizLogUtil.recordException(e);
        }
        finally {
            BizLogUtil.end();
        }
    }

    @Override
    public void exchangeMsg(Message msg, String routingKey) {
        //check param valid
        if(Objects.nonNull(msg)||msgQueueRegistry.contains(routingKey)){
            exchangeMsgInternal(msg,routingKey);
        }
    }

    protected abstract void exchangeMsgInternal(Message message, String routingKey);

}
