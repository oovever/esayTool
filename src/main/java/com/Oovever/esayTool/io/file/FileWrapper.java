package com.Oovever.esayTool.io.file;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *  文件包装器，扩展文件对象
 * @author OovEver
 * 2018/6/9 23:13
 */
public class FileWrapper {
    /** 默认编码：UTF-8 */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected File file;
    protected Charset charset;
    /**
     * 构造
     * @param file 文件
     * @param charset 编码
     */
    public FileWrapper(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }
}
