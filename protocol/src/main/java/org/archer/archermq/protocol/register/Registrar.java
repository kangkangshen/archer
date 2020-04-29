package org.archer.archermq.protocol.register;

import java.util.List;
import java.util.Set;

public interface Registrar<ID,TYPE> {

    boolean contains(ID id);

    boolean register(ID id,TYPE instance);

    TYPE remove(ID id);

    TYPE get(ID id);

    Set<ID> ids();

    List<TYPE> instances();

    int size();

}
