package com.Oovever.esayTool.util;

import com.sun.xml.internal.ws.util.UtilException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author OovEver
 * 2018/6/8 16:30
 */
public class URLUtil {
    /**
     * 转URL为URI
     *
     * @param url URL
     * @return URI
     * @exception UtilException 包装URISyntaxException
     */
    public static URI toURI(URL url) throws UtilException {
        if (null == url) {
            return null;
        }
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new UtilException(e);
        }
    }
}
