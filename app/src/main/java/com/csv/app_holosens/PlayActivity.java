package com.csv.app_holosens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.csv.app_holosens.bean.ProtocolType;
import com.csv.app_holosens.commons.BundleKey;

public class PlayActivity extends AppCompatActivity {
    public final static int PLAY_BACK_TYPE_LOCAL = 1;
    public final static int PLAY_BACK_TYPE_CLOUD = 2;

    private long mCreateTime;
    private int playBackType = PLAY_BACK_TYPE_LOCAL;
    private boolean connected = false;//视频是否已经连接
    private String deviceFullNo;//设备号
    private String nickName;
    private int channelIndex = 0;// 通道号，从1开始
    private int connectIndex = 0;// 窗口从0开始
    private String mAlarmTime = "";//报警录像查看指定的时间
    /**
     * 设备的连接类型  1：好望 2：国标
     */
    private int connectType = ProtocolType.HOLO;
    /**
     * 国标设备的channelid
     */
    private String connectGbCHannelId;

    /**
     * 播放
     */
    private SurfaceView playSurface;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置视频播放不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);

        initSetting();
        initUi();

        mCreateTime = System.currentTimeMillis();
    }

    private void initSetting() {
        Intent intent = getIntent();
        if (intent != null) {
            playBackType = intent.getIntExtra(BundleKey.PLAYBACK_TYPE, PLAY_BACK_TYPE_LOCAL);
            connected = intent.getBooleanExtra(BundleKey.CONNECTED, false);
            deviceFullNo = intent.getStringExtra(BundleKey.DEVICE_ID);
            nickName = intent.getStringExtra(BundleKey.NICK_NAME);
            channelIndex = intent.getIntExtra(BundleKey.CHANNEL_ID, 0);
            connectIndex = intent.getIntExtra(BundleKey.WINDOW_INDEX, 10000);
            mAlarmTime = intent.getStringExtra(BundleKey.ALARM_TIME);
            connectType = intent.getIntExtra(BundleKey.CONNECT_TYPE, ProtocolType.HOLO);
            connectGbCHannelId = intent.getStringExtra(BundleKey.GB_CHANNEL_ID);
        }

    }

    private void initUi() {
        initPlay();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPlay() {
        playSurface = findViewById(R.id.playsurface);
        playSurface.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //响应缩放
                return true;
            }
        });

        surfaceHolder = playSurface.getHolder();

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceHolder = holder;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
}