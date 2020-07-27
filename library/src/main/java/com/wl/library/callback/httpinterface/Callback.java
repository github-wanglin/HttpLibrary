package com.wl.library.callback.httpinterface;





import com.wl.library.http.request.Request;
import com.wl.library.http.response.Response;

import java.io.IOException;

/**
 * author:wanglin
 * date:2020/7/6 3:36 PM
 * description:回调接口 成功和失败
 */
public interface Callback {

    /**
     * @param response 返回内容
     */
    void onResponse(Response response);

    /**
     * @param request  请求
     * @param e 异常
     */
    void onFail(Request request, IOException e);
}
