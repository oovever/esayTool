package com.Oovever.esayTool.util;

/**
 * 常见的工具类
 * @author OovEver
 * 2018/6/5 0:57
 */
public class CommonUtil<T>{

        /**
         * 判断一个Object对象是否为空
         * @param Object 要判断的对象
         * @param <T> 对象类型
         * @param errorMessage 异常信息
         */
    public static <T> void notNull(T Object,String errorMessage) {
        if (Object == null) {
            throw new NullPointerException(errorMessage);
        }
    }
}
