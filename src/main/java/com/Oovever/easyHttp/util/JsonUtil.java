package com.Oovever.easyHttp.util;

import com.Oovever.easyHttp.exception.JSONException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

/**
 * Json工具类
 * @author OovEver
 * 2018/7/1 23:32
 */
public class JsonUtil {
//   将属性转化为JSON字符串
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        设置时间格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //反序列化时候，如果json串中的字段出现String("")或者null的就把java对应属性设置为Null
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//        特性决定parser将是否允许解析使用Java/C++ 样式的注释（包括'/'+'*' 和'//' 变量）。 由于JSON标准说明书上面没有提到注释是否是合法的组成，所以这是一个非标准的特性；
//         * 尽管如此，这个特性还是被广泛地使用。
//         *
//         * 注意：该属性默认是false，因此必须显式允许，即通过JsonParser.Feature.ALLOW_COMMENTS 配置为true
        mapper.getFactory().enable(JsonParser.Feature.ALLOW_COMMENTS);
        // 允许单引号
        mapper.getFactory().enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);

        //只输出非空属性到Json字符串，属性为null不进行序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    /**
     *
     * 把json输入流中的内容转换为指定类型的对象
     *
     * @param jsonInputStream JSON输入流
     * @param type 类型
     * @return 转化后的对象
     * @throws JSONException JSON异常
     */
    public static <T> T toBean(InputStream jsonInputStream, TypeReference<T> type) throws JSONException {
        try {
            return mapper.readValue(jsonInputStream, type);
        } catch (Exception t) {
            throw new JSONException("把json输入流中的内容转换为指定类型的对象（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 把json输入流中的内容转换为指定类型的对象
     *
     * @param jsonInputStream JSON输入流
     * @param clazz 类型
     * @return 转化后的对象
     * @throws JSONException JSON异常
     */
    public static <T> T toBean(InputStream jsonInputStream, Class<T> clazz) throws JSONException {
        try {
            return mapper.readValue(jsonInputStream, clazz);
        } catch (Exception t) {
            throw new JSONException("把json输入流中的内容转换为指定类型的对象（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 把json字节数组转换为指定类型的对象
     *
     * @param jsonByteArray JSON字节数组
     * @param type 要转化的类型
     * @return 转化后的JSON对象
     * @throws JSONException JSON转化过程中出现的异常
     */
    public static <T> T toBean(byte[] jsonByteArray, TypeReference<T> type) throws JSONException {
        try {
            return mapper.readValue(jsonByteArray, type);
        } catch (Exception t) {
            throw new JSONException("把json字节数组转换为指定类型的对象是出错（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 把json字节数组转换为指定类型的对象
     *
     * @param jsonByteArray JSON字节数组
     * @param clazz 要转化的类型
     * @return 转化后的JSON对象
     * @throws JSONException 转化后出现异常
     */
    public static <T> T toBean(byte[] jsonByteArray, Class<T> clazz) throws JSONException {
        try {
            return mapper.readValue(jsonByteArray, clazz);
        } catch (Exception t) {
            throw new JSONException("把json字节数组转换为指定类型的对象是出错（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 把json字符串转换为指定类型的对象
     *
     * @param jsonString JSON字符串
     * @param type JSON类型 TypeReference明确指定反序列化类型
     * @return 转化后的JSON对象
     * @throws JSONException 转化过程中出现的异常
     */
    public static <T> T toBean(String jsonString, TypeReference<T> type) throws JSONException {
        try {
            return mapper.readValue(jsonString, type);
        } catch (Exception t) {
            throw new JSONException("把json字符串转换为指定类型的对象出错（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 转换json字符串为指定对象
     *
     * @param jsonString JSON字符串
     * @param clazz JSON类型
     * @return 转化后的JSON对象
     * @throws JSONException 转化过程中出现的异常
     */
    public static <T> T toBean(String jsonString, Class<T> clazz) throws JSONException {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (Exception t) {
            throw new JSONException("把json字符串转换为指定类型的对象出错（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 把object转换为指定类型的对象
     *
     * @param obj object对象
     * @param type 要转化的类型
     * @return 转化后的Object对象
     * @throws JSONException 转化过程中出现异常
     */
    public static <T> T toBean(Object obj, TypeReference<T> type) throws JSONException {
        if (obj instanceof String) {
            return toBean(obj.toString(), type);
        }
        try {
            return mapper.convertValue(obj, type);
        } catch (Exception t) {
            throw new JSONException("把obj转换为指定类型的对象出错（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 把obj转换为指定对象
     *
     * @param obj object对象
     * @param clazz 要转化的类型
     * @return 转化后的Object对象
     * @throws JSONException 转化过程中出现异常
     */
    public static <T> T toBean(Object obj, Class<T> clazz) throws JSONException {
        if (obj instanceof String) {
            return toBean(obj.toString(), clazz);
        }
        try {
            return mapper.convertValue(obj, clazz);
        } catch (Exception t) {
            throw new JSONException("把obj转换为指定对象出错（" + t.getMessage() + ")", t);
        }
    }
    /**
     *
     * 从reader中读取json信息并转换为指定对象
     *
     * @param reader reader对象
     * @param type 要转化的类型
     * @return 转化后的对象
     * @throws JSONException 从reader中读取json信息并转换为指定对象出错
     */
    public static <T> T toBean(Reader reader, TypeReference<T> type) throws JSONException {
        try {
            return mapper.readValue(reader, type);
        } catch (Exception t) {
            throw new JSONException("从reader中读取json信息并转换为指定对象出错（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 从reader中读取json信息并转换为指定对象
     *
     * @param reader reader对象
     * @param clazz 要转化的类型
     * @return 转化后的对象
     * @throws JSONException 从reader中读取json信息并转换为指定对象出错
     */
    public static <T> T toBean(Reader reader, Class<T> clazz) throws JSONException {
        try {
            return mapper.readValue(reader, clazz);
        } catch (Exception t) {
            throw new JSONException("从reader中读取json信息并转换为指定对象出错（" + t.getMessage() + "）", t);
        }
    }
    /**
     *
     * 把object对象转换为json字符串
     *
     * @param obj object对象
     * @return JSON字符串
     * @throws JSONException 把obj转换为json字符串出错
     */
    public static String toJson(Object obj) throws JSONException {
        if (obj == null) {
            return "{}";
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception t) {
            throw new JSONException("把obj转换为json字符串出错（" + t.getMessage() + "）", t);
        }

    }
    /**
     *
     * 把obj转换为json字节数组
     *
     * @param obj object对象
     * @return 将object对象转化为JSON字节数组
     * @throws JSONException 把obj转换为json字符串出错
     */
    public static byte[] toJsonBytes(Object obj) throws JSONException {
        if (obj == null) {
            return "{}".getBytes(Charset.forName("UTF-8"));
        }

        try {
            return mapper.writeValueAsBytes(obj);
        } catch (Exception t) {
            throw new JSONException("把obj转换为json字符串出错（" + t.getMessage() + "）", t);
        }

    }
    /**
     * @return 返回mapper对象
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }

}
