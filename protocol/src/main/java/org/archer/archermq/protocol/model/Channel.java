package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

public final class Channel extends FeatureBased {

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
