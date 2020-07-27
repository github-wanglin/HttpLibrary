package com.wl.library.http.call;

import com.wl.library.callback.httpinterface.Call;
import com.wl.library.callback.httpinterface.Callback;
import com.wl.library.callback.httpinterface.IRequestHandler;
import com.wl.library.http.client.HttpClient;
import com.wl.library.http.request.Request;
import com.wl.library.http.response.Response;
import com.wl.library.http.response.ResponseBody;
import com.wl.library.http.task.HttpTask;
import com.wl.library.http.task.HttpThreadPool;
import com.wl.library.http.task.RequestHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * author:wanglin
 * date:2020/7/6 5:47 PM
 * description:实体call
 */
public class HttpCall implements Call {
    public final Request request;
    public final HttpClient.Config config;

    private IRequestHandler requestHandler = new RequestHandler();

    public HttpCall(HttpClient.Config config, Request request) {
        this.config = config;
        this.request = request;
    }

    @Override
    public Response execute() {
        Callable<Response> task = new SyncTask();
        Response response;
        try {
            response = HttpThreadPool.getInstance().submit(task);
            return response;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new Response.Builder()
                .code(400)
                .message("线程异常中断")
                .body(new ResponseBody(null))
                .build();
    }

    @Override
    public void enqueue(Callback callback) {
        Runnable runnable = new HttpTask(this, callback, requestHandler);
        HttpThreadPool.getInstance().execute(new FutureTask<>(runnable, null));
    }

    /**
     * 同步提交Callable
     */
    public class SyncTask implements Callable<Response> {

        @Override
        public Response call() throws Exception {
            return requestHandler.handlerRequest(HttpCall.this);
        }
    }
}
