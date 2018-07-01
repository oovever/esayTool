package com.Oovever.easyHttp.util;

import com.Oovever.easyHttp.exception.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * http请求工具类
 * @author OovEver
 * 2018/6/14 19:59
 */
public class HttpUtil {
//http请求连接池
private static final PoolingHttpClientConnectionManager POOL_MANAGER  = new PoolingHttpClientConnectionManager();
    private static final HttpUtil HTTP_UTIL = new HttpUtil();
//    TLS协议
    public static final String TLS = "TLS";
//    私有化构造函数
    private HttpUtil() {

    }

    /**
     * 设置连接池默认的配置
     * @param defaultConnectionConfig  连接池默认配置
     * @return HttpUtil对象
     */
    public static HttpUtil setDefaultConnectionConfig(final ConnectionConfig defaultConnectionConfig) {
        POOL_MANAGER.setDefaultConnectionConfig(defaultConnectionConfig);
        return HTTP_UTIL;
    }

    /**
     * 设置默认的最大路由数(端到目的的路程)
     * @param maxRoute 最大路由数
     * @return HttpUtil对象
     */
    public static HttpUtil setDefaultMaxPerRoute(final int maxRoute){
        POOL_MANAGER.setDefaultMaxPerRoute(maxRoute);
        return HTTP_UTIL;
    }

    /**
     * 设置默认的Socket配置
     * @param defaultSocketConfig 默认的Socket配置
     * @return HttpUtil对象
     */
    public static HttpUtil setDefaultSocketConfig(final SocketConfig defaultSocketConfig){
        POOL_MANAGER.setDefaultSocketConfig(defaultSocketConfig);
        return HTTP_UTIL;
    }

    /**
     * 设置链接配置
     * @param httpHost http主机地址
     * @param connectionConfig 连接配置
     * @return HttpUtil对象
     */
    public static HttpUtil setConnectionConfig(final HttpHost httpHost, final ConnectionConfig connectionConfig){
        POOL_MANAGER.setConnectionConfig(httpHost, connectionConfig);
        return HTTP_UTIL;
    }
    /**
     * 获取默认Socket配置
     * @return 默认的Socket配置
     */
    public static SocketConfig getDefaultSocketConfig(){
        return POOL_MANAGER.getDefaultSocketConfig();
    }
}
