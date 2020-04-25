package org.archer.archermq.protocol.constants;

import org.archer.archermq.common.EnumSpec;

import java.util.Objects;

public enum DeliverMode implements EnumSpec<Integer> {
    PULL(0, "pull mode"),
    PUSH(1, "push mode");

    DeliverMode(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    private final Integer val;

    private final String desc;


    @Override
    public Integer getVal() {
        return val;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static DeliverMode getByVal(Integer val) {
        DeliverMode[] deliverModes = values();
        for (DeliverMode deliverMode : deliverModes) {
            if (Objects.equals(deliverMode.val, val)) {
                return deliverMode;
            }
        }
        return null;
    }

}
