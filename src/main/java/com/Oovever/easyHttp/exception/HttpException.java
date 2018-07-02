package com.Oovever.easyHttp.exception;

import java.io.UnsupportedEncodingException;

/**
 * @author OovEver
 * 2018/7/1 22:54
 */
public class HttpException extends RuntimeException {
    public HttpException(Throwable e) {
        super(e.getMessage(), e);
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Exception e) {
        super(message, e);
    }
}
