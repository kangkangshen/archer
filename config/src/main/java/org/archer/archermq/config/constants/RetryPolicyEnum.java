package org.archer.archermq.config.constants;

import org.apache.curator.RetryPolicy;
import org.archer.archermq.common.EnumSpec;
import scala.Int;

/**
 * 重试策略字符串常量
 */
public enum RetryPolicyEnum implements EnumSpec<Integer> {
    RETRY_FOREVER(1,"RetryForever"),
    RETRY_ONE_TIME(2,"RetryOneTime"),
    RETRY_NTIMES(3,"RetryNTimes");



    private final Integer val;
    private final String desc;


    RetryPolicyEnum(Integer val, String desc) {
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

    public static RetryPolicyEnum getByPolicyName(String retryPolicy){
        RetryPolicyEnum[] enums = values();
        for(RetryPolicyEnum retryPolicyEnum:enums){
            if(retryPolicyEnum.desc.equalsIgnoreCase(retryPolicy)){
                return retryPolicyEnum;
            }
        }
        return null;
    }
}
