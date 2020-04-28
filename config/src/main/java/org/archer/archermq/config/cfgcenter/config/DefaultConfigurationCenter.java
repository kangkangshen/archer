package org.archer.archermq.config.cfgcenter.config;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/4/23 1:28
 */
@Slf4j
public class DefaultConfigurationCenter implements ConfigurationCenter {

	private static final String CONFIGURATION_ROOT_PATH = "/configuration";

	private volatile Map<String, String> configurations = Maps.newHashMap();

	private CuratorFramework client;

	private PathChildrenCache pathChildrenCache;

	private ExecutorService executor = Executors.newFixedThreadPool(2);

	public DefaultConfigurationCenter(String connectionString) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		this.client = CuratorFrameworkFactory.builder()
				.connectString(connectionString)
				.sessionTimeoutMs(50000)
				.connectionTimeoutMs(50000)
				.retryPolicy(retryPolicy)
				.build();
		initCuratorClient();
	}

	private void initCuratorClient() {
		try {
			client.start();
			Stat stat = client.checkExists().forPath(CONFIGURATION_ROOT_PATH);
			if (null == stat) {
				client.create().withMode(CreateMode.PERSISTENT).forPath(CONFIGURATION_ROOT_PATH);
			}
			pathChildrenCache = new PathChildrenCache(client, CONFIGURATION_ROOT_PATH, true);
			pathChildrenCache.getListenable().addListener((curatorFramework, event) -> {
				String eventType = event.getType().name();
				String path = event.getData().getPath();
				String key = path.substring(path.lastIndexOf("/") + 1);
				String data = null != event.getData() ? new String(event.getData().getData(), "UTF-8") : "";
				log.debug(String.format("eventType:%s,path:%s,data:%s", eventType, path, data));
				if (PathChildrenCacheEvent.Type.CHILD_ADDED == event.getType()
						|| PathChildrenCacheEvent.Type.CHILD_UPDATED == event.getType()) {
					reloadSingleConfigurationMapEntity(key, data);
				} else if (PathChildrenCacheEvent.Type.CHILD_REMOVED == event.getType()) {
					removeSingleConfigurationMapEntity(key);
				}
			}, executor);
			pathChildrenCache.start();
			initConfiguration();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String concatKey(String key) {
		return CONFIGURATION_ROOT_PATH.concat("/").concat(key);
	}

	private boolean checkChildPathExists(String key) throws Exception {
		Stat stat = client.checkExists().forPath(concatKey(key));
		if (null != stat) {
			log.warn("configuration item {} exists!", key);
			return true;
		}
		return false;
	}


	public void initConfiguration() throws Exception {
		configurations = getAllConfiguration();
	}

	@Override
	public void addConfiguration(String key, String value) throws Exception {
		if (!checkChildPathExists(key)) {
			client.create().withMode(CreateMode.PERSISTENT).forPath(concatKey(key), value.getBytes("UTF-8"));
		}
	}

	@Override
	public void deleteConfiguration(String key) throws Exception {
		if (checkChildPathExists(key)) {
			client.delete().forPath(concatKey(key));
		} else {
			log.warn("delete configuration item {} failed,item ZNode has not been found!", key);
		}
	}

	@Override
	public void updateConfiguration(String key, String value) throws Exception {
		if (checkChildPathExists(key)) {
			client.setData().forPath(concatKey(key), value.getBytes("UTF-8"));
		} else {
			log.warn("update configuration item {} failed,item ZNode has not been found!", key);
		}
	}

	@Override
	public String getConfiguration(String key) throws Exception {
		if (configurations.containsKey(key)) {
			return configurations.get(key);
		}
		if (checkChildPathExists(key)) {
			return new String(client.getData().forPath(concatKey(key)), "UTF-8");
		} else {
			log.warn("get configuration item {} failed,item ZNode has not been found!", key);
			return null;
		}
	}

	private void reloadConfiguration() throws Exception {
		List<String> childPaths = client.getChildren().forPath(CONFIGURATION_ROOT_PATH);
		if (null != childPaths && !childPaths.isEmpty()) {
			for (String childPath : childPaths) {
				String key = childPath.substring(childPath.lastIndexOf("/") + 1);
				String value = new String(client.getData().forPath(concatKey(childPath)));
				reloadSingleConfigurationMapEntity(key, value);
			}
		}
	}

	@Override
	public Map<String, String> getAllConfiguration() throws Exception {
		if (!configurations.isEmpty()) {
			return configurations;
		}
		List<String> childPaths = client.getChildren().forPath(CONFIGURATION_ROOT_PATH);
		Map<String, String> configs = Maps.newHashMap();
		if (null != childPaths && !childPaths.isEmpty()) {
			for (String childPath : childPaths) {
				String key = childPath.substring(childPath.indexOf("/") + 1);
				String value = new String(client.getData().forPath(concatKey(childPath)), "UTF-8");
				configs.put(key, value);
			}
		}
		return configs;
	}

	private String reloadSingleConfigurationMapEntity(String key, String value) {
		return configurations.put(key, value);
	}

	private String removeSingleConfigurationMapEntity(String key) {
		if (configurations.containsKey(key)) {
			return configurations.remove(key);
		}
		return null;
	}

	public void close() {
		CloseableUtils.closeQuietly(pathChildrenCache);
		CloseableUtils.closeQuietly(client);
		if (null != executor && !executor.isShutdown()) {
			executor.shutdown();
		}
	}

	public static void main(String[] args) throws Exception {
		String connectionInfo = "192.168.1.103:2181";
		ConfigurationCenter configurationCenter = new DefaultConfigurationCenter(connectionInfo);
		Map<String, String> all = configurationCenter.getAllConfiguration();
		all.forEach((key, value) -> System.out.println("key:" + key + ",value:" + value));
		configurationCenter.updateConfiguration("CORE_THREAD_SIEZ", "100");
		configurationCenter.updateConfiguration("MAX_THREAD_SIEZ", "1000");
		configurationCenter.updateConfiguration("QUEUE_SIEZ", "10000");
		configurationCenter.updateConfiguration("CORE_THREAD_TIMEOUT_SECONDS", "600");

		Thread.sleep(1000);
		all = configurationCenter.getAllConfiguration();
		all.forEach((key, value) -> System.out.println("key:" + key + ",value:" + value));

		connectionInfo = "192.168.1.107:2181";
		ConfigurationCenter configurationCenter2 = new DefaultConfigurationCenter(connectionInfo);
		all = configurationCenter2.getAllConfiguration();
		all.forEach((key, value) -> System.out.println("configurationCenter2 key:" + key + ",value:" + value));

		connectionInfo = "192.168.1.101:2181";
		ConfigurationCenter configurationCenter3 = new DefaultConfigurationCenter(connectionInfo);
		all = configurationCenter3.getAllConfiguration();
		all.forEach((key, value) -> System.out.println("configurationCenter3 key:" + key + ",value:" + value));

		//测试获取无Znode节点的值
		Thread.sleep(1000);
		System.out.println("NONE_THIS_CONFIG : " + configurationCenter3.getConfiguration("NONE_THIS_CONFIG"));
	}

}
