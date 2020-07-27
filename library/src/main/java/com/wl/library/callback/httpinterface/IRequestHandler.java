package com.wl.library.callback.httpinterface;




import com.wl.library.http.call.HttpCall;
import com.wl.library.http.response.Response;

import java.io.IOException;

/**
 * author:wanglin
 * date:2020/7/6 5:53 PM
 * description:实际网络请求的发起者
 */
public interface IRequestHandler {

    /**
     * @param call 一次请求发起
     * @return 应答
     * @throws IOException 网路连接或者其他异常
     */
    Response handlerRequest(HttpCall call)throws IOException;
}
