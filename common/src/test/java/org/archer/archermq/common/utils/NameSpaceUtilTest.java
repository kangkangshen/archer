package org.archer.archermq.common.utils;

import org.archer.archermq.common.Namespace;
import org.junit.Test;

import static org.junit.Assert.*;

public class NameSpaceUtilTest {

    @Test
    public void namespace() {
        Namespace namespace = NameSpaceUtil.namespace("archermq.protol.impl");
        System.out.println(namespace.name());
    }
}