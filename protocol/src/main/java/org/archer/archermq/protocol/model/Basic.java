package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

public final class Basic extends FeatureBased {

    public class Qos implements Command {

        @Override
        public void execute() {

        }
    }

    public class Consume extends BaseTransactionalCommand {

    }

    public class Cancel extends BaseTransactionalCommand {

    }

    public class Publish extends BaseTransactionalCommand {

    }

    public class Return extends BaseTransactionalCommand {

    }

    public class Deliver extends BaseTransactionalCommand {

    }

    public class Get extends BaseTransactionalCommand {

    }

    public class Ack implements Command {

        @Override
        public void execute() {

        }
    }

    public class Reject extends BaseTransactionalCommand {

    }

    public class Recover extends BaseTransactionalCommand {

    }

    public class Nack extends BaseTransactionalCommand {

    }
}
