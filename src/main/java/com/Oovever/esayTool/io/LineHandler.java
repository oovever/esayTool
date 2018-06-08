package com.Oovever.esayTool.io;

/**
 * 行处理器
 * @author OovEver
 * 2018/6/8 11:26
 */
public interface LineHandler {
    /**
     * 处理一行数据，可以编辑后存入指定地方
     * @param line 行
     */
    void handle(String line);
}
