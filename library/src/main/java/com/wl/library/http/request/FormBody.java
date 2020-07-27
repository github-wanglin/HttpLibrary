package com.wl.library.http.request;

import android.text.TextUtils;
import android.util.ArrayMap;


import com.wl.library.utils.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * author:wanglin
 * date:2020/7/7 5:58 PM
 * description:表单
 */
public class FormBody extends RequestBody {

    //限制参数不要过多
    public static final int MAX_FORM = 1000;

    final Map<String, String> map;

    public FormBody(Builder builder) {
        this.map = builder.map;
    }

    @Override
    public String contentType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        try {
            outputStream.write(transToString(map).getBytes("UTF-8"));
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private String transToString(Map<String, String> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (!TextUtils.isEmpty(sb)) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(key, "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(map.get(key), "UTF-8"));
        }
        return sb.toString();
    }

    public static class Builder {
        private Map<String, String> map;

        public Builder() {
            map = new HashMap<>();
        }

        public Builder add(String key, String value) {
            if (map.size() > MAX_FORM) throw new IndexOutOfBoundsException("请求参数过多");
            Util.checkMap(key, value);
            map.put(key, value);
            return this;
        }

        public Builder map(Map<String, String> map) {
            if (map.size() > MAX_FORM) throw new IndexOutOfBoundsException("请求参数过多");
            this.map = map;
            return this;
        }

        public FormBody build() {
            return new FormBody(this);
        }
    }
}
