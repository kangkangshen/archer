package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

public final class Tx extends FeatureBased {


    public class Select extends BaseTransactionalCommand{

    }

    public class Commit extends BaseTransactionalCommand{

    }

    public class Rollback extends BaseTransactionalCommand{

    }
}
