package org.archer.archermq.config.adapter.zk;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.util.Assert;

import java.util.List;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * 基于zookeeper实现的配置项
 *
 * @author dongyue
 * @date 2020年04月18日17:19:43
 */
@Deprecated
public class DistributedPreferences extends AbstractPreferences {

    private final CuratorFramework curatorClient;

    static Preferences systemRoot;

    static Preferences userRoot;

    private final boolean userMode;

    private final String configStoreName;

    private final String appName;

    public DistributedPreferences(String configStoreName,String appName,CuratorFramework zkClient) {
        this(configStoreName,appName,true,zkClient);
    }

    public DistributedPreferences(String configStoreName,String appName,boolean userMode,CuratorFramework zkClient) {
        this(null, StringUtils.EMPTY,configStoreName,appName,userMode,zkClient);
    }


    protected DistributedPreferences(AbstractPreferences parent, String name,String configStoreName,String appName,boolean userMode,CuratorFramework zkClient) {
        super(parent, name);
        Assert.hasText(configStoreName,"config store name must be not blank");
        Assert.hasText(appName,"app name must be not blank");
        this.configStoreName = configStoreName;
        this.appName = appName;
        this.userMode = userMode;
        this.curatorClient = zkClient;
    }

    @Override
    @SneakyThrows
    protected void putSpi(String key, String value) {
        Assert.hasText(value, "value cannot be blank");
        curatorClient.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(preprocessKey(key), value.getBytes());
    }

    @SneakyThrows
    @Override
    protected String getSpi(String key) {
        byte[] data = curatorClient.getData()
                .forPath(preprocessKey(key));
        return new String(data);
    }

    @SneakyThrows
    @Override
    protected void removeSpi(String key) {
        curatorClient.delete()
                .guaranteed()                   // 如果删除失败，那么在后端还是继续会删除，直到成功
                .forPath(preprocessKey(key));
    }

    @SneakyThrows
    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        curatorClient.delete()
                .guaranteed()
                .forPath(name());
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        return new String[0];
    }

    @SneakyThrows
    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        List<String> childrenNames = curatorClient.getChildren().forPath(name());
        String[] tmp = new String[childrenNames.size()];
        return childrenNames.toArray(tmp);
    }

    @SneakyThrows
    @Override
    protected AbstractPreferences childSpi(String name) {
        if (curatorClient.getChildren().forPath(name()).contains(name)) {
            return new DistributedPreferences(this,name,this.configStoreName,this.appName,this.userMode,this.curatorClient);
        }else{
            curatorClient.create()
                    .creatingParentsIfNeeded()
                    .forPath(preprocessKey(name));
            return new DistributedPreferences(this,name,this.configStoreName,this.appName,this.userMode,this.curatorClient);
        }
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
        //ignore
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
        //ignore
    }

    static synchronized Preferences getSystemRoot(String configStoreName,String appName,CuratorFramework zkClient) {
        if (systemRoot == null) {
            systemRoot = new DistributedPreferences(configStoreName,appName,false,zkClient);
        }
        return systemRoot;
    }

    static synchronized Preferences getUserRoot(String configStoreName,String appName,CuratorFramework zkClient){
        if(userRoot == null){
            userRoot = new DistributedPreferences(configStoreName,appName,zkClient);
        }
        return userRoot;
    }



    private String preprocessKey(String key) {
        if (StringUtils.isBlank(key)) {
            return StringUtils.EMPTY;
        }
        key = key.replace(".", "/");
        if (!key.endsWith("/")) {
            key = key.concat("/");
        }
        //相对路径
        if (!key.startsWith(name())) {
            return name() + "/" + key;
        }
        return key;
    }

    public boolean isUserMode() {
        return userMode;
    }

    public String getConfigStoreName() {
        return configStoreName;
    }

    public String getAppName() {
        return appName;
    }
}
