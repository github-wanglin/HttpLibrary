package com.wl.library.http.request;

import java.io.IOException;
import java.io.OutputStream;

/**
 * author:wanglin
 * date:2020-06-04 18:02
 * description:请求类型的抽象类
 */
public abstract class RequestBody {

    /**
     * body的类型
     *
     * @return 请求类型
     */
    public abstract String contentType();


    /**
     * @param outputStream 请求内容
     * @throws IOException 抛异常
     */
    public abstract void writeTo(OutputStream outputStream) throws IOException;
}
