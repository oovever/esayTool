package com.Oovever.easyHttp.exception;

import java.io.Serializable;

/**
 * JSON异常
 * @author OovEver
 * 2018/7/1 23:25
 */
public class JSONException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public JSONException() {
        super();
    }
    public JSONException(String message) {
        super(message);
    }
    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }
    public JSONException(Throwable cause) {
        super(cause);
    }

}
