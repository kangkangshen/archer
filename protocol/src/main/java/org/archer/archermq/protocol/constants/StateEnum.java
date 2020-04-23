package org.archer.archermq.protocol.constants;

import org.archer.archermq.common.EnumSpec;

import java.util.Objects;

/**
 * 状态枚举，用于表示Connection和Channel状态
 *
 * @author dongyue
 * @date 2020年04月23日22:12:21
 */

public enum StateEnum implements EnumSpec<Integer> {
    NEW(1, "just created"),
    START(2, "started"),
    CLOSED(3, "closed");

    private final Integer val;

    private final String desc;

    StateEnum(Integer val, String desc) {
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

    public static StateEnum getByVal(Integer val) {
        StateEnum[] stateEnums = values();
        for(StateEnum stateEnum:stateEnums){
            if(Objects.equals(stateEnum.val,val)){
                return stateEnum;
            }
        }
        return null;
    }
}
