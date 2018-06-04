package com.Oovever.esayTool.io;

import com.Oovever.esayTool.util.CommonUtil;
import org.junit.Assert;

import java.io.*;

/**
 * @author OovEver
 * 2018/5/31 16:46
 * IO工具类只是辅助流的读写
 */
public class IoUtil {
    /** 默认缓存大小 */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    /** 默认缓存大小 */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 4096;
    /** 数据流末尾 */
    public static final int EOF = -1;
    /**
     * 将Reader中的内容复制到Writer中 使用默认缓存大小
     *
     * @param reader Reader
     * @param writer Writer
     * @return 拷贝的字节数
     * @throws IORuntimeException IO异常
     */
    public static long copy(Reader reader, Writer writer) {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE);
    }
    /**
     * 将Reader中的内容复制到Writer中
     *
     * @param reader Reader
     * @param writer Writer
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copy(Reader reader, Writer writer, int bufferSize) {
        return copy(reader, writer, bufferSize, null);
    }
    /**
     * 将Reader中的内容复制到Writer中
     *
     * @param reader Reader
     * @param writer Writer
     * @param bufferSize 缓存大小
     * @param streamProgress 进度处理器
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        char[] buffer = new char[bufferSize];
        long size = 0;
        int readSize;
        if (null != streamProgress) {
            streamProgress.start();
        }
        try {
            while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
                writer.write(buffer, 0, readSize);
                size += readSize;
                writer.flush();
                if (null != streamProgress) {
                    streamProgress.progress(size);
                }
            }
        }catch (IOException e) {
            throw new IORuntimeException(e);
        }
        if (null != streamProgress) {
            streamProgress.finish();
        }
        return size;
    }
    /**
     * 拷贝流，使用默认Buffer大小
     *
     * @param in 输入流
     * @param out 输出流
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copy(InputStream in, OutputStream out) throws IORuntimeException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }
    /**
     * 拷贝流
     *
     * @param in 输入流
     * @param out 输出流
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize) throws IORuntimeException {
        return copy(in, out, bufferSize, null);
    }
    /**
     * 拷贝流
     *
     * @param in 输入流
     * @param out 输出流
     * @param bufferSize 缓存大小
     * @param streamProgress 进度条
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        CommonUtil.notNull(in,"输入流不能为空");
        CommonUtil.notNull(out,"输出流不能为空");
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        byte[] buffer = new byte[bufferSize];
        long size = 0;
        if (null != streamProgress) {
            streamProgress.start();
        }
        try {
            for (int readSize = -1; (readSize = in.read(buffer)) != EOF;) {
                out.write(buffer, 0, readSize);
                size += readSize;
                out.flush();
                if (null != streamProgress) {
                    streamProgress.progress(size);
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        if (null != streamProgress) {
            streamProgress.finish();
        }
        return size;
    }
}
