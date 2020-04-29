package org.archer.archermq.config.cfgcenter;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

public interface ConfigCenterService extends EventListener {


    String appName();

    default boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    default byte getByte(String key) {
        return Byte.parseByte(get(key));
    }

    default char getChar(String key) {
        return get(key).charAt(0);
    }

    default short getShort(String key) {
        return Short.parseShort(get(key));
    }

    default int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    default float getFloat(String key) {
        return Float.parseFloat(get(key));
    }

    default double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    default long getLong(String key) {
        return Long.parseLong(get(key));
    }

    default <T> T getObject(String key, Class<T> type) {
        return getOrDefault(key, type, null);
    }

    default List<String> getList(String key) {
        return getList(key,String.class);
    }

    <T> List<T> getList(String key, Class<T> type);

    default String get(String key) {
        return getOrDefault(key, String.class, StringUtils.EMPTY);
    }

    <T> T getOrDefault(String key, Class<T> type, T defaultVal);

    default void putBoolean(String key, boolean val) {
        putObject(key, val);
    }

    default void putByte(String key, byte val) {
        putObject(key, val);
    }

    default void putChar(String key, char val) {
        putObject(key, val);
    }

    default void putShort(String key, short val) {
        putObject(key, val);
    }

    default void putInt(String key, int val) {
        putObject(key, val);
    }

    default void putFloat(String key, int val) {
        putObject(key, val);
    }

    default void putDouble(String key, int val) {
        putObject(key, val);
    }

    default void putLong(String key, int val) {
        putObject(key, val);
    }

    void putObject(String key, Object val);

    default void putList(List<Pair<String,Object>> configs) {
        if(!CollectionUtils.isEmpty(configs)){
            configs.forEach(pair->putObject(pair.getKey(),pair.getValue()));
        }
    }

    Map<String,Object> cache();

}
