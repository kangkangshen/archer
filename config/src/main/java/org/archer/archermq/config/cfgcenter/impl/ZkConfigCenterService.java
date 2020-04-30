package org.archer.archermq.config.cfgcenter.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.data.Stat;
import org.archer.archermq.common.constants.Delimiters;


import org.archer.archermq.config.cfgcenter.ConfigCenterService;
import org.archer.archermq.config.register.Registrar;
import org.archer.archermq.config.register.StandardMemRegistrar;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ZkConfigCenterService implements ConfigCenterService, InitializingBean, PathChildrenCacheListener {


    @Value("${spring.application.name}")
    private String appName;


    @Autowired
    private CuratorFramework curatorClient;

    @Autowired
    private PathChildrenCache pathChildrenCache;


    @Autowired
    private ConversionService conversionService;

    private final Registrar<String, Object> localConfig = new StandardMemRegistrar<>();

    @Override
    public String appName() {
        return appName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> type) {
        if (localConfig.contains(key)) {
            return (List<T>) localConfig.get(key);
        } else {
            try {
                byte[] rawData = curatorClient.getData().forPath(polish(appName, key));
                String data = new String(rawData, StandardCharsets.UTF_8);
                return JSON.parseArray(data, type);
            } catch (Exception e) {
                return Lists.newArrayList();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized <T> T getOrDefault(String key, Class<T> type, T defaultVal) {
        if (localConfig.contains(key)) {
            return (T) localConfig.get(key);
        } else {
            try {
                byte[] rawData = curatorClient.getData().forPath(polish(appName, key));
                if (type.isAssignableFrom(String.class)) {
                    return (T) new String(rawData, StandardCharsets.UTF_8);
                } else {
                    return JSON.parseObject(new String(rawData, StandardCharsets.UTF_8), type);
                }

            } catch (Exception e) {
                return defaultVal;
            }
        }

    }

    @Override
    public synchronized void putObject(String key, Object val) {
        try {
            if (Objects.isNull(val)) {
                String zkPath = polish(appName, key);
                curatorClient.delete().deletingChildrenIfNeeded().forPath(zkPath);
                localConfig.remove(key);
            } else {
                byte[] data = JSON.toJSONString(val).getBytes();
                String zkPath = polish(appName, key);
                if (Objects.isNull(curatorClient.checkExists().forPath(zkPath))) {
                    curatorClient.create().forPath(zkPath, data);
                } else {
                    curatorClient.setData().forPath(zkPath, data);
                }

                if (localConfig.contains(key)) {
                    localConfig.remove(key);
                }
                localConfig.register(key, val);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> cache() {
        //todo dongyue
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Stat stat = curatorClient.checkExists().forPath(polish(appName, null));
        if (Objects.isNull(stat)) {
            curatorClient.create().forPath(polish(appName, null));
        }
        pathChildrenCache.getListenable().addListener(this);
    }


    @PreDestroy
    public void close() {
        CloseableUtils.closeQuietly(pathChildrenCache);
        CloseableUtils.closeQuietly(curatorClient);
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setCuratorClient(CuratorFramework curatorClient) {
        this.curatorClient = curatorClient;
    }

    private static String polish(String appName, String key) {
        if (StringUtils.isBlank(key)) {
            return Delimiters.SLASH + StringUtils.EMPTY;
        }

        key = key.replace(Delimiters.PERIOD, Delimiters.SLASH);

        String prefixTemplate = Delimiters.SLASH + appName + Delimiters.SLASH;
        if (key.startsWith(prefixTemplate)) {
            return key.substring(prefixTemplate.length());
        }
        if (key.startsWith(String.valueOf(Delimiters.SLASH))) {
            return key;
        }
        return Delimiters.SLASH + key;

    }


    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        List<PathChildrenCacheEvent.Type> configChanged = Lists.newArrayList(PathChildrenCacheEvent.Type.CHILD_UPDATED, PathChildrenCacheEvent.Type.CHILD_ADDED, PathChildrenCacheEvent.Type.CHILD_REMOVED);
        if (configChanged.contains(event.getType()) && Objects.nonNull(event.getData())) {
            ChildData data = event.getData();
            String path = data.getPath();
            String key = path.replace(Delimiters.SLASH, Delimiters.PERIOD);
            String strData = new String(data.getData(), StandardCharsets.UTF_8);
            Object obj = JSON.parseObject(strData);
            switch (event.getType()) {
                case CHILD_ADDED:
                    localConfig.register(key, obj);
                    break;
                case CHILD_REMOVED:
                    localConfig.remove(key);
                    break;
                case CHILD_UPDATED:
                    localConfig.remove(key);
                    localConfig.register(key, obj);
                    break;
                default:
                    break;
            }
        }

    }
}
