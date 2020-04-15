package org.archer.archermq.protocol.transport.bootstrap;

import org.junit.Test;

import static org.junit.Assert.*;

public class AmqpServerContainerTest {

    @Test
    public void afterPropertiesSet() {

        AmqpServerContainer amqpServerContainer = new AmqpServerContainer();
        try {
            amqpServerContainer.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}