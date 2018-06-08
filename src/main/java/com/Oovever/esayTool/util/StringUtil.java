package com.Oovever.esayTool.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author OovEver
 * 2018/6/7 20:53
 */
public class StringUtil {
    public static final String EMPTY = "";
    /**
     * 字符串是否为空白 空白的定义如下： <br>
     * 1、为null <br>
     * 2、为不可见字符（如空格）<br>
     * 3、""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isBlank(CharSequence str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (false == isBlankChar(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     * @param c 字符
     * @return 是否空白符
     * @since 3.0.6
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c);
    }
    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder() {
        return new StringBuilder();
    }
    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String BuffetToString(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }
    /**
     * 编码字符串
     *
     * @param str 字符串 CharSequence就是字符序列，String, StringBuilder和StringBuffer本质上都是通过字符数组实现的
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        }

        if (null == charset) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }

    /**
     * 将Object对象转化为String
     * @param value Object的值
     * @param defaultValue 如果Object为Null时取的转化默认值
     * @return Object转化后的String
     */
    public static String ObjectToString(Object value, String defaultValue) {
        if (value == null || value.equals(null)) {
            return defaultValue;
        }
        return value.toString();

    }

}
