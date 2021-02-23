package com.csv.module_net.net.cookie;


import androidx.annotation.NonNull;

import com.csv.module_net.net.cookie.store.CookieStore;
import com.csv.module_net.net.cookie.store.HasCookieStore;
import com.csv.module_net.net.exception.Exceptions;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


public class CookieJarImpl implements CookieJar, HasCookieStore {
    private final CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            //抛出异常IllegalArgumentException ( cookieStore 不能为 null)
            Exceptions.illegalArgument("cookieStore can not be null.");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return cookieStore.get(url);
    }

    @Override
    public CookieStore getCookieStore() {
        return cookieStore;
    }
}
