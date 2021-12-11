package com.uyu.device.devicetraining.presentation.view.fragment.trial;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.trial.RGFixedVectorConfig;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.RGFixedVectorTrainPresenter;
import com.uyu.device.devicetraining.presentation.presenter.trial.TrialRGFixedVectorTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.fragment.TrainFragment;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevelConfiguration;
import com.uyu.device.devicetraining.presentation.view.widget.RedGreenFusionPicLevel;

import butterknife.Bind;

/**
 * Created by windern on 2015/12/4.
 */
public class TrialRGFixedVectorTrainFragment extends TrialTrainFragment<TrialRGFixedVectorTrainPresenter> implements TrialRGFixedVectorTrainPresenter.RGFixedVectorListener{
    @Bind(R.id.iv_train_pic)
    RedGreenFusionPicLevel iv_train_pic;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_r_g_fixed_vector_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.R_G_FIXED_VECTOR.getShowName()+getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);
        trainPresenter.setItemListener(this);

        trainPresenter.setFusionPicLevel(iv_train_pic);

        FusionPicLevelConfiguration configuration = new FusionPicLevelConfiguration.Builder()
                .setFailMaxTime(RGFixedVectorConfig.Fail_Max_Time)
                .setFusingTime(RGFixedVectorConfig.Fusing_Max_Time)
                .setKeepingTime(RGFixedVectorConfig.Keeping_Max_Time)
                .setFusingTip(RGFixedVectorConfig.Fusing_Tip)
                .setKeepingTip(RGFixedVectorConfig.Keeping_Tip)
                .setFinishTip(RGFixedVectorConfig.Finish_Tip)
                .setFusingTipResid(RGFixedVectorConfig.Fusing_Tip_Resid)
                .setKeepingTipResid(RGFixedVectorConfig.Keeping_Tip_Resid)
                .setFinishTipResid(RGFixedVectorConfig.Finish_Tip_Resid)
                .build();
        iv_train_pic.setConfiguration(configuration);
    }
}
