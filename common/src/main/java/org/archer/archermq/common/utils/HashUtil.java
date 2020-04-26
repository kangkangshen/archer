package org.archer.archermq.common.utils;

import com.alibaba.fastjson.JSON;
import com.twitter.hashing.KeyHasher;
import com.twitter.hashing.KeyHashers;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.apache.bcel.generic.INVOKEINTERFACE;

import java.util.Date;
import java.util.Random;

/**
 * 哈希工具类，包含生成MD5,UUID,SHA1等哈希值
 *
 * @author dongyue
 * @date 2020年04月14日21:38:43
 */
public class HashUtil {

    public final static String DEFAULT_HASH_FUNCTION = "md5";

    public final static String MD5 = "md5";

    public final static String SHA1 = "sha1";


    public static String hash(String rawText) {
        return hash(rawText, DEFAULT_HASH_FUNCTION);
    }

    public static String hash(byte[] rawBytes) {
        return hash(rawBytes, DEFAULT_HASH_FUNCTION);
    }

    public static String hash() {
        return hash(randomString(), DEFAULT_HASH_FUNCTION);
    }

    public static String hash(String rawText, String hashFunc) {
        switch (hashFunc) {
            case MD5:
                return DigestUtils.md5Hex(rawText);
            case SHA1:
                return DigestUtils.sha1Hex(rawText);
            default:
                throw new UnsupportedOperationException(hashFunc);
        }
    }

    public static String hash(byte[] rawBytes, String hashFunc) {
        switch (hashFunc) {
            case MD5:
                return DigestUtils.md5Hex(rawBytes);
            case SHA1:
                return DigestUtils.sha1Hex(rawBytes);
            default:
                throw new UnsupportedOperationException(hashFunc);
        }
    }

    private static String randomString() {
        Random random = new Random();
        StackTraceElement[] invokeStackTrace = Thread.currentThread().getStackTrace();
        return random.nextLong() + JSON.toJSONString(invokeStackTrace);
    }


}
