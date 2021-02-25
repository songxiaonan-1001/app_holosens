package com.csv.app_holosens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csv.app_holosens.bean.ChannelResponseBean;
import com.csv.app_holosens.bean.PlayBean;
import com.csv.app_holosens.commons.BundleKey;
import com.csv.common.consts.MySharedPreferenceKey;
import com.csv.common.utils.MySharedPreference;
import com.csv.module_net.holobase.Consts;
import com.csv.module_net.net.api.AppImpl;
import com.csv.module_net.net.api.ResponseListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.csv.module_net.holobase.Consts.TAG;

public class ChannelListActivity extends AppCompatActivity {

    private String deviceId;
    private String deviceType;
    private List<ChannelResponseBean.ChannelsBean> channelLists = new ArrayList<>();
    private ChannelListAdapter channelListAdapter;
    private List<ChannelResponseBean.ChannelsBean> channels;
    private List<PlayBean> playBeans = new ArrayList<>();
    private String play_bean_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_channel_list);
        setContentView(R.layout.activity_main);

        //获取传递的信息
        Intent intent = getIntent();
        deviceId = intent.getStringExtra("deviceId");
        deviceType = intent.getStringExtra("deviceType");
        //初始化页面
        initView();
        //加载数据
        loadData();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        channelListAdapter = new ChannelListAdapter(this, channelLists);
        recyclerView.setAdapter(channelListAdapter);

        //条目列表点击事件
        channelListAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ChannelListActivity.this, "跳转去播放界面", Toast.LENGTH_SHORT).show();
                playBeans.clear();
                //启动该通道的播放页面
                ChannelResponseBean.ChannelsBean channelsBean = channelLists.get(position);
                PlayBean playBean = new PlayBean(1,
                        channelsBean.getDevice_id(),
                        channelsBean.getChannel_id(),
                        channelsBean.getChannel_name(),
                        channelsBean.getAccess_protocol(),
                        channelsBean.getChannel_state().equals("ONLINE") ? 0 : 1);
                playBeans.add(playBean);
                play_bean_list = new Gson().toJson(playBeans);

                Intent intent = new Intent(ChannelListActivity.this, PlayActivity.class);
                intent.putExtra(BundleKey.PLAY_BEAN_LIST, play_bean_list);
                intent.putExtra(BundleKey.SELECT_NO, position);
                intent.putExtra("protocol", channelsBean.getAccess_protocol());
                intent.putExtra("deviceId", deviceId);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取通道列表
     */
    private void loadData() {
        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ChannelResponseBean channelResponseBean = gson.fromJson(result, ChannelResponseBean.class);
                channels = channelResponseBean.getChannels();
                if (channels == null) {
                    Toast.makeText(ChannelListActivity.this, "通道列表为null", Toast.LENGTH_SHORT).show();
                }
                //设置数据
                channelListAdapter.setData(channels);
            }

            @Override
            public void onFailed(Throwable throwable) {
                Log.i(TAG, "loadData onFailed 请求通道列表数据失败:" + throwable);
            }
        };

        //请求通道列表数据
        //          /v1/{user_id}/channels?device_id= deviceId &&limit=1000&&offset=0
        String path = Consts.channelList.replace("{user_id}", Consts.userId);
        String url = path + "?device_id=" + deviceId + "&&limit=1000&&offset=0";
        AppImpl.getInstance(this).getData(url, listener, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN, ""));
    }
}