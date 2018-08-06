package com.Oovever.esayTool.io.file;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author OovEver
 * 2018/6/11 17:30
 */
public class FileWriterTest {
    /**
     * 测试覆盖写入
     */
    @Test
    public void testWrite() {
        FileWriter writer = new FileWriter("E:\\git库\\esayTool\\src\\main\\resources\\test.txt");
        writer.write("test1");
        writer.write("test2");
        writer.write("test3");

    }
    /**
     * 测试追加写入
     */
    @Test
    public void testWriteAppend() {
        FileWriter writer = new FileWriter("E:\\git库\\esayTool\\src\\main\\resources\\testAppend.txt");
//        追加写入
        writer.append("test append1");
        writer.append("test append2");
        writer.append("test append3");
    }
    /**
     * 测试覆盖写入集合
     *
     */
    @Test
    public void testWriteCollection() {
        FileWriter writer = new FileWriter("E:\\git库\\esayTool\\src\\main\\resources\\testCollection.txt");
//        追加写入
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        List<String> list2 = new ArrayList<>();
        list2.add("test4");
        list2.add("test5");
        list2.add("test6");
        writer.writeLines(list);
        writer.writeLines(list2);
    }
    /**
     * 测试追加写入集合
     */
    @Test
    public void testWriteCollectionAppend() {
        FileWriter writer = new FileWriter("E:\\git库\\esayTool\\src\\main\\resources\\testCollectionAppend.txt");
//        追加写入
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        List<String> list2 = new ArrayList<>();
        list2.add("test4");
        list2.add("test5");
        list2.add("test6");
        writer.writeLines(list, true);
        writer.writeLines(list2, true);
    }


}