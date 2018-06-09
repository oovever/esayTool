package com.Oovever.esayTool.util;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author OovEver
 * 2018/6/7 20:53
 */
public class StringUtil {
    /** Windows路径分隔符 */
    public static final char C_BACKSLASH = '\\';
    public static final String DOT = ".";
    public static final String EMPTY = "";
    public static final String COLON = ":";
    public static final char C_SLASH = '/';
    public static final String SLASH = "/";
    public static final String DOUBLE_DOT = "..";
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
    /**
     * 比较两个字符串（大小写不敏感）。
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     *
     * @return 如果两个字符串相同，或者都是null，则返回true
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }
    /**
     * 比较两个字符串是否相等。
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同，或者都是null，则返回true
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            //只有两个都为null才判断相等
            return str2 == null;
        }
        if(null == str2) {
            //字符串2空，字符串1非空，直接false
            return false;
        }

        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.equals(str2);
        }
    }
    /**
     * 忽略大小写去掉指定前缀
     *
     * @param str 字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
     */
    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str(str);
        }

        final String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            return str2.substring(prefix.length());// 截取后半段
        }
        return str2;
    }
    /**
     * 字符串是否为空，空的定义如下:<br>
     * 1、为null <br>
     * 2、为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
    /**
     * {@link CharSequence} 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }
    /**
     * 切分字符串<br>
     * a#b#c =》 [a,b,c] <br>
     * a##b#c =》 [a,"",b,c]
     *
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合
     */
    public static List<String> split(String str, String separator) {
        List<String> res = new ArrayList<>();
        if (str == null || str == "") {
            return res;
        }
        for (String sepStr : str.split(separator)) {
            res.add(sepStr);
        }
        return res;

    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * @param stringList 分割后的字符串
     * @param conjunction 连接符
     * @return 连接后的字符串
     */
    public static  String join(List<String> stringList, CharSequence conjunction) {
        String res = "";
        for (String str : stringList) {
            res = res + str + conjunction;
        }
        return res;
    }


}
