package com.uyu.device.devicetraining.presentation.presenter.trial;

import android.content.Context;

import com.google.gson.Gson;
import com.uyu.device.devicetraining.data.entity.ApiResult;
import com.uyu.device.devicetraining.data.entity.other.ReceptionTrial;
import com.uyu.device.devicetraining.data.entity.trainback.StereoscopeTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.TrialFracturedRulerTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.TrialRGFixedVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.TrialRGVariableVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.TrialReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.TrialStereoscopeTrainBack;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnStartTrainListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainAllFinishView;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainItemTimeType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2016/4/5.
 */
@PerActivity
public class TrainTrialStagePresenter implements OnStartTrainListener,TrainAllFinishView.TrainAllFinishViewListener {
    private final ContentUseCase contentUseCase;
    private final TrainBackUseCase trainBackUseCase;
    private final Context context;

    //体验训练模型
    private ReceptionTrial receptionTrial;

    private TrainItemTimeType itemTimeType = TrainItemTimeType.ALL_PRES;
    private EnumTrainItem trainItem = EnumTrainItem.REVERSAL;
    private EnumEyeType eyeType = EnumEyeType.LEFT;
    private EnumFusionTrain fusionTrain = EnumFusionTrain.BO;

    private List<TrainBack> trainBacks = new ArrayList<>();

    public interface TrainTrialStagePresenterListener{

    }

    private TrainTrialStagePresenterListener listener;

    public void setListener(TrainTrialStagePresenterListener listener) {
        this.listener = listener;
    }

    @Inject
    public TrainTrialStagePresenter(@Named("contentUseCase") ContentUseCase contentUseCase
            , @Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , Context context) {
        this.contentUseCase = contentUseCase;
        this.trainBackUseCase = trainBackUseCase;
        this.context = context;
    }

    public ReceptionTrial getReceptionTrial() {
        return receptionTrial;
    }

    public void setReceptionTrial(ReceptionTrial receptionTrial) {
        this.receptionTrial = receptionTrial;
    }

    @Override
    public void onStartTrain() {

    }

    @Override
    public void onFinishViewClick() {

    }
}
