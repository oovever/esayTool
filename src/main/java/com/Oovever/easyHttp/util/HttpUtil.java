package com.Oovever.easyHttp.util;

import com.Oovever.easyHttp.exception.HttpException;
import com.Oovever.easyHttp.request.BasicParameterRequest;
import com.Oovever.easyHttp.request.BasicUriRequest;
import com.Oovever.easyHttp.request.RequestBase;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URI;
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
     * @param connectionConfig  连接池默认配置
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
    /**
     * 创建post请求
     * @param url 请求地址
     * @return post请求
     */
    public static RequestBase post(String url) {
        return new BasicParameterRequest(new HttpPost(url), POOL_MANAGER);
    }
    /**
     * 创建get请求
     * @param url 请求地址
     * @return get请求
     */
    public static RequestBase get(String url) {
        return new BasicUriRequest(new HttpGet(url), POOL_MANAGER);
    }
    /**
     * 创建put请求
     * @param url 请求地址
     * @return put请求
     */
    public static RequestBase put(String url) {
        return new BasicParameterRequest(new HttpPut(url), POOL_MANAGER);
    }
    /**
     * 创建delete请求
     * @param url 请求地址
     * @return delete请求
     */
    public static RequestBase delete(String url) {
        return new BasicUriRequest(new HttpDelete(url), POOL_MANAGER);
    }
    /**
     * 创建post请求
     * @param uri 请求地址
     * @return post请求
     */
    public static RequestBase post(URI uri) {
        return post(uri.toString());
    }
    /**
     * 创建get请求
     * @param uri 请求地址
     * @return get请求
     */
    public static RequestBase get(URI uri) {
        return get(uri.toString());
    }
    /**
     * 创建put请求
     * @param uri 请求地址
     * @return put请求
     */
    public static RequestBase put(URI uri) {
        return put(uri.toString());
    }
    /**
     * 创建delete请求
     * @param uri 请求地址
     * @return delete请求
     */
    public static RequestBase delete(URI uri) {
        return delete(uri.toString());
    }
    /**
     * 创建post请求
     * @param url 请求地址
     * @param request 请求
     * @return post请求
     */
    public static RequestBase post(String url, RequestBase request) {
        return new BasicParameterRequest(new HttpPost(url), request, POOL_MANAGER);
    }
    /**
     * 创建get请求
     * @param url 请求地址
     * @param request 请求
     * @return get请求
     */
    public static RequestBase get(String url, RequestBase request) {
        return new BasicUriRequest(new HttpGet(url), request, POOL_MANAGER);
    }
    /**
     * 创建put请求
     * @param url 请求地址
     * @param request 请求
     * @return put请求
     */
    public static RequestBase put(String url, RequestBase request) {
        return new BasicParameterRequest(new HttpPut(url), request, POOL_MANAGER);
    }
    /**
     * 创建delete请求
     * @param url 请求地址
     * @param request 请求
     * @return delete请求
     */
    public static RequestBase delete(String url, RequestBase request) {
        return new BasicUriRequest(new HttpDelete(url), request, POOL_MANAGER);
    }
    /**
     * 创建post请求
     * @param uri 请求地址
     * @param request 请求
     * @return post请求
     */
    public static RequestBase post(URI uri, RequestBase request) {
        return post(uri.toString(), request);
    }

    /**
     * 创建get请求
     * @param uri 请求地址
     * @param request 请求
     * @return get请求
     */
    public static RequestBase get(URI uri, RequestBase request) {
        return get(uri.toString(), request);
    }

    /**
     * 创建put请求
     * @param uri 请求地址
     * @param request 请求
     * @return put请求
     */
    public static RequestBase put(URI uri, RequestBase request) {
        return put(uri.toString(), request);
    }

    /**
     * 创建delete请求
     * @param uri 请求地址
     * @param request 请求
     * @return delete请求
     */
    public static RequestBase delete(URI uri, RequestBase request) {
        return delete(uri.toString(), request);
    }
}
