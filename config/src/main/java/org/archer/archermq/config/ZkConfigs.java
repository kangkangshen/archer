package org.archer.archermq.config;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.archer.archermq.common.constants.Delimiters;
import org.archer.archermq.config.adapter.zk.DistributedConfigStore;
import org.archer.archermq.config.constants.RetryPolicyEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Optional;
import java.util.Set;
import java.util.prefs.Preferences;

/**
 * zk相关一揽子配置类全集，包含属性配置，zk客户端bean生成
 *
 * @date 2020年04月19日18:48:16
 * @author dongyue
 */
@Configuration
public class ZkConfigs {

    @Value("${zk.server.path}")
    private String zkServerPath;

    @Value("${spring.application.name}")
    private String appName ;

    @Value("${zk.retry.policy:RetryNTimes}")
    private String retryPolicy;

    @Value("${zk.max.retry:10}")
    private int maxRetryTimes;

    @Value("${zk.retry.interval:3000}")
    private int retryInterval;

    @Value("${zk.session.timeout:15000}")
    private int sessionTimeout;

    @Bean
    public CuratorFramework curator(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(sessionTimeout)
                .retryPolicy(retryPolicy())
                .namespace(appName)
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

    @SneakyThrows
    @Bean
    public PathChildrenCache pathChildrenCache(CuratorFramework curatorClient){
        PathChildrenCache childrenCache = new PathChildrenCache(curatorClient, Delimiters.SLASH+curatorClient.getNamespace(),true);
        childrenCache.start();
        return childrenCache;
    }

//    @Bean
    public Preferences systemRoot(DistributedConfigStore configStore){
        return configStore.systemRoot();
    }

//    @Bean
    public Preferences userRoot(DistributedConfigStore configStore){
        return configStore.userRoot();
    }

    /**
     * 根据传入的参数解析重试策略
     *
     * @return 重试策略
     * @throws UnsupportedOperationException
     */
    private RetryPolicy retryPolicy(){
        RetryPolicyEnum retryPolicyEnum = Optional.ofNullable(RetryPolicyEnum.getByPolicyName(retryPolicy)).orElse(RetryPolicyEnum.RETRY_FOREVER);
        switch (retryPolicyEnum){
            case RETRY_FOREVER:
                return new RetryForever(retryInterval);
            case RETRY_NTIMES:
                return new RetryNTimes(maxRetryTimes, retryInterval);
            case RETRY_ONE_TIME:
                return new RetryOneTime(retryInterval);
        }
        //todo dongyue 未来将使用string模板
        throw new UnsupportedOperationException(retryPolicy+" policy cannot supported");
    }
}
