package com.Oovever.easyHttp;

/**
 * @author OovEver
 * 2018/6/14 19:47
 * Http头部信息
 */
public enum  Header {
    /**提供日期和时间标志,说明报文是什么时间创建的*/
    DATE("Date");
    private String value;
    private Header(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
