package com.uyu.device.devicetraining.presentation.view.fragment.trial;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.trial.RGVariableVectorConfig;
import com.uyu.device.devicetraining.data.entity.config.trial.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.RGVariableVectorTrainPresenter;
import com.uyu.device.devicetraining.presentation.presenter.trial.TrialRGVariableVectorTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.fragment.TrainFragment;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevelConfiguration;
import com.uyu.device.devicetraining.presentation.view.widget.RGVariableConfiguration;
import com.uyu.device.devicetraining.presentation.view.widget.RGVariableVectorView;

import butterknife.Bind;

/**
 * Created by windern on 2015/12/4.
 */
public class TrialRGVariableVectorTrainFragment extends TrialTrainFragment<TrialRGVariableVectorTrainPresenter> implements TrialRGVariableVectorTrainPresenter.RGVariableVectorListener{
    @Bind(R.id.iv_train_pic)
    RGVariableVectorView rgVariableVectorView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_r_g_variable_vector_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.R_G_VARIABLE_VECTOR.getShowName()+getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);
        trainPresenter.setItemListener(this);
        trainPresenter.setRgVariableVectorView(rgVariableVectorView);

        RGVariableConfiguration configuration = new RGVariableConfiguration.Builder()
                .setExtraEndWaitSecond(RGVariableVectorConfig.ExtraEndWaitSecond)
                .setTwoSplitMaxSecond(RGVariableVectorConfig.TwoSplitMaxSecond)
                .build();
        rgVariableVectorView.setConfiguration(configuration);
    }
}
