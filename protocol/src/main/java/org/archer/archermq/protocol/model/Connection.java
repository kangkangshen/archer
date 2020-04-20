package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

/**
 * 对应于amqp的connection类
 *
 * @author dongyue
 * @date 2020年04月20日18:49:31
 */
public final class Connection extends FeatureBased {

    /**
     * 建立连接相关
     */
    public class Start implements Command {

        @Override
        public void execute() {

        }
    }

    /**
     * 建立连接相关
     */
    public class Tune implements Command {

        @Override
        public void execute() {

        }
    }

    /**
     * 建立连接相关
     */
    public class Open implements Command {

        @Override
        public void execute() {

        }
    }

    /**
     * 关闭连接
     */
    public class Close implements Command {

        @Override
        public void execute() {

        }
    }


}
