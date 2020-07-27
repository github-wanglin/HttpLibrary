package com.wl.library.callback.tcpinterface;

/**
 * author:wanglin
 * date:2020/7/17 11:23 AM
 * description:tcp服务段异常回调
 */
public interface TcpServerFailCallback {
    void serverFail(Exception e);
}
