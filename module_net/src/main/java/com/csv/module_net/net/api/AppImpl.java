package com.csv.module_net.net.api;

import android.content.Context;
import android.util.Log;

import com.csv.module_net.net.request.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.csv.module_net.holobase.Consts.TAG;

/**
 * @author CSV
 * @describe:
 * @date: 2021/2/23
 */
public class AppImpl {
    private static Context mContext;
    private static AppImpl app;
    private final AppService appService;

    public AppImpl(Context context) {
        AppImpl.mContext = context;
        appService = RetrofitClient.getInstance(context).create(AppService.class);
    }

    public static AppImpl getInstance(Context context) {
        if (app == null) {
            return new AppImpl(context);
        } else {
            return app;
        }
    }

    public void getToken(String url, HashMap<String, Object> params, final ResponseListener listener) {
        //创建gson对象
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()//将Gson配置为按原样传递HTML字符
                .create();
        String jsonParam = gson.toJson(params);
        Log.i(TAG, "jsonParam: " + jsonParam);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);

        appService.getToken(url, body)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.i(TAG, "getToken result:" + response.body() + "code:" + response.code() + "message:" + response.message());
                        if (response.code() == 200) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onSuccess(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.i(TAG, "getToken failed:" + t.toString());
                        listener.onFailed(t);
                    }
                });
    }
}
