package com.Oovever.esayTool.io;

/**
 * @author OovEver
 * 2018/6/4 16:14
 */
public interface StreamProgress {
    /**
     * 开始
     */
    public void start();

    /**
     * 进行中
     * @param progressSize 已经进行的大小
     */
    public void progress(long progressSize);

    /**
     * 结束
     */
    public void finish();
}
