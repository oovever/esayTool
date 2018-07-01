package com.Oovever.easyHttp.util;

import com.Oovever.easyHttp.exception.HttpException;

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
//    <meta> 元素可提供有关页面的元信息（meta-information），比如针对搜索引擎和更新频度的描述和关键词。
//
//<meta> 标签位于文档的头部，不包含任何内容。<meta> 标签的属性定义了与文档相关联的名称/值对。
public static final Pattern CHARSET_PATTERN = Pattern.compile("<meta.*?charset=(.*?)\"");
    /**
     * 编码字符为 application/x-www-form-urlencoded
     *
     * @param content 被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encode(String content, Charset charset) {
        if (null == charset) {
            charset = CharsetUtil.defaultCharset();
        }
        return encode(content, charset.name());
    }
    /**
     * 编码字符为 application/x-www-form-urlencoded
     *
     * @param content 被编码内容
     * @param charsetStr 编码
     * @return 编码后的字符
     * @throws HttpException 编码不支持
     */
    public static String encode(String content, String charsetStr) throws HttpException {
        if (StringUtil.isBlank(content)) {
            return content;
        }

        String encodeContent = null;
        try {
            encodeContent = URLEncoder.encode(content, charsetStr);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException("Unsupported encoding:"+charsetStr, e);
        }
        return encodeContent;
    }
    /**
     * 解码application/x-www-form-urlencoded字符
     *
     * @param content 被解码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String decode(String content, Charset charset) {
        return decode(content, charset.name());
    }
    /**
     * 解码application/x-www-form-urlencoded字符
     *
     * @param content 被解码内容
     * @param charsetStr 编码
     * @return 编码后的字符
     */
    public static String decode(String content, String charsetStr) {
        if (StringUtil.isBlank(content)) {
            return content;
        }
        String encodeContnt = null;
        try {
            encodeContnt = URLDecoder.decode(content, charsetStr);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException("Unsupported encoding:"+charsetStr, e);
        }
        return encodeContnt;
    }


}
