package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.AppProvider;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.FollowTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.widget.FollowView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2015/12/4.
 */
public class FollowTrainFragment extends TrainFragment<FollowTrainPresenter> implements FollowTrainPresenter.FollowListener{
    @Bind(R.id.iv_train_pic)
    FollowView iv_train_pic;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_follow_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.FOLLOW.getShowName()+ getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);
        trainPresenter.setItemListener(this);

        trainPresenter.setFollowView(iv_train_pic);
    }
}
