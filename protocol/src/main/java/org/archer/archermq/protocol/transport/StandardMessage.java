package org.archer.archermq.protocol.transport;

import org.archer.archermq.common.Extensible;
import org.archer.archermq.protocol.*;

import java.util.Map;

public class StandardMessage extends BaseLifeCycleSupport implements Message, Extensible {

    private final String msgKey;

    private final String msgId;

    private String msgDesc;

    private Map<String,Object> msgProperties;

    private MessageHead messageHead;

    private MessageBody messageBody;

    public StandardMessage(String msgKey, String msgId) {
        this.msgKey = msgKey;
        this.msgId = msgId;
    }

    @Override
    public String msgKey() {
        return msgKey;
    }

    @Override
    public String msgId() {
        return msgId;
    }

    @Override
    public String msgDescription() {
        return msgDesc;
    }

    @Override
    public Map<String, Object> msgProperties() {
        return msgProperties;
    }

    @Override
    public MessageHead head() {
        return messageHead;
    }

    @Override
    public MessageBody body() {
        return messageBody;
    }
}
