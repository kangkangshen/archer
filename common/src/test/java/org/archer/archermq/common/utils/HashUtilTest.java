package org.archer.archermq.common.utils;

import org.junit.Test;

public class HashUtilTest {

    @Test
    public void md5() {

//        String wukang = "wukang shuai bao le ";
//        System.out.println(HashUtil.md5(wukang));

        System.out.println(HashUtil.md5(""));
        System.out.println(HashUtil.md5(""));

    }

    @Test
    public void sha1() {
        String wukang = "wukang shuai bao le ";
        System.out.println(HashUtil.sha1(wukang));

    }
}