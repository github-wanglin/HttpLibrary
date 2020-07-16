package com.wl.library.tcp.client;

import com.wl.library.callback.tcpinterface.Client;
import com.wl.library.callback.tcpinterface.SendRegister;
import com.wl.library.callback.tcpinterface.TcpConnectCallback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * author:wanglin
 * date:2020-06-02 16:27
 * description:socket TCP BIO 客户端
 */
public class BIOClient implements Client {
    private Socket socket;
    private BufferedOutputStream out;
    private BufferedInputStream in;
    private SendRegister sendRegister;
    private TcpConnectCallback connectCallback;

    public BIOClient(String host, int port, SendRegister sendRegister, TcpConnectCallback tcpConnectCallback) {
        this.sendRegister = sendRegister;
        this.connectCallback = tcpConnectCallback;

        try {
            socket = new Socket(host, port);
            out = new BufferedOutputStream(socket.getOutputStream());
            in = new BufferedInputStream(socket.getInputStream());
            if (connectCallback != null) {
                connectCallback.connected();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public byte[] send(Object data) {
        byte[] re = null;
        try {
            re = sendRegister.send(socket, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re;
    }

    @Override
    public void close() {
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (connectCallback != null) {
                connectCallback.disConnected();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
