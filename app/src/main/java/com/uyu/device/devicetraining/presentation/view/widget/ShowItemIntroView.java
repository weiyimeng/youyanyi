package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

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
 * Created by windern on 16/7/21.
 */
public class ShowItemIntroView extends RelativeLayout implements OnStartTrainListener {
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

    private Context context;
    private View view;

    @Bind(R.id.train_prepare_view)
    TrainPrepareViewA trainPrepareView;

    public ShowItemIntroView(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    public ShowItemIntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initLayout();
    }

    public ShowItemIntroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initLayout();
    }

    /**
     * 初始化布局
     */
    public void initLayout(){
        view = LayoutInflater.from(context).inflate(R.layout.dialog_item_intro, this,
                true);
        ButterKnife.bind(this, view);

        //弹出项目使用说明的时候隐藏右边的目录
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
     * 关闭小项介绍
     */
    public void closeItemIntro(){
        if(mListener!=null){
            mListener.onCloseItemIntro();
        }
    }

    /**
     * 跳转到下一界面
     */
    public void go2NextStage(){
        trainPrepareView.go2NextStage();
    }

    /**
     * 重置界面
     */
    public void reset(){
        trainPrepareView.reset();
    }
}
