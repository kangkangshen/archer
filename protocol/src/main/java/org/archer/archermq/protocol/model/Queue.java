package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

public final class Queue extends FeatureBased {

    public class Declare extends BaseTransactionalCommand{

    }

    public class Bind extends BaseTransactionalCommand{

    }

    public class Unbind extends BaseTransactionalCommand{

    }

    public class Purge extends BaseTransactionalCommand{

    }

    public class Delete extends BaseTransactionalCommand{

    }
}
