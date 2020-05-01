package org.archer.archermq.protocol;

import org.archer.archermq.protocol.transport.Frame;

import java.net.InetAddress;

public interface InternalClient {

    void start();

    void destroy();

    void connect(String host,int port);

    void send(Frame frame);
}
