package com.wl.library.http.task;





import com.wl.library.callback.httpinterface.Callback;
import com.wl.library.callback.httpinterface.IRequestHandler;
import com.wl.library.callback.httpinterface.IResponseHandler;
import com.wl.library.http.call.HttpCall;
import com.wl.library.http.response.Response;

import java.io.IOException;

/**
 * author:wanglin
 * date:2020/7/6 5:26 PM
 * description:NetworkDemo
 */
public class HttpTask implements Runnable {
    private HttpCall call;
    private Callback callback;
    private IRequestHandler requestHandler;
    private IResponseHandler handler = IResponseHandler.RESPONSE_HANDLER;

    public HttpTask(HttpCall call, Callback callback, IRequestHandler requestHandler) {
        this.call = call;
        this.callback = callback;
        this.requestHandler = requestHandler;
    }


    @Override
    public void run() {
        Response response;
        try {
            response = requestHandler.handlerRequest(call);
            handler.handlerSuccess(callback, response);
        } catch (IOException e) {
            handler.handFail(callback, call.request, e);
            e.printStackTrace();
        }
    }
}
