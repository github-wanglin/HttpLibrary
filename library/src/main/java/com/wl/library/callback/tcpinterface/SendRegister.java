package com.wl.library.callback.tcpinterface;

import java.io.IOException;
import java.net.Socket;

/**
 * author:wanglin
 * date:2020-06-02 15:45
 * description:发送数据行为策略
 */
public interface SendRegister {
     byte[] send(Socket socket, byte[] data) throws IOException;
}
