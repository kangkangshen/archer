package org.archer.archermq.protocol.constants;

import org.archer.archermq.common.EnumSpec;
import org.archer.archermq.protocol.transport.Frame;

import java.util.Objects;

/**
 * 数据帧类型枚举
 *
 * @author dongyue
 * @date 2020年04月13日22:41:14
 */
public enum FrameTypeEnum implements EnumSpec<Byte> {

    METHOD((byte) 1, "method frame"),
    CONTENT_HEADER((byte) 2, "content header frame"),
    CONTENT_BODY((byte) 3, "content body frame"),
    HEARTBEAT((byte) 8, "heart beat frame");


    private final Byte val;
    private final String desc;

    FrameTypeEnum(byte val, String desc) {
        this.val = val;
        this.desc = desc;
    }


    @Override
    public Byte getVal() {
        return val;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static FrameTypeEnum getByVal(Byte val) {
        FrameTypeEnum[] types = values();
        for (FrameTypeEnum type : types) {
            if (Objects.equals(val, type.val)) {
                return type;
            }
        }
        return null;
    }

}
