package com.wl.library.tcp.type;

import com.wl.library.callback.tcpinterface.ReceiveRegister;
import com.wl.library.callback.tcpinterface.SendRegister;
import com.wl.library.utils.IOUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * author:wanglin
 * date:2020-06-02 16:37
 * description:按长度标识定界的发送策略
 */
public class LengthFlagSendRegister implements SendRegister {
    private final ReceiveRegister receiveRegister;

    public LengthFlagSendRegister(ReceiveRegister receiveRegister) {
        this.receiveRegister = receiveRegister;
    }

    @Override
    public byte[] send(Socket socket, byte[] data) throws IOException {
        byte[] result;
        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
        int length = data.length;
        //写入长度
        out.write(ByteBuffer.allocate(4).putInt(length).array());
        //写入数据
        out.write(data);
        out.flush();
        //如果未传入接收报文定界策略，则不需要接收回复数据
        if (receiveRegister == null) {
            return null;
        }
        while (true) {
            if (socket.getInputStream().available() > 0) break;
        }
        result = receiveRegister.read(socket.getInputStream());
        return result;
    }
}
