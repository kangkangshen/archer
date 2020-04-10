package org.archer.archermq;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * 支持feature的数据对象基类
 *
 */
public class FeatureBased {

    private Map<String, Object> feature = Maps.newConcurrentMap();

    /**
     * 添加feature,如果已存在，则忽略此次添加
     * 如果val==null,放弃本次添加
     *
     * @param key featureKey
     * @param val featureVal
     */
    public void addFeature(String key, Object val) {
        if(!Objects.isNull(val)){
            feature.putIfAbsent(key, val);
        }
    }

    /**
     * 删除feature
     *
     * @param key featureKey
     * @return 返回该feature删除之前的值
     */
    public Object removeFeature(String key) {
        return feature.remove(key);
    }

    /**
     * 获取key对应的feature值
     *
     * @param key featureKey
     * @return key对应的feature值
     */
    public Object getFeature(String key) {
        return feature.get(key);
    }

    /**
     * 替换feature,返回替换之前的feature值
     * @param key 欲替换的featureKey
     * @param val 替换后的featureVal
     * @return  替换前的featureVal
     */
    public Object replaceFeature(String key,Object val){
        if(!Objects.isNull(val)){
            return feature.put(key,val);
        }
        return null;

    }

    /**
     * 检查是否包含指定的feature
     *
     * @param key featureKey
     * @return true if包含指定的feature,false if 不包含指定的feature
     */
    public boolean containsKey(String key){
        return feature.containsKey(key);
    }

}
