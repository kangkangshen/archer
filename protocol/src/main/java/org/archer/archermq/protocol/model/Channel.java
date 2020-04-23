package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

import javax.activation.CommandMap;
import java.util.List;

/**
 * 对应于amqp的channel类
 *
 * @author dongyue
 * @date 2020年04月20日18:51:38
 */
public final class Channel extends FeatureBased implements Class{

    @Override
    public int classId() {
        return 20;
    }

    @Override
    public String desc() {
        return "work with channels";
    }

    @Override
    public List<String> methods() {
        return null;
    }

    /**
     * 开启信道
     */
    public class Open implements Command {

        @Override
        public String desc() {
            return "open a channel for use";
        }

        @Override
        public int commandId() {
            return 10;
        }

        @Override
        public void execute() {

        }
    }

    public class OpenOk implements Command{

        @Override
        public String desc() {
            return "signal that the channel is ready";
        }

        @Override
        public int commandId() {
            return 11;
        }

        @Override
        public void execute() {

        }
    }

    public class Flow implements Command{

        @Override
        public String desc() {
            return "enable/disable flow from peer";
        }

        @Override
        public int commandId() {
            return 20;
        }

        @Override
        public void execute() {

        }
    }

    public class FlowOk implements Command{

        @Override
        public String desc() {
            return "confirm a flow method";
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
     * 关闭信道
     */
    public class Close implements Command {

        @Override
        public String desc() {
            return "request a channel close";
        }

        @Override
        public int commandId() {
            return 40;
        }

        @Override
        public void execute() {

        }
    }

    public class CloseOk implements Command{

        @Override
        public String desc() {
            return "confirm a channel close";
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
