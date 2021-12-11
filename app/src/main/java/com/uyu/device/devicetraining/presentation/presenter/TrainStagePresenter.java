package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;

import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.LoginUseCase;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnFinishTrainListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnStartTrainListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDescManager;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainAllFinishView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by windern on 2016/4/5.
 */
@PerActivity
public class TrainStagePresenter implements OnStartTrainListener,TrainAllFinishView.TrainAllFinishViewListener {
    private final ContentUseCase contentUseCase;
    private final Context context;

    //训练处方
    TrainPres trainPres;

    public interface TrainStageViewListener{

    }

    private TrainStageViewListener viewListener;

    public void setViewListener(TrainStageViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Inject
    public TrainStagePresenter(@Named("contentUseCase") ContentUseCase contentUseCase, Context context) {
        this.contentUseCase = contentUseCase;
        this.context = context;
    }

    @Override
    public void onStartTrain() {

    }

    @Override
    public void onFinishViewClick() {

    }
}
