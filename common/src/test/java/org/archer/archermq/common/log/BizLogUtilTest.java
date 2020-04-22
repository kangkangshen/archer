package org.archer.archermq.common.log;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BizLogUtilTest {

    @Test
    public void recordException() {
        try {
            int number = Integer.parseInt("wukang");
        }catch (NumberFormatException e){
            BizLogUtil.recordException(e);
            LogInfo logInfo = BizLogUtil.get();
            System.out.println(JSON.toJSONString(logInfo));
        }

    }

    @Test
    public void logHelloWorld(){
        Logger logger = LoggerFactory.getLogger("sys-err");
        logger.info("Hello World");
    }
}