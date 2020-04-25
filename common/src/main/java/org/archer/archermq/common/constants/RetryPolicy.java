package org.archer.archermq.common.constants;

import org.archer.archermq.common.EnumSpec;

import java.util.Objects;

public enum RetryPolicy implements EnumSpec<Integer> {


    ONCE(0, "retry once");

    private final Integer val;

    private final String desc;

    RetryPolicy(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    @Override
    public Integer getVal() {
        return val;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static RetryPolicy getByVal(Integer val) {
        RetryPolicy[] retryPolicies = values();
        for (RetryPolicy retryPolicy : retryPolicies) {
            if (Objects.equals(retryPolicy.val, val)) {
                return retryPolicy;
            }
        }
        return null;
    }
}
