package com.wl.library.http.request;

import android.text.TextUtils;
import android.util.ArrayMap;


import com.wl.library.http.enums.HttpMethod;
import com.wl.library.utils.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * author:wanglin
 * date:2020-06-04 18:09
 * description:使用建造者模式构建请求体
 */
public class Request {

    public final HttpMethod method;
    public final String url;
    public final Map<String, String> heads;
    public final RequestBody body;

    public Request(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.heads = builder.heads;
        this.body = builder.body;
    }

    public static final class Builder {
        HttpMethod method;
        String url;
        Map<String, String> heads;
        RequestBody body;

        public Builder() {
            this.method = HttpMethod.GET;
            this.heads = new HashMap<>();
        }

        public Builder(Request request) {
            this.method = request.method;
            this.url = request.url;

        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder header(String name, String value) {
            Util.checkMap(name, value);
            heads.put(name, value);
            return this;
        }

        public Builder get() {
            method(HttpMethod.GET, null);
            return this;
        }

        public Builder post(RequestBody body) {
            method(HttpMethod.POST, body);
            return this;
        }

        public Builder put(RequestBody body) {
            method(HttpMethod.PUT, body);
            return this;
        }

        public Builder delete(RequestBody body) {
            method(HttpMethod.DELETE, body);
            return this;
        }


        public Builder method(HttpMethod method, RequestBody body) {
            Util.checkMethod(method, body);
            this.method = method;
            this.body = body;
            return this;
        }

        public Request build() {
            if (url == null) {
                throw new IllegalStateException("访问url不能为空");
            }
            if (body != null) {
                if (!TextUtils.isEmpty(body.contentType())) {
                    heads.put("Content-Type", body.contentType());
                }
            }

            heads.put("Connection", "Keep-Alive");
            heads.put("Charset", "UTF-8");
            return new Request(this);
        }
    }

}
