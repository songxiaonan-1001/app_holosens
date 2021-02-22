package com.csv.module_net.holobase;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.csv.module_net.R;

/**
 * @author CSV
 * @describe: 加载Dialog
 * @date: 2021/2/22
 */
public class LoadingDialog extends Dialog {


    //-----------------------------------------
    //构造方法
    //-----------------------------------------

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.TipDialog);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_lodding_dialog);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
