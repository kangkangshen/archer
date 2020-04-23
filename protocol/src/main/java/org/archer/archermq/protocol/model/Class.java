package org.archer.archermq.protocol.model;

import java.util.List;

public interface Class {

    int classId();

    String desc();

    List<String/*methodName*/> methods();

}
