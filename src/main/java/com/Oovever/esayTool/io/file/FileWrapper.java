package com.Oovever.esayTool.io.file;

import com.Oovever.esayTool.io.FileUtil;

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
    /**
     * 设置字符集编码
     * @param charset 编码
     * @return 自身
     */
    public FileWrapper setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }
    // ------------------------------------------------------- Setters and Getters start end

    /**
     * 可读的文件大小
     * @return 大小
     */
    public String readableFileSize() {
        return FileUtil.readableFileSize(file.length());
    }
}
