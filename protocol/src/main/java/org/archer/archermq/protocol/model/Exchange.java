package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

import java.util.List;

/**
 * 对应amqp的exchange类
 *
 * @author dongyue
 * @date 2020年04月20日18:52:39
 */
public final class Exchange extends FeatureBased implements Class{


    @Override
    public int classId() {
        return 40;
    }

    @Override
    public String desc() {
        return "work with exchanges";
    }

    @Override
    public List<String> methods() {
        return null;
    }

    /*
     * 声明交换器
     */
    public class Declare extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "verify exchange exists, create if needed";
        }

        @Override
        public int commandId() {
            return 10;
        }
    }

    public class DeclareOk implements Command{

        @Override
        public String desc() {
            return "confirm exchange declaration";
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
     * 删除交换器
     */
    public class Delete extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "delete an exchange";
        }

        @Override
        public int commandId() {
            return 20;
        }
    }

    public class DeleteOk implements Command{

        @Override
        public String desc() {
            return "confirm deletion of an exchange";
        }

        @Override
        public int commandId() {
            return 21;
        }

        @Override
        public void execute() {

        }
    }


}
