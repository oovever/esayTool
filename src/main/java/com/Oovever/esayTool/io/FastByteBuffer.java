package com.Oovever.esayTool.io;

/**
 * 快速缓冲，将数据存放在缓冲集中，取代以往的单一数组
 * @author OovEver
 * 2018/6/7 18:43
 */
public class FastByteBuffer {
    /**
     * 缓冲集
     */
    private byte[][] buffers = new byte[16][];
    /**
     * 缓冲数
     */
    private int buffersCount;
    /**
     * 当前缓冲索引
     */
    private int currentBufferIndex = -1;
    /**
     * 当前缓冲
     */
    private byte[] currentBuffer;
    /**
     * 当前缓冲偏移量
     */
    private int offset;
    /**
     * 缓冲字节数
     */
    private int size;

    /**
     * 一个缓冲区的最小字节数
     */
    private final int minChunkLen;
    public FastByteBuffer() {
        this.minChunkLen = 1024;
    }
    public FastByteBuffer(int size) {
        this.minChunkLen = Math.abs(size);
    }
    /**
     * 分配下一个缓冲区，不会小于1024
     *
     * @param newSize 理想缓冲区字节数
     */
    private void needNewBuffer(int newSize) {
        int delta = newSize - size;
        int newBufferSize = Math.max(minChunkLen, delta);
//当前索引加1
        currentBufferIndex++;
        currentBuffer = new byte[newBufferSize];
//        索引归0
        offset = 0;

        //添加缓存区
        if (currentBufferIndex >= buffers.length) {
            int newLen = buffers.length << 1;
            byte[][] newBuffers = new byte[newLen][];
//            复制原数组到新数组
            System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
            buffers = newBuffers;
        }
        buffers[currentBufferIndex] = currentBuffer;
        buffersCount++;
    }
    /**
     * 向快速缓冲加入数据
     *
     * @param array 数据
     * @param off 偏移量
     * @param len 字节数
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(byte[] array, int off, int len) {
        int end = off + len;
        if ((off < 0) || (len < 0) || (end > array.length)) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return this;
        }
//        新的缓存字节数
        int newSize = size + len;
        int remaining = len;

        if (currentBuffer != null) {
            //首先填充当前buffer
            int part = Math.min(remaining, currentBuffer.length - offset);
            System.arraycopy(array, end - remaining, currentBuffer, offset, part);
//            剩余字节数
            remaining -= part;
//字节偏移量
            offset += part;
//            当前缓存区字节数
            size += part;
        }
//        第一个缓存区放不下，或者不存在第一个缓存区
        if (remaining > 0) {
            // still some data left
            // ask for new buffer
            needNewBuffer(newSize);

            // then copy remaining
            // but this time we are sure that it will fit
            int part = Math.min(remaining, currentBuffer.length - offset);
            System.arraycopy(array, end - remaining, currentBuffer, offset, part);
            offset += part;
            size += part;
        }

        return this;
    }
    /**
     * 向快速缓冲加入数据
     *
     * @param array 数据
     *
     * @return 快速缓冲自身 @see FastByteBuffer
     */
    public FastByteBuffer append(byte[] array) {
        return append(array, 0, array.length);
    }
    /**
     * 向快速缓冲加入一个字节
     *
     * @param element 一个字节的数据
     * @return 快速缓冲自身 @see FastByteBuffer
     */
    public FastByteBuffer append(byte element) {
        if ((currentBuffer == null) || (offset == currentBuffer.length)) {
            needNewBuffer(size + 1);
        }

        currentBuffer[offset] = element;
        offset++;
        size++;

        return this;
    }
    /**
     * 将另一个快速缓冲加入到自身
     *
     * @param buff 快速缓冲
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(FastByteBuffer buff) {
        if (buff.size == 0) {
            return this;
        }
//        添加当前buffer之前的所有buffer，当前buffer可能未填满，所以不填充当前的
        for (int i = 0; i < buff.currentBufferIndex; i++) {
            append(buff.buffers[i]);
        }
        append(buff.currentBuffer, 0, buff.offset);
        return this;
    }

    /**
     *
     * @return 返回缓冲字节数
     */
    public int size() {
        return size;
    }

    /**
     *
     * @return 当前FastByteBuffer是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }
    /**
     * 当前缓冲位于缓冲区的索引位
     *
     * @return 当前缓冲位于缓冲区的索引位
     */
    public int index() {
        return currentBufferIndex;
    }

    /**
     *
     * @return 当前缓冲区偏移量
     */
    public int offset() {
        return offset;
    }
    /**
     * 根据索引位返回缓冲集中的缓冲
     *
     * @param index 索引位
     * @return 缓冲
     */
    public byte[] array(int index) {
        return buffers[index];
    }

    /**
     * 重置FastByteBuffer缓冲区
     */
    public void reset() {
        size = 0;
        offset = 0;
        currentBufferIndex = -1;
        currentBuffer = null;
        buffersCount = 0;
    }

    /**
     *
     * @return 返回快速缓冲中的数据
     */
    public byte[] toArray() {
//        当前位置
        int pos = 0;
        byte[] array = new byte[size];
        if (currentBufferIndex == -1) {
            return array;
        }
        for (int i = 0; i < currentBufferIndex; i++) {
            int len = buffers[i].length;
            System.arraycopy(buffers[i], 0, array, pos, len);
            pos += len;
        }
        System.arraycopy(buffers[currentBufferIndex], 0, array, pos, offset);
        return array;
    }
    /**
     * 返回快速缓冲中的数据
     *
     * @param start 逻辑起始位置
     * @param len 逻辑字节长
     * @return 快速缓冲中的数据
     */
    public byte[] toArray(int start, int len) {
        int remaining = len;
        int pos = 0;
        byte[] array = new byte[len];

        if (len == 0) {
            return array;
        }

        int i = 0;
        while (start >= buffers[i].length) {
            start -= buffers[i].length;
            i++;
        }

        while (i < buffersCount) {
            byte[] buf = buffers[i];
            int c = Math.min(buf.length - start, remaining);
            System.arraycopy(buf, start, array, pos, c);
            pos += c;
            remaining -= c;
            if (remaining == 0) {
                break;
            }
            start = 0;
            i++;
        }
        return array;
    }
    /**
     * 根据索引位返回一个字节
     *
     * @param index 索引位
     * @return 一个字节
     */
    public byte get(int index) {
        if ((index >= size) || (index < 0)) {
            throw new IndexOutOfBoundsException();
        }
        int ndx = 0;
        while (true) {
            byte[] b = buffers[ndx];
            if (index < b.length) {
                return b[index];
            }
            ndx++;
            index -= b.length;
        }
    }
}
