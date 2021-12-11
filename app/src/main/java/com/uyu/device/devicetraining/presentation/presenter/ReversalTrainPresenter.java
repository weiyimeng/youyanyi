package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.uyu.device.devicetraining.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.uyu.device.devicetraining.data.entity.EnumUserTrainMode;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.TrainBackApiResult;
import com.uyu.device.devicetraining.data.entity.config.ReversalConfig;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.entity.content.ContentManager;
import com.uyu.device.devicetraining.data.entity.content.News;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent_Table;
import com.uyu.device.devicetraining.data.entity.content.UserUploadIds;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.ReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.ReversalTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.RedGreenReadTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumGlassType;
import com.uyu.device.devicetraining.data.entity.type.EnumPresAdjust;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.TurntableLocation;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnDrawFinish;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.util.MyConfig;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.util.Ulog;
import com.uyu.device.devicetraining.presentation.util.Util;
import com.uyu.device.devicetraining.presentation.view.widget.LineView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
public class ReversalTrainPresenter extends TrainPresenter<ReversalTrainPres,ReversalTrainBack,ReversalTrainNormal> implements OnDrawFinish {
    /**
     * 构造函数
     */

    @Inject
    public ReversalTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase, contentUseCase, context, ttsEngine);
    }

    /**
     * 每个项目单独默认的listener
     */

    public interface ReversalListener{

    }

    private ReversalListener itemListener;

    public void setItemListener(ReversalListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * 每个项目都要独立实现的
     */

    @Override
    public void initStartStatus() {
        useSecond = 0;
        glassUseSecond = 0;
        //重置到正片、电机到正片
        glassType=EnumGlassType.Zheng;

        super.initStartStatus();
    }

    @Override
    public void preparePresBack() {
        super.preparePresBack();

        //换个位置，立即开始后立即技术，会报错，api接口提示没有eye_type
        //放在startTrain的位置必须要等设备调整完成时才会赋值
        trainBack.setEyeType(trainPres.getEyeType());

        trainBack.setLPositiveDegreeLevel(trainPres.getLPositiveDegreeLevel());
        trainBack.setLNegativeDegreeLevel(trainPres.getLNegativeDegreeLevel());
        trainBack.setRPositiveDegreeLevel(trainPres.getRPositiveDegreeLevel());
        trainBack.setRNegativeDegreeLevel(trainPres.getRNegativeDegreeLevel());
    }

    @Override
    protected void newTrainBack() {
        trainBack = new ReversalTrainBack();
        trainNomal = new ReversalTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        handleLoc();

        if(getUserTrainMode()== EnumUserTrainMode.CONTROL) {
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                //和上一个处方完全一样，不用重新取内容了
                if (ReversalTrainPres.isCompletedEqual(previousTrainPres, trainPres)) {
                    realStartTraining();
                } else {
                    //先取文章
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
                //查找本地文章的ids
                for(int i=0;i<userUploadIdsList.size();i++){
                    if(userUploadIdsList.get(i).type==0){
                        userUploadIds = userUploadIdsList.get(i);
                    }
                }
                //如果用户没有选择本地文章
                if(userUploadIds!=null && userUploadIds.ids!=null && userUploadIds.ids.size()>0){
                    //选中文章之前内容
                    String befor = "";
                    String after = "";
                    boolean isFindId = false;
                    for(int j=0;j<userUploadIds.ids.size();j++){
                        int id = userUploadIds.ids.get(j);
                        TrainingContent trainingContent = SQLite.select().from(TrainingContent.class).where(TrainingContent_Table.id.eq(id)).querySingle();
                        if(trainingContent!=null){
                            if(id==trainPres.getTrainingContentArticleId()){
                                isFindId = true;
                            }
                            if(isFindId){
                                after += trainingContent.getContent();
                            }else{
                                befor += trainingContent.getContent();
                            }
                        }
                    }
                    String finalContent = after+befor;
                    content = ContentManager.removeEmptyChar(finalContent);

                    realStartTraining();
                }else{
                    trainPres.setTrainingContentType(EnumContentType.LETTER);
                    realStartTraining();
                }
            } else if(trainPres.getTrainingContentType() == EnumContentType.NEWS) {
                //先取新闻
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
        trainBack.setPrescriptionAdjustStatus(EnumPresAdjust.UNCHANGE);
        trainBack.setPositiveLetterSize(zhengLevel);
        trainBack.setNegativeLetterSize(fuLevel);

        super.prepareFinishData();
    }

    @Override
    public void finishTrain() {
        stopTimer();

        super.finishTrain();

        //完成某个训练后，清空所有指令
        //主要是针对翻转拍的，不断的按键，如果视光师立即结束，后面的转盘转动就不执行了
        MotorBus.getInstance().removeAllMessage();
    }

    @Override
    public void receiveFinishServerMessage(){
        stopTimer();

        super.receiveFinishServerMessage();

        //完成某个训练后，清空所有指令
        //主要是针对翻转拍的，不断的按键，如果视光师立即结束，后面的转盘转动就不执行了
        MotorBus.getInstance().removeAllMessage();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createReversal(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createReversal(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack(isUpdateReception));
    }

    @Override
    public void pauseTrain() {
        super.pauseTrain();

        stopTimer();
    }

    @Override
    public void resumeTrain() {
        super.resumeTrain();

        //恢复训练的时候重新开始计时，否则会出现暂停后，点击按键没有反应的情况
        glassUseSecond = 0;
        Calendar now = Calendar.getInstance();
        startDate = now.getTime();

        startTimer();
    }

    /**
     * 每个项目额外可覆盖的父类的方法
     */

    @Override
    public void pressEnter() {
        Timber.d("pressEnter");
        //按键去抖
        Calendar now = Calendar.getInstance();
        long nowTime = now.getTime().getTime();
        if(nowTime-pressTime > TrainConfig.ButtonPressShakeTime){
            pressTime = nowTime;
            changeGlass();
        }else{
            showMsg(context.getString(R.string.plese_not_frequently_press_key));
        }
    }

    //自动版下面两个暂时就不用了，给手动版预留的

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
     * 单独的实现的接口
     */

    @Override
    public void onDrawFinish() {
        sendNowInfo();
    }

    /**
     * 单独的部分
     */

    /**
     * button按下时间
     */
    public long pressTime = 0;

    //正负片转盘的位置
    private TurntableLocation leftZhengLoc = TurntableLocation.ZHENG150;
    private TurntableLocation leftFuLoc = TurntableLocation.FU150;
    private TurntableLocation rightZhengLoc = TurntableLocation.ZHENG150;
    private TurntableLocation rightFuLoc = TurntableLocation.FU150;

    private ReversalTrainPres previousTrainPres;

    /**
     * 显示训练的控件
     */
    private LineView lineView;

    public void setLineView(LineView lineView) {
        this.lineView = lineView;
        this.lineView.setDrawFinishListener(this);
    }

    private String content = "";

    /**
     * 当前眼睛
     */
    public EnumEyeType eyeType = EnumEyeType.LEFT;
    /**
     * 当前镜片
     */
    public EnumGlassType glassType = EnumGlassType.Zheng;

    /**
     * 训练字母大小
     */
    private float trainingTextSize = 45;
    /**
     * 训练等级
     */
    private int level = 0;
    /**
     * 正面训练等级
     */
    private int zhengLevel = 10;
    /**
     * 负面训练等级
     */
    private int fuLevel = 10;
    /**
     * 已看时间s
     */
    public int useSecond = 0;
    /**
     * 镜片监听10s的变量
     */
    public int glassUseSecond = 0;

    /**
     * 镜面一次训练开始时间
     */
    private Date startDate;
    /**
     * 镜面一次训练结束时间
     */
    private Date endDate;

    /**
     * 时间界面处理器
     */
    private Handler handlerChangeTime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int leftTime = ReversalConfig.oneTrainingSecond - useSecond;
            Timber.d("剩余时间: %s 秒",leftTime);
        }
    };

    private Observable<Long> observableTimer = null;
    private Subscriber<Long> subscriberTimer = null;
    private Subscription subscriptionTimer = null;

    private void startTimer(){
        Timber.d("startTimer");
        observableTimer = Observable.interval(ReversalConfig.TimeRecordIntervalSecond, TimeUnit.SECONDS);
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
                Timber.d("训练时间: %s 秒",useSecond);

                //训练时间到
                if (useSecond >= ReversalConfig.oneTrainingSecond) {
                    finishTrain();
                } else {
                    handlerChangeTime.sendEmptyMessage(0);

                    //镜片超时
                    if (glassUseSecond == ReversalConfig.glassMaxSecond) {
                        finishOneGlass(3);

                        initTraining(false);
                        glassUseSecond = 0;
                    }
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
     * 结束训练单个镜面
     *
     * @param glassResult 结果0加难度，1不变，2减难度，3档次减难度
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
            // 如果是点击看过的，通过数加1
            trainBack.detailCountAdd(glassType, level);

            // 1变0，0变1
            int glassTypeValue = 1 - glassType.getValue();
            glassType = EnumGlassType.values()[glassTypeValue];
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

        //先不初始化，设备完成后更换内容
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
                content = ContentManager.removeEmptyChar(trainingContent.getContent());
            }else{
                showMsg(context.getString(R.string.get_article_fail));
                trainPres.setTrainingContentType(EnumContentType.LETTER);
            }

            realStartTraining();
        }
    }

    /**
     * 真正开始的地方，获取文章信息后
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
                    finalContent = finalContent + news.newsContent;
                }
                content = ContentManager.removeEmptyChar(finalContent);
            }else{
                showMsg(context.getString(R.string.get_news_fail));
                trainPres.setTrainingContentType(EnumContentType.LETTER);
            }

            realStartTraining();
        }
    }

    public void realStartTraining() {
        if(ReversalTrainPres.isCompletedEqual(previousTrainPres,trainPres)) {
            //如果处方完全相同，则不需要刷新内容
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

        startTimer();
        trainStatus = TrainStatus.TRAINING;

        previousTrainPres = trainPres;

        firstSetContent();
        initTraining(false);
        changeToTrain();
    }

    /**
     * 第一次设置内容
     */
    public void firstSetContent(){
        if (trainPres.getTrainingContentType()== EnumContentType.LETTER || trainPres.getTrainingContentType()== EnumContentType.NUMBER) {
            int letterCount = 3;

            //随机生成字母
            String randomLetters = Util.getOneRandomText(letterCount);
            if(trainPres.getTrainingContentType()== EnumContentType.NUMBER){
                randomLetters = Util.getOneRandomNumber(letterCount);
            }
            // 如果是训练字母，每次都需要变回去
            while(content.equals(randomLetters)){
                randomLetters = Util.getOneRandomText(letterCount);
                if(trainPres.getTrainingContentType()== EnumContentType.NUMBER){
                    randomLetters = Util.getOneRandomNumber(letterCount);
                }
            }
            content = randomLetters;
        }
        lineView.setSrcContent(content);
    }

    /**
     * 切换到训练界面
     */
    public void changeToTrain(){
        trainStatus = TrainStatus.TRAINING;
        tflistener.changeToStatus(trainStatus);
    }

    /**
     * 重新刷新显示视标
     */
    public void initTraining(boolean isContentNext) {
        glassUseSecond = 0;

        Calendar now = Calendar.getInstance();
        startDate = now.getTime();
        Timber.d("initTraining:startDate:%s",startDate);

        computeTextSize();
        Timber.d("initTraining:glass:%s:level:%s", glassType.getName(), level);
        tflistener.setItemTip(glassType.getName()+String.valueOf(level)+context.getString(R.string.level));

        ReversalTrainPres trainPres = (ReversalTrainPres)presTrainMessageContent.getTp();
        //判断是否是字母
        if (trainPres.getTrainingContentType()== EnumContentType.LETTER || trainPres.getTrainingContentType()== EnumContentType.NUMBER) {
            //下一行重新变换内容
            if(isContentNext) {
                int letterCount = 3;

                if (trainingTextSize > 25) {
                    letterCount = 1;
                }

                //随机生成字母
                String randomLetters = Util.getOneRandomText(letterCount);
                if (trainPres.getTrainingContentType() == EnumContentType.NUMBER) {
                    randomLetters = Util.getOneRandomNumber(letterCount);
                }
                // 如果是训练字母，每次都需要变回去
                while (content.equals(randomLetters)) {
                    randomLetters = Util.getOneRandomText(letterCount);
                    if (trainPres.getTrainingContentType() == EnumContentType.NUMBER) {
                        randomLetters = Util.getOneRandomNumber(letterCount);
                    }
                }
                content = randomLetters;
                lineView.setSrcContent(content);
            }
            refreshContent(false, level);
        }else{
            refreshContent(isContentNext, level);
        }
    }

    /**
     * 刷新内容显示显示
     * @param isContentNext
     * @param level
     */
    public void refreshContent(boolean isContentNext,int level){
        float trainingTextSize = Util.arrayVisionCardLevels[level].getTextSize();
        lineView.setMmSize(trainingTextSize);

        if(isContentNext){
            lineView.nextRow();
        }
    }

    /**
     * 计算当前显示视标
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
     * 计算当前镜面训练结果
     * 如果用到计算的，肯定要变换镜片，所有超时的而直接用2，不能出现3
     * @return
     */
    public int computeGlassResult() {
        int glassResult = 3;
        long timeLong = endDate.getTime() - startDate.getTime();
        //long timeLong = glassUseSecond;不能用这个因为，有可能点击的拿一下不是整时间，glassUseSecond不满
        if (eyeType == EnumEyeType.DOUBLE) {
            if (timeLong <= ReversalConfig.SmallOverTimeDouble) {
                glassResult = 0;
            } else if (timeLong <= ReversalConfig.NoChangeOverTimeDouble) {
                glassResult = 1;
            } else if (timeLong <= ReversalConfig.BigOverTimeDouble) {
                glassResult = 2;
            } else {
                glassResult = 2;
            }
        } else {
            if (timeLong <= ReversalConfig.SmallOverTime) {
                glassResult = 0;
            } else if (timeLong <= ReversalConfig.NoChangeOverTime) {
                glassResult = 1;
            } else if (timeLong <= ReversalConfig.BigOverTime) {
                glassResult = 2;
            } else {
                glassResult = 2;
            }
        }

        return glassResult;
    }

    /**
     * 镜片切换
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
            showMsg("还未准备好");
        }
    }

// 手动版的，暂时先不用了
//    public void getGlassSign(EnumGlassType newglassType){
//        if(trainStatus == TrainStatus.TRAINING) {
//            if (newglassType != glassType) {
//                manualChange();
//                glassType = newglassType;
//
//                deviceAdjust();
//            }
//        }else{
//            showMsg("还未开始训练");
//        }
//    }

    /**
     * 手动切换
     */
    private void manualChange(){
        enterNextGlass();
    }

    /**
     * 到下一个
     */
    public void enterNextGlass() {
        Calendar now = Calendar.getInstance();
        endDate = now.getTime();

        int glassResult = computeGlassResult();
        finishOneGlass(glassResult);
    }

    /**
     * 处理获取转盘位置
     */
    private void handleLoc(){
        leftFuLoc = TurntableLocation.getFuByDegreeLevel(trainPres.getLNegativeDegreeLevel());
        leftZhengLoc = TurntableLocation.getZhengByDegreeLevel(trainPres.getLPositiveDegreeLevel());

        rightFuLoc = TurntableLocation.getFuByDegreeLevel(trainPres.getRNegativeDegreeLevel());
        rightZhengLoc = TurntableLocation.getZhengByDegreeLevel(trainPres.getRPositiveDegreeLevel());
    }

    /**
     * 设备调整
     */
    public void deviceAdjust(){
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

        //转盘转动的时候隐藏，显示白的
        lineView.setVisibility(View.GONE);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Timber.d("device adjust finish");
                    //接收到转盘完成后刷新文字，并显示出来
                    lineView.setVisibility(View.VISIBLE);
                    initTraining(true);
                });

//        Calendar now = Calendar.getInstance();
//        turnStartDate = now.getTime();
    }

//    private Date turnStartDate;
//    private Date turnEndDate;

    /**
     * 设备调整完成
     */
    private Handler handlerDeviceAdjustFinish = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            Calendar now = Calendar.getInstance();
//            turnEndDate = now.getTime();
//            Timber.d("turn total time:%s=%s-%s",turnEndDate.getTime()-turnStartDate.getTime(),turnEndDate.getTime(),turnStartDate.getTime());
//            ttsEngine.startSpeaking(R.raw.adjust_device_finish,handlerDeviceAdjustVoiceOver);
        }
    };

//    /**
//     * 设备调整语音完成
//     */
//    private Handler handlerDeviceAdjustVoiceOver = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            trainStatus = TrainStatus.TRAINING;
//
//            initTraining();
//        }
//    };
}
