package org.archer.archermq.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionMessagesTest {

    @Test
    public void buildExceptionMsgWithTemplate() {

        String exceptionMsgTemplate = "bean # created failed!";
        String msg = "amqpDecoder";
        System.out.println(ExceptionMessages.buildExceptionMsgWithTemplate(exceptionMsgTemplate,msg));
    }
}