package com.wl.library.callback.tcpinterface;

public interface TcpConnectCallback {
    void connected();

    void disConnected();

    void connectFail(Exception e);

}
