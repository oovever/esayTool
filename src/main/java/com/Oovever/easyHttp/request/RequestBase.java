package com.Oovever.easyHttp.request;

import com.Oovever.easyHttp.response.ResponseWrap;
import com.Oovever.easyHttp.util.HttpUtil;
import com.Oovever.easyHttp.util.RequestUtil;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Lookup;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;


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
    /**
     * 构造Request
     * @param request request请求
     */
    public RequestBase(HttpRequestBase request){

        this.request = request;
        this.context = HttpClientContext.create();
        this.clientBuilder = HttpClientBuilder.create().setConnectionManager(connManager);

        this.config = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT);
        this.cookieStore = new BasicCookieStore();
        this.isHttps = request.getURI().getScheme().equalsIgnoreCase("https");
    }
    /**
     * 构造Request,会根据已有的AbstractRequest,
     * 为config, cookieStore, Header赋值
     * @param request request请求
     * @param prevReq 前一个请求
     */
    public RequestBase(HttpRequestBase request, RequestBase prevReq){
        this(request);

        if(prevReq != null) {
            this.config = prevReq.config;
            this.cookieStore = prevReq.cookieStore;
            this.request.setHeaders(prevReq.getAllHeaders());
        }
    }
    /**
     * 构造Request,会根据已有的AbstractRequest,
     * 复制config, cookieStore, Header
     * @param request 请求
     * @param req 前一个请求
     * @param manager 链接管理器
     */
    public RequestBase(HttpRequestBase request, RequestBase req, HttpClientConnectionManager manager){
        this(request, req);
        this.connManager = manager;
    }
    /**
     * 设置参数列表,会覆盖之前设置的参数
     * @param parameters 要设置的参数列表
     * @return 设置结果
     */
    public abstract RequestBase setParameters(final NameValuePair...parameters);
    /**
     * 设置参数列表,会覆盖之前设置的参数
     * @param parameters 要设置的参数列表
     * @return 设置结果
     */
    public abstract RequestBase setParameters(final List<NameValuePair> parameters);
    /**
     * 设置请求参数,会覆盖之前的参数
     * @param parameters 要设置的参数列表
     * @return 设置结果
     */
    public abstract RequestBase setParameters(final Map<String, ?> parameters);

    /**
     * 设置请求参数,会覆盖之前的参数
     * @param file 要设置的参数列表
     * @return 不支持File参数
     */
    public RequestBase setParameter(final File file) {
        throw new UnsupportedOperationException("不支持File参数");
    }

    /**
     * 设置请求参数,会覆盖之前的参数
     * @param binary 要设置的参数列表
     * @return 不支持byte[]参数
     */
    public RequestBase setParameter(final byte[] binary) {
        throw new UnsupportedOperationException("不支持byte[]参数");
    }
    /**
     * 设置请求参数,会覆盖之前的参数
     * @param serializable 要设置的参数列表
     * @return 不支持Serializable参数
     */
    public RequestBase setParameter(final Serializable serializable) {
        throw new UnsupportedOperationException("不支持Serializable参数");
    }
    /**
     * 设置请求参数,会覆盖之前的参数
     * @param object Object类型的参数,例如 简单类型,Map,List,数组,实体等会自动解析为请求参数
     * @return 设置结果
     */
    public abstract RequestBase setParameter(final Object object);

    /**
     * 设置请求参数,会覆盖之前的参数
     * @param text 要设置的参数
     * @return 设置结果
     */
    public abstract RequestBase setParameter(final String text);


    /**
     * 设置请求参数,会覆盖之前的参数
     * @param stream 要设置的参数
     * @return 不支持InputStream参数
     */
    public RequestBase setParameter(final InputStream stream) {
        throw new UnsupportedOperationException("不支持InputStream参数");
    }

    /**
     * 设置参数为Json对象
     * @param parameter 要设置的参数
     * @return 不支持Json对象参数
     */
    public RequestBase setParameterJson(final Object parameter) {
        throw new UnsupportedOperationException("不支持Json对象参数");
    }
    /**
     * 添加参数
     * @param name 参数名称
     * @param value 参数值
     * @return 添加结果
     */
    public abstract RequestBase addParameter(final String name, final String value);
    /**
     * 添加参数
     * @param name 参数名称
     * @param value 参数值
     * @return 添加结果
     */
    public abstract RequestBase addParameter(final String name, final Object value);
    /**
     * 添加参数
     * @param parameters 参数名 参数值
     * @return 添加结果
     */
    public abstract RequestBase addParameters(final NameValuePair ...parameters);
    /**
     * 添加参数
     * @param parameters 参数名
     * @return 添加结果
     */
    public abstract RequestBase addParameters(final List<NameValuePair> parameters);
    /**
     * 添加参数
     * @param parameters 参数名
     * @return 添加结果
     */
    public abstract RequestBase addParameters(final Map<String, ?> parameters);
    /**
     * 获取请求Url
     * @return 返回URL
     */
    public URI getURI(){
        return request.getURI();
    }


    /**
     * 设置请求Url
     * @return RequestBase对象
     */
    public RequestBase setURI(final URI uri){
        request.setURI(uri);
        return this;
    }
    /**
     * 设置请求Url
     * @return 设置结果
     */
    public RequestBase setURI(final String uri){
        return setURI(URI.create(uri));
    }

    /**
     * @return 获取{@link #cookieStore}
     */
    public CookieStore getCookieStore() {
        return cookieStore;
    }
    /**
     * @param cookieStore 设置cookieStore
     * @return 返回RequestBase对象
     */
    public RequestBase setCookieStore(final CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }
    /**
     * 判断是否为Https
     * @return 获取{@link #isHttps}
     */
    public boolean isHttps() {
        return isHttps;
    }

    /**
     * 设置内容编码
     * @param encoding 要设置的编码
     * @return 不支持设置编码
     */
    public RequestBase setContentEncoding(final String encoding) {
        throw new UnsupportedOperationException("不支持设置编码");
    }
    /**
     * 设置ContentType
     * @param contentType 要设置的ContentType
     * @return 不支持设置ContentType
     */
    public RequestBase setContentType(final ContentType contentType) {
        throw new UnsupportedOperationException("不支持设置ContentType");
    }
    /**
     * 设置ContentType
     * @param mimeType 要设置的ContentType
     * @param charset 内容编码
     * @return 不支持设置ContentType
     */
    public RequestBase setContentType(final String mimeType, final Charset charset) {
        throw new UnsupportedOperationException("不支持设置ContentType");
    }
    /**
     * 添加Header
     * @param value value值
     * @return RequestBase设置结果
     */
    public RequestBase addHeader(String name, String value) {
        request.addHeader(name, value);
        return this;
    }
    /**
     * 添加Header
     * @param headers 要设置的headerMap
     * @return RequestBase设置结果
     */
    public RequestBase addHeaders(Map<String, String> headers) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }

        return this;
    }
    /**
     * 设置Header,会覆盖所有之前的Header
     * @param headers 要设置的Header
     * @return 设置结果
     */
    public RequestBase setHeaders(Map<String, String> headers) {
        Header [] headerArray = new Header[headers.size()];
        int i = 0;

        for (Map.Entry<String, String> header : headers.entrySet()) {
            headerArray[i++] = new BasicHeader(header.getKey(), header.getValue());
        }

        request.setHeaders(headerArray);
        return this;
    }
    /**
     * 设置Header,会覆盖所有之前的Header
     * @param headers 要设置的Header
     * @return 设置结果
     */
    public RequestBase setHeaders(Header [] headers) {
        request.setHeaders(headers);
        return this;
    }
    /**
     * 设置Header,会覆盖所有之前的Header
     * @param header 要设置的Header
     * @return 设置结果
     */
    public RequestBase setHeader(Header header) {
        request.setHeader(header);
        return this;
    }
    /**
     * 设置Header,会覆盖所有之前的Header
     * @param key 要设置的key
     * @param name 设置的结果
     * @return 设置结果
     */
    public RequestBase setHeader(String key, String name) {
        request.setHeader(new BasicHeader(key, name));
        return this;
    }

    /**
     * 获取所有Header
     * @return 所有的Header
     */
    public Header[] getAllHeaders() {
        return request.getAllHeaders();
    }
    /**
     * 移除指定name的Header列表
     * @param name 要移除name的header
     * @return 返回RequestBase对象
     */
    public RequestBase removeHeaders(String name){
        request.removeHeaders(name);
        return this;
    }
    /**
     * 移除指定的Header
     * @param header 要移除的header
     * @return 返回RequestBase对象
     */
    public RequestBase removeHeader(Header header){
        request.removeHeader(header);
        return this;
    }
    /**
     * 移除指定的Header
     * @param name 移除header的name
     * @param value 要移除header的value
     * @return 返回RequestBase对象
     */
    public RequestBase removeHeader(String name, String value){
        request.removeHeader(new BasicHeader(name, value));
        return this;
    }
    /**
     * 是否存在指定name的Header
     * @param name 指定的name
     * @return 是否存在指定name的Header
     */
    public boolean containsHeader(String name){
        return request.containsHeader(name);
    }
    /**
     * 获取Header
     * @param name Header 名称
     * @return 获取Header对象
     */
    public Header[] getHeaders(String name){
        return request.getHeaders(name);
    }
    /**
     * 获取第一个Header
     * @param name Header 名称
     * @return 第一个Header
     */
    public Header getFirstHeader(String name){
        return request.getFirstHeader(name);
    }
    /**
     * 获取最后一个Header
     * @param name Header 名称
     * @return 最后一个Header
     */
    public Header getLastHeader(String name){
        return request.getLastHeader(name);
    }
    /**
     * 获取Header的迭代器
     * @return Header的迭代器
     */
    public HeaderIterator headerIterator(){
        return request.headerIterator();
    }

    /**
     * 获取协议版本信息
     * @return 协议版本信息
     */
    public ProtocolVersion getProtocolVersion(){
        return request.getProtocolVersion();
    }
    /**
     * @return 获取{@link #request}
     */
    public HttpRequestBase getRequest() {
        return request;
    }
    /**
     * @return 获取{@link #config}
     */
    public RequestConfig.Builder getConfig() {
        return config;
    }
    /**
     * @return 获取默认的{@link #context},不能获取到用户自定义的HttpClientContext
     */
    public HttpClientContext getContext() {
        return context;
    }
    /**
     * @return 获取{@link #clientBuilder}
     */
    public HttpClientBuilder getClientBuilder() {
        return clientBuilder;
    }
    /**
     * 添加Cookie
     * @param cookies 要添加的Cookie
     * @return 添加后的结果，RequestBase对象
     */
    public RequestBase addCookie(Cookie...cookies){
        if(cookies == null) return this;

        for (int i = 0; i < cookies.length; i++) {
            cookieStore.addCookie(cookies[i]);
        }
        return this;
    }
    /**
     * 设置网络代理
     * @param hostname 网络名称
     * @param port 网络端口
     * @return 设置记过
     */
    public RequestBase setProxy(String hostname, int port) {
        HttpHost proxy = new HttpHost(hostname, port);
        return setProxy(proxy);
    }
    /**
     * 设置网络代理
     * @param hostname 网络名称
     * @param port 网络端口
     * @param schema 网络协议
     * @return 设置结果
     */
    public RequestBase setProxy(String hostname, int port, String schema) {
        HttpHost proxy = new HttpHost(hostname, port, schema);
        return setProxy(proxy);
    }
    /**
     * 设置网络代理
     * @param address 网络地址
     * @return 这是结果
     */
    public RequestBase setProxy(InetAddress address) {
        HttpHost proxy = new HttpHost(address);
        return setProxy(proxy);
    }
    /**
     * 设置网络代理
     * @param host 网络端口
     * @return 设置结果
     */
    public RequestBase setProxy(HttpHost host) {
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(host);
        clientBuilder.setRoutePlanner(routePlanner);
        return this;
    }
    /**
     * 设置Socket超时时间,单位:ms
     * @param socketTimeout 超时时间
     * @return 设置结果
     */
    public RequestBase setSocketTimeout(int socketTimeout){
        config.setSocketTimeout(socketTimeout);
        return this;
    }

    /**
     * 设置连接超时时间,单位:ms
     * @param connectTimeout 超时时间
     * @return 设置结果
     */
    public RequestBase setConnectTimeout(int connectTimeout) {
        config.setConnectTimeout(connectTimeout);
        return this;
    }
    /**
     * 设置请求超时时间,单位:ms
     * @param connectionRequestTimeout 请求超时时间
     * @return 设置结果
     */
    public RequestBase setConnectionRequestTimeout(int connectionRequestTimeout) {
        config.setConnectionRequestTimeout(connectionRequestTimeout);
        return this;
    }
    /**
     * 设置是否允许服务端循环重定向
     * @param circularRedirectsAllowed 服务器端是否允许循环重定向
     * @return 设置结果
     */
    public RequestBase setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
        config.setCircularRedirectsAllowed(circularRedirectsAllowed);
        return this;
    }
    /**
     * 设置是否启用跳转
     * @param redirectsEnabled 是否启用跳转
     * @return 设置结果
     */
    public RequestBase setRedirectsEnabled(boolean redirectsEnabled) {
        config.setRedirectsEnabled(redirectsEnabled);
        return this;
    }
    /**
     * 设置重定向策略
     * HttpClient会自动处理所有类型的重定向
     * @param redirectStrategy 重庆想策略
     * @return 设置结果
     */
    public RequestBase setRedirectStrategy(final RedirectStrategy redirectStrategy) {
        clientBuilder.setRedirectStrategy(redirectStrategy);
        return this;
    }

    /**
     * 设置请求失败后重试次数
     * @param maxTimes 最大重试次数
     * @return 设置结果
     */
    public RequestBase setRetryTimes(final int maxTimes){
        clientBuilder.setRetryHandler(RequestUtil.getTimesRetryHandler(maxTimes));
        return this;
    }
    /**
     * 设置请求失败后重试处理器
     * @param retryHandler 重试处理器
     * @return 设置结果
     */
    public RequestBase setRetryHandler(final HttpRequestRetryHandler retryHandler){
        clientBuilder.setRetryHandler(retryHandler);
        return this;
    }
    /**
     * 设置KeepAlive策略
     * @param keepAliveStrategy KeepAlive策略接口
     * @return 设置结果
     */
    public RequestBase setKeepAliveStrategy(final ConnectionKeepAliveStrategy keepAliveStrategy){
        clientBuilder.setKeepAliveStrategy(keepAliveStrategy);
        return this;
    }
    /**
     * 设置每个路由值的最大连接数
     * @param maxConnPerRoute 大连接数
     * @return 设置结果
     */
    public RequestBase setMaxConnPerRoute(final int maxConnPerRoute){
        clientBuilder.setMaxConnPerRoute(maxConnPerRoute);
        return this;
    }
    /**
     * 设置用户令牌处理器
     * @param userTokenHandler 用户令牌处理器
     * @return 设置结果
     */
    public RequestBase setUserTokenHandler(final UserTokenHandler userTokenHandler){
        clientBuilder.setUserTokenHandler(userTokenHandler);
        return this;
    }
    /**
     * 设置用户令牌
     * @param userToken 用户令牌
     * @return 设置结果
     */
    public RequestBase setUserToken(final Object userToken){
        this.userToken = userToken;
        return this;
    }
    /**
     * 添加请求拦截器
     * @param itcp 请求拦截器
     * @return 返回添加结果
     */
    public RequestBase addInterceptorLast(final HttpRequestInterceptor itcp){
        clientBuilder.addInterceptorLast(itcp);
        return this;
    }

    /**
     * 添加响应拦截器
     * @param itcp 响应拦截器
     * @return 添加结果
     */
    public RequestBase addInterceptorLast(final HttpResponseInterceptor itcp){
        clientBuilder.addInterceptorLast(itcp);
        return this;
    }
    /**
     * 设置重定向的次数
     * @param maxRedirects 重定向次数
     * @return 返回设置结果
     */
    public RequestBase setMaxRedirects(int maxRedirects){
        config.setMaxRedirects(maxRedirects);
        return this;
    }
    /**
     * 设置UserAgent
     * @param userAgent 用户agent
     * @return 设置结果
     */
    public RequestBase setUserAgent(String userAgent) {
        clientBuilder.setUserAgent(userAgent);
        return this;
    }
    /**
     * 设置最大连接数
     * @param maxConnTotal 最大连接数
     * @return 设置结果
     */
    public RequestBase setMaxConnTotal(final int maxConnTotal){
        clientBuilder.setMaxConnTotal(maxConnTotal);
        return this;
    }
    /**
     * 设置目标主机
     * @param host 目标主机
     * @return 设置结果
     */
    public RequestBase setTargetHost(final HttpHost host){
        context.setTargetHost(host);
        return this;
    }
    /**
     * 设置连接管理器
     * @param manager 连接管理器
     * @return 设置结果
     */
    public RequestBase setConnectionManager(HttpClientConnectionManager manager){
        this.connManager = manager;
        return this;
    }
    /**
     * @return 获取{@link #connManager}
     */
    public HttpClientConnectionManager getConnectionManager() {
        return connManager;
    }
    /**
     * 使用浏览器模式上传文件,提交参数
     * @return 上传结果
     */
    public RequestBase useForm(FormPart form){
        this.form = form;
        return this;
    }
    /**
     * 构建请求
     * @param context 请求上下文
     */
    protected abstract void settingRequest(HttpClientContext context);

    /**
     * 设置请求配置项
     */
    private HttpClientContext settings(HttpContext context){

        if (isHttps) {
            if(socketFactory != null) {
                clientBuilder.setSSLSocketFactory(socketFactory);
            }
            else {
                clientBuilder.setSSLSocketFactory(DEFAULT_SSL_SOCKET_FACTORY);
            }
        }

        clientBuilder.setDefaultCookieStore(cookieStore);
        request.setConfig(config.build());
        httpClient = clientBuilder.build();

        HttpClientContext clientContext = null;
        if(context == null){
            clientContext = this.context ;
        } else {
            clientContext = buildContext(context);
        }

        clientContext.setCookieStore(cookieStore);
        clientContext.setRequestConfig(request.getConfig());

        if(userToken != null) {
            clientContext.setUserToken(userToken);
        }

        this.settingRequest(clientContext);
        return clientContext;
    }
    /**
     * 设置HttpContext
     * @param context HttpContext HttpContext对象
     * @return 设置结果
     */
    private HttpClientContext buildContext(HttpContext context) {
        HttpClientContext clientContext = HttpClientContext.adapt(context);

        if (this.context.getCredentialsProvider() != null) {
            clientContext.setCredentialsProvider(this.context.getCredentialsProvider());
        }

        if (this.context.getAuthCache() != null) {
            clientContext.setAuthCache(this.context.getAuthCache());
        }

        if (this.context.getAuthSchemeRegistry() != null) {
            clientContext.setAuthSchemeRegistry(this.context.getAuthSchemeRegistry());
        }

        if (this.context.getTargetHost() != null) {
            clientContext.setTargetHost(this.context.getTargetHost());
        }

        this.context = clientContext;

        return clientContext;
    }
    /**
     * 执行同步请求,使用默认的HttpContext
     * 带有连接池管理的HttpClient
     * @return 返回一个包装了HttpResponse的对象
     */
    public ResponseWrap execute(){
        return execute((HttpContext)null);
    }
    /**
     * 执行同步请求
     * 带有连接池管理的HttpClient
     * @param context httpContext可以包含任意类型的对象，因此如果在多线程中共享上下文会不安全。推荐每个线程都只包含自己的http上下文
     * @return 返回一个包装了HttpResponse的对象
     */
    public ResponseWrap execute(HttpContext context){
        try {

            HttpClientContext     clientContext = settings(context);
            CloseableHttpResponse response      = httpClient.execute(request, clientContext);
            return new ResponseWrap(httpClient, request, response, clientContext);

        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 执行同步请求
     * 带有连接池管理的HttpClient
     * @param responseHandler 使用HttpResponse自定义返回值
     * @return 返回由ResponseHandler处理后的结果
     */
    public <T> T execute(final ResponseHandler<T> responseHandler){
        return execute(responseHandler, null);
    }
    /**
     * 执行同步请求
     * 带有连接池管理的HttpClient
     * @param responseHandler 使用HttpResponse自定义返回值
     * @param context httpContext可以包含任意类型的对象，因此如果在多线程中共享上下文会不安全。推荐每个线程都只包含自己的http上下文
     * @return 返回由ResponseHandler处理后的结果
     */
    public <T> T execute(final ResponseHandler<T> responseHandler, HttpContext context){
        try {
            HttpClientContext clientContext = settings(context);
            return httpClient.execute(request, responseHandler, clientContext);
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    /**
     * 多线程异步请求,获取结果时会阻塞
     * 这里会使用默认上下文
     * @param executorService java ExecutorService
     * @param handler 响应处理器
     * @return 返回一个异步且带有跟踪指标的对象
     */
    public <T> RequestFuture<T> executeCallback(final ExecutorService executorService, final ResponseHandler<T> handler){
        return executeCallback(executorService, settings(null), handler);
    }
    /**
     * 多线程带有回调函数的请求,获取结果时会阻塞
     * @param executorService java ExecutorService
     * @param context 请求上下文
     * @param handler 响应处理器
     * @return 返回一个异步且带有跟踪指标的对象
     */
    public <T> RequestFuture<T> executeCallback(
            final ExecutorService executorService,
            final HttpContext context,
            final ResponseHandler<T> handler){

        return executeCallback(executorService, context, handler, null);
    }
    /**
     * 多线程带有回调函数的请求,获取结果时会阻塞
     * @param executorService java ExecutorService
     * @param handler 响应处理器
     * @param callback 回调函数
     * @return 返回一个异步且带有跟踪指标的对象
     */
    public <T> RequestFuture<T> executeCallback(
            final ExecutorService executorService,
            final ResponseHandler<T> handler,
            final FutureCallback<T> callback){

        return executeCallback(executorService, null, handler, callback);
    }
    /**
     * 多线程带有回调函数的请求,获取结果时会阻塞
     * @param executorService java ExecutorService
     * @param context 请求上下文,如果自定义了HttpContext,那么之前有关HttpContext的设置将被忽略
     * @param handler 响应处理器
     * @param callback 回调函数
     * @return 返回一个异步且带有跟踪指标的对象
     */
    public <T> RequestFuture<T> executeCallback(
            final ExecutorService executorService,
            final HttpContext context,
            final ResponseHandler<T> handler,
            final FutureCallback<T> callback){

        //不能直接把settings(context)写到execute里,否则空指针
        HttpClientContext             clientContext = settings(context);
        FutureRequestExecutionService service       = new FutureRequestExecutionService(httpClient, executorService);
        HttpRequestFutureTask<T>      futureTask    = service.execute(request, clientContext, handler, callback);

        return new RequestFuture<T>(futureTask, service);
    }

}
