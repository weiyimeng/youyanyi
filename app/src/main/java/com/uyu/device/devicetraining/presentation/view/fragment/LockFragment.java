package com.uyu.device.devicetraining.presentation.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.CheckAuthPresenter;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;
import com.uyu.device.devicetraining.presentation.view.widget.LockView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2016/5/17.
 */
public class LockFragment extends BaseFragment implements LockView.LockViewListener{
    @Bind(R.id.lock_view)
    LockView lockView;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_lock, container, true);
        ButterKnife.bind(this, fragmentView);

        lockView.setmListener(this);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    protected void initialize() {

    }

    public void refreshCode(){
        lockView.initPauseQrcode();
    }

    public void startTimer(){
        lockView.startTimer();
    }

    public void stopTimer(){
        lockView.stopTimer();
    }

    @Override
    public void noOptFinishTrain() {
        ((WelcomeActivity)getActivity()).noOptFinishTrain();
    }
}
