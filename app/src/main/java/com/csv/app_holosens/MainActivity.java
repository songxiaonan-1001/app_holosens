package com.csv.app_holosens;

import android.content.Intent;
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
    private DeviceListAdapter deviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //获取设备列表
        loadData(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN, ""));
    }

    private void initView() {
        btnGetToken = findViewById(R.id.btn_getToken);
        btnGetToken.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceListAdapter = new DeviceListAdapter(deviceLists, this);
        recyclerView.setAdapter(deviceListAdapter);

        deviceListAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(MainActivity.this, "下标为:"+position, Toast.LENGTH_SHORT).show();
                //获取到点击条目的信息
                DeviceResponseBean.DevicesBean devicesBean = deviceLists.get(position);

                //跳转页面
                Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
                Log.i(TAG, "点击条目的 deviceId :" + devicesBean.getDevice_id());
                intent.putExtra("deviceId", devicesBean.getDevice_id());
                Log.i(TAG, "点击条目的deviceType :" + devicesBean.getDevice_type());
                intent.putExtra("deviceType", devicesBean.getDevice_type());
                startActivity(intent);
            }
        });
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
        hashMap.put("sk", Consts.sk);
        String url = Consts.gainToken.replace("{user_id}", Consts.userId);
        AppImpl.getInstance(this).getToken(url, hashMap, listener);
    }

    /**
     * 获取设备列表
     *
     * @param access_token
     */
    private void loadData(String access_token) {
        Log.i(TAG, "loadData token: " + access_token);

        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "loadData result:" + result);
                if (null != result) {
                    btnGetToken.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    Gson gson = new Gson();
                    DeviceResponseBean deviceResponseBean = gson.fromJson(result, DeviceResponseBean.class);
                    Log.i(TAG, "loadData onSuccess: " + deviceResponseBean);
                    deviceLists = deviceResponseBean.getDevices();
                    //给适配器设置数据
                    deviceListAdapter.setData(deviceLists);
                } else {
                    btnGetToken.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                Log.i(TAG, "loadData onFailed: 获取设备列表失败" + throwable.toString());
            }
        };

        String path = Consts.deviceList.replace("{user_id}", Consts.userId);
        String url = path + "?limit=1000&&offset=0";
        //查询设备列表的方法
        AppImpl.getInstance(this).getData(url, listener, access_token);
    }

    //-----------------------
    //退出应用
    //-----------------------

    private long exitTime = 0;
    private boolean isKillPrecess;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            isKillPrecess = false;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            if (isKillPrecess) {
                return;
            }
            isKillPrecess = true;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            finish();
            System.exit(0);
        }
    }
}