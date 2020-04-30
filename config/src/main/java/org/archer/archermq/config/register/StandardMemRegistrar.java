package org.archer.archermq.config.register;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 基于内存的标准注册机实现
 *
 * @param <ID>
 * @param <TYPE>
 */
public class StandardMemRegistrar<ID, TYPE> implements Registrar<ID, TYPE> {

    private final Map<ID, TYPE> delegateRegistry = Maps.newHashMap();

    @Override
    public boolean contains(@Nonnull ID id) {
        return delegateRegistry.containsKey(id);
    }

    @Override
    public synchronized boolean register(@Nonnull ID id, @Nonnull TYPE instance) {
        if(delegateRegistry.containsKey(Objects.requireNonNull(id))){
            return false;
        }else{
            delegateRegistry.put(Objects.requireNonNull(id),Objects.requireNonNull(instance));
            return true;
        }
    }

    @Override
    public TYPE remove(@Nonnull ID id) {
        return delegateRegistry.remove(Objects.requireNonNull(id));
    }

    @Override
    public TYPE get(@Nonnull ID id) {
        return delegateRegistry.get(id);
    }

    @Override
    public Set<ID> ids() {
        return delegateRegistry.keySet();
    }

    @Override
    public List<TYPE> instances() {
        return Lists.newArrayList(delegateRegistry.values());
    }

    @Override
    public int size() {
        return delegateRegistry.size();
    }
}
