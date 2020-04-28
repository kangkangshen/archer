package org.archer.archermq.config.cfgcenter;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.EventListener;
import java.util.List;

public interface ConfigCenterService extends EventListener {


    String appName();

    default boolean getBoolean(String key) {
        return getOrDefault(key, Boolean.FALSE);
    }

    default byte getByte(String key){
        return getOrDefault(key,Byte.MIN_VALUE);
    }

    default char getChar(String key){
        return getOrDefault(key,Character.MIN_VALUE);
    }

    default short getShort(String key){
        return getOrDefault(key,Short.MIN_VALUE);
    }

    default int getInt(String key){
        return getOrDefault(key,Integer.MIN_VALUE);
    }

    default float getFloat(String key){
        return getOrDefault(key, Float.MIN_VALUE);
    }

    default double getDouble(String key){
        return getOrDefault(key,Double.MIN_VALUE);
    }

    default long getLong(String key){
        return getOrDefault(key,Long.MIN_VALUE);
    }

    default <T> T getObject(String key, Class<T> type){
        return getOrDefault(key,null);
    }

    default List<String> getList(String key){
        return Lists.newArrayList();
    }

    default <T> List<T> getList(String key, Class<T> type){
        return Lists.newArrayList();
    }

    default String get(String key){
        return getOrDefault(key, StringUtils.EMPTY);
    }

    <T> T getOrDefault(String key, T defaultVal);

    default void putBoolean(String key, boolean val){
        putObject(key,val);
    }

    default void putByte(String key, byte val){
        putObject(key,val);
    }

    default void putChar(String key, char val){
        putObject(key,val);
    }

    default void putShort(String key, short val){
        putObject(key,val);
    }

    default void putInt(String key, int val){
        putObject(key,val);
    }

    default void putFloat(String key, int val){
        putObject(key,val);
    }

    default void putDouble(String key, int val){
        putObject(key,val);
    }

    default void putLong(String key, int val){
        putObject(key,val);
    }

    void putObject(String key, Object val);

    void putList(String key, List<Object> val);

}
