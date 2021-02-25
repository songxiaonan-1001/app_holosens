package com.csv.app_holosens.base;

/**
 * 类似Handler的时间通知接口
 * @author CSV
 */
public interface IHandlerLikeNotify {
    /**
     * 消息通知
     *
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     */
    void onNotify(int what, int arg1, int arg2, Object obj);
}
