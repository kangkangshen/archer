package org.archer.archermq.protocol.constants;

import org.archer.archermq.common.EnumSpec;

import java.util.Objects;

public enum ServerRoleTypeEnum implements EnumSpec<Integer> {
    PIONEER(1,"pioneer"),
    SOLDIER(2,"soldier");

    private final Integer val;
    private final String desc;

    ServerRoleTypeEnum(Integer val, String desc) {
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

    public static ServerRoleTypeEnum getByVal(Integer val){
        ServerRoleTypeEnum[] serverRoleTypeEnums = values();
        for(ServerRoleTypeEnum serverRoleTypeEnum:serverRoleTypeEnums){
            if(Objects.equals(serverRoleTypeEnum.val,val)){
                return serverRoleTypeEnum;
            }
        }
        return null;
    }
}
