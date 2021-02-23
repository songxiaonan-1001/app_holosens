package com.csv.module_net.net.api;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author CSV
 * @describe:
 * @date: 2021/2/22
 */
public interface AppService {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<String> loadData(@Url String url,
                                @Body RequestBody body);

    @POST
    Observable<String> loadData(@Url String url,
                                @Header("Authorization") String auth,
                                @Body RequestBody body);

    /**
     * 获取Token
     *
     * @param url
     * @param body
     * @return
     */
    @POST
    Call<String> getToken(@Url String url, @Body RequestBody body);
}
