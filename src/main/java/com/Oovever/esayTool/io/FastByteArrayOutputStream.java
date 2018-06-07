package com.Oovever.esayTool.io;

import com.Oovever.esayTool.util.CharsetUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 基于快速缓冲FastByteBuffer的OutputStream，随着数据的增长自动扩充缓冲区
 * <p>
 * 可以通过{@link #toByteArray()}和 {@link #toString()}来获取数据
 * 这种设计避免重新分配内存块而是分配新增的缓冲区，缓冲区不会被GC，数据也不会被拷贝到其他缓冲区。
 * @author OovEver
 * 2018/6/7 18:28
 */
public class FastByteArrayOutputStream extends OutputStream {
//    快速缓冲
    private final FastByteBuffer buffer;
    public FastByteArrayOutputStream() {
        this(1024);
    }

    /**
     * 快速缓冲对象创建
     * @param size buffer缓存字节数
     */
    public FastByteArrayOutputStream(int size) {
        buffer = new FastByteBuffer(size);
    }

    /**
     * 向快速缓冲加入数据
     * @param b 字节数组
     * @param off 偏移量
     * @param len 字节数
     */
    @Override
    public void write(byte[] b, int off, int len) {
        buffer.append(b, off, len);
    }

    /**
     *
     * @param b 字节
     */
    public void write(int b) {
        buffer.append((byte) b);
    }
    @Override
    public void close() {

    }

    /**
     * 重置缓冲区
     */
    public void reset() {
        buffer.reset();
    }
    /**
     * 写出
     * @param out 输出流
     * @throws IORuntimeException IO异常
     */
    public void writeTo(OutputStream out) throws IORuntimeException {
        final int index = buffer.index();
        byte[] buf;
        try {
            for (int i = 0; i < index; i++) {
                buf = buffer.array(i);
                out.write(buf);
            }
            out.write(buffer.array(index), 0, buffer.offset());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 转为Byte数组
     * @return Byte数组
     */
    public byte[] toByteArray() {
        return buffer.toArray();
    }
    @Override
    public String toString() {
        return new String(toByteArray());
    }
    /**
     * 转为字符串
     * @param charsetName 编码
     * @return 字符串
     */
    public String toString(String charsetName) {
        return toString(CharsetUtil.charset(charsetName));
    }

    /**
     * 转为字符串
     * @param charset 编码
     * @return 字符串
     */
    public String toString(Charset charset) {
        return new String(toByteArray(), charset);
    }
}
