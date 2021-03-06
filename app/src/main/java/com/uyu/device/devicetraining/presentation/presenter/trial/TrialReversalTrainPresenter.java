package com.uyu.device.devicetraining.presentation.presenter.trial;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.EnumUserTrainMode;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.config.trial.ReversalConfig;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.entity.content.ContentManager;
import com.uyu.device.devicetraining.data.entity.content.News;
import com.uyu.device.devicetraining.data.entity.content.ShowContent;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent_Table;
import com.uyu.device.devicetraining.data.entity.content.UserUploadIds;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.ReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.TrialReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.ReversalTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumContentLoopMode;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumGlassType;
import com.uyu.device.devicetraining.data.entity.type.EnumPresAdjust;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.TurntableLocation;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnDrawFinish;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.presenter.TrainPresenter;
import com.uyu.device.devicetraining.presentation.type.EnumDeviceStatus;
import com.uyu.device.devicetraining.presentation.util.MyConfig;
import com.uyu.device.devicetraining.presentation.util.Util;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainItemTimeType;
import com.uyu.device.devicetraining.presentation.view.widget.LineView;
import com.uyu.device.devicetraining.presentation.view.widget.SelectDropView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class TrialReversalTrainPresenter extends TrialTrainPresenter<ReversalTrainPres,TrialReversalTrainBack,ReversalTrainNormal> implements OnDrawFinish,SelectDropView.SelectDropViewListener {
    /**
     * ????????????
     */

    @Inject
    public TrialReversalTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase, contentUseCase, context, ttsEngine);
    }

    /**
     * ???????????????????????????listener
     */

    public interface ReversalListener{

    }

    private ReversalListener itemListener;

    public void setItemListener(ReversalListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * ?????????????????????????????????
     */

    @Override
    public void initStartStatus() {
        useSecond = 0;
        glassUseSecond = 0;
        useSecondFindRightGlass = 0;

        glassLevel = 0;
        isOnFindGlassLevel = true;
        glassChangeCount = 0;

        //?????????????????????????????????
        glassType=EnumGlassType.Zheng;

        super.initStartStatus();
    }

    @Override
    public void preparePresBack() {
        super.preparePresBack();

        //?????????????????????????????????????????????????????????api??????????????????eye_type
        //??????startTrain??????????????????????????????????????????????????????
        trainBack.setEyeType(trainPres.getEyeType());

        trainBack.setLPositiveDegreeLevel(trainPres.getLPositiveDegreeLevel());
        trainBack.setLNegativeDegreeLevel(trainPres.getLNegativeDegreeLevel());
        trainBack.setRPositiveDegreeLevel(trainPres.getRPositiveDegreeLevel());
        trainBack.setRNegativeDegreeLevel(trainPres.getRNegativeDegreeLevel());
    }

    @Override
    protected void newTrainBack() {
        trainBack = new TrialReversalTrainBack();
        trainNomal = new ReversalTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        handleLoc();

        if(getUserTrainMode()== EnumUserTrainMode.CONTROL) {
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                //?????????????????????????????????????????????????????????
                if (ReversalTrainPres.isCompletedEqual(previousTrainPres, trainPres)) {
                    realStartTraining();
                } else {
                    //????????????
                    String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                    contentUseCase.getTrainingContent(token, trainPres.getTrainingContentArticleId())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CallBackGetArticle());
                }
            } else {
                realStartTraining();
            }
        }else{
            String resultUploads = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_NOW_TRAIN_USER_UPLOADIDS);
            List<UserUploadIds> userUploadIdsList = new ArrayList<>();
            Gson gson = new Gson();
            userUploadIdsList = gson.fromJson(resultUploads, new TypeToken<List<UserUploadIds>>(){}.getType());

            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                UserUploadIds userUploadIds = null;
                //?????????????????????ids
                for(int i=0;i<userUploadIdsList.size();i++){
                    if(userUploadIdsList.get(i).type==0){
                        userUploadIds = userUploadIdsList.get(i);
                    }
                }
                //????????????????????????????????????
                if(userUploadIds!=null && userUploadIds.ids!=null && userUploadIds.ids.size()>0){
                    //????????????????????????
                    List<ShowContent> beforList = new ArrayList<>();
                    List<ShowContent> afterList = new ArrayList<>();
                    boolean isFindId = false;
                    for(int j=0;j<userUploadIds.ids.size();j++){
                        int id = userUploadIds.ids.get(j);
                        TrainingContent trainingContent = SQLite.select().from(TrainingContent.class).where(TrainingContent_Table.id.eq(id)).querySingle();
                        if(trainingContent!=null){
                            if(id==trainPres.getTrainingContentArticleId()){
                                isFindId = true;
                            }
                            if(isFindId){
                                afterList.add(trainingContent.toShowContent());
                            }else{
                                beforList.add(trainingContent.toShowContent());
                            }
                        }
                    }
                    for(int m=0;m<afterList.size();m++){
                        listShowContent.add(afterList.get(m));
                    }
                    for(int m=0;m<beforList.size();m++){
                        listShowContent.add(beforList.get(m));
                    }

                    realStartTraining();
                }else{
                    trainPres.setTrainingContentType(EnumContentType.LETTER);
                    realStartTraining();
                }
            } else if(trainPres.getTrainingContentType() == EnumContentType.NEWS) {
                //????????????
                String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                contentUseCase.getNewsOnce(token, trainPres.getTrainingContentCategoryId())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CallBackGetNews());

            } else{
                realStartTraining();
            }
        }
    }

    @Override
    public void startTrain(ReversalTrainPres trainPres) {
        super.startTrain(trainPres);

        handleLoc();

        if(getUserTrainMode()== EnumUserTrainMode.CONTROL) {
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                //?????????????????????????????????????????????????????????
                if (ReversalTrainPres.isCompletedEqual(previousTrainPres, trainPres)) {
                    realStartTraining();
                } else {
                    //????????????
                    String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                    contentUseCase.getTrainingContent(token, trainPres.getTrainingContentArticleId())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CallBackGetArticle());
                }
            } else {
                realStartTraining();
            }
        }else{
            String resultUploads = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_NOW_TRAIN_USER_UPLOADIDS);
            List<UserUploadIds> userUploadIdsList = new ArrayList<>();
            Gson gson = new Gson();
            userUploadIdsList = gson.fromJson(resultUploads, new TypeToken<List<UserUploadIds>>(){}.getType());

            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                UserUploadIds userUploadIds = null;
                //?????????????????????ids
                for(int i=0;i<userUploadIdsList.size();i++){
                    if(userUploadIdsList.get(i).type==0){
                        userUploadIds = userUploadIdsList.get(i);
                    }
                }
                //????????????????????????????????????
                if(userUploadIds!=null && userUploadIds.ids!=null && userUploadIds.ids.size()>0){
                    //????????????????????????
                    List<ShowContent> beforList = new ArrayList<>();
                    List<ShowContent> afterList = new ArrayList<>();
                    boolean isFindId = false;
                    for(int j=0;j<userUploadIds.ids.size();j++){
                        int id = userUploadIds.ids.get(j);
                        TrainingContent trainingContent = SQLite.select().from(TrainingContent.class).where(TrainingContent_Table.id.eq(id)).querySingle();
                        if(trainingContent!=null){
                            if(id==trainPres.getTrainingContentArticleId()){
                                isFindId = true;
                            }
                            if(isFindId){
                                afterList.add(trainingContent.toShowContent());
                            }else{
                                beforList.add(trainingContent.toShowContent());
                            }
                        }
                    }
                    for(int m=0;m<afterList.size();m++){
                        listShowContent.add(afterList.get(m));
                    }
                    for(int m=0;m<beforList.size();m++){
                        listShowContent.add(beforList.get(m));
                    }

                    realStartTraining();
                }else{
                    trainPres.setTrainingContentType(EnumContentType.LETTER);
                    realStartTraining();
                }
            } else if(trainPres.getTrainingContentType() == EnumContentType.NEWS) {
                //????????????
                String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                contentUseCase.getNewsOnce(token, trainPres.getTrainingContentCategoryId())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CallBackGetNews());

            } else{
                realStartTraining();
            }
        }
    }

    @Override
    public void sendNowInfo() {
        trainNomal.setTrainContent(lineView.getNowShowContent());

        super.sendNowInfo();
    }

    @Override
    public void finishTrainViewStatus() {

    }

    public void prepareFinishData() {
        trainBack.setLNegativeDegreeLevel(glassLevel);
        trainBack.setLPositiveDegreeLevel(glassLevel);
        trainBack.setRNegativeDegreeLevel(glassLevel);
        trainBack.setRPositiveDegreeLevel(glassLevel);

        trainBack.setPrescriptionAdjustStatus(EnumPresAdjust.UNCHANGE);
        trainBack.setPositiveLetterSize(zhengLevel);
        trainBack.setNegativeLetterSize(fuLevel);


        trainBack.setLPositiveDegreeLevel(glassLevel);
        trainBack.setLNegativeDegreeLevel(glassLevel);
        trainBack.setRPositiveDegreeLevel(glassLevel);
        trainBack.setRNegativeDegreeLevel(glassLevel);

        trainBack.computeScore();

        super.prepareFinishData();
    }

    @Override
    public void finishTrain() {
        stopTimer();
        stopFindRightGlassTimer();
        stopGlassLookTimer();

        super.finishTrain();

        //??????????????????????????????????????????
        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        MotorBus.getInstance().removeAllMessage();
    }

    @Override
    public void receiveFinishServerMessage(){
        stopTimer();
        stopFindRightGlassTimer();
        stopGlassLookTimer();

        super.receiveFinishServerMessage();

        //??????????????????????????????????????????
        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        MotorBus.getInstance().removeAllMessage();
    }

    @Override
    public void postData() {
        super.postData();
//        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
//        trainBackUseCase.createReversal(token, trainBack)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new TrainPresenter.CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
//        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
//        trainBackUseCase.createReversal(token, trainBack)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new TrainPresenter.CallBackPostTrainingBack(isUpdateReception));
    }

    @Override
    public void pauseTrain() {
        super.pauseTrain();

        stopTimer();
        stopFindRightGlassTimer();
        stopGlassLookTimer();
    }

    @Override
    public void resumeTrain() {
        super.resumeTrain();

        //??????????????????????????????????????????????????????????????????????????????????????????????????????
        glassUseSecond = 0;
        Calendar now = Calendar.getInstance();
        startDate = now.getTime();

        if(isOnFindGlassLevel){
            startFindRightGlassTimer();
        }else {
            startTimer();
        }
        startGlassLookTimer();
    }

    /**
     * ?????????????????????????????????????????????
     */

    @Override
    public void pressEnter() {
        Timber.d("pressEnter");
        //????????????
        Calendar now = Calendar.getInstance();
        long nowTime = now.getTime().getTime();
        if(nowTime-pressTime > TrainConfig.ButtonPressShakeTime){
            pressTime = nowTime;

            stopGlassLookTimer();
            changeGlass();
        }else{
            showMsg(context.getString(R.string.plese_not_frequently_press_key));
        }
    }

    //???????????????????????????????????????????????????????????????

//    @Override
//    public void pressUp() {
//        getGlassSign(EnumGlassType.Zheng);
//    }
//
//    @Override
//    public void pressDown() {
//        getGlassSign(EnumGlassType.Fu);
//    }

    /**
     * ????????????????????????
     */

    @Override
    public void onDrawFinish() {
        sendNowInfo();
    }

    @Override
    public void select(int pos) {

    }

    /**
     * ???????????????
     */

    /**
     * button????????????
     */
    public long pressTime = 0;

    //????????????????????????
    private TurntableLocation leftZhengLoc = TurntableLocation.ZHENG150;
    private TurntableLocation leftFuLoc = TurntableLocation.FU150;
    private TurntableLocation rightZhengLoc = TurntableLocation.ZHENG150;
    private TurntableLocation rightFuLoc = TurntableLocation.FU150;

    private ReversalTrainPres previousTrainPres;

    /**
     * ?????????????????????
     */
    private LineView lineView;
    /**
     * ?????????????????????????????????
     */
    private SelectDropView viewLoopType;

    public void setLineView(LineView lineView) {
        this.lineView = lineView;
        this.lineView.setDrawFinishListener(this);
    }

    public void setViewLoopType(SelectDropView viewLoopType){
        this.viewLoopType = viewLoopType;
        this.viewLoopType.setViewListener(this);

        this.viewLoopType.setSelectTip(context.getString(R.string.content_loop_mode));
        ArrayList<String> datas = new ArrayList<>();
        for(int i = 0; i< EnumContentLoopMode.values().length; i++){
            datas.add(EnumContentLoopMode.values()[i].getName());
        }
        this.viewLoopType.setDataList(datas);
    }

    /**
     * ??????????????????
     */
    private EnumContentLoopMode contentLoopMode = EnumContentLoopMode.SEQUENCE;
    /**
     * ????????????
     */
    private List<ShowContent> listShowContent = new ArrayList<>();
    /**
     * ???????????????????????????
     */
    private int nowShowContentPos = 0;

    /**
     * ????????????
     */
    public EnumEyeType eyeType = EnumEyeType.LEFT;
    /**
     * ????????????
     */
    public EnumGlassType glassType = EnumGlassType.Zheng;

    /**
     * ??????????????????
     */
    private float trainingTextSize = 45;
    /**
     * ????????????
     */
    private int level = 0;
    /**
     * ??????????????????
     */
    private int zhengLevel = 10;
    /**
     * ??????????????????
     */
    private int fuLevel = 10;
    /**
     * ????????????s
     */
    public int useSecond = 0;
    /**
     * ????????????10s?????????
     */
    public int glassUseSecond = 0;
    /**
     * ?????????????????????
     */
    public int useSecondFindRightGlass = 0;

    /**
     * ??????????????????????????????
     */
    private Date startDate;
    /**
     * ??????????????????????????????
     */
    private Date endDate;

    /**
     * ????????????
     */
    private int glassLevel = 0;
    /**
     * ???????????????????????????
     * false????????????????????????????????????
     */
    private boolean isOnFindGlassLevel = true;
    /**
     * ?????????????????????
     */
    private int glassChangeCount = 0;

    /**
     * ?????????????????????
     */
    private Handler handlerChangeTime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int leftTime = com.uyu.device.devicetraining.data.entity.config.ReversalConfig.oneTrainingSecond - useSecond;
            Timber.d("????????????: %s ???",leftTime);
        }
    };

    private Observable<Long> observableTimer = null;
    private Subscriber<Long> subscriberTimer = null;
    private Subscription subscriptionTimer = null;

    private void startTimer(){
        Timber.d("startTimer");
        observableTimer = Observable.interval(TrainConfig.UnitOne, TimeUnit.SECONDS);
        subscriberTimer = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                glassUseSecond++;
                useSecond++;
                Timber.d("????????????: %s ???",useSecond);

                //???????????????
                if (useSecond >= ReversalConfig.oneTrainingSecond) {
                    finishTrain();
                }
            }
        };

        subscriptionTimer = observableTimer.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberTimer);
    }
    private void stopTimer(){
        Timber.d("stopTimmer");
        if(subscriptionTimer !=null && !subscriptionTimer.isUnsubscribed()){
            subscriptionTimer.unsubscribe();
        }
        if(subscriberTimer !=null && !subscriberTimer.isUnsubscribed()){
            subscriberTimer.unsubscribe();
        }
        subscriptionTimer = null;
        subscriberTimer = null;
        observableTimer = null;
    }

    /**
     * ????????????????????????
     *
     * @param glassResult ??????0????????????1?????????2????????????3???????????????
     */
    public void finishOneGlass(int glassResult) {
        if (context == null) {
            return;
        }

        if (glassResult < 3) {
            if (glassResult == 0) {
                if (glassType == EnumGlassType.Zheng) {
                    if (zhengLevel < Util.arrayVisionCardLevels.length - 1) {
                        zhengLevel++;
                    }
                } else {
                    if (fuLevel < Util.arrayVisionCardLevels.length - 1) {
                        fuLevel++;
                    }
                }

            } else if (glassResult == 1) {

            } else if (glassResult == 2) {
                if (glassType == EnumGlassType.Zheng) {
                    if (zhengLevel > 0) {
                        zhengLevel--;
                    }
                } else {
                    if (fuLevel > 0) {
                        fuLevel--;
                    }
                }
            }
            // ???????????????????????????????????????1
            trainBack.detailCountAdd(glassType, level);

            // 1???0???0???1
            int glassTypeValue = 1 - glassType.getValue();
            glassType = EnumGlassType.values()[glassTypeValue];

            if(isOnFindGlassLevel){
                glassChangeCount++;
                int levelTryCompareCount = ReversalConfig.arrayLevelTryCount[glassLevel];
                if(glassChangeCount==levelTryCompareCount*2){
                    boolean isNeedStartRealTrain = false;
                    if(zhengLevel>= ReversalConfig.DefaultLetterSize && fuLevel>= ReversalConfig.DefaultLetterSize){
                        if(glassLevel>= ReversalConfig.MaxGlassLevel-1){
                            //??????????????????????????????????????????
                            isNeedStartRealTrain = true;
                        }else{
                            glassLevel++;
                            //????????????????????????????????????????????????
                            glassChangeCount = 0;
                            //????????????????????????????????????????????????13??????????????????????????????15????????????????????????
                            zhengLevel = 13;
                            fuLevel = 13;
                            Timber.d("????????????+1,???"+String.valueOf(glassLevel)+"???");
                        }
                    }else{
                        isNeedStartRealTrain = true;
                    }

                    if(isNeedStartRealTrain){
                        Timber.d("??????????????????");
                        isOnFindGlassLevel = false;
                        startTimer();
                    }
                }
            }
        } else if (glassResult == 3) {
            if (glassType == EnumGlassType.Zheng) {
                if (zhengLevel > 0) {
                    zhengLevel--;
                }
            } else {
                if (fuLevel > 0) {
                    fuLevel--;
                }
            }
        }

        //?????????????????????????????????????????????
        //initTraining();
    }

    private final class CallBackGetArticle extends Subscriber<ModelApiResult<TrainingContent>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showMsg(context.getString(R.string.get_article_fail));
            trainPres.setTrainingContentType(EnumContentType.LETTER);
            realStartTraining();
        }

        @Override
        public void onNext(ModelApiResult<TrainingContent> apiResult) {
            if(apiResult.getCode()==0){
                TrainingContent trainingContent = apiResult.getData();
                listShowContent.add(trainingContent.toShowContent());
            }else{
                showMsg(context.getString(R.string.get_article_fail));
                trainPres.setTrainingContentType(EnumContentType.LETTER);
            }

            realStartTraining();
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    private final class CallBackGetNews extends Subscriber<ModelApiResult<List<News>>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showMsg(context.getString(R.string.get_news_fail));
            trainPres.setTrainingContentType(EnumContentType.LETTER);
            realStartTraining();
        }

        @Override
        public void onNext(ModelApiResult<List<News>> apiResult) {
            if(apiResult.getCode()==0){
                List<News> newsList = apiResult.getData();
                String finalContent = "";
                for(News news:newsList){
                    news.save();
                    listShowContent.add(news.toShowContent());
                }
            }else{
                showMsg(context.getString(R.string.get_news_fail));
                trainPres.setTrainingContentType(EnumContentType.LETTER);
            }

            realStartTraining();
        }
    }

    public void realStartTraining() {
        //???????????????????????????????????????
        if (trainPres.getTrainingContentType()== EnumContentType.LETTER || trainPres.getTrainingContentType()== EnumContentType.NUMBER) {
            listShowContent = ContentManager.createShowContentList(trainPres.getTrainingContentType(), ReversalConfig.MaxCacheLetterCount);
        }

        if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE){
            viewLoopType.setVisibility(View.VISIBLE);
        }else{
            viewLoopType.setVisibility(View.GONE);
        }
        contentLoopMode = EnumContentLoopMode.SEQUENCE;
        viewLoopType.setSelectContent(contentLoopMode.getName());

        if(ReversalTrainPres.isCompletedEqual(previousTrainPres,trainPres)) {
            //???????????????????????????????????????????????????
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {

            }else{
                lineView.resetIndex();
            }
        }else{
            lineView.resetIndex();
        }

        zhengLevel = trainPres.getPositiveLetterSize();
        fuLevel = trainPres.getNegativeLetterSize();
        if(trainPres.getEyeType()==EnumEyeType.RIGHT){
            zhengLevel = trainPres.getPositiveLetterSizeRight();
            fuLevel = trainPres.getNegativeLetterSizeRight();
        }

        startFindRightGlassTimer();
        startGlassLookTimer();

        trainStatus = TrainStatus.TRAINING;

        previousTrainPres = trainPres;

        firstSetContent();
        initTraining(false);
        changeToTrain();
    }

    /**
     * ?????????????????????
     */
    public void firstSetContent(){
        //????????????????????????????????????
        ShowContent showContent = listShowContent.get(nowShowContentPos);
        lineView.setShowContentType(showContent.getShowContentType());
        lineView.setSrcContent(showContent.getContent());
        lineView.invalidate();
    }

    /**
     * ?????????????????????
     */
    public void changeToTrain(){
        trainStatus = TrainStatus.TRAINING;
        tflistener.changeToStatus(trainStatus);
    }

    /**
     * ????????????????????????
     */
    public void initTraining(boolean isContentNext) {
        startGlassLookTimer();

        glassUseSecond = 0;

        Calendar now = Calendar.getInstance();
        startDate = now.getTime();
        Timber.d("initTraining:startDate:%s",startDate);

        computeTextSize();
        Timber.d("initTraining:glass:%s:level:%s", glassType.getName(), level);

        String glassLevelTip = "??????"+String.valueOf(glassLevel)+context.getString(R.string.level);
        tflistener.setItemTip(glassType.getName() + String.valueOf(level) + context.getString(R.string.level));
//        tflistener.setItemTip(glassType.getName()+String.valueOf(level)+"???");

        computeTextSize();
        refreshContent(isContentNext, level);
    }

    /**
     * ????????????????????????
     * @param isContentNext
     * @param level
     */
    public void refreshContent(boolean isContentNext,int level){
        float trainingTextSize = Util.arrayVisionCardLevels[level].getTextSize();
        lineView.setMmSize(trainingTextSize);

        if(isContentNext){
            if(lineView.isHasNextRow()){
                lineView.nextRow();
                lineView.invalidate();
            }else {
                if (contentLoopMode == EnumContentLoopMode.SINGLE) {

                } else if (contentLoopMode == EnumContentLoopMode.SEQUENCE) {
                    if (nowShowContentPos < listShowContent.size() - 1) {
                        nowShowContentPos++;
                    } else {
                        nowShowContentPos = 0;
                    }
                } else if (contentLoopMode == EnumContentLoopMode.SHUFFLE) {
                    Random random = new Random();
                    int nextPos = random.nextInt(listShowContent.size());
                    while (nowShowContentPos == nextPos) {
                        nextPos = random.nextInt(listShowContent.size());
                    }
                    nowShowContentPos = nextPos;
                }

                ShowContent showContent = listShowContent.get(nowShowContentPos);
                lineView.resetIndex();
                lineView.setShowContentType(showContent.getShowContentType());
                lineView.setSrcContent(showContent.getContent());
                lineView.invalidate();
            }
        }
        lineView.invalidate();
    }

    /**
     * ????????????????????????
     */
    public void computeTextSize() {
        if (glassType == EnumGlassType.Zheng) {
            level = zhengLevel;
        } else {
            level = fuLevel;
        }
        trainingTextSize = Util.arrayVisionCardLevels[level].getTextSize();
    }

    /**
     * ??????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????2???????????????3
     * @return
     */
    public int computeGlassResult() {
        int glassResult = 3;
        long timeLong = endDate.getTime() - startDate.getTime();
        //long timeLong = glassUseSecond;?????????????????????????????????????????????????????????????????????glassUseSecond??????
        if (timeLong <= ReversalConfig.SmallOverTime) {
            glassResult = 0;
        } else if (timeLong <= ReversalConfig.NoChangeOverTime) {
            glassResult = 1;
        } else if (timeLong <= ReversalConfig.BigOverTime) {
            glassResult = 2;
        } else {
            glassResult = 3;
        }

        return glassResult;
    }

    /**
     * ????????????
     */
    public void changeGlass(){
        if(trainStatus == TrainStatus.TRAINING) {
            manualChange();

//            Observable.timer(ReversalConfig.OverlayShowLetterTime, TimeUnit.MILLISECONDS)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(aLong -> {
//                        initTraining(true);
//                    });

            deviceAdjust();
        }else{
            showMsg(context.getString(R.string.not_ready));
        }
    }

// ?????????????????????????????????
//    public void getGlassSign(EnumGlassType newglassType){
//        if(trainStatus == TrainStatus.TRAINING) {
//            if (newglassType != glassType) {
//                manualChange();
//                glassType = newglassType;
//
//                deviceAdjust();
//            }
//        }else{
//            showMsg("??????????????????");
//        }
//    }

    /**
     * ????????????
     */
    private void manualChange(){
        enterNextGlass();
    }

    /**
     * ????????????
     */
    public void enterNextGlass() {
        Calendar now = Calendar.getInstance();
        endDate = now.getTime();

        int glassResult = computeGlassResult();
        finishOneGlass(glassResult);
    }

    /**
     * ????????????????????????
     */
    private void handleLoc(){
        leftFuLoc = TurntableLocation.getFuByDegreeLevel(glassLevel);
        leftZhengLoc = TurntableLocation.getZhengByDegreeLevel(glassLevel);

        rightFuLoc = TurntableLocation.getFuByDegreeLevel(glassLevel);
        rightZhengLoc = TurntableLocation.getZhengByDegreeLevel(glassLevel);
    }

    /**
     * ????????????
     */
    public void deviceAdjust(){
        handleLoc();

        trainStatus = TrainStatus.WAITING;
        int leftLoc = -1;
        int rightLoc = -1;
        if(glassType==EnumGlassType.Zheng){
            if(trainPres.getEyeType()==EnumEyeType.LEFT){
                leftLoc = leftZhengLoc.getValue();
            }else if(trainPres.getEyeType()==EnumEyeType.RIGHT){
                rightLoc = rightZhengLoc.getValue();
            }else if(trainPres.getEyeType()==EnumEyeType.DOUBLE){
                leftLoc = leftZhengLoc.getValue();
                rightLoc = rightZhengLoc.getValue();
            }
        }else{
            if(trainPres.getEyeType()==EnumEyeType.LEFT){
                leftLoc = leftFuLoc.getValue();
            }else if(trainPres.getEyeType()==EnumEyeType.RIGHT){
                rightLoc = rightFuLoc.getValue();
            }else if(trainPres.getEyeType()==EnumEyeType.DOUBLE){
                leftLoc = leftFuLoc.getValue();
                rightLoc = rightFuLoc.getValue();
            }
        }

        //ttsEngine.startSpeaking(R.raw.wait_adjust_device);
        ControlMessageSet messageSet = TrainUseCase.createReversalPrepare(leftLoc,rightLoc);
        //messageSet.setHandler(handlerDeviceAdjustFinish);
        //MotorBus.getInstance().sendMessageSet(messageSet);

        trainStatus = TrainStatus.TRAINING;

        //??????????????????????????????????????????
        lineView.setVisibility(View.GONE);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Timber.d("device adjust finish");
                    //??????????????????????????????????????????????????????
                    lineView.setVisibility(View.VISIBLE);
                    initTraining(true);
                });

//        Calendar now = Calendar.getInstance();
//        turnStartDate = now.getTime();
    }

    //?????????????????????
    private Observable<Long> observableGlassLook = null;
    private Subscriber<Long> subscriberGlassLook = null;
    private Subscription subscriptionGlassLook = null;

    private void startGlassLookTimer(){
        stopGlassLookTimer();

        observableGlassLook = Observable.timer(ReversalConfig.glassMaxSecond, TimeUnit.SECONDS);
        subscriberGlassLook = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                glassLookOver();
            }
        };

        subscriptionGlassLook = observableGlassLook.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberGlassLook);
    }
    private void stopGlassLookTimer(){
        Timber.d("stopLockTimmer");
        if(subscriptionGlassLook!=null && !subscriptionGlassLook.isUnsubscribed()){
            subscriptionGlassLook.unsubscribe();
        }
        if(subscriberGlassLook!=null && !subscriberGlassLook.isUnsubscribed()){
            subscriberGlassLook.unsubscribe();
        }
        subscriptionGlassLook = null;
        subscriberGlassLook = null;
        observableGlassLook = null;
    }

    /**
     * ???????????????
     */
    public void glassLookOver(){
        finishOneGlass(3);
        glassUseSecond = 0;

        initTraining(false);
    }


    //?????????????????????
    private Observable<Long> observableFindRightGlass = null;
    private Subscriber<Long> subscriberFindRightGlass = null;
    private Subscription subscriptionFindRightGlass = null;

    private void startFindRightGlassTimer(){
        stopFindRightGlassTimer();

        observableFindRightGlass = Observable.interval(TrainConfig.UnitOne, TimeUnit.SECONDS);
        subscriberFindRightGlass = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                useSecondFindRightGlass++;
                Timber.d("windern:?????????????????????useSecondFindRightGlass???%s",useSecondFindRightGlass);
                if(useSecondFindRightGlass>=ReversalConfig.FindRightGlassMaxSecond){
                    findRightGlassTimeOut();
                }
            }
        };

        subscriptionFindRightGlass = observableFindRightGlass.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberFindRightGlass);
    }
    private void stopFindRightGlassTimer(){
        Timber.d("windern:stopFindRightGlassTimer");
        if(subscriptionFindRightGlass!=null && !subscriptionFindRightGlass.isUnsubscribed()){
            subscriptionFindRightGlass.unsubscribe();
        }
        if(subscriberFindRightGlass!=null && !subscriberFindRightGlass.isUnsubscribed()){
            subscriberFindRightGlass.unsubscribe();
        }
        subscriptionFindRightGlass = null;
        subscriberFindRightGlass = null;
        observableFindRightGlass = null;
    }

    public void findRightGlassTimeOut(){
        finishTrain();
    }
}
