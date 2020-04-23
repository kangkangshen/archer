package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

import java.util.List;

/**
 * 对应于amqp的connection类
 *
 * @author dongyue
 * @date 2020年04月20日18:49:31
 */
public final class Connection extends FeatureBased implements Class {

    @Override
    public int classId() {
        return 10;
    }

    @Override
    public String desc() {
        return "work with socket connections";
    }

    @Override
    public List<String> methods() {
        return null;
    }

    /**
     * 建立连接相关
     */
    public class Start implements Command {

        @Override
        public String desc() {
            return "start connection negotiation";
        }

        @Override
        public int commandId() {
            return 10;
        }

        @Override
        public void execute() {

        }
    }

    public class StartOk implements Command {

        @Override
        public String desc() {
            return "select security mechanism and locale";
        }

        @Override
        public int commandId() {
            return 11;
        }

        @Override
        public void execute() {

        }
    }

    public class Secure implements Command {

        @Override
        public String desc() {
            return "security mechanism challenge";
        }

        @Override
        public int commandId() {
            return 20;
        }

        @Override
        public void execute() {

        }
    }

    public class SecureOk implements Command {

        @Override
        public String desc() {
            return "security mechanism response";
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
     * 建立连接相关
     */
    public class Tune implements Command {

        @Override
        public String desc() {
            return "propose connection tuning parameters";
        }

        @Override
        public int commandId() {
            return 30;
        }

        @Override
        public void execute() {

        }
    }

    public class TuneOk implements Command {

        @Override
        public String desc() {
            return "negotiate connection tuning parameters";
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
     * 建立连接相关
     */
    public class Open implements Command {

        @Override
        public String desc() {
            return "open connection to virtual host";
        }

        @Override
        public int commandId() {
            return 40;
        }

        @Override
        public void execute() {

        }
    }


    /**
     * 关闭连接
     */
    public class Close implements Command {

        @Override
        public String desc() {
            return "request a connection close";
        }

        @Override
        public int commandId() {
            return 50;
        }

        @Override
        public void execute() {

        }
    }

    public class CloseOk implements Command{

        @Override
        public String desc() {
            return "confirm a connection close";
        }

        @Override
        public int commandId() {
            return 51;
        }

        @Override
        public void execute() {

        }
    }



}
