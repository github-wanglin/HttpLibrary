package com.wl.library.tcp.server;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.wl.library.callback.tcpinterface.ReceiveRegister;
import com.wl.library.callback.tcpinterface.SendRegister;
import com.wl.library.callback.tcpinterface.Server;
import com.wl.library.callback.tcpinterface.TcpServerFailCallback;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

/**
 * author:wanglin
 * date:2020-06-02 14:50
 * description: socket TCP BIO 服务端（同步阻塞）
 */
public class BIOServer implements Server {
    //接收数据的报文定界策略
    private final ReceiveRegister receiveRegister;
    //回复数据的报文定界策略
    private final SendRegister sendRegister;
    //接收数据处理业务逻辑
    private final BiFunction<Socket, byte[], Object> dataHandler;
    //任务线程池
    private final ExecutorService threadPool;
    //主线程线程对象
    private final Thread mainThread;
    //监听端口
    private int port;

    private TcpServerFailCallback tcpServerFailCallback;

    public BIOServer(int port, ReceiveRegister receiveRegister, SendRegister sendRegister, TcpServerFailCallback tcpServerFailCallback, BiFunction<Socket, byte[], Object> dataHandler) {
        this.receiveRegister = receiveRegister;
        this.sendRegister = sendRegister;
        this.tcpServerFailCallback = tcpServerFailCallback;
        this.dataHandler = dataHandler;
        this.threadPool = Executors.newCachedThreadPool();
        mainThread = Thread.currentThread();
        this.port = port;
    }

    @Override
    public void start() {
        ServerSocket server;
        try {
            //阻塞等待连接请求
            server = new ServerSocket(port);
            System.out.println("server start");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("server start error :" + e.getMessage());
            if (tcpServerFailCallback != null) {
                tcpServerFailCallback.serverFail(e);
            }
            return;
        }

        while (!Thread.currentThread().isInterrupted()) {
            final Socket socket;
            final BufferedInputStream in;

            try {
                socket = server.accept();
                System.out.println("建立连接：" + socket.getInetAddress());
                in = new BufferedInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("建立连接失败");
                if (tcpServerFailCallback != null) {
                    tcpServerFailCallback.serverFail(e);
                }
                continue;
            }
            threadPool.execute(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        byte[] result;
                        try {
                            //通过策略模式解耦报文定界策略与本类
                            result = receiveRegister.read(in);
                            //由调用者传入业务处理逻辑，如需返回数据，可通过socket对象获取发送缓冲区并写入返回数据
                            Object returnMsg = dataHandler.apply(socket, result);
                            if (returnMsg != null) {
                                //需回复数据不为null ，则回复数据
                                sendRegister.send(socket, returnMsg);
                            }
                        } catch (IOException e) {
                            System.out.println("an connect is closed with IOException:" + e.getMessage());
                            if (tcpServerFailCallback != null) {
                                tcpServerFailCallback.serverFail(e);
                            }
                            return;
                        }
                    }
                }
            });
        }
    }


    @Override
    public void stop() {
        System.out.println("Stop receive :" + this.port);
        threadPool.shutdown();
        mainThread.interrupt();
    }
}
