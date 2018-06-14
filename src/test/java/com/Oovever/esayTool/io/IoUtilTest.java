package com.Oovever.esayTool.io;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import static org.junit.Assert.*;

/**
 * IO测试类
 * @author OovEver
 * 2018/6/11 19:17
 */
public class IoUtilTest {
    /**
     * 文件拷贝流测试
     */
    @Test
    public void testCopy() {
        BufferedInputStream  in       = FileUtil.getInputStream("E:\\git库\\esayTool\\src\\main\\resources\\IoUtil\\testIn.txt");
        BufferedOutputStream out      = FileUtil.getOutputStream("E:\\git库\\esayTool\\src\\main\\resources\\IoUtil\\testOut.txt");
        long                 copySize = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE);
    }
    /**
     * Nio文件拷贝流测试
     */
    @Test
    public void testCopyNio() {
        BufferedInputStream  in       = FileUtil.getInputStream("E:\\git库\\esayTool\\src\\main\\resources\\IoUtil\\testNioIn.txt");
        BufferedOutputStream out      = FileUtil.getOutputStream("E:\\git库\\esayTool\\src\\main\\resources\\IoUtil\\testNioOut.txt");
        long                 copySize = IoUtil.copyByNIO(in, out, IoUtil.DEFAULT_BUFFER_SIZE,null);
    }


}