package com.Oovever.easyHttp.request;

import com.Oovever.easyHttp.exception.JSONException;
import com.Oovever.easyHttp.util.JsonUtil;
import com.Oovever.easyHttp.util.RequestUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 非url参数请求
 * @author OovEver
 * 2018/7/2 1:32
 */
public class BasicParameterRequest extends RequestBase {
    /**
     * 请求参数的Builder,用于Post, put请求的参数
     */
    protected     EntityBuilder                  builder;
//    请求
    protected HttpEntityEnclosingRequestBase request;

    /**
     * BasicParameterRequest 构造函数
     * @param request 请求
     * @param manager ConnectionManager对象
     */
    public BasicParameterRequest(HttpEntityEnclosingRequestBase request, HttpClientConnectionManager manager) {
        this(request, null, manager);
    }

    /**
     *
     * @param request 请求对象
     * @param req 请求
     * @param manager Manager对象
     */
    public BasicParameterRequest(HttpEntityEnclosingRequestBase request, RequestBase req, HttpClientConnectionManager manager) {
        super(request, req, manager);
        this.request = request;
        this.builder = EntityBuilder.create().setParameters(new ArrayList<NameValuePair>());
    }
    @Override
    public RequestBase setParameters(NameValuePair... parameters) {
        builder.setParameters(parameters);
        return this;
    }

    @Override
    public RequestBase setParameters(List<NameValuePair> parameters) {
        builder.setParameters(parameters);
        return this;
    }

    @Override
    public RequestBase addParameter(String name, String value) {
        builder.getParameters().add(new BasicNameValuePair(name, value));
        return this;
    }

    @Override
    public RequestBase addParameter(String name, Object value) {
        builder.getParameters().add(new BasicNameValuePair(name, value == null? null : value.toString()));
        return this;
    }

    @Override
    public RequestBase addParameters(NameValuePair... parameters) {
        builder.getParameters().addAll(Arrays.asList(parameters));
        return this;
    }

    @Override
    public RequestBase addParameters(final List<NameValuePair> parameters) {
        builder.getParameters().addAll(parameters);
        return this;
    }

    @Override
    public RequestBase addParameters(Map<String, ?> parameters) {
        builder.getParameters().addAll(RequestUtil.convertNameValuePair(parameters));
        return this;
    }

    @Override
    public RequestBase setParameters(Map<String, ?> parameters) {
        builder.setParameters(RequestUtil.convertNameValuePair(parameters));
        return this;
    }

    @Override
    public RequestBase setParameter(File file) {
        builder.setFile(file);
        return this;
    }

    @Override
    public RequestBase setParameter(byte[] binary) {
        builder.setBinary(binary);
        return this;
    }

    @Override
    public RequestBase setParameter(Serializable serializable) {
        builder.setSerializable(serializable);
        return this;
    }

    @Override
    public RequestBase setParameter(final Object object) {
        List<NameValuePair> parameters = RequestUtil.getParameters(object);
        if (!parameters.isEmpty()) {
            builder.setParameters(parameters);
        }
        return this;
    }

    @Override
    public RequestBase setParameterJson(Object parameter) {
        try {
            builder.setBinary(JsonUtil.toJsonBytes(parameter));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public RequestBase setParameter(InputStream stream) {
        builder.setStream(stream);
        return this;
    }

    @Override
    public RequestBase setParameter(String text) {
        builder.setText(text);
        return this;
    }

    @Override
    public RequestBase setContentEncoding(String encoding) {
        builder.setContentEncoding(encoding);
        return this;
    }

    @Override
    public RequestBase setContentType(ContentType contentType) {
        builder.setContentType(contentType);
        return this;
    }

    @Override
    public RequestBase setContentType(String mimeType, Charset charset) {
        this.setContentType(ContentType.create(mimeType, charset));
        return this;
    }

    @Override
    protected void settingRequest(HttpClientContext context) {
        if(super.form != null) {
            this.request.setEntity(super.form.getHttpEntity());
        } else {
            this.request.setEntity(builder.build());
        }
    }
}
