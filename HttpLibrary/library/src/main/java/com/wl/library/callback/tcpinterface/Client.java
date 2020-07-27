package com.wl.library.callback.tcpinterface;

import java.net.Socket;

/**
 * author:wanglin
 * date:2020-06-02 16:15
 * description:NetworkDemo
 */
public interface Client {
    Socket getSocket();

    byte[] send(byte[] data);

    void close();
}
