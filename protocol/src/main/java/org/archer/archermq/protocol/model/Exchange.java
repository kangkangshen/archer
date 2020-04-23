package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.protocol.transport.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 对应amqp的exchange类
 *
 * @author dongyue
 * @date 2020年04月20日18:52:39
 */
public final class Exchange extends FeatureBased implements Class {

    private static final Logger logger = LoggerFactory.getLogger("");


    @Override
    public int classId() {
        return 40;
    }

    @Override
    public String desc() {
        return "work with exchanges";
    }


    /*
     * 声明交换器
     */
    public class Declare extends BaseTransactionalCommand<DeclareOk> {

        private String exchange;

        private String type;

        private boolean passive;

        private boolean durable;

        private String noWait;

        private Map<String, Object> arguments;

        @Override
        public String desc() {
            return "verify exchange exists, create if needed";
        }

        @Override
        public int commandId() {
            return 10;
        }

        @Override
        protected DeclareOk executeInternal() {
            return null;
        }
    }

    public class DeclareOk implements Command<Void> {

        @Override
        public String desc() {
            return "confirm exchange declaration";
        }

        @Override
        public int commandId() {
            return 11;
        }

        @Override
        public Void execute() {
            return null;
        }
    }

    /**
     * 删除交换器
     */
    public class Delete extends BaseTransactionalCommand<DeleteOk> {

        @Override
        public String desc() {
            return "delete an exchange";
        }

        @Override
        public int commandId() {
            return 20;
        }

        @Override
        protected DeleteOk executeInternal() {
            return null;
        }
    }

    public class DeleteOk implements Command<Void> {

        @Override
        public String desc() {
            return "confirm deletion of an exchange";
        }

        @Override
        public int commandId() {
            return 21;
        }

        @Override
        public Void execute() {
            return null;
        }
    }


}
