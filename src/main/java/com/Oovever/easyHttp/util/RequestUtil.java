package com.Oovever.easyHttp.util;

import com.Oovever.easyHttp.exception.HttpException;
import com.Oovever.esayTool.io.IORuntimeException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
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
//    通过属性名获取对应的值，利用反射方法，如下：
    private static final ConcurrentMap<Class<?>, PropertyDescriptor[]> typeDescriptorCache = new ConcurrentHashMap<Class<?>, PropertyDescriptor[]>();

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

}
