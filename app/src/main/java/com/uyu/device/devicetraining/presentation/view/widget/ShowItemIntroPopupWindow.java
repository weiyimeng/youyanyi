package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnStartTrainListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainPrepareViewA;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jwc on 2016/7/20.
 */
public class ShowItemIntroPopupWindow extends PopupWindow implements OnStartTrainListener {
    public interface OnCloseItemIntroListener{
        void onCloseItemIntro();
    }

    private OnCloseItemIntroListener mListener;

    public OnCloseItemIntroListener getmListener() {
        return mListener;
    }

    public void setmListener(OnCloseItemIntroListener mListener) {
        this.mListener = mListener;
    }

    Context context;

    @Bind(R.id.train_prepare_view)
    TrainPrepareViewA trainPrepareView;

    public ShowItemIntroPopupWindow(View contentView) {
        super(contentView);
        init();
    }

    public ShowItemIntroPopupWindow(int width, int height) {
        super(width, height);
        init();
    }

    public ShowItemIntroPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        init();
    }

    public ShowItemIntroPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        init();
    }



    public ShowItemIntroPopupWindow(Context context) {
        super(context);
        this.context = context;
        init();
    }
    private void init(){
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_item_intro,null);
        setContentView(view);
        ButterKnife.bind(this,view);

        //??????????????????????????????????????????????????????
        trainPrepareView.setContainerMenuVisible(View.GONE);
    }

    @OnClick({R.id.rl_root,R.id.tv_exit})
    public void exit(){
        closeItemIntro();
    }


    public void setDescList(List<PrepareDesc> descList, TrainPres trainPres) {
        trainPrepareView.setDescList(descList,this,trainPres);
    }

    @Override
    public void onStartTrain() {
        closeItemIntro();
    }

    /**
     * ??????????????????
     */
    public void closeItemIntro(){
        this.dismiss();
        if(mListener!=null){
            mListener.onCloseItemIntro();
        }
    }

    /**
     * ?????????????????????
     */
    public void go2NextStage(){
        trainPrepareView.go2NextStage();
    }

    /**
     * ????????????
     */
    public void reset(){
        trainPrepareView.reset();
    }
}
