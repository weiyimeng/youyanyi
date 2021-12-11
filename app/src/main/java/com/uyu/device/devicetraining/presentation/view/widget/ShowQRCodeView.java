package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.uyu.device.devicetraining.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jwc on 2016/7/20.
 */
public class ShowQRCodeView extends PopupWindow {

    Context context;

    public ShowQRCodeView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    private void init(){
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_qr,null);
        setContentView(view);
        ButterKnife.bind(this,view);
    }

    @OnClick({R.id.rl_root,R.id.tv_exit})
    public void exit(){
        this.dismiss();
    }
}
