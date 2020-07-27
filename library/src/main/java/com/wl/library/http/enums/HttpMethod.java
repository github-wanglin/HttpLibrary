package com.wl.library.http.enums;

/**
 * author:wanglin
 * date:2020-06-04 18:24
 * description:请求方式枚举类
 */
public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    public String methodValue;

    HttpMethod(String methodValue) {
        this.methodValue = methodValue;
    }

    public static boolean checkNeedBody(HttpMethod method) {
        return POST.equals(method) || PUT.equals(method);
    }

    public static boolean checkNoBody(HttpMethod method) {
        return GET.equals(method) || DELETE.equals(method);
    }
}
