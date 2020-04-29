package org.archer.archermq.protocol.register;

import java.util.List;
import java.util.Set;

/**
 * 基于内存的标准注册机实现
 *
 * @param <ID>
 * @param <TYPE>
 */
public class StandardMemRegistrar<ID, TYPE> implements Registrar<ID, TYPE> {


    @Override
    public boolean contains(ID id) {
        return false;
    }

    @Override
    public boolean register(ID id, TYPE instance) {
        return false;
    }

    @Override
    public TYPE remove(ID id) {
        return null;
    }

    @Override
    public TYPE get(ID id) {
        return null;
    }

    @Override
    public Set<ID> ids() {
        return null;
    }

    @Override
    public List<TYPE> instances() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
