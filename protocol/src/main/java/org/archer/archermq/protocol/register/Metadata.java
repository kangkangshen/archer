package org.archer.archermq.protocol.register;

import java.util.Map;

public interface Metadata {

    String id();

    String desc();

    String tag();

    Map<String, Object> properties();
}
