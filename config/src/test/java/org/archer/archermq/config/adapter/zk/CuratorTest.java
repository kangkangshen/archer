package org.archer.archermq.config.adapter.zk;


import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.server.PrepRequestProcessor;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.junit.Test;
import org.springframework.util.Assert;

public class CuratorTest {

    @Test
    public void put(){
        CuratorFramework curatorClient = curator();
        String key = "/wukang";
        String value = "niubi";
        Assert.hasText(value, "value cannot be blank");
        try {
            String s =curatorClient.create().forPath(key, value.getBytes());
            System.out.println(s);
            System.out.println("caonima");
        } catch (Exception e) {
            e.printStackTrace();
            curatorClient.close();
        }
        curatorClient.close();
    }

    @Test
    public void get(){
        CuratorFramework curatorClient = curator();
        try {
            byte[] s = curatorClient.getData().forPath("/zookeeper/quota/zk/zk3");
            System.out.println(new String(s));
            System.out.println("caonima");
        } catch (Exception e) {
            e.printStackTrace();
            curatorClient.close();
        }
        curatorClient.close();
    }


    private CuratorFramework curator(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.1.63:2181")
                .namespace("")
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryOneTime(5000))
                .build();
        curatorFramework.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return curatorFramework;
    }
}
