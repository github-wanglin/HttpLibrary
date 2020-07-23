package com.wl.library.tcp.type;



import com.wl.library.callback.tcpinterface.ReceiveRegister;
import com.wl.library.utils.IOUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * author:wanglin
 * date:2020-06-02 11:36
 * description:按长度标识读取报文的读取策略，每次报文头4个字节标识本次请求的报文长度，用于划定请求边界
 */
public class LengthFlagReceiveRegister implements ReceiveRegister {


    @Override
    public byte[] read(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is null");
        }

        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] result;
        //读取头4个字节，转换为int即为报文长度
        byte[] lengthByte = IOUtil.readBytesFromInputStream(bufferedInputStream, 4);
        int length = ByteBuffer.wrap(lengthByte).getInt();


        //读取指定长度字节
        result = IOUtil.readBytesFromInputStream(bufferedInputStream, length);
        System.out.println("from receive:" + new String(result));

        return result;
    }

}
