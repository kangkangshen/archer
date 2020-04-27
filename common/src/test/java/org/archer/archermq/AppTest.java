package org.archer.archermq;

import static org.junit.Assert.assertTrue;
import org.archer.archermq.common.annotation.Trace;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testTraceInspectAspect(){

        HelloWorld helloWorld = new HelloWorld();
        helloWorld.helloWorld();
        helloWorld.goodBye();



    }


    static class HelloWorld{

        @Trace(begin = true)
        public void helloWorld(){
            System.out.println("hello,world");
        }

        @Trace(end = true)
        public void goodBye(){
            System.out.println("good,bye");
        }
    }
}
