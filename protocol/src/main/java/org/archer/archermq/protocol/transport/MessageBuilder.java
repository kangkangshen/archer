package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageBody;
import org.archer.archermq.protocol.MessageHead;

public class MessageBuilder {


    public static Message buildMsg(Frame headerMsgFrame, Frame bodyMsgFrame) {
        StandardContentFrame.StandardContentHeaderFrame contentHeaderFrame = (StandardContentFrame.StandardContentHeaderFrame) headerMsgFrame;
        StandardContentFrame.StandardContentBodyFrame contentBodyFrame = (StandardContentFrame.StandardContentBodyFrame) bodyMsgFrame;


        MessageHead msgHead = new StandardMessage.StandardMessageHead(contentHeaderFrame.getProperties());
        MessageBody msgBody = new StandardMessage.StandardMessageBody(contentBodyFrame.content());

        return new StandardMessage(msgHead, msgBody);

    }
}
