package org.archer.archermq.protocol.constants;

import org.archer.archermq.common.EnumSpec;

import java.util.Objects;

public enum MethodEnum implements EnumSpec<Integer> {
    ;


    private final Integer val;
    private final String desc;

    MethodEnum(Integer val, String desc) {
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

    public static MethodEnum getByVal(Integer val){
        MethodEnum[] methodEnums = values();
        for(MethodEnum methodEnum:methodEnums){
            if(Objects.equals(methodEnum.val,val)){
                return methodEnum;
            }
        }
        return null;
    }
}
