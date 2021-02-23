package com.csv.module_net.net.api;

/**
 * @author CSV
 */
public interface ResponseListener {
    /**
     * 成功
     *
     * @param result
     */
    void onSuccess(String result);

    /**
     * 失败
     *
     * @param throwable
     */
    void onFailed(Throwable throwable);
}
