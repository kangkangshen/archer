package org.archer.archermq.config.adpter;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;



public abstract class BasedDistributedConfigStore implements PreferencesFactory {


    /**
     * 当前所属的configStore名称
     */
    private String configStoreName;

    /**
     * 当前所属的应用名称
     */
    private String appName;


    @Override
    public Preferences systemRoot() {
        return BasedDistributedPreferences.systemRoot();
    }

    @Override
    public Preferences userRoot() {
        return BasedDistributedPreferences.userRoot();
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
