package org.archer.archermq.protocol.constants;

/**
 * 生命周期阶段常量表
 *
 * @author dongyue
 * @date 2020年04月13日22:10:02
 */
public interface LifeCyclePhases {


    interface Server {
        String STARTING = "starting";

        String RUNNING = "run";

        String TERMINATE = "terminate";
    }

    interface Exchange {
        /**
         * 如果不存在就创建，否则继续
         */

        String CREATE = "create";

        String INUSE = "inuse";

        String DELETE = "delete";

        String CANCEL = "cancel";

    }

    interface MessageQueue {
        /**
         * 如果不存在就创建，否则继续
         */

        String CREATE = "create";

        String BIND = "bind";

        String UNBIND = "unbind";

        String DELETE = "delete";

        String CANCEL = "cancel";

    }

    interface Message{

        String CREATE = "create";

        String ACCEPT = "accept";

        String ENQUEUE = "enqueue";

        String DEQUEUE = "dequeue";

        String UNCONFIRMED = "unConfirmed";

        String CONFIRMED = "confirmed";

        String CONSUMED = "consumed";

    }

    interface Connection{
        String CREATE = "create";

        String CLOSE = "close";
    }

    interface Channel{
        String CREATE = "create";

        String CLOSE = "close";
    }




    /**
     * 生命周期的阶段状态
     */
    interface Status {
        String START = "start";
        String FINISH = "finish";
        String ABNORMAL = "abnormal";

    }
}
