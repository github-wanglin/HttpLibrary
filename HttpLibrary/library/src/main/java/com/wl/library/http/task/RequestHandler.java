package com.wl.library.http.task;

import com.wl.library.callback.httpinterface.IRequestHandler;
import com.wl.library.http.call.HttpCall;
import com.wl.library.http.enums.HttpMethod;
import com.wl.library.http.request.Request;
import com.wl.library.http.request.RequestBody;
import com.wl.library.http.response.Response;
import com.wl.library.http.response.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

/**
 * author:wanglin
 * date:2020/7/7 5:24 PM
 * description:网络请求处理者
 */
public class RequestHandler implements IRequestHandler {
    @Override
    public Response handlerRequest(HttpCall call) throws IOException {
        HttpURLConnection connection = mangeConfig(call);
        if (!call.request.heads.isEmpty()) addHeaders(connection, call.request);
        if (call.request.body != null) writeContent(connection, call.request.body);
        if (!connection.getDoOutput()) connection.connect();

        //解析返回内容
        int responseCode = connection.getResponseCode();
        if (responseCode >= 200 && responseCode < 400) {
            byte[] bytes = new byte[1024];
            int length;
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((length = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, length);
            }
            Response response = new Response.Builder()
                    .code(responseCode)
                    .message(connection.getResponseMessage())
                    .body(new ResponseBody(byteArrayOutputStream.toByteArray()))
                    .build();
            try {
                inputStream.close();
                connection.disconnect();
            } finally {
                inputStream.close();
                connection.disconnect();
            }
            return response;
        }
        throw new IOException(String.valueOf(connection.getResponseCode()));
    }

    /**
     * @param connection 连接
     * @param body       请求内容
     * @throws IOException 异常
     */
    private void writeContent(HttpURLConnection connection, RequestBody body) throws IOException {
        OutputStream ous = connection.getOutputStream();
        body.writeTo(ous);
    }

    /**
     * HttpURLConnection基本参数配置
     *
     * @param call 请求
     * @return HttpURLConnection
     * @throws IOException 网络异常
     */
    private HttpURLConnection mangeConfig(HttpCall call) throws IOException {
        URL url = new URL(call.request.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(call.config.connTimeOut);
        connection.setReadTimeout(call.config.readTimeOut);
        connection.setDoInput(true);
        if (call.request.body != null && HttpMethod.checkNeedBody(call.request.method)) {
            connection.setDoOutput(true);
        }
        return connection;
    }

    /**
     * 给HttpURLConnection添加请求头
     *
     * @param connection HttpURLConnection对象
     * @param request    请求对象
     */
    private void addHeaders(HttpURLConnection connection, Request request) {
        Set<String> keys = request.heads.keySet();
        for (String key : keys) {
            connection.addRequestProperty(key, request.heads.get(key));
        }

    }
}
