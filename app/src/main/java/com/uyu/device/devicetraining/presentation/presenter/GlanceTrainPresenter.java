package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.content.ContentManager;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.GlanceTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.TrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.GlanceTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.RawProvider;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.util.Ulog;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.util.Util;
import com.uyu.device.devicetraining.presentation.view.widget.GlanceView;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class GlanceTrainPresenter extends TrainPresenter<GlanceTrainPres,GlanceTrainBack,TrainNormal> implements OnFinishResultListener {

    TtsEngine mTtsEngine;
    /**
     * 构造函数
     */

    @Inject
    public GlanceTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
        if(mTtsEngine == null) {
            mTtsEngine = new TtsEngine(context);
        }
    }

    public void startSpeak(){
        /**
         * 开始语音播报
         */
        if(mTtsEngine != null){
            mTtsEngine.startSpeaking(RawProvider.glance_prompt);
        }
    }

    public void stopSpeaking() {
        /**
         * 停止语音播报
         */
        if (mTtsEngine != null) {
            mTtsEngine.stopSpeaking();
            mTtsEngine.release();
        }
    }


    /**
     * 每个项目单独默认的listener
     */

    public interface GlanceListener{

    }

    private GlanceListener itemListener;

    public void setItemListener(GlanceListener itemListener) {
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
        trainBack = new GlanceTrainBack();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        if(trainPres.getTrainingContentType()==EnumContentType.ARTICLE){
            //第一遍先取文章，后面直接开始
            if(presTrainMessageContent.getCurrentRepeatTime()==1) {
                String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                contentUseCase.getTrainingContent(token, trainPres.getTrainingContentArticleId())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CallBackGetArticle());
            }else{
                realStartTraining();
            }
        }else{
            realStartTraining();
        }
        startSpeak();
    }

    @Override
    public void sendNowInfo() {

    }

    @Override
    public void finishTrainViewStatus() {

    }

    @Override
    public void prepareFinishData() {
        trainBack.setLetterSize(trainPres.getLetterSize());
        trainBack.setLetterCount(glanceView.getHasTrainLetterCount());

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createGlance(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createGlance(token, trainBack)
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
        stopSpeaking();
    }

    /**
     * 单独的部分
     */


    private String content = "";
    /**
     * 显示的控件
     */
    private GlanceView glanceView;

    public void setGlanceView(GlanceView glanceView) {
        this.glanceView = glanceView;
        this.glanceView.setFinishResultListener(this);
    }

    private void changeToNext(){
        glanceView.nextPage();
    }

    public void realStartTraining() {
        changeToTrain();

        //第一遍需要获取内容
        if(presTrainMessageContent.getCurrentRepeatTime()==1) {
            glanceView.resetStartStatus(true);

            int letterSize = trainPres.getLetterSize();
            //trainPres.setTrainingContentType(EnumContentType.NUMBER);
            //int letterSize = 5;
            float trainingTextSize = Util.arrayVisionCardLevels[letterSize].getTextSize();
            glanceView.setMmSize(trainingTextSize);

            glanceView.setContentType(trainPres.getTrainingContentType());
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                glanceView.setSrcContent(content);
            }
        }else{
            //直接还原开始
            glanceView.resetStartStatus(false);
        }
        changeToNext();
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
