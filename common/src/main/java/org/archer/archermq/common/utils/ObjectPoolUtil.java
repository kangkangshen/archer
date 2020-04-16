package org.archer.archermq.common.utils;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Queue;

/**
 * 对象池工具类实现
 * tips:
 * 因为使用threadLocal，所以建议在使用该工具类的时候尽量使用线程池保证thread不会经常被销毁
 *
 * @author dongyue
 * @date 2020年04月14日13:35:34
 */
@Deprecated
public class ObjectPoolUtil {

    public static int DEFAULT_MAX_HOLD_OBJECT_NUMS = 16;

    public static ThreadLocal<Map<Class<?>,Queue<SoftReference<Object>>>> objectPoolHolder = new ThreadLocal<>();


    public static <T> T allocateObject(Class<T> clazz){
        Map<Class<?>,Queue<SoftReference<Object>>> targetObjectPool = objectPoolHolder.get();
        if(CollectionUtils.isEmpty(targetObjectPool)){
            targetObjectPool = Maps.newHashMap();

        }
        return null;
    }



}
