package org.archer.archermq.common;

/**
 * 命名空间，支持嵌套
 */
public interface Namespace {

    String name();

    Namespace parent();
}
