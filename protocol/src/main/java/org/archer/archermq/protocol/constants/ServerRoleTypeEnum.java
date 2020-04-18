package org.archer.archermq.protocol.constants;

import org.archer.archermq.common.EnumSpec;

import java.util.Objects;

public enum ServerRoleTypeEnum implements EnumSpec<Integer> {
    PIONEER(1,"先锋，除了正常提供消息服务外，还承担着管理士兵的任务，而且，成为先锋的机器往往是访问量最多的那个，顶在前面"),
    SOLDIER(2,"士兵，提供基础的消息服务，每个士兵都有一个成为先锋的梦");

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
