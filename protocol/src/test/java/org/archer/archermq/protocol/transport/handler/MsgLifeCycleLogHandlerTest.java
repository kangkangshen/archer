package org.archer.archermq.protocol.transport.handler;

import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.transport.StandardMessage;
import org.junit.Test;

import static org.junit.Assert.*;

public class MsgLifeCycleLogHandlerTest {

    @Test
    public void responseEvent() {
        Message message = new StandardMessage("wukang","wukang");
        message.acceptListener(new MsgLifeCycleLogHandler());
        message.updateCurrState(LifeCyclePhases.Message.CREATE,LifeCyclePhases.Status.FINISH);
        message.triggerEvent();
    }
}