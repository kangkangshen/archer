package org.archer.archermq.config.adapter.zk;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * 基于zookeeper实现的配置中心
 *
 * @author dongyue
 * @date 2020年04月18日17:20:26
 */
@Deprecated
//@Component
public class DistributedConfigStore implements PreferencesFactory {


    /**
     * 当前所属的configStore名称
     */
    @Value("archermq.configstore.name")
    private String configStoreName;

    /**
     * 当前所属的应用名称
     */
    @Value("archermq.app.name")
    private String appName;

    @Autowired
    private CuratorFramework zkClient;


    @Override
    public Preferences systemRoot() {
        return DistributedPreferences.getSystemRoot(configStoreName,appName, zkClient);
    }

    @Override
    public Preferences userRoot() {
        return DistributedPreferences.getUserRoot(configStoreName,appName,zkClient);
    }

    public String getConfigStoreName() {
        return configStoreName;
    }

    public void setConfigStoreName(String configStoreName) {
        this.configStoreName = configStoreName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
