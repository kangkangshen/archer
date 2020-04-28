package org.archer;

import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameDispatcher;
import org.archer.archermq.protocol.transport.handler.StandardFrameDispatcher;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws NoSuchMethodException {
//        ConnectionFactory connectionFactory = new CachingConnectionFactory();
//        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
//        admin.declareQueue(new Queue("myqueue"));
//        AmqpTemplate template = new RabbitTemplate(connectionFactory);
//        template.convertAndSend("myqueue", "foo");
//        String foo = (String) template.receiveAndConvert("myqueue");

        Method method = FrameDispatcher.class.getMethod("dispatchFrame", Frame.class);
        System.out.println(Objects.isNull(method.getAnnotation(Log.class)));

    }
}
