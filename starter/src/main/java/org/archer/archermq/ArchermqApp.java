package org.archer.archermq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 基于springboot的启动器
 *
 * @author dongyue
 * @date 2020年04月15日09:59:24
 */
@SpringBootApplication
@SpringBootConfiguration
public class ArchermqApp {
    public static void main(String[] args) {
        SpringApplication.run(ArchermqApp.class,args);
        try {
            Thread.sleep(10*60*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
