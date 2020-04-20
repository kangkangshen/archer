package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

/**
 * 对应于amqp的channel类
 *
 * @author dongyue
 * @date 2020年04月20日18:51:38
 */
public final class Channel extends FeatureBased {

    /**
     * 开启信道
     */
    public class Open implements Command {

        @Override
        public void execute() {

        }
    }

    /**
     * 关闭信道
     */
    public class Close implements Command {

        @Override
        public void execute() {

        }
    }

}
