package com.wl.library.utils;



import com.wl.library.http.enums.HttpMethod;
import com.wl.library.http.part.MultipartBody;
import com.wl.library.http.request.RequestBody;

import java.io.UnsupportedEncodingException;

/**
 * author:wanglin
 * date:2020-06-04 18:43
 * description:Http 工具类
 */
public class Util {

    public static void checkMap(String key, String value) {
        if (key == null) throw new NullPointerException("key == null");
        if (key.isEmpty()) throw new NullPointerException("key is empty");
        if (value == null) throw new NullPointerException("value == null");
        if (value.isEmpty()) throw new NullPointerException("value is empty");
    }

    public static void checkMethod(HttpMethod method, RequestBody body) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        if (body != null && HttpMethod.checkNoBody(method)) {
            throw new IllegalStateException("方法" + method + "不能有请求体");
        }
        if (body == null && HttpMethod.checkNeedBody(method)) {
            throw new IllegalStateException("方法" + method + "必须有请求体");
        }
    }

    /**
     * 转换成file的头
     *
     * @param key
     * @param fileName
     * @return
     */
    public static String trans2FileHead(String key, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(MultipartBody.disposition)
                .append("name=")
                .append("\"").append(key).append("\"").append(";").append(" ")//key
                .append("filename=")
                .append("\"").append(fileName).append("\"")
                .append("\r\n");

        return sb.toString();
    }

    /**
     * 转换成表单形势
     *
     * @param key
     * @return
     */
    public static String trans2FormHead(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(MultipartBody.disposition)
                .append("name=")
                .append("\"").append(key).append("\"")
                .append("\r\n");
        return sb.toString();
    }

    public static byte[] getUTF8Bytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }
}
