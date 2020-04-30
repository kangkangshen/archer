package org.archer.archermq.protocol.model;

import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.archer.archermq.config.register.Registrar;
import org.archer.archermq.protocol.VirtualHost;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.ExchangeTypeEnum;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.transport.BaseExchange;
import org.archer.archermq.protocol.transport.ChannelException;
import org.archer.archermq.protocol.transport.impl.exchange.DirectExchange;
import org.archer.archermq.protocol.transport.impl.exchange.FanoutExchange;
import org.archer.archermq.protocol.transport.impl.exchange.HeadersExchange;
import org.archer.archermq.protocol.transport.impl.exchange.TopicExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 对应amqp的exchange类
 *
 * @author dongyue
 * @date 2020年04月20日18:52:39
 */
public final class Exchange extends FeatureBased implements Class {

    private static final Logger logger = LoggerFactory.getLogger("");
    public static final int classId = 40;


    @Override
    public int classId() {
        return classId;
    }

    @Override
    public String desc() {
        return "work with exchanges";
    }


    /*
     * 声明交换器
     */
    public class Declare extends BaseTransactionalCommand<DeclareOk> {

        private String reserved1;

        private String reserved2;

        private String reserved3;

        private final String exchange;

        private final String type;

        private final boolean passive;

        private final boolean durable;

        private final String noWait;

        private final Map<String, Object> arguments;

        public Declare(String exchange, String type, boolean passive, boolean durable, String noWait, Map<String, Object> arguments) {
            super(classId, 10);
            this.exchange = exchange;
            this.type = type;
            this.passive = passive;
            this.durable = durable;
            this.noWait = noWait;
            this.arguments = arguments;

        }

        @Override
        public String desc() {
            return "verify exchange exists, create if needed";
        }

        @Override
        protected DeclareOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Registrar<String, org.archer.archermq.protocol.Exchange> exchangeRegistrar = virtualHost.getExchangeRegistry();
            if (passive) {
                //If set, the server will reply with Declare­Ok if the exchange already exists with the same name, and raise an error if not.
                Assert.isTrue(exchangeRegistrar.contains(exchange), ExceptionMessages.buildExceptionMsgWithTemplate("current # not contains #", virtualHost.name(), exchange));
                return new DeclareOk();
            } else {
                //构建基础的exchange对象
                BaseExchange newExchange;
                ExchangeTypeEnum exchangeType = ExchangeTypeEnum.valueOf(type.toUpperCase());
                switch (exchangeType){
                    case DIRECT:
                        newExchange = new DirectExchange(exchange,(String) arguments.get(FeatureKeys.Custom.EXCHANGE_ID),durable);
                        break;
                    case TOPIC:
                        newExchange = new TopicExchange(exchange,(String) arguments.get(FeatureKeys.Custom.EXCHANGE_ID),durable);
                        break;
                    case HEADERS:
                        newExchange = new HeadersExchange(exchange,(String) arguments.get(FeatureKeys.Custom.EXCHANGE_ID),durable);
                        break;
                    case FANOUT:
                        newExchange = new FanoutExchange(exchange,(String) arguments.get(FeatureKeys.Custom.EXCHANGE_ID),durable);
                        break;
                    default:
                        throw new UnsupportedOperationException(ExceptionMessages.buildExceptionMsgWithTemplate("# type dont support yet"));
                }
                newExchange.updateCurrState(LifeCyclePhases.Exchange.CREATE, LifeCyclePhases.Status.START);
                //解析特定的路由规则 todo dongyue

                //注册之
                exchangeRegistrar.register(newExchange.exchangeId(), newExchange);
                newExchange.updateCurrState(LifeCyclePhases.Exchange.CREATE, LifeCyclePhases.Status.FINISH);
                return new DeclareOk();
            }
        }

        public String getReserved1() {
            return reserved1;
        }

        public String getReserved2() {
            return reserved2;
        }

        public String getReserved3() {
            return reserved3;
        }

        public String getExchange() {
            return exchange;
        }

        public String getType() {
            return type;
        }

        public boolean isPassive() {
            return passive;
        }

        public boolean isDurable() {
            return durable;
        }

        public String getNoWait() {
            return noWait;
        }

        public Map<String, Object> getArguments() {
            return arguments;
        }
    }

    public class DeclareOk extends BaseCommand<Void> {

        public DeclareOk() {
            super(classId, 11);
        }

        @Override
        public String desc() {
            return "confirm exchange declaration";
        }

        @Override
        public Void execute() {
            throw new ChannelException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /**
     * 删除交换器
     */
    public class Delete extends BaseTransactionalCommand<DeleteOk> {

        String reserved1;

        String exchange;

        boolean ifUnused;

        boolean noWait;

        public Delete(String reserved1, String exchange, boolean ifUnused, boolean noWait) {
            super(classId, 20);
            this.reserved1 = reserved1;
            this.exchange = exchange;
            this.ifUnused = ifUnused;
            this.noWait = noWait;
        }


        @Override
        public String desc() {
            return "delete an exchange";
        }

        @Override
        protected DeleteOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Registrar<String, org.archer.archermq.protocol.Exchange> exchangeRegistrar = virtualHost.getExchangeRegistry();
            if (!exchangeRegistrar.contains(exchange)) {
                return new DeleteOk();
            }
            if (StringUtils.equals(LifeCyclePhases.Exchange.INUSE, exchangeRegistrar.get(exchange).currPhase())) {
                return new DeleteOk();
            }
            exchangeRegistrar.remove(exchange);
            return new DeleteOk();
        }
    }

    public class DeleteOk extends BaseCommand<Void> {

        public DeleteOk() {
            super(classId, 21);
        }

        @Override
        public String desc() {
            return "confirm deletion of an exchange";
        }

        @Override
        public Void execute() {
            throw new ChannelException(ExceptionMessages.ChannelErrors.PRECONDITION_FAILED);
        }
    }



}
