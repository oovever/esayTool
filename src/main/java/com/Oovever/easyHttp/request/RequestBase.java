package com.Oovever.easyHttp.request;

import com.Oovever.easyHttp.util.RequestUtil;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Http请求抽象类
 * @author OovEver
 * 2018/7/2 1:33
 */
public abstract class RequestBase {
    //    日志配置
    protected static    Logger                         logger                     = LoggerFactory.getLogger(RequestBase.class);
    /**
     * 默认的SSLSocketFactory
     */
    public static final LayeredConnectionSocketFactory DEFAULT_SSL_SOCKET_FACTORY = RequestUtil.getSSLSocketFactory();
    /**
     * 请求对象
     */
    protected           HttpRequestBase                request;
    /**
     * 连接管理器
     */
    private             HttpClientConnectionManager    connManager;
    /**
     * form表单
     */
    protected           FormPart                       form;
    /**
     * 是否是https请求
     */
    private             boolean                        isHttps;
    /**
     * 用户令牌
     */
    private             Object                         userToken;
    /**
     * cookie存储器
     */
    private             CookieStore                    cookieStore;
    /**
     * 请求的相关配置
     */
    private             RequestConfig.Builder          config;
    /**
     * 请求上下文
     */
    private             HttpClientContext              context;
    private             HttpClientBuilder              clientBuilder; //构建httpclient
    private                 CloseableHttpClient            httpClient;
    private LayeredConnectionSocketFactory socketFactory; //连接工厂
}
