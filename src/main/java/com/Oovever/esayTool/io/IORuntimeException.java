package com.Oovever.esayTool.io;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author OovEver
 * 2018/6/4 16:20
 */
public class IORuntimeException extends RuntimeException {
    public IORuntimeException(String message) {
        super(message);
    }
    public IORuntimeException(Throwable e) {
       super(e);
    }

}
