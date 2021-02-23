package com.csv.app_holosens;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csv.app_holosens.bean.DeviceResponseBean;
import com.csv.app_holosens.bean.TokenBean;
import com.csv.common.consts.MySharedPreferenceKey;
import com.csv.common.utils.MySharedPreference;
import com.csv.module_net.holobase.Consts;
import com.csv.module_net.net.api.AppImpl;
import com.csv.module_net.net.api.ResponseListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.csv.module_net.holobase.Consts.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGetToken;
    private List<DeviceResponseBean.DevicesBean> deviceLists = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnGetToken = findViewById(R.id.btn_getToken);
        btnGetToken.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
new DeviceListAdapter();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getToken:
                loadToken();
                break;
            default:
                break;
        }
    }

    private void loadToken() {
        //回调
        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "loadToken result: " + result);
                if (!TextUtils.isEmpty(result)) {
                    TokenBean tokenBean = new Gson().fromJson(result, TokenBean.class);
                    //将Token存进SP中
                    MySharedPreference.putString(MySharedPreferenceKey.LoginKey.TOKEN, tokenBean.getAccess_token());
                    //获取设备列表
                    loadData(tokenBean.getAccess_token());
                    //隐藏按钮
                    btnGetToken.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MainActivity.this, "获取Token失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                Toast.makeText(MainActivity.this, "获取Token失败", Toast.LENGTH_SHORT).show();
            }
        };

        //集合存储ak和sk的值
        HashMap<String, Object> hashMap = new HashMap<>(2);
        hashMap.put("ak", Consts.ak);
        hashMap.put("sk",Consts.sk);
        String url = Consts.gainToken.replace("{user_id}", Consts.userId);
        AppImpl.getInstance(this).getToken(url,hashMap,listener);
    }

    //获取设备列表
    private void loadData(String access_token) {
        Log.i(TAG, "loadData token: "+access_token);

        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "loadData result:" + result);
                if (null!=result){
                    btnGetToken.setVisibility(View.GONE);

                    Gson gson = new Gson();
                    DeviceResponseBean deviceResponseBean = gson.fromJson(result, DeviceResponseBean.class);
                    deviceLists = deviceResponseBean.getDevices();
                }else {
                    btnGetToken.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        };
    }
}