package org.archer.archermq.protocol.constants;


import org.archer.archermq.protocol.Exchange;

import java.util.Objects;

/**
 * 交换机类型
 */
public enum ExchangeType {
    DEFAULT(0,"default exchange"),
    DIRECT(1, "direct exchange"),
    FANOUT(2, "fanout exchange"),
    TOPIC(3, "topic exchange"),
    HEADERS(4, "heads exchange");

    private int val;
    private String desc;

    ExchangeType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public ExchangeType getByVal(Integer val) {
        ExchangeType[] types = values();
        for (ExchangeType type : types) {
            if (Objects.equals(type.val, val)) {
                return type;
            }
        }
        return null;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
