package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.RedGreenReadTrainPresenter;
import com.uyu.device.devicetraining.presentation.util.Util;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevel;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevelConfiguration;
import com.uyu.device.devicetraining.presentation.view.widget.RedGreenReadView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2015/12/4.
 */
public class RedGreenReadTrainFragment extends TrainFragment<RedGreenReadTrainPresenter> implements RedGreenReadTrainPresenter.RedGreenReadListener{
    @Bind(R.id.tv_train_content)
    RedGreenReadView tv_train_content;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_red_green_read_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.RED_GREEN_READ.getShowName()+getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);
        trainPresenter.setItemListener(this);

        trainPresenter.setRedGreenReadView(tv_train_content);
    }
}
