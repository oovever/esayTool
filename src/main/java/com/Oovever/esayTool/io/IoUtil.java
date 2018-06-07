package com.Oovever.esayTool.io;

import com.Oovever.esayTool.util.CommonUtil;
import org.junit.Assert;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

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
    /*
     * 本方法不会关闭流
     *
     * @param in 输入流
     * @param out 输出流
     * @param bufferSize 缓存大小
     * @param streamProgress 进度条
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copyByNIO(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        return copy(Channels.newChannel(in), Channels.newChannel(out), bufferSize, streamProgress);
    }
    /**
     * 拷贝文件流，使用NIO
     *
     * @param in 输入
     * @param out 输出
     * @return 拷贝的字节数
     * @throws IORuntimeException IO异常
     */
    public static long copy(FileInputStream in, FileOutputStream out) throws IORuntimeException {
        CommonUtil.notNull(in, "FileInputStream is null!");
        CommonUtil.notNull(out, "FileOutputStream is null!");

        final FileChannel inChannel  = in.getChannel();
        final FileChannel outChannel = out.getChannel();

        try {
            return inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 拷贝流，使用NIO，不会关闭流
     *
     * @param in {@link ReadableByteChannel}
     * @param out {@link WritableByteChannel}
     * @param bufferSize 缓冲大小，如果小于等于0，使用默认
     * @param streamProgress {@link StreamProgress}进度处理器
     * @return 拷贝的字节数
     * @throws IORuntimeException IO异常
     */
    public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        CommonUtil.notNull(in, "InputStream is null !");
        CommonUtil.notNull(out, "OutputStream is null !");

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
        long       size       = 0;
        if (null != streamProgress) {
            streamProgress.start();
        }
        try {
            while (in.read(byteBuffer) != EOF) {
                byteBuffer.flip();// 读写转化
                size += out.write(byteBuffer);
                byteBuffer.clear();
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

    /**
     * 获取一个bufferedReader读取器
     * @param inputStream 输入流
     * @param charsetName 字符集名称
     * @return bufferedReader对象
     */
    public static BufferedReader getReader(InputStream inputStream, String charsetName) {
        return getReader(inputStream, Charset.forName(charsetName));
    }
    /**
     * 获取一个bufferedReader读取器
     * @param inputStream 输入流
     * @param charsetName 字符集名称
     * @return bufferedReader对象
     */
    public static BufferedReader getReader(InputStream inputStream, Charset charsetName) {
        if (inputStream == null) {
            return null;
        }
        InputStreamReader reader = null;
        if (null == charsetName) {
            reader = new InputStreamReader(inputStream);
        } else {
            reader = new InputStreamReader(inputStream, charsetName);
        }
        return new BufferedReader(reader);
    }

    /**
     * 获取BufferedReader对象，如果为Null返回空
     * @param reader 普通reader对象
     * @return BufferedReader对象
     */
    public static BufferedReader getReader(Reader reader) {
        if (reader == null) {
            return null;
        }
        return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
    }
    /**
     * 获得一个Writer
     *
     * @param out 输入流
     * @param charsetName 字符集
     * @return OutputStreamWriter对象
     */
    public static OutputStreamWriter getWriter(OutputStream out, String charsetName) {
        return getWriter(out, Charset.forName(charsetName));
    }
    /**
     * 获得一个Writer
     *
     * @param out 输入流
     * @param charset 字符集
     * @return OutputStreamWriter对象
     */
    public static OutputStreamWriter getWriter(OutputStream out, Charset charset) {
        if (null == out) {
            return null;
        }

        if (null == charset) {
            return new OutputStreamWriter(out);
        } else {
            return new OutputStreamWriter(out, charset);
        }
    }

}
