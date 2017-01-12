package com.wangly.okhttputil.utils;

import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by wangly on 2017/1/10.
 */

public class ApiException {


    public static String getExceptionInfo(Throwable throwable) {
        if (throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
            return "无网络!";
        } else if (throwable instanceof SocketTimeoutException) {
            return "网络连接超时···";
        } else if (throwable instanceof FileNotFoundException) {
            return "文件没有发现!";

        } else if (throwable instanceof JsonSyntaxException) {
            return "Json解析异常";
        } else {
            return "未知错误!";
        }
    }
}
