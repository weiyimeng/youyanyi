package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.GlanceTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.widget.GlanceView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2015/12/4.
 */
public class GlanceTrainFragment extends TrainFragment<GlanceTrainPresenter> implements GlanceTrainPresenter.GlanceListener{
    @Bind(R.id.tv_train_content)
    GlanceView tv_train_content;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_glance_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.GLANCE.getShowName()+getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);
        trainPresenter.setItemListener(this);

        trainPresenter.setGlanceView(tv_train_content);
    }
}
