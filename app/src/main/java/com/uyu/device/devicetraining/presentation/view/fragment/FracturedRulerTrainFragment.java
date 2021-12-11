package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.FracturedRulerConfig;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.AppProvider;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.quick.FracturedRulerTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevel;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevelConfiguration;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2015/12/4.
 */
public class FracturedRulerTrainFragment extends TrainFragment<FracturedRulerTrainPresenter> implements FracturedRulerTrainPresenter.FracturedRulerListener{
    @Bind(R.id.iv_train_pic)
    FusionPicLevel iv_train_pic;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_fractured_ruler_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.FRACTURED_RULER.getShowName()+ getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);

        trainPresenter.setItemListener(this);
        trainPresenter.setFusionPicLevel(iv_train_pic);

        FusionPicLevelConfiguration configuration = new FusionPicLevelConfiguration.Builder()
                .setFailMaxTime(FracturedRulerConfig.Fail_Max_Time)
                .setFusingTime(FracturedRulerConfig.Fusing_Max_Time)
                .setKeepingTime(FracturedRulerConfig.Keeping_Max_Time)
                .setFusingTip(FracturedRulerConfig.Fusing_Tip)
                .setKeepingTip(FracturedRulerConfig.Keeping_Tip)
                .setFinishTip(FracturedRulerConfig.Finish_Tip)
                .setFusingTipResid(FracturedRulerConfig.Fusing_Tip_Resid)
                .setKeepingTipResid(FracturedRulerConfig.Keeping_Tip_Resid)
                //.setFinishTipResid(FracturedRulerConfig.Finish_Tip_Resid)//不需要了，自动就接上调整设备了
                .setToastPosition(0)
                .build();
        iv_train_pic.setConfiguration(configuration);
    }
}
