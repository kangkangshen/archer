package org.archer.archermq.protocol.constants;

import com.google.common.collect.Sets;
import org.archer.archermq.common.EnumSpec;

import java.util.Objects;
import java.util.Set;

/**
 * 类定义
 *
 * @author dongyue
 * @date 2020年04月23日09:20:00
 */
public enum ClassEnum implements EnumSpec<Integer> {
    CONNECTION(10, "work with socket connections", Sets.newHashSet()),
    CHANNEL(20, "work with channels", Sets.newHashSet()),
    EXCHANGE(40, "work with exchanges", Sets.newHashSet()),
    QUEUE(50, "work with queues", Sets.newHashSet()),
    BASIC(60, "work with basic content", Sets.newHashSet()),
    TX(90, "work with transactions", Sets.newHashSet());


    private final int val;
    private final String desc;
    private final Set<MethodEnum> methods;

    ClassEnum(int val, String desc, Set<MethodEnum> methods) {
        this.val = val;
        this.desc = desc;
        this.methods = methods;
    }

    public static ClassEnum getByVal(Integer val) {
        ClassEnum[] classEnums = values();
        for (ClassEnum classEnum : classEnums) {
            if (Objects.equals(classEnum.val, val)) {
                return classEnum;
            }
        }
        return null;
    }

    @Override
    public Integer getVal() {
        return val;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public boolean allow(MethodEnum method) {
        return methods.contains(method);
    }
}
