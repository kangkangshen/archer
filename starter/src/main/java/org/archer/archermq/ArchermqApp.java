package org.archer.archermq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 基于springboot的启动器
 *
 * @author dongyue
 * @date 2020年04月15日09:59:24
 */
@ImportResource({"dataSource.xml","virtualHost.xml"})
@SpringBootApplication()
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ArchermqApp {
    public static void main(String[] args) {
        SpringApplication.run(ArchermqApp.class, args);
    }
}
