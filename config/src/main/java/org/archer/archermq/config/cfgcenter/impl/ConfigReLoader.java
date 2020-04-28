package org.archer.archermq.config.cfgcenter.impl;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.archer.archermq.common.register.Registrar;
import org.archer.archermq.common.register.StandardMemRegistrar;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Component
public class ConfigReLoader implements CuratorListener, Registrar<String,Object>, ApplicationListener<ContextRefreshedEvent> {

    private final Registrar<String,Object> registrar = new StandardMemRegistrar<>();


    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {

        if(isConfigChangedEvent(event.getType())&&registrar.contains(event.getPath())){
        }
    }

    private boolean isConfigChangedEvent(CuratorEventType type){
        List<CuratorEventType> configChangedEventTypes = Lists.newArrayList(CuratorEventType.CREATE,CuratorEventType.DELETE,CuratorEventType.SET_DATA);
        return configChangedEventTypes.contains(type);
    }



    @Override
    public boolean contains(String s) {
        return registrar.contains(s);
    }

    @Override
    public boolean register(String s, Object instance) {
        return registrar.register(s,instance);
    }

    @Override
    public Object remove(String s) {
        return registrar.remove(s);
    }

    @Override
    public Object get(String s) {
        return registrar.get(s);
    }

    @Override
    public Set<String> ids() {
        return registrar.ids();
    }

    @Override
    public List<Object> instances() {
        return registrar.instances();
    }

    @Override
    public int size() {
        return registrar.size();
    }

    private void inject(String property,String data){
//        Field field = ReflectionUtils.findField()
    }




    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }


}
