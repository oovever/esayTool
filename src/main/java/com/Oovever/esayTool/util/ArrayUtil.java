package com.Oovever.esayTool.util;

/**
 * 数组工具类
 * @author OovEver
 * 2018/6/8 15:53
 */
public class ArrayUtil {
    /**
     * 数组是否为空
     *
     * @param <T> 数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(final T... array) {
        return array == null || array.length == 0;
    }
}
