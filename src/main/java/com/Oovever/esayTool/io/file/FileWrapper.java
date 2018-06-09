package com.Oovever.esayTool.io.file;

import java.io.File;
import java.nio.charset.Charset;

/**
 *  文件包装器，扩展文件对象
 * @author OovEver
 * 2018/6/9 23:13
 */
public class FileWrapper {
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
