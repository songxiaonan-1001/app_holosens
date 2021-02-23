package com.csv.module_net.net.exception;

/**
 * @author CSV
 * @describe: 异常类
 * @date: 2021/2/23
 */
public class Exceptions {
    /**
     * 非法的argument
     * @param msg
     * @param params
     */
    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }
}
