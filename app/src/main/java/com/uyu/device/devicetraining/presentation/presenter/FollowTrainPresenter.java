package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.TrainBackApiResult;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.FollowTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.TrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.FollowTrainPres;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.model.FollowInspectionDesc;
import com.uyu.device.devicetraining.presentation.model.FollowInspectionDescManager;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.view.widget.FollowView;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class FollowTrainPresenter extends TrainPresenter<FollowTrainPres,FollowTrainBack,TrainNormal> implements OnFinishResultListener {
    /**
     * 构造函数
     */

    @Inject
    public FollowTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * 每个项目单独默认的listener
     */

    public interface FollowListener{

    }

    private FollowListener itemListener;

    public void setItemListener(FollowListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * 每个项目都要独立实现的
     */

    @Override
    public void initStartStatus() {
        super.initStartStatus();
    }

    @Override
    protected void newTrainBack() {
        trainBack = new FollowTrainBack();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        ArrayList<FollowInspectionDesc> list = FollowInspectionDescManager.getList(trainPres.getLineType(),trainPres.getLineCount(),8);
        followView.setList(list);

        followView.start();
    }

    @Override
    public void sendNowInfo() {

    }

    @Override
    public void finishTrainViewStatus() {
        followView.initViewAfterFinish();
    }

    @Override
    public void prepareFinishData() {
        accuracy = followView.getAccuracy();
        //精确保留两位小数
        BigDecimal bd = new BigDecimal(accuracy);
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        accuracy = bd.doubleValue();
        Timber.d("finishTrain:accuracy:%s",accuracy);
        trainBack.setAccuracy(accuracy);

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createFollow(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createFollow(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack(isUpdateReception));
    }

    @Override
    public void pauseTrain() {
        super.pauseTrain();

        followView.pauseTrain();
    }

    @Override
    public void resumeTrain() {
        super.resumeTrain();

        followView.resumeTrain();
    }

    /**
     * 每个项目额外可覆盖的父类的方法
     */

    @Override
    public void pressEnter() {
        if(trainStatus==TrainStatus.WELCOME){
            showMsg(context.getString(R.string.training_has_not_yet_begun_please_wait));
        } else if (trainStatus==TrainStatus.WAITING){
            showMsg(context.getString(R.string.the_training_is_not_ready_please_wait));
        } else if(trainStatus==TrainStatus.TRAINING){
            followView.pressOne();
        }else if(trainStatus==TrainStatus.PAUSING){
            showMsg(context.getString(R.string.pause_status_please_wait));
        }
    }

    @Override
    public void pressUp() {
        followView.pressTwo();
    }

    @Override
    public void pressDown() {
        followView.pressThree();
    }

    /**
     * 单独的实现的接口
     */

    @Override
    public void onFinishResult(EnumResultType resultType) {
        finishTrain();
    }

    /**
     * 单独的部分
     */

    private double accuracy = 0d;

    private String content = "";
    /**
     * 显示的控件
     */
    private FollowView followView;

    public void setFollowView(FollowView followView) {
        this.followView = followView;
        this.followView.setResultListener(this);
    }


}
