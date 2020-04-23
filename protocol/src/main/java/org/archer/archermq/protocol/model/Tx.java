package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

import java.util.List;

/**
 * 对应amqp的tx类
 */
public final class Tx extends FeatureBased implements Class{

    @Override
    public int classId() {
        return 90;
    }

    @Override
    public String desc() {
        return "work with transactions";
    }

    @Override
    public List<String> methods() {
        return null;
    }

    /**
     * 开启事务
     */
    public class Select extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "select standard transaction mode";
        }

        @Override
        public int commandId() {
            return 10;
        }
    }

    public class SelectOk implements Command {

        @Override
        public String desc() {
            return "confirm transaction mode";
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
     * 事务提交
     */
    public class Commit extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "commit the current transaction";
        }

        @Override
        public int commandId() {
            return 20;
        }
    }

    public class CommmitOk implements Command {

        @Override
        public String desc() {
            return "confirm a successful commit";
        }

        @Override
        public int commandId() {
            return 21;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 事务回滚
     */
    public class Rollback extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "abandon the current transaction";
        }

        @Override
        public int commandId() {
            return 30;
        }
    }

    public class RollbackOk implements Command {

        @Override
        public String desc() {
            return "confirm successful rollback";
        }

        @Override
        public int commandId() {
            return 31;
        }

        @Override
        public void execute() {

        }
    }
}
