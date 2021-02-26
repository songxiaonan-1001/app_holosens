package com.csv.app_holosens.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.csv.common.play.IHandlerLikeNotify;
import com.csv.common.play.IHandlerNotify;

/**
 * @author CSV
 * @describe:
 * @date: 2021/2/25
 */
public class BaseActivity extends AppCompatActivity implements IHandlerNotify, IHandlerLikeNotify {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {

    }
}
