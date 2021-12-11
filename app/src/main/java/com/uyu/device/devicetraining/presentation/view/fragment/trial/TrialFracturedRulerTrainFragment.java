package com.uyu.device.devicetraining.presentation.view.fragment.trial;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.trial.FracturedRulerConfig;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.FracturedRulerTrainPresenter;
import com.uyu.device.devicetraining.presentation.presenter.trial.TrialFracturedRulerTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.fragment.TrainFragment;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevel;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevelConfiguration;

import butterknife.Bind;

/**
 * Created by windern on 2015/12/4.
 */
public class TrialFracturedRulerTrainFragment extends TrialTrainFragment<TrialFracturedRulerTrainPresenter> implements TrialFracturedRulerTrainPresenter.FracturedRulerListener{
    @Bind(R.id.iv_train_pic)
    FusionPicLevel iv_train_pic;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_fractured_ruler_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.FRACTURED_RULER.getShowName()+getActivity().getResources().getString(R.string.train);
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
                .setFinishTipResid(FracturedRulerConfig.Finish_Tip_Resid)
                .setToastPosition(1)
                .build();
        iv_train_pic.setConfiguration(configuration);
    }
}
