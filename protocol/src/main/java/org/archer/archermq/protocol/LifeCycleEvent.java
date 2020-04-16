package org.archer.archermq.protocol;

import lombok.Data;

/**
 * 生命周期事件
 *
 * @param <T> 当前事件发生的对象
 * @author dongyue
 * @date 2020年04月16日19:03:22
 */
@Data
public class LifeCycleEvent<T> {

    private String phase;

    private String phaseStatus;

    private T target;

}
