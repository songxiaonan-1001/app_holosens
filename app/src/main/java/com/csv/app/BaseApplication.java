package com.csv.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.csv.app_holosens.base.IHandlerLikeNotify;
import com.csv.common.consts.NativeCbConsts;
import com.csv.common.utils.BackgroundHandler;
import com.csv.common.utils.MySharedPreference;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * @author CSV
 * @describe:
 * @date: 2021/1/28
 */
public class BaseApplication extends Application {
    private static Context mContext;
    private static BaseApplication myApp;
    private AppFrontBackHelper appFrontBackHelper;

    public static BaseApplication getInstance() {
        return myApp;
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        mContext = getApplicationContext();

        //初始化SP
//        SpUtil.init(this);
        MySharedPreference.init(this);

        //初始化okhttp和retrofit
//        NetWorkManager.getInstance().init();

        //应用前后台切换
        appFrontBackHelper = new AppFrontBackHelper();
        appFrontBackHelper.register(this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //app切到前台处理,刷新消息未读数量
                MsgEvent msgEvent = new MsgEvent();
                msgEvent.setMsgTag(MsgEvent.MSG_EVENT_UPDATE_UNREAD_COUNT);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("refresh", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msgEvent.setAttachment(jsonObject.toString());
                EventBus.getDefault().post(msgEvent);
            }

            @Override
            public void onBack() {
                //应用切换到后台处理
            }
        });
    }

    private static Handler mHandler = new Handler(BackgroundHandler.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            IHandlerLikeNotify nativeCallback = myApp.getCurrentNotifier();
            if (null == nativeCallback) {
                Log.e("TAG", "null notifier");
                return;
            }

            nativeCallback.onNotify(msg.what, msg.arg1, msg.arg2, msg.obj);

        }
    };

    /**
     * 注:该方法只适用于Android设备的模拟器中，如果application退出才会回调
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        appFrontBackHelper.unregister(this);
    }

    //----------------------------------------------------------------------------------------------

    private WeakReference<IHandlerLikeNotify> mNotifyWeakReference;

    /**
     * 获取currentNotifier
     *
     * @return
     */
    public IHandlerLikeNotify getCurrentNotifier() {
        return mNotifyWeakReference.get();
    }

    /**
     * 修改当前显示的 Activity/Fragment 引用
     *
     * @param currentNotifier
     */
    public void setCurrentNotifier(IHandlerLikeNotify currentNotifier) {
        Log.e("TAG", "setCurrentNotifier: " + currentNotifier.getClass().getName());
        mNotifyWeakReference = new WeakReference<>(currentNotifier);
    }

    //----------------------------------------------------------------------------------------------

    public HashMap<Integer, Integer> playerIdWindowMap = new HashMap<Integer, Integer>();

    public HashMap<Integer, Integer> getPlayerIdWindowMap() {
        return playerIdWindowMap;
    }

    public void setPlayerIdWindowMap(HashMap<Integer, Integer> playerIdWindowMap) {
        this.playerIdWindowMap = playerIdWindowMap;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * (启用)底层所有的回调接口
     *
     * @param player_id
     * @param event_type
     * @param event_state
     * @param json_data
     */
    void OnEvent(int player_id, int event_type, int event_state, String json_data) {
        Log.i("TAG", "OnEvent: player_id(连接返回playerId，用来对应画面):" + player_id);
        Log.i("TAG", "OnEvent: event_type(事件类型，取值如下：\n" +
                "0：直播或者回放连接事件；\n" +
                "1：查询录像文件列表事件；\n" +
                "3：回放播放时间戳回调；):" + event_type);
        Log.i("TAG", "OnEvent: event_state(事件状态，取值如下：\n" +
                "0：无状态；\n" +
                "1：已连接；\n" +
                "2：连接失败；\n" +
                "3：连接限制（p2p连接时，设备连接达到上线了）\n" +
                "4：连接中断（网络异常或服务中断）\n" +
                "5：正在缓冲\n" +
                "6：解码失败\n" +
                "7：解码成功，收到解码成功后即可调用show接口来预览图像):" + event_state);
        Log.i("TAG", "OnEvent: json_data(携带数据，例如查询的录像列表数据；):" + json_data);

        switch (event_type) {
            case NativeCbConsts.EVENT_TYPE_HPET_PLAY:
                //直播或者回放连接事件
            case NativeCbConsts.EVENT_TYPE_HPET_PLAY_TIME_POS: {
                //回放播放时间戳回调
                int window = -1;
                if (null != playerIdWindowMap && playerIdWindowMap.size() > 0) {
                    Log.i("TAG", "OnEvent: " + playerIdWindowMap.size());
                    try {
                        window = playerIdWindowMap.get(player_id);
                    } catch (Exception e) {
                        window = -1;
                        e.printStackTrace();
                    }
                    if (window == -1) {
                        return;
                    }
                }
                if (null != getCurrentNotifier()) {
                    Log.e("TAG", "window ==" + window);
//                    NativeCbTs.transmit(getCurrentNotifier(), event_type, window, event_state, json_data);
                } else {
                    Log.e("TAG", "currentNotify is null!");
                }
            }
            break;
            default://查询录像文件列表事件
                getCurrentNotifier().onNotify(event_type, player_id, event_state, json_data);
                break;
        }
        if (event_state == 7) {
            Log.i("TAG", "OnEvent: 调用显示视频流的方法");
//            PlayActivity.show(player_id);
        }
    }
}
