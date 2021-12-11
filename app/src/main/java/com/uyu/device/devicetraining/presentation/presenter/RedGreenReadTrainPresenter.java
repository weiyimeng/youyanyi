package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.content.ContentManager;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.RedGreenReadTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.RedGreenReadTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.RedGreenReadTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnDrawFinish;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.util.Ulog;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.util.Util;
import com.uyu.device.devicetraining.presentation.view.widget.RedGreenReadView;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class RedGreenReadTrainPresenter extends TrainPresenter<RedGreenReadTrainPres,RedGreenReadTrainBack,RedGreenReadTrainNormal> implements OnFinishResultListener,OnDrawFinish {
    /**
     * 构造函数
     */

    @Inject
    public RedGreenReadTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * 每个项目单独默认的listener
     */

    public interface RedGreenReadListener{

    }

    private RedGreenReadListener itemListener;

    public void setItemListener(RedGreenReadListener itemListener) {
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
        trainBack = new RedGreenReadTrainBack();
        trainNomal = new RedGreenReadTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        if(trainPres.getTrainingContentType()==EnumContentType.ARTICLE){
            //和上一个处方完全一样，不用重新取内容了
            if(RedGreenReadTrainPres.isCompletedEqual(previousTrainPres,trainPres)) {
                realStartTraining();
            }else{
                //先取文章
                String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                contentUseCase.getTrainingContent(token, trainPres.getTrainingContentArticleId())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CallBackGetArticle());
            }
        }else{
            realStartTraining();
        }
        tflistener.setItemTip(context.getString(R.string.font_level)+trainPres.getLetterSize()+context);
    }

    @Override
    public void sendNowInfo() {
        trainNomal.setTrainContent(redGreenReadView.getNowShowContent());

        super.sendNowInfo();
    }

    @Override
    public void finishTrainViewStatus() {

    }

    @Override
    public void prepareFinishData() {
        trainBack.setLetterSize(trainPres.getLetterSize());
        trainBack.setLetterCount(redGreenReadView.getHasTrainLetterCount());

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createRedGreenRead(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createRedGreenRead(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack(isUpdateReception));
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
            changeToNext();
        }else if(trainStatus==TrainStatus.PAUSING){
            showMsg(context.getString(R.string.pause_status_please_wait));
        }
    }

    /**
     * 单独的实现的接口
     */

    @Override
    public void onFinishResult(EnumResultType resultType) {
        finishTrain();
    }

    @Override
    public void onDrawFinish() {
        sendNowInfo();
    }

    /**
     * 单独的部分
     */

    private RedGreenReadTrainPres previousTrainPres;

    private String content = "";
    /**
     * 显示的控件
     */
    private RedGreenReadView redGreenReadView;

    public void setRedGreenReadView(RedGreenReadView redGreenReadView) {
        this.redGreenReadView = redGreenReadView;
        this.redGreenReadView.setFinishResultListener(this);
        this.redGreenReadView.setDrawFinishListener(this);
    }

    private void changeToNext(){
        redGreenReadView.nextPage();
    }

    public void realStartTraining() {
        if(RedGreenReadTrainPres.isCompletedEqual(previousTrainPres,trainPres)) {
            //如果处方完全相同，则不需要刷新内容
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                redGreenReadView.resetStartStatus(false);
            }else{
                redGreenReadView.resetStartStatus(true);
            }
        }else{
            redGreenReadView.resetStartStatus(true);
            //处方不同，需要重新赋值文章的内容
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                redGreenReadView.setSrcContent(content);
            }
        }

        int letterSize = trainPres.getLetterSize();
        float trainingTextSize = Util.arrayVisionCardLevels[letterSize].getTextSize();
        redGreenReadView.setMmSize(trainingTextSize);
        redGreenReadView.setContentType(trainPres.getTrainingContentType());

        previousTrainPres = trainPres;

        changeToNext();
        changeToTrain();
    }

    public void changeToTrain(){
        trainStatus = TrainStatus.TRAINING;
        tflistener.changeToStatus(trainStatus);
    }

    private final class CallBackGetArticle extends Subscriber<ModelApiResult<TrainingContent>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            trainPres.setTrainingContentType(EnumContentType.LETTER);
            Ulog.d("ReversalTrainTrainPresenter", "获取文章失败");

            realStartTraining();
        }

        @Override
        public void onNext(ModelApiResult<TrainingContent> apiResult) {
            if(apiResult.getCode()==0){
                TrainingContent trainingContent = apiResult.getData();
                content = ContentManager.removeEmptyChar(trainingContent.getContent());
            }else{
                trainPres.setTrainingContentType(EnumContentType.LETTER);
                Ulog.d("ReversalTrainTrainPresenter", "获取文章失败");
            }

            realStartTraining();
        }
    }
}
