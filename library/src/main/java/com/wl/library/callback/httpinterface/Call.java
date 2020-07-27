package com.wl.library.callback.httpinterface;


import com.wl.library.http.response.Response;

/**
 * author:wanglin
 * date:2020/7/6 3:21 PM
 * description:回调接口 支持同步和异步，同步直接返回Response,异步提供一个回调接口回调结果
 */
 public interface Call {

    /**
     * @return response
     */
     Response execute();

    /**
     * @param callback  回调接口
     */
     void enqueue(Callback callback);
}
