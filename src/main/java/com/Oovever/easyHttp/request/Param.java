package com.Oovever.easyHttp.request;

/**
 * 单个参数,包含参数名称和对应的值
 * @author OovEver
 * 2018/7/3 22:29
 */
public class Param {
    /**
     * 参数名称
     */
    public String name;
    /**
     * 参数值
     */
    public String value;

    /**
     * @param name 参数名称
     * @param value 参数值
     */
    public Param(String name, Object value) {
        this.name = name;
        this.value = value == null ? "" : value.toString();
    }

    @Override
    public String toString() {
        return "Param [name=" + name + ", value=" + value + "]";
    }
}
