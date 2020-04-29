package org.archer.archermq.config.cfgcenter.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

public class ConfigReLoader implements PathChildrenCacheListener {




    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {

    }
}
