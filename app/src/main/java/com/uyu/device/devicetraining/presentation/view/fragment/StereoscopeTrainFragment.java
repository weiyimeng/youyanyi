package com.uyu.device.devicetraining.presentation.view.fragment;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.quick.StereoscopeTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevel;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevelConfiguration;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2015/12/4.
 */
public class StereoscopeTrainFragment extends TrainFragment<StereoscopeTrainPresenter> implements StereoscopeTrainPresenter.StereoscopeListener{
    @Bind(R.id.iv_train_pic)
    FusionPicLevel iv_train_pic;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_stereoscope_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.STEREOSCOPE.getShowName()+getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);
        trainPresenter.setItemListener(this);
        trainPresenter.setFusionPicLevel(iv_train_pic);

        FusionPicLevelConfiguration configuration = new FusionPicLevelConfiguration.Builder()
                .setFailMaxTime(StereoscopeConfig.Fail_Max_Time)
                .setFusingTime(StereoscopeConfig.Fusing_Max_Time)
                .setKeepingTime(StereoscopeConfig.Keeping_Max_Time)
                .setFusingTip(StereoscopeConfig.Fusing_Tip)
                .setKeepingTip(StereoscopeConfig.Keeping_Tip)
                .setFinishTip(StereoscopeConfig.Finish_Tip)
                .setFusingTipResid(StereoscopeConfig.Fusing_Tip_Resid)
                .setKeepingTipResid(StereoscopeConfig.Keeping_Tip_Resid)
                .setFinishTipResid(StereoscopeConfig.Finish_Tip_Resid)
                .build();
        iv_train_pic.setConfiguration(configuration);
    }
}
