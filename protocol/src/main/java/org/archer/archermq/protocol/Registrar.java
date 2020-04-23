package org.archer.archermq.protocol;

public interface Registrar<ID,TYPE> {

    boolean contains(ID id);

}
