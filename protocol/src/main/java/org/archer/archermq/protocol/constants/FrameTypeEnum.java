package org.archer.archermq.protocol.constants;

import org.archer.archermq.common.EnumSpec;
import org.archer.archermq.protocol.transport.Frame;

import java.util.Objects;

/**
 * 数据帧类型枚举
 * @date 2020年04月13日22:41:14
 * @author dongyue
 */
public enum FrameTypeEnum implements EnumSpec<Integer> {

    METHOD(1,"method frame"),
    CONTENT_HEADER(2,"content header frame"),
    CONTENT_BODY(3,"content body frame"),
    HEARTBEAT(8,"heart beat frame");


    private final int val;
    private final String desc;

    FrameTypeEnum(int val, String desc) {
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

    public static FrameTypeEnum getByVal(Integer val){
        FrameTypeEnum[] types = values();
        for(FrameTypeEnum type:types){
            if(Objects.equals(val,type.val)){
                return type;
            }
        }
        return null;
    }

}
