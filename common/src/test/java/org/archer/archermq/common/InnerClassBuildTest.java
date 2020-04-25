package org.archer.archermq.common;

import org.junit.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Constructor;

public class InnerClassBuildTest {


    @Test
    public void testCreateInnerClass() {
        String s = "123.sad$adav";
//        StringTokenizer tokenizer = new StringTokenizer(s,"$");
//        while(tokenizer.hasMoreTokens()){
//            System.out.println(tokenizer.nextToken());
//        }
//        String[] splits = StringUtils.split(s,Delimiters.DOLLAR);
//        for(String str:splits){
//            System.out.println(str);
//        }
        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        Constructor<?>[] constructors = OuterClazz.InnerClazz.class.getConstructors();
        for(Constructor<?> constructor:constructors){
            String[] argNames = parameterNameDiscoverer.getParameterNames(constructor);
            if(argNames!=null){
                for(String argName:argNames){
                    System.out.print(argName+" ");
                }
            }
            System.out.println();
        }
    }


}
