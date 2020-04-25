package org.archer.archermq.protocol;

public interface Consumer {

    String id();

    boolean exclusive();

    Channel channel();
}
