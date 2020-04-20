package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

/**
 * 对应于amqp的connection类
 */
public final class Connection extends FeatureBased {

    public class Start implements Command{

        @Override
        public void execute() {

        }
    }

    public class Tune implements Command{

        @Override
        public void execute() {

        }
    }

    public class Open implements Command{

        @Override
        public void execute() {

        }
    }

    public class Close implements Command{

        @Override
        public void execute() {

        }
    }





}
