package com.Oovever.easyHttp.util;

import com.Oovever.easyHttp.request.Param;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.*;

/**
 * Http请求参数构建工具
 * 针对使用Form表单提交
 * 对于空值不参与拼装参数
 * @author OovEver
 * 2018/7/3 22:30
 */
public class HttpParamUtil {
    /**
     * 组装后的参数列表
     */
    private List<Param> params = new ArrayList<Param>();
    /**
     * 创建一个HttpParamUtils实例
     */
    private HttpParamUtil() {}
    /**
     * 创建一个HttpParamUtils实例
     * @param value 请求参数值
     */
    private HttpParamUtil(Object value) {
        this(null, value);
    }
    /**
     * 创建一个HttpParamUtils实例
     * @param name 请求参数名称
     * @param value 请求参数值
     */
    private HttpParamUtil(String name, Object value) {
        this.addValue(name, value);
    }
    /**
     * 创建一个HttpParamUtils实例
     * @return HttpParamUtils
     */
    public static HttpParamUtil create() {
        return new HttpParamUtil();
    }

    /**
     * 创建一个HttpParamUtils实例
     * @param value 请求参数值
     * @return HttpParamUtils
     */
    public static HttpParamUtil create(Object value) {
        return new HttpParamUtil(value);
    }
    /**
     * 创建一个HttpParamUtils实例
     * @param name 请求参数名称
     * @param value 请求参数值
     * @return HttpParamUtils
     */
    public static HttpParamUtil create(String name, Object value) {
        return new HttpParamUtil(name, value);
    }

    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Number value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, CharSequence value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Boolean value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, double[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, float[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, int[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, long[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Boolean[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Double[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Float[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Integer[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Long[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, CharSequence[] value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Map<String, ?> value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Iterable<?> value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Collection<?> value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Set<?> value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param name 参数名
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(String name, Object value) {
        return addValue(name, value);
    }
    /**
     * 添加参数信息
     * @param value 参数值
     * @return 添加结果
     */
    public HttpParamUtil addParam(Object value) {
        return addValue(null, value);
    }
    /**
     * 获取URL参数,形如 key=value1&key2=value2
     * @return URL参数
     */
    public String getUri() {
        StringBuilder uri = new StringBuilder();
        for (Param param : params) {
            uri.append('&').append(encodeUri(param.name)).append('=').append(encodeUri(param.value));
        }
//        删除第一个&
        if (!params.isEmpty()) {
            uri.deleteCharAt(0);
        }

        return uri.toString();
    }
    /**
     * 获取name和value转换后的参数列表
     * @return 获取name和value转换后的参数列表
     */
    public List<Param> getParams(){
        return params;
    }

    /**
     * 重写toString方法
     * @return toString方法
     */
    @Override
    public String toString() {
        return getUri();
    }
    /**
     * 添加参数
     * @param name 参数名称 ,可以是一个实体类,但不支持文件形式
     * @param value 参数值
     * @return 添加结果
     */
    private HttpParamUtil addValue(String name, Object value) {
        if (value == null) {
            return this;
        }


        Class<?> clazz = value.getClass();
        // 简单类型
        if (RequestUtil.isSimpleValueType(clazz)) {
            params.add(new Param(name, value));
            return this;
        }

        if (clazz.isArray()) {
            int length = Array.getLength(value);

            // 简单类型（数组）,直接组装
            if (RequestUtil.isSimpleValueType(clazz.getComponentType())) {
                for (int i = 0; i < length; i++) {
                    params.add(new Param(name, Array.get(value, i)));
                }
                return this;
            }

            // 对象类型
            for (int i = 0; i < length; i++) {
                addValue((name == null ? "" : name) + "[" + i + "]", Array.get(value, i));
            }
            return this;
        }

        // 处理Map集合
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, ?> mapValue = (Map<String, ?>) value;

            for (Map.Entry<String, ?> item : mapValue.entrySet()) {
                if (item.getValue() == null) {
                    continue;
                }

                if (RequestUtil.isSimpleValueType(item.getValue().getClass())) {
                    params.add(new Param(name == null ? item.getKey() : name + "['" + item.getKey() + "']", item.getValue()));
                    continue;
                }

                addValue(name == null ? item.getKey() : name + "['" + item.getKey() + "']", item.getValue());
            }

            return this;
        }

        if (value instanceof Iterable) {
            Iterable<?> listvalue = (Iterable<?>) value;
            Object object;
            int i = 0;

            for (Iterator<?> iterator = listvalue.iterator(); iterator.hasNext();) {
                object = iterator.next();
                if (object == null) {
                    continue;
                }

                if (RequestUtil.isSimpleValueType(object.getClass())) {
                    params.add(new Param((name == null ? "" : name) + "[" + i + "]", object));
                    i++;
                    continue;
                }

                addValue((name == null ? "" : name) + "[" + i + "]", object);
                i++;
            }

            return this;
        }
//获取clazz类的PropertyDescriptor
        PropertyDescriptor[] descriptors = RequestUtil.getPropertyDescriptors(clazz);

        Object object = null;
        Method readMethod;

        for (int i = 0; i < descriptors.length; i++) {
            readMethod = descriptors[i].getReadMethod();
//            Java的每个类都带有一个运行时类对象，该Class对象中保存了创建对象所需的所有信息。
            if (readMethod == null || "getClass".equals(readMethod.getName())) {
                continue;
            }
            try {
//如果是Public方法
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                object = readMethod.invoke(value);
            } catch (Throwable ex) {
                throw new IllegalArgumentException("Could not read property'" + descriptors[i].getName() + "' from source to target", ex);
            }

            if (object == null) {
                continue;
            }

            if (RequestUtil.isSimpleValueType(object.getClass())) {
                params.add(new Param((name == null ? "" : name + ".") + descriptors[i].getDisplayName(), object));
                continue;
            }
            addValue((name == null ? "" : name + ".") + descriptors[i].getDisplayName(), object);
        }
        return this;
    }
    /**
     * 对参数编码
     *
     * @param string 要编码的字符串
     * @return 返回以UTF-8格式编码的字符串
     */
    private String encodeUri(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }
}
