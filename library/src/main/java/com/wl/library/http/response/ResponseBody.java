package com.wl.library.http.response;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * author:wanglin
 * date:2020-06-05 10:25
 * description:响应体
 */
public class ResponseBody {

    byte[] bytes;

    public ResponseBody(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] bytes() {
        return this.bytes;
    }

    public String string() {
        try {
            return new String(bytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
