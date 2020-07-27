package com.wl.library.callback.httpinterface;

import android.os.Handler;
import android.os.Looper;


import com.wl.library.http.request.Request;
import com.wl.library.http.response.Response;

import java.io.IOException;

/**
 * author:wanglin
 * date:2020/7/6 5:56 PM
 * description:NetworkDemo
 */
public interface IResponseHandler {

    /**
     * @param callback 回调接口
     * @param response 返回结果
     */
    void handlerSuccess(Callback callback, Response response);

    /**
     * @param callback 回调接口
     * @param request  请求
     * @param e        异常
     */
    void handFail(Callback callback, Request request, IOException e);

    IResponseHandler RESPONSE_HANDLER = new IResponseHandler() {

        Handler HANDLER = new Handler(Looper.getMainLooper());

        @Override
        public void handlerSuccess(final Callback callback, final Response response) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    callback.onResponse(response);
                }
            };
            execute(runnable);
        }

        @Override
        public void handFail(final Callback callback, final Request request, final IOException e) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    callback.onFail(request, e);
                }
            };
            execute(runnable);
        }

        /**
         * 移除所有消息
         */
        public void removeAllMessage() {
            HANDLER.removeCallbacksAndMessages(null);
        }

        /**
         * @param runnable  线程切换
         */
        public void execute(Runnable runnable) {
            HANDLER.post(runnable);
        }

    };
}
