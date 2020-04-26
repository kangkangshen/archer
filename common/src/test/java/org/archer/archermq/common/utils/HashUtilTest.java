package org.archer.archermq.common.utils;

import org.junit.Test;

public class HashUtilTest {

    @Test
    public void hash() {
        System.out.println(HashUtil.hash());
        System.out.println(HashUtil.hash());

    }

    @Test
    public void testHash() {
        String wukang = "wukang shuai bao le";
        System.out.println(HashUtil.hash(wukang,HashUtil.MD5));
    }

    @Test
    public void testHash1() {
        String wukang = "wukang shuai bao le";
        System.out.println(HashUtil.hash(wukang,HashUtil.SHA1));
    }

    @Test
    public void testHash2() {

        String wukang = "wukang shuai bao le";
        System.out.println(HashUtil.hash(wukang.getBytes(),HashUtil.MD5));
    }

    @Test
    public void testHash3() {
        String wukang = "wukang shuai bao le";
        System.out.println(HashUtil.hash(wukang.getBytes(),HashUtil.SHA1));
    }

}