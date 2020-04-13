package org.archer.archermq.protocol.constants;

/**
 * 生命周期阶段常量表
 *
 * @author dongyue
 * @date 2020年04月13日22:10:02
 */
public interface LifeCyclePhases {

    interface Exchange{
        /**
         * 如果不存在就创建，否则继续
         */
        String DECLARE = "declare";

    }


    /**
     * 生命周期的阶段状态
     */
    interface Status{
        String START = "start";
        String FINISH = "finish";

    }
}
