package com.wl.library.callback.tcpinterface;

import java.io.IOException;
import java.io.InputStream;

/**
 * author:wanglin
 * date:2020-06-02 16:44
 * description:读取 接受缓冲区 接收数据行为策略
 */
public interface ReceiveRegister {

    byte[] read(InputStream inputStream) throws IOException;
}
