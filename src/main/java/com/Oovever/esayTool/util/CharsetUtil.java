package com.Oovever.esayTool.util;

import com.Oovever.esayTool.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * @author OovEver
 * 2018/6/7 20:52
 */
public class CharsetUtil {
    /** ISO-8859-1 */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /** UTF-8 */
    public static final String UTF_8 = "UTF-8";
    /** GBK */
    public static final String GBK = "GBK";

    /** ISO-8859-1 */
    public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;
    /** UTF-8 */
    public static final Charset CHARSET_UTF_8      = StandardCharsets.UTF_8;
    /** GBK */
    public static final Charset CHARSET_GBK        = Charset.forName(GBK);
    /**
     * 转换为Charset对象
     * @param charsetName 字符集，为空则返回默认字符集
     * @return Charset
     * @throws UnsupportedCharsetException 编码不支持
     */
    public static Charset charset(String charsetName) throws UnsupportedCharsetException{
        return StringUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }
    /**
     * 转换文件编码<br>
     * 此方法用于转换文件编码，读取的文件实际编码必须与指定的srcCharset编码一致，否则导致乱码
     *
     * @param file 文件
     * @param srcCharset 原文件的编码，必须与文件内容的编码保持一致
     * @param destCharset 转码后的编码
     * @return 被转换编码的文件
     */
    public static File convert(File file, Charset srcCharset, Charset destCharset) {
        final String str = FileUtil.readString(file, srcCharset);
        return FileUtil.writeString(str, file, destCharset);
    }
}
