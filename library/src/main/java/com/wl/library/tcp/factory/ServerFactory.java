package com.wl.library.tcp.factory;


import com.wl.library.callback.tcpinterface.ReceiveRegister;
import com.wl.library.callback.tcpinterface.SendRegister;
import com.wl.library.callback.tcpinterface.Server;
import com.wl.library.callback.tcpinterface.TcpConnectCallback;
import com.wl.library.tcp.server.BIOServer;
import com.wl.library.tcp.type.LengthFlagReceiveRegister;
import com.wl.library.tcp.type.LengthFlagSendRegister;

import java.net.Socket;
import java.util.function.BiFunction;

/**
 * author:wanglin
 * date:2020-06-02 15:37
 * description:构建socket服务端的简单工厂
 */
public class ServerFactory {


    /**
     * @param port        监听端口
     * @param dataHandler 业务逻辑函数
     * @return 构建的服务端对线，通过start启动
     */
    public static Server getServer(int port, TcpConnectCallback connectCallback, BiFunction<Socket, byte[], Object> dataHandler) {
        Server re = null;
        ReceiveRegister receiveRegister = null;
        SendRegister sendRegister = null;

        receiveRegister = new LengthFlagReceiveRegister();

        sendRegister = new LengthFlagSendRegister(null);

        re = new BIOServer(port, receiveRegister, sendRegister, connectCallback, dataHandler);

        return re;
    }
}
