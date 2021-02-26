package com.csv.app_holosens.live.playback.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csv.app_holosens.R;

/**
 * @author CSV
 * @describe: 显示连接的自定义View
 * @date: 2021/2/25
 */
public class ConnectView extends RelativeLayout {
    public static final int connecting = 0x20;// 连接中 ： 开始连接
    public static final int buffering1 = 0x21;// 缓冲中 ： connect change 回来，等待O帧
    public static final int buffering2 = 0x22;// 缓冲中... ： O帧过来了，等待I帧（I帧有可能解码失败）
    public static final int connected = 0x23;// 已连接 ： I帧过来了（分辨率很高时有可能I帧传的比较慢）
    public static final int connectFailed = 0x24;// 连接失败 : 连接失败
    public static final int disconnected = 0x25;// 断开连接 : 主动断开连接
    public static final int paused = 0x26;// 点击继续播放 ： 已暂停
    public static final int connectedNoData = 0x27;// 连接成功但是无数据

    public int height = -1;
    public int connectState = 0;//连接状态

    private Context mContext;
    private LayoutInflater layoutInflater;
    private Animation animation;

    public TextView linkState;  //连接文字
    public TextView linkParam;  //连接参数文字
    public ImageView logoImg;   //小维logo
    public ImageView playImg;   //播放按钮

    public LinearLayout mTouchArea;

    public ConnectView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ConnectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ConnectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
        layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.view_connect, null);

        linkState = view.findViewById(R.id.link_state);
        linkParam = view.findViewById(R.id.link_params);
        logoImg = view.findViewById(R.id.link_loading);
        playImg = view.findViewById(R.id.link_play);
        mTouchArea = view.findViewById(R.id.link_play_area);
    }


}
