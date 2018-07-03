package com.Oovever.easyHttp.request;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.*;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * 使用Form方式提交multipart/form-data表单,模拟浏览器方式提交参数
 * @author OovEver
 * 2018/7/3 0:50
 */
public class FormPart {
    /**
     * 用于上传文件的请求参数构造
     */
    private MultipartEntityBuilder partBuilder;
    /**
     * 通过AbstractRequest构造FormRequest
     */
    private FormPart() {
        partBuilder = MultipartEntityBuilder.create();
        //使用浏览器方式
        partBuilder.setLaxMode();
    }

    /**
     * 通过AbstractRequest构造FormRequest
     * @param name 名称
     * @param text 内容     *
     */
    private FormPart(String name, String text) {
        this();
        addParameter(name, text);
    }
    /**
     * 创建FormPart
     * @return FormPart对象
     */
    public static FormPart create(){
        return new FormPart();
    }
    /**
     * 创建FormPart
     * @return FormPart对象
     */
    public static FormPart create(String name, String text){
        return new FormPart(name, text);
    }

    /**
     *.设置上传的模式
     * @param mode 其中mode主要有BROWSER_COMPATIBLE，RFC6532，STRICT三种，默认值是STRICT
     * @return FormPart对象
     */
    public FormPart setMode(final HttpMultipartMode mode) {
        partBuilder.setMode(mode);
        return this;
    }

    /**
     * 设置模式为HttpMultipartMode.BROWSER_COMPATIBLE
     * @return FormPart对象
     */
    public FormPart setLaxMode() {
        partBuilder.setLaxMode();
        return this;
    }

    /**
     * 设置模式为 HttpMultipartMode.STRICT
     * @return FormPart对象
     */
    public FormPart setStrictMode() {
        partBuilder.setStrictMode();
        return this;
    }

    /**
     * 设置边界
     * @param boundary 边界设置
     * @return FormPart对象
     */
    public FormPart setBoundary(final String boundary) {
        partBuilder.setBoundary(boundary);
        return this;
    }

    /**
     * 设置字符集
     * @param charset 字符集设置
     * @return FormPart对象
     */
    public FormPart setCharset(final Charset charset) {
        partBuilder.setCharset(charset);
        return this;
    }

    /**
     *
     * @param bodyPart 添加Form参数
     * @return FormPart对象
     */
    public FormPart addParameter(final FormBodyPart bodyPart) {
        partBuilder.addPart(bodyPart.getName(), bodyPart.getBody());
        return this;
    }

    /**
     * 添加参数信息
     * @param name 参数名称
     * @param contentBody 参数内容
     * @return FormPart对象
     */
    public FormPart addParameter(final String name, final ContentBody contentBody) {
        partBuilder.addPart(name, contentBody);
        return this;
    }

    /**
     * 添加参数信息
     * @param name 参数名
     * @param text 参数内容
     * @param contentType 参数类型
     * @return FormPart添加结果
     */
    public FormPart addParameter(final String name, final String text, final ContentType contentType) {
        return addParameter(name, new StringBody(text, contentType));
    }
    /**
     * 添加请求参数,默认ContentType设置为text/plain,编码为UTF-8
     * @param name 参数名称
     * @param text 参数值
     * @return FormPart对象
     */
    public FormPart addParameter(final String name, final Serializable text) {
        return addParameter(name, text.toString(), ContentType.TEXT_PLAIN.withCharset("UTF-8"));
    }
    /**
     * 添加请求参数,默认ContentType设置为text/plain,编码为UTF-8
     * @param name 参数名称
     * @param text 参数值
     * @return FormPart对象
     */
    public FormPart addParameter(final String name, final String text) {
        return addParameter(name, text, ContentType.TEXT_PLAIN.withCharset("UTF-8"));
    }

    /**
     * 添加参数信息
     * @param name 参数名称
     * @param bytes 具体内容
     * @param contentType 参数类型
     * @param filename 文件名称
     * @return FormPart对象
     */
    public FormPart addParameter(final String name, final byte[] bytes, final ContentType contentType, final String filename) {
        return addParameter(name, new ByteArrayBody(bytes, contentType, filename));
    }

    /**
     * 添加参数内容
     * @param name 参数名称
     * @param bytes 参数内容
     * @return FormPart对象
     */
    public FormPart addParameter(final String name, final byte[] bytes) {
        return addParameter(name, bytes, ContentType.DEFAULT_BINARY, (String)null);
    }

    /**
     * 添加参数信息
     * @param name 参数名称
     * @param file 文件
     * @param contentType 编码格式
     * @param filename 文件名称
     * @return 添加结果
     */
    public FormPart addParameter(final String name, final File file, final ContentType contentType, final String filename) {
        return addParameter(name, new FileBody(file, contentType, filename));
    }

    /**
     * 添加参数信息
     * @param name 参数名称
     * @param file 文件内容
     * @return 添加结果
     */
    public FormPart addParameter(final String name, final File file) {
        return addParameter(name, file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null);
    }

    /**
     * 添加参数信息
     * @param name 参数名称
     * @param stream 输入流
     * @param contentType 编码类型
     * @param filename 文件内容
     * @return FormPart对象
     */
    public FormPart addParameter(final String name, final InputStream stream, final ContentType contentType, final String filename) {
        return addParameter(name, new InputStreamBody(stream, contentType, filename));
    }

    /**
     * 添加参数信息
     * @param name 参数名称
     * @param stream 输入流
     * @return FormPart对象
     */
    public FormPart addParameter(final String name, final InputStream stream) {
        return addParameter(name, stream, ContentType.DEFAULT_BINARY, (String)null);
    }
    /**
     * 添加参数到参数列表
     * @param parameters 参数列表
     * @return 添加结果
     */
    public FormPart addParameter(final NameValuePair...parameters) {
        if (parameters != null && parameters.length > 0) {
            for (int i = 0, len = parameters.length; i < len; i++) {
                addParameter(parameters[i].getName(), parameters[i].getValue());
            }
        }
        return this;
    }
    /**
     * 获取HttpEntity
     * @return HttpEntity对象
     */
    public HttpEntity getHttpEntity(){
        return partBuilder.build();
    }
}
