package org.archer;

import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameDispatcher;
import org.archer.archermq.protocol.transport.handler.StandardFrameDispatcher;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
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
//        System.out.println(Boolean.TRUE.toString());
        Integer a = 17;

        System.out.println(Arrays.toString(a.toString().getBytes()));

        System.out.println(Arrays.toString("17".getBytes()));
    }

    @Test
    public void test(){
        byte a = Frame.FRAME_END;
        System.out.println(a);
        System.out.println(Objects.equals(a,Frame.FRAME_END));
    }
}
