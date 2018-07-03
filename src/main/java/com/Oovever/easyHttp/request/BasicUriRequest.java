package com.Oovever.easyHttp.request;

import com.Oovever.easyHttp.util.RequestUtil;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.HttpClientConnectionManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 带URL参数请求
 * @author OovEver
 * 2018/7/4 0:13
 */
public class BasicUriRequest extends RequestBase {
    /**
     * 请求参数的Builder,用于get, delete请求的参数
     */
    protected URIBuilder uriBuilder;
    /**
     *
     * @param request request请求
     * @param manager connection对象
     */
    public BasicUriRequest(HttpRequestBase request, HttpClientConnectionManager manager) {
        this(request, null, manager);
    }

    /**
     *
     * @param request
     * @param req
     * @param manager
     */
    public BasicUriRequest(HttpRequestBase request, RequestBase req, HttpClientConnectionManager manager) {
        super(request, req, manager);
        this.uriBuilder = new URIBuilder();
    }
    @Override
    public RequestBase setParameters(NameValuePair... parameters) {
        uriBuilder.setParameters(Arrays.asList(parameters));
        return this;
    }

    @Override
    public RequestBase setParameters(List<NameValuePair> parameters) {
        uriBuilder.setParameters(parameters);
        return this;
    }

    @Override
    public RequestBase addParameter(String name, String value) {
        uriBuilder.addParameter(name, value);
        return this;
    }

    @Override
    public RequestBase addParameter(String name, Object value) {
        List<NameValuePair> parameters = RequestUtil.getParameters(name, value);
        if (!parameters.isEmpty()) {
            uriBuilder.addParameters(parameters);
        }
        return this;
    }

    @Override
    public RequestBase addParameters(NameValuePair... parameters) {
        uriBuilder.addParameters(Arrays.asList(parameters));
        return this;
    }

    @Override
    public RequestBase addParameters(final List<NameValuePair> parameters) {
        uriBuilder.addParameters(parameters);
        return this;
    }

    @Override
    public RequestBase addParameters(Map<String, ?> parameters) {
        uriBuilder.addParameters(RequestUtil.convertNameValuePair(parameters));
        return this;
    }

    @Override
    public RequestBase setParameters(Map<String, ?> parameters) {
        uriBuilder.setParameters(RequestUtil.convertNameValuePair(parameters));
        return this;
    }

    @Override
    public RequestBase setParameter(final Object object) {
        List<NameValuePair> parameters = RequestUtil.getParameters(object);
        if (!parameters.isEmpty()) {
            uriBuilder.setParameters(parameters);
        }
        return this;
    }

    @Override
    public RequestBase setParameter(String text) {
        uriBuilder.setParameters(URLEncodedUtils.parse(text, Consts.UTF_8));
        return this;
    }

    @Override
    protected void settingRequest(HttpClientContext context) {

        if(uriBuilder == null || uriBuilder.getQueryParams().size() == 0) {
            return;
        }

        try {
            URI uri = uriBuilder.setPath(request.getURI().toString()).build();
            if (uri != null) {
                request.setURI(uri);
            }
        } catch (URISyntaxException e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
