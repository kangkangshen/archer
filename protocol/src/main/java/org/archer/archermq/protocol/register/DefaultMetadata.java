package org.archer.archermq.protocol.register;

import lombok.Data;

import java.util.Map;

@Data
public class DefaultMetadata implements Metadata{

    private String id;
    private String desc;
    private String tag;
    private Map<String,Object> properties;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String desc() {
        return desc;
    }

    @Override
    public String tag() {
        return tag;
    }

    @Override
    public Map<String, Object> properties() {
        return properties;
    }
}
