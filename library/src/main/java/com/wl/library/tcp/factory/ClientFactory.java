package com.wl.library.tcp.factory;


import com.wl.library.callback.tcpinterface.Client;
import com.wl.library.callback.tcpinterface.ReceiveRegister;
import com.wl.library.callback.tcpinterface.SendRegister;
import com.wl.library.callback.tcpinterface.TcpConnectCallback;
import com.wl.library.tcp.client.BIOClient;
import com.wl.library.tcp.type.LengthFlagReceiveRegister;
import com.wl.library.tcp.type.LengthFlagSendRegister;

/**
 * author:wanglin
 * date:2020-06-02 17:24
 * description:构建客户端的工厂
 */
public class ClientFactory {

    public static Client getClient(String host, int port, TcpConnectCallback connectCallback) {
        ReceiveRegister receiveRegister = null;
        SendRegister sendRegister = null;
        Client client = null;

        receiveRegister = new LengthFlagReceiveRegister();

        sendRegister = new LengthFlagSendRegister(receiveRegister);

        client = new BIOClient(host, port, sendRegister, connectCallback);

        return client;
    }

    public static Client getClient(String host, int port, ReceiveRegister receiveRegister, TcpConnectCallback connectCallback) {
        SendRegister sendRegister = null;
        Client client = null;

        sendRegister = new LengthFlagSendRegister(receiveRegister);

        client = new BIOClient(host, port, sendRegister, connectCallback);

        return client;
    }

    public static Client getClient(String host, int port, SendRegister sendRegister, TcpConnectCallback connectCallback) {

        Client client = null;
        client = new BIOClient(host, port, sendRegister, connectCallback);

        return client;
    }

}
