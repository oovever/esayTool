package com.Oovever.easyHttp.util;

import com.Oovever.easyHttp.exception.HttpException;
import com.Oovever.easyHttp.request.Param;
import com.Oovever.esayTool.io.IORuntimeException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 请求工具类
 * @author OovEver
 * 2018/7/2 1:37
 */
public class RequestUtil {
    //    日志配置
    private static       Logger                                        logger = LoggerFactory.getLogger(RequestUtil.class);
//    PropertyDescriptor类表示JavaBean类通过存储器导出一个属性。主要方法：
//
//            1、getPropertyType()，获得属性的Class对象。
//
//            2、getReadMethod()，获得用于读取属性值的方法；getWriteMethod()，获得用于写入属性值的方法。
//
//            3、hashCode()，获取对象的哈希值。
//
//            4、setReadMethod(Method readMethod)，设置用于读取属性值的方法；setWriteMethod(MethodwriteMethod)，设置用于写入属性值的方法；
//
//    导包java.bean.*;
//
    private static final ConcurrentMap<Class<?>, PropertyDescriptor[]> typeDescriptorCache = new ConcurrentHashMap<Class<?>, PropertyDescriptor[]>();
//在IdentityHashMap中，判断两个键值k1和 k2相等的条件是 k1 == k2 。在正常的Map 实现（如 HashMap）中，当且仅当满足下列条件时才认为两个键 k1 和 k2 相等：(k1==null ? k2==null : e1.equals(e2))。
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<Class<?>, Class<?>>(8){
        private static final long serialVersionUID = -3990873552652201588L;
        {
            put(Boolean.class, boolean.class);
            put(Byte.class, byte.class);
            put(Character.class, char.class);
            put(Double.class, double.class);
            put(Float.class, float.class);
            put(Integer.class, int.class);
            put(Long.class, long.class);
            put(Short.class, short.class);
        }
    };
    /**
     * 关闭OutputStream
     * @param closeable OutPutStream关闭异常
     */
    public static void closeQuietly(OutputStream closeable) throws HttpException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new HttpException("OutputStream流关闭异常", e);
            }
        }
    }
    /**
     * 关闭InputStream
     * @param closeable 要关闭的InputStream流
     */
    public static void closeQuietly(InputStream closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            throw new HttpException("InputStream流关闭异常", e);
        }
    }

    /**
     * 关闭ImageInputStream
     * @param closeable ImageInputStream流
     */
    public static void closeQuietly(ImageInputStream closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            throw new HttpException("ImageInputStream流关闭异常", e);
        }
    }

    /**
     * 关闭ImageOutputStream 流
     * @param closeable ImageOutputStream流
     */
    public static void closeQuietly(ImageOutputStream closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            throw new HttpException("ImageOutputStream流关闭异常", e);
        }
    }

    /**
     * 关闭Http相应
     * @param closeable HttpResponse对象
     */
    public static void closeQuietly(HttpResponse closeable) {
        try {
            if (closeable != null && closeable instanceof CloseableHttpResponse) {
                ((CloseableHttpResponse)closeable).close();
            }
        } catch (IOException e) {
            throw new HttpException("HttpResponse关闭异常", e);
        }
    }

    /**
     * 关系HttpClient
     * @param closeable CloseableHttpClient对象
     */
    public static void closeQuietly(CloseableHttpClient closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            throw new HttpException("CloseableHttpClient关闭异常", e);
        }
    }
    /**
     * 当访问url的schema为https时，调用SSL连接套接字工厂来建立连接
     * 获取LayeredConnectionSocketFactory 使用ssl单向认证
     * @return 获取LayeredConnectionSocketFactory
     * HttpClient4.3.x请求https的通用方法
     */
    public static LayeredConnectionSocketFactory getSSLSocketFactory() {
//        SSLSocket扩展了Socket并提供SSL或者TSL套接字在TCP上加安全保护层
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 所有证书可信
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
//在使用httpclient访问https网站的时候，经常会遇到javax.net.ssl包中的异常
//创建不校验证书链的SSLContext
            return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    /**
     * NameValuePair 一个简单的类 封装一个名称/值对
     * 把Map参数转换为List
     * @param parameters Map类型的参数
     * @return 转化后的List
     */
    public static List<NameValuePair> convertNameValuePair(final Map<String, ?> parameters) {
        List<NameValuePair> values = new ArrayList<NameValuePair>(parameters.size());

        String value = null;
        for (Map.Entry<String, ?> parameter : parameters.entrySet()) {
            value = parameter.getValue() == null? null : parameter.getValue().toString();
            values.add(new BasicNameValuePair(parameter.getKey(), value));
        }
        return values;
    }

    /**
     * 最大重试次数设置
     * @param maxTimes 重试次数
     * @return HttpRequestRetryHandler自定义 重试次数以及重试的时候业务处理
     */
    public static HttpRequestRetryHandler getTimesRetryHandler(final int maxTimes) {
//        HttpRequestRetryHandler自定义 重试次数以及重试的时候业务处理
        return new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

                if (executionCount >= maxTimes) {
                    // 如果超过最大重试次数，不重试
                    return false;
                }

                if (exception instanceof InterruptedIOException) {
                    // 超时
                    return false;
                }

                if (exception instanceof UnknownHostException) {
//                    未知异常
                    return false;
                }

                if (exception instanceof ConnectTimeoutException) {
                    // 连接被拒绝
                    return false;
                }

                if (exception instanceof SSLException) {
                    // SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest       request       = clientContext.getRequest();
                boolean           idempotent    = !(request instanceof HttpEntityEnclosingRequest);

                if (idempotent) {
                    // 如果请求被认为是等幂，则重试
                    return true;
                }

                return false;
            };
        };
    }
    /**
     * 判断给定的类型是否是简单类型
     * @param clazz 要检查的类型
     * @return 给定的类型是否为简单类型
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
//        java.lang.Class.isEnum() 当且仅当这个类被声明为在源代码中的枚举返回true。
        return ((clazz.isPrimitive() || primitiveWrapperTypeMap.containsKey(clazz)) || clazz.isEnum() ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                URI.class == clazz || URL.class == clazz ||
                Locale.class == clazz || Class.class == clazz);
    }
    /**
     * 获取Class的属性列表
     * @param clazz 要获取的Class属性列表
     * @return class的属性列表
     * @throws IllegalArgumentException 非法参数异常
     */
    public static final PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IllegalArgumentException {
        try {
            PropertyDescriptor[] props = typeDescriptorCache.get(clazz);
            if (props != null) {
                return props;
            }

            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            props = beanInfo.getPropertyDescriptors();

            typeDescriptorCache.put(clazz, props);
            return props;
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 把Object对象转换为请求参数,以Key/Value形式返回列表
     * @param object 参数对象,可以是实体类,Map,List<Map> 等
     * @return ,以Key/Value形式返回列表
     */
    public static List<NameValuePair> getParameters(final Object object) {
        return getParameters(null, object);
    }

    /**
     * 把Object对象转换为请求参数,以Key/Value形式返回列表
     * @param name 参数名称
     * @param object 参数对象,可以是实体类,Map,List<Map> 等
     * @return ,以Key/Value形式返回列表
     */
    public static List<NameValuePair> getParameters(final String name, final Object object) {
        List<Param> params = HttpParamUtil.create(name, object).getParams();

        if (params.isEmpty()) {
            return Collections.<NameValuePair>emptyList();
        }

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(params.size());
        for (Param param : params) {
            parameters.add(new BasicNameValuePair(param.name, param.value));
        }

        return parameters;
    }
}
