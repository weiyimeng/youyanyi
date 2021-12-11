package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.ApproachTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.widget.ApproachView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2015/12/4.
 */
public class ApproachTrainFragment extends TrainFragment<ApproachTrainPresenter> implements ApproachTrainPresenter.ApproachListener{
    @Bind(R.id.view_train)
    ApproachView view_train;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_approach_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.APPROACH.getShowName()+getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);
        trainPresenter.setItemListener(this);

        trainPresenter.setApproachView(view_train);
    }
}
