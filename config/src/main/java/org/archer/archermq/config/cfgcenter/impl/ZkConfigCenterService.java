package org.archer.archermq.config.cfgcenter.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.utils.CloseableUtils;
import org.archer.archermq.config.cfgcenter.ConfigCenterService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZkConfigCenterService implements ConfigCenterService, InitializingBean {


    @Value("${spring.application.name:archermq}")
    private String appName;


    @Autowired
    private CuratorFramework curatorClient;




    @Override
    public String appName() {
        return appName;
    }

    @Override
    public <T> T getOrDefault(String key, T defaultVal) {
        return null;
    }

    @Override
    public void putObject(String key, Object val) {

    }

    @Override
    public void putList(String key, List<Object> val) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //check appName node exists


    }


    public void close(){
        CloseableUtils.closeQuietly(curatorClient);
    }
}
