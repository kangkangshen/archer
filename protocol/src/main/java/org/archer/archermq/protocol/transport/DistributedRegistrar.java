package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.Registrar;

import java.util.List;
import java.util.Set;

public class DistributedRegistrar<ID,TYPE> implements Registrar<ID,TYPE> {




    public DistributedRegistrar() {
    }

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
