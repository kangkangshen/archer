package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

import java.util.List;

/**
 * 对应于amqp的queue类
 *
 * @author dongyue
 * @date 2020年04月20日18:55:40
 */
public final class Queue extends FeatureBased implements Class{

    @Override
    public int classId() {
        return 50;
    }

    @Override
    public String desc() {
        return "work with queues";
    }

    @Override
    public List<String> methods() {
        return null;
    }

    /**
     * 声明队列
     */
    public class Declare extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "declare queue, create if needed";
        }

        @Override
        public int commandId() {
            return 10;
        }
    }

    public class DeclareOk implements Command{

        @Override
        public String desc() {
            return "confirms a queue definition";
        }

        @Override
        public int commandId() {
            return 11;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 队列与交换器绑定
     */
    public class Bind extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "bind queue to an exchange";
        }

        @Override
        public int commandId() {
            return 20;
        }
    }

    public class BindOk implements Command{

        @Override
        public String desc() {
            return "confirm bind successful";
        }

        @Override
        public int commandId() {
            return 21;
        }

        @Override
        public void execute() {

        }
    }

    /*
     * 队列与交换器解绑
     */
    public class Unbind extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "unbind a queue from an exchange";
        }

        @Override
        public int commandId() {
            return 51;
        }
    }

    public class UnbindOk implements Command{

        @Override
        public String desc() {
            return "confirm unbind successful";
        }

        @Override
        public int commandId() {
            return 51;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 清除队列中的内容
     */
    public class Purge extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "purge a queue";
        }

        @Override
        public int commandId() {
            return 30;
        }
    }

    public class PurgeOk implements Command{

        @Override
        public String desc() {
            return "confirms a queue purge";
        }

        @Override
        public int commandId() {
            return 31;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 删除队列
     */
    public class Delete extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "delete a queue";
        }

        @Override
        public int commandId() {
            return 40;
        }
    }

    public class DeleteOk implements Command{

        @Override
        public String desc() {
            return "confirm deletion of a queue";
        }

        @Override
        public int commandId() {
            return 41;
        }

        @Override
        public void execute() {

        }
    }
}
