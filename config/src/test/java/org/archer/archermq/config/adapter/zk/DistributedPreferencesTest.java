package org.archer.archermq.config.adapter.zk;

import org.junit.Test;

import java.util.prefs.Preferences;

public class DistributedPreferencesTest {

    @Test
    public void putSpi() {
        Preferences preferences = DistributedPreferences.systemRoot();
        preferences.put("wukang","niubi");
    }

    @Test
    public void getSpi() {
    }

    @Test
    public void removeSpi() {
    }

    @Test
    public void removeNodeSpi() {
    }

    @Test
    public void keysSpi() {
    }

    @Test
    public void childrenNamesSpi() {
    }

    @Test
    public void childSpi() {
    }

    @Test
    public void syncSpi() {
    }

    @Test
    public void flushSpi() {
    }

}