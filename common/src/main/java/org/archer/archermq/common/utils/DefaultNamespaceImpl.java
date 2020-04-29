package org.archer.archermq.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.Namespace;

import java.util.Objects;

public class DefaultNamespaceImpl implements Namespace {

    private String name;

    private Namespace parent;

    public DefaultNamespaceImpl(String name, Namespace parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Namespace parent() {
        return parent;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Namespace){
            Namespace namespace = (Namespace) obj;
            return Objects.equals(name,namespace.name())&&Objects.equals(parent,namespace.parent());
        }
        return false;
    }
}
