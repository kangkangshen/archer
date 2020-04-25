package org.archer.archermq.protocol.model;

public interface MethodResolver {

    boolean support(int classId,int methodId);

    void register(int classId,int methodId);

}
