package org.archer.archermq.common.log;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import javax.security.auth.login.LoginContext;

import java.security.acl.LastOwnerException;

import static org.junit.Assert.*;

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
}