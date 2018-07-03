package com.Oovever.easyHttp.response;

import com.Oovever.easyHttp.util.RequestUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
}
