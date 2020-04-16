package org.archer.archermq.common.utils;

import org.archer.archermq.common.annotation.Trace;
import org.junit.Test;

import static org.junit.Assert.*;

public class TraceUtilTest {

    @Test
    public void generateTraceId() {
        System.out.println(TraceUtil.generateTraceId());
    }

    @Test
    public void saveThraceInfo() {
        TraceUtil.saveThraceInfo(TraceUtil.generateTraceId());
        System.out.println(TraceUtil.getTraceInfo());
    }

    @Test
    public void getThreadInfo() {
    }

    @Test
    public void removeThraceInfo() {
    }
}