package com.Oovever.esayTool.io;

import com.Oovever.esayTool.util.CharsetUtil;
import com.Oovever.esayTool.util.CommonUtil;
import com.Oovever.esayTool.util.StringUtil;
import org.junit.Assert;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Collection;

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
    /**
     * 从流中读取内容
     *
     * @param in 输入流
     * @param charsetName 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String read(InputStream in, String charsetName) throws IORuntimeException {
        FastByteArrayOutputStream out = read(in);
        return StringUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
    }
    /**
     * 从流中读取内容，读到输出流中
     *
     * @param in 输入流
     * @return 输出流
     * @throws IORuntimeException IO异常
     */
    public static FastByteArrayOutputStream read(InputStream in) throws IORuntimeException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        return out;
    }
    /**
     * 从Reader中读取String，读取完毕后并不关闭Reader
     *
     * @param reader Reader
     * @return String
     * @throws IORuntimeException IO异常
     */
    public static String read(Reader reader) throws IORuntimeException {
//        StringBuilder对象
        final StringBuilder builder = StringUtil.builder();
        final CharBuffer    buffer  = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        try {
            while (-1 != reader.read(buffer)) {
//                转化为写
                builder.append(buffer.flip().toString());
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return builder.toString();
    }

    /**
     * 从FileChannel中读取UTF-8编码内容
     *
     * @param fileChannel 文件管道
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readUtf8(FileChannel fileChannel) throws IORuntimeException {
        return read(fileChannel, CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 从FileChannel中读取内容，读取完毕后并不关闭Channel
     *
     * @param fileChannel 文件管道
     * @param charsetName 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String read(FileChannel fileChannel, String charsetName) throws IORuntimeException {
        return read(fileChannel, CharsetUtil.charset(charsetName));
    }
    /**
     * 从FileChannel中读取内容
     *
     * @param fileChannel 文件管道
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String read(FileChannel fileChannel, Charset charset) throws IORuntimeException {
        MappedByteBuffer buffer;
        try {
//            load()将缓冲区的内容载入内存，并返回该缓冲区的引用
            buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return StringUtil.BuffetToString(buffer, charset);
    }
    /**
     * 从流中读取bytes
     *
     * @param in 输入流
     * @return bytes数组
     * @throws IORuntimeException IO异常
     */
    public static byte[] readBytes(InputStream in) throws IORuntimeException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }
    /**
     * 读取指定长度的byte数组，不关闭流
     *
     * @param in 输入流，为null返回null
     * @param length 长度，小于等于0返回空byte数组
     * @return bytes指定长度的字节数组
     * @throws IORuntimeException IO异常
     */
    public static byte[] readBytes(InputStream in, int length) throws IORuntimeException {
        if (null == in) {
            return null;
        }
        if (length <= 0) {
            return new byte[0];
        }
        byte[] b = new byte[length];
        int readLength;
        try {
            readLength = in.read(b);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
//        实际长度小于想要读取的长度
        if (readLength > 0 && readLength < length) {
            byte[] b2 = new byte[length];
            System.arraycopy(b, 0, b2, 0, readLength);
            return b2;
        } else {
            return b;
        }


    }
    /**
     * 从流中读取内容，使用UTF-8编码
     *
     * @param <T> 集合类型
     * @param in 输入流
     * @param collection 返回集合
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readUtf8Lines(InputStream in, T collection) throws IORuntimeException {
        return readLines(in, CharsetUtil.CHARSET_UTF_8, collection);
    }
    /**
     * 从流中读取内容
     *
     * @param <T> 集合类型
     * @param in 输入流
     * @param charset 字符集
     * @param collection 返回集合
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readLines(InputStream in, Charset charset, T collection) throws IORuntimeException {
        return readLines(getReader(in, charset), collection);
    }
    /**
     * 从流中读取内容
     *
     * @param <T> 集合类型
     * @param in 输入流
     * @param charsetName 字符集
     * @param collection 返回集合
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readLines(InputStream in, String charsetName, T collection) throws IORuntimeException {
        return readLines(in, CharsetUtil.charset(charsetName), collection);
    }
    /**
     * 从Reader中读取内容
     *
     * @param <T> 集合类型
     * @param reader {@link Reader}
     * @param collection 返回集合
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static <T extends Collection<String>> T readLines(Reader reader, final T collection) throws IORuntimeException {
        readLines(reader, new LineHandler(){
            @Override
            public void handle(String line) {
                collection.add(line);
            }
        });
        return collection;
    }
    /**
     * 按行读取UTF-8编码数据，针对每行的数据做处理
     *
     * @param in {@link InputStream}
     * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
     * @throws IORuntimeException IO异常
     * @since 3.1.1
     */
    public static void readUtf8Lines(InputStream in, LineHandler lineHandler) throws IORuntimeException {
        readLines(in, CharsetUtil.CHARSET_UTF_8, lineHandler);
    }

    /**
     * 按行读取数据，针对每行的数据做处理
     *
     * @param in {@link InputStream}
     * @param charset {@link Charset}编码
     * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
     * @throws IORuntimeException IO异常
     * @since 3.0.9
     */
    public static void readLines(InputStream in, Charset charset, LineHandler lineHandler) throws IORuntimeException {
        readLines(getReader(in, charset), lineHandler);
    }
    /**
     * 按行读取数据，针对每行的数据做处理<br>
     * {@link Reader}自带编码定义，因此读取数据的编码跟随其编码。
     *
     * @param reader {@link Reader}
     * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
     * @throws IORuntimeException IO异常
     */
    public static void readLines(Reader reader, LineHandler lineHandler) throws IORuntimeException {
        CommonUtil.notNull(reader,"reader不能为空");
        CommonUtil.notNull(lineHandler,"行处理器不能为空");

        // 从返回的内容中读取所需内容
        final BufferedReader bReader = getReader(reader);
        String line = null;
        try {
            while ((line = bReader.readLine()) != null) {
                lineHandler.handle(line);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
