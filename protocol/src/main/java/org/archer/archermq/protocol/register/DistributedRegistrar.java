package org.archer.archermq.protocol.register;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.archer.archermq.common.constants.Delimiters;
import org.archer.archermq.config.cfgcenter.ConfigCenterService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DistributedRegistrar<TYPE extends Recordable> implements Registrar<String, TYPE> {


    private final ConfigCenterService configCenterService;

    private static final String META_INFO_PATH = "/META-INF";

    private final String tag;

    public DistributedRegistrar(ConfigCenterService configCenterService, String tag) {
        this.configCenterService = configCenterService;
        this.tag = tag;
    }

    @Override
    public boolean contains(String id) {
        return Objects.isNull(configCenterService.get(tag + Delimiters.PERIOD + id));
    }

    @Override
    public boolean register(String id, TYPE instance) {
        Metadata metadata = instance.meta();
        String key = metadata.tag() + Delimiters.PERIOD + metadata.id();
        try {
            configCenterService.putObject(key, instance.meta());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TYPE remove(String id) {
        Recordable recordable = configCenterService.getObject(id, Recordable.class);
        if (Objects.nonNull(recordable)) {
            configCenterService.putObject(tag + Delimiters.PERIOD + id, null);
        }
        return (TYPE) recordable;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TYPE get(String id) {
        return (TYPE) configCenterService.getObject(tag + Delimiters.PERIOD + id, Recordable.class);
    }

    @Override
    public Set<String> ids() {
        Map<String, Object> cache = configCenterService.cache();
        if (!CollectionUtils.isEmpty(cache)) {
            return cache.keySet().stream()
                    .map(key -> key.substring(key.indexOf(tag) + tag.length()))
                    .collect(Collectors.toSet());
        } else {
            return Sets.newHashSet();
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TYPE> instances() {
        Map<String, Object> cache = configCenterService.cache();
        if (!CollectionUtils.isEmpty(cache)) {
            List<TYPE> result = Lists.newArrayList();
            for (Object o : cache.values()) {
                result.add((TYPE) o);
            }
            return result;
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public int size() {
        Map<String, Object> cache = configCenterService.cache();
        if (CollectionUtils.isEmpty(cache)) {
            return 0;
        } else {
            return cache.size();
        }
    }


}
