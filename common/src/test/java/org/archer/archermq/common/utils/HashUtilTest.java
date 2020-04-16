package org.archer.archermq.common.utils;

import com.twitter.hashing.KeyHashers;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.util.Date;

import static org.junit.Assert.*;

public class HashUtilTest {

    @Test
    public void testMd5(){
        String base = "str" +"/"+"slat";
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        System.out.println(md5);
    }

}