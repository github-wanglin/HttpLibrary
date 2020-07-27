package com.wl.library.http.client;


import com.wl.library.callback.httpinterface.Call;
import com.wl.library.http.call.HttpCall;
import com.wl.library.http.request.Request;

/**
 * author:wanglin
 * date:2020/7/6 5:15 PM
 * description:发起请求的客户端
 */
public class HttpClient {

    private Config config;

    public HttpClient(Builder builder) {
        this.config = new Config(builder);
    }

    public Call newCall(Request request) {
        return new HttpCall(config, request);
    }

    public static class Config {
        public final int connTimeOut;
        public final int readTimeOut;
        public final int writeTimeOut;

        public Config(Builder builder) {
            this.connTimeOut = builder.connTimeOut;
            this.readTimeOut = builder.readTimeOut;
            this.writeTimeOut = builder.writeTimeOUt;
        }
    }

    public static final class Builder {
        private int connTimeOut;
        private int readTimeOut;
        private int writeTimeOUt;

        public Builder() {
            this.connTimeOut = 10 * 1000;
            this.readTimeOut = 10 * 1000;
            this.writeTimeOUt = 10 * 1000;
        }

        public Builder readTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder connTimeOut(int connTimeOut) {
            this.connTimeOut = connTimeOut;
            return this;
        }

        public Builder writeTimeOut(int writeTimeOUt) {
            this.writeTimeOUt = writeTimeOUt;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }
}
