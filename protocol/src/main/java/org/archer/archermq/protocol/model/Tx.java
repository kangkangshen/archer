package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 对应amqp的tx类
 * The Tx class allows publish and ack operations to be batched into atomic units of work.
 * The intention is that all publish and ack requests issued within a transaction will complete successfully or none of them will.
 * Servers SHOULD implement atomic transactions at least where all publish or ack requests affect a single queue.
 * Transactions that cover multiple queues may be non­atomic, given that queues can be created and destroyed asynchronously, and such events do not form part of any transaction.
 * Further, the behaviour of transactions with respect to the immediate and mandatory flags on Basic.Publish methods is not defined.
 */
public final class Tx extends FeatureBased implements Class {

    private static final int classId = 90;

    @Override
    public int classId() {
        return classId;
    }

    @Override
    public String desc() {
        return "work with transactions";
    }

    /**
     * 开启事务
     */
    public class Select extends BaseTransactionalCommand<SelectOk> {

        @Override
        public String desc() {
            return "select standard transaction mode";
        }

        @Override
        public int commandId() {
            return 10;
        }

        @Override
        protected SelectOk executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            channel.beginTx();
            return new SelectOk();
        }
    }

    public class SelectOk extends BaseCommand<Void> {

        @Override
        public String desc() {
            return "confirm transaction mode";
        }

        @Override
        public int commandId() {
            return 11;
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /**
     * 事务提交
     */
    public class Commit extends BaseTransactionalCommand<CommitOk> {

        @Override
        public String desc() {
            return "commit the current transaction";
        }

        @Override
        public int commandId() {
            return 20;
        }

        @Override
        protected CommitOk executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            channel.commit();
            return new CommitOk();
        }
    }

    public class CommitOk extends BaseCommand<Void> {

        @Override
        public String desc() {
            return "confirm a successful commit";
        }

        @Override
        public int commandId() {
            return 21;
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.NOT_IMPLEMENTED);
        }
    }

    /**
     * 事务回滚
     */
    public class Rollback extends BaseTransactionalCommand<RollbackOk> {

        @Override
        public String desc() {
            return "abandon the current transaction";
        }

        @Override
        public int commandId() {
            return 30;
        }

        @Override
        protected RollbackOk executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            channel.rollback();
            return new RollbackOk();
        }
    }

    public class RollbackOk extends BaseCommand<Void> {

        @Override
        public String desc() {
            return "confirm successful rollback";
        }

        @Override
        public int commandId() {
            return 31;
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.NOT_IMPLEMENTED);
        }
    }

    static {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        MethodResolver methodResolver = context.getBean(MethodResolver.class);
        methodResolver.register(classId, 31);
        methodResolver.register(classId, 30);
        methodResolver.register(classId, 21);
        methodResolver.register(classId, 20);
        methodResolver.register(classId, 11);
        methodResolver.register(classId, 10);
    }
}
