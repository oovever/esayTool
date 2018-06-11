package com.Oovever.esayTool.io.file;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author OovEver
 * 2018/6/11 19:04
 */
public class FileReaderTest {
    /*
     * 读取文件所有数据<br>
     * 文件的长度不能超过 {@link Integer#MAX_VALUE}
     * 读取为字节数组
     */
    @Test
    public void testReadBytes() {
        FileReader fileReader = new FileReader("E:\\git库\\esayTool\\src\\main\\resources\\testCollectionAppend.txt");
        byte[] res = fileReader.readBytes();
    }
    /*
     * 读取文件所有数据<br>
     * 文件的长度不能超过 {@link Integer#MAX_VALUE}
     * 读取为string字符串
     */
    @Test
    public void testReadString() {
        FileReader fileReader = new FileReader("E:\\git库\\esayTool\\src\\main\\resources\\testCollectionAppend.txt");
        String res = fileReader.readString();
        System.out.println(res);
    }
    /*
     * 读取文件所有数据<br>
     * 文件的长度不能超过 {@link Integer#MAX_VALUE}
     * 按行读取
     */
    @Test
    public void testReadLines() {
        FileReader   fileReader = new FileReader("E:\\git库\\esayTool\\src\\main\\resources\\testCollectionAppend.txt");
        List<String> res        = fileReader.readLines();
        System.out.println(res);
    }

}