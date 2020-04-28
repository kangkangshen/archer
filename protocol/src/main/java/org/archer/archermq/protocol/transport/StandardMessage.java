package org.archer.archermq.protocol.transport;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import org.archer.archermq.common.Extensible;
import org.archer.archermq.protocol.*;

import java.util.Map;

public class StandardMessage extends BaseLifeCycleSupport implements Message, Extensible {

    private final MessageHead messageHead;

    private final MessageBody messageBody;

    public StandardMessage(MessageHead messageHead, MessageBody messageBody) {
        this.messageHead = messageHead;
        this.messageBody = messageBody;
    }

    @Override
    public MessageHead head() {
        return messageHead;
    }

    @Override
    public MessageBody body() {
        return messageBody;
    }

    public static class StandardMessageHead implements MessageHead{

        private final Map<String,Object> msgProperties;

        public  StandardMessageHead(Map<String,Object> msgProperties){
            this.msgProperties = msgProperties;
        }

        @Override
        public Map<String, Object> msgProperties() {
            return msgProperties;
        }
    }

    public static class StandardMessageBody implements MessageBody{

        private final ByteBuf content;

        public StandardMessageBody(ByteBuf content) {
            this.content = content;
        }

        @Override
        public ByteBuf content() {
            return content;
        }
    }
}
