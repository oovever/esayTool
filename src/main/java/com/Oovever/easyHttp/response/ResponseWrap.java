package com.Oovever.easyHttp.response;

import com.Oovever.easyHttp.exception.JSONException;
import com.Oovever.easyHttp.util.JsonUtil;
import com.Oovever.easyHttp.util.RequestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

/**
 * 相应结果包装
 * @author OovEver
 * 2018/7/3 16:17
 */
public class ResponseWrap {
    private Logger logger = LoggerFactory.getLogger(ResponseWrap.class);

    /**
     * 响应
     */
    private HttpResponse        response;
    /**
     * 请求后的HttpClient
     */
    private CloseableHttpClient httpClient;
    /**
     * 一个可复用的HttpEntity
     */
    private HttpEntity          entity;
    /**
     * 请求对象
     */
    private HttpRequestBase     request;
    /**
     * 请求的上下文环境
     */
    private HttpClientContext   context;

    /**
     * 创建一个ResponseWrap
     * @param httpClient
     * @param request
     * @param response
     * @param context
     */
    public ResponseWrap(CloseableHttpClient httpClient, HttpRequestBase request, HttpResponse response, HttpClientContext context) {
        this.response = response;
        this.httpClient = httpClient;
        this.request = request;
        this.context = context;

        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                this.entity = new BufferedHttpEntity(entity);
            } else {
                this.entity = new BasicHttpEntity();
            }

            EntityUtils.consumeQuietly(entity);
        } catch (IOException e) {
            logger.warn(e.getMessage());
        } finally {
            RequestUtil.closeQuietly(this.response);
        }
    }
    /**
     * 创建一个ResponseWrap,注意使用这构造方法,就意味着有很多非基于HttpResponse的方法不可用
     * @param response response对象
     */
    public ResponseWrap(HttpResponse response) {
        this(null, null, response, null);
    }
    /**
     * 终止请求
     */
    public void abort() {
        request.abort();
    }

    /**
     * 获取重定向的地址
     * @return 重定向地址
     */
    public List<URI> getRedirectUrls() {
        return context.getRedirectLocations();
    }

    /**
     * 获取重定向后的地址,没有重定向地址返回Null
     * @return 重定向地址
     */
    public URI getRedirectUrl(){
        List<URI> urls = getRedirectUrls();

        if(urls == null || urls.size() == 0){
            return null;
        }

        try {
            return URIUtils.resolve(request.getURI(), context.getTargetHost(), getRedirectUrls());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * 关闭httpClient连接
     */
    public void close() {
        RequestUtil.closeQuietly(this.httpClient);
    }

    /**
     * 获取响应内容为String,默认编码为 "UTF-8"
     * @return UTF-8格式的相应内容
     */
    public String getString() {
        return getString(Consts.UTF_8);
    }

    @Override
    public String toString() {
        return getString();
    }

    /**
     * 获取响应内容为String
     * @param defaultCharset 指定编码
     * @return 获取指定编码的相应内容
     */
    public String getString(Charset defaultCharset) {
        try {
            return EntityUtils.toString(entity, defaultCharset);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取响应的类型
     * @return 响应类型
     */
    public Header getContentType() {
        return entity.getContentType();
    }

    /**
     * 获取响应编码,如果是文本的话
     * @return 文本响应编码
     */
    public Charset getCharset() {
        ContentType contentType = ContentType.get(entity);
        if (contentType == null)
            return null;
        return contentType.getCharset();
    }

    /**
     * 获取响应内容为字节数组
     * @return 字节数组
     */
    public byte[] getByteArray() {
        try {
            return EntityUtils.toByteArray(entity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取响应的本地化设置
     * @return 本地设置
     */
    public Locale getLocale(){
        return response.getLocale();
    }

    /**
     * 获取所有Header
     * @return Header信息
     */
    public Header[] getAllHeaders() {
        return response.getAllHeaders();
    }

    /**
     * 获取指定名称的Header列表
     * @return Header列表
     */
    public Header[] getHeaders(String name) {
        return response.getHeaders(name);
    }

    /**
     * 获取定名称的第一个Header
     * @return 第一个Header
     */
    public Header getFirstHeader(String name) {
        return response.getFirstHeader(name);
    }

    /**
     * 获取定名称的最后一个Header
     * @return 最后一个header
     */
    public Header getLastHeader(String name) {
        return response.getLastHeader(name);
    }

    /**
     * 获取响应状态信息
     * @return 响应状态信息
     */
    public StatusLine getStatusLine() {
        return response.getStatusLine();
    }

    /**
     * 移除指定name的Header列表
     * @param name 要移除name的header
     */
    public void removeHeaders(String name) {
        response.removeHeaders(name);
    }

    /**
     * 移除指定的Header
     * @param header 要移除的Header
     */
    public void removeHeader(Header header) {
        response.removeHeader(header);
    }

    /**
     * 移除指定的Header
     * @param name header name
     * @param value header值
     */
    public void removeHeader(String name, String value) {
        response.removeHeader(new BasicHeader(name, value));
    }

    /**
     * 是否存在指定name的Header
     * @param name header名称
     * @return 是否存在指定名称的Header
     */
    public boolean containsHeader(String name) {
        return response.containsHeader(name);
    }

    /**
     * 获取Header的迭代器
     * @return header迭代器
     */
    public HeaderIterator headerIterator() {
        return response.headerIterator();
    }

    /**
     * 获取协议版本信息
     * @return 协议版本信息
     */
    public ProtocolVersion getProtocolVersion() {
        return response.getProtocolVersion();
    }

    /**
     * 获取CookieStore
     * @return CookieStore
     */
    public CookieStore getCookieStore() {
        return context.getCookieStore();
    }

    /**
     * 获取Cookie列表
     * @return Cookie列表
     */
    public List<Cookie> getCookies() {
        return getCookieStore().getCookies();
    }

    /**
     * 获取InputStream,需要手动关闭流
     * @return InputStream
     */
    public InputStream getInputStream() {
        try {
            return entity.getContent();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取BufferedReader
     * @return BufferedReader
     */
    public BufferedReader getBufferedReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharset()));
    }

    /**
     * 响应内容写入到文件
     * @param filePth 路径
     */
    public void transferTo(String filePth) {
        transferTo(new File(filePth));
    }

    /**
     * 响应内容写入到文件
     * @param file 要写入的文件
     */
    public void transferTo(File file) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            transferTo(fileOutputStream);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        finally {
            RequestUtil.closeQuietly(fileOutputStream);
        }
    }

    /**
     * 写入到OutputStream,并不会关闭OutputStream
     * @param outputStream OutputStream
     */
    public void transferTo(OutputStream outputStream) {
        try {
            entity.writeTo(outputStream);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取JSON对象
     * @param clazz 要获取的class
     * @return JSON对象
     */
    public <T> T getJson(Class<T> clazz) {
        try {
            return JsonUtil.toBean(getByteArray(), clazz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把Json转换成List
     * @author mdc
     * @date 2015年7月24日
     * @param clazz
     * @return
     */
    public <T> List<T> getJsonList(Class<T> clazz) {
        try {
            return JsonUtil.toBean(getByteArray(), new TypeReference<List<T>>() {});
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 获取{@link #httpClient}
     */
    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * @return 获取{@link #request}
     */
    public HttpRequestBase getRequest() {
        return request;
    }

    /**
     * @return 获取{@link #context}
     */
    public HttpClientContext getContext() {
        return context;
    }

    /**
     * @return 获取{@link #response}
     */
    public HttpResponse getResponse() {
        return response;
    }
}
