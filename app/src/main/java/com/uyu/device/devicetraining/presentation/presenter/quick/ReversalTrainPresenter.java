package com.uyu.device.devicetraining.presentation.presenter.quick;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.EnumUserTrainMode;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.config.ReversalTrainConfiguration;
import com.uyu.device.devicetraining.data.entity.config.ReversalConfig;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.entity.content.ContentManager;
import com.uyu.device.devicetraining.data.entity.content.News;
import com.uyu.device.devicetraining.data.entity.content.NewsCategory;
import com.uyu.device.devicetraining.data.entity.content.ShowContent;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent_Table;
import com.uyu.device.devicetraining.data.entity.content.UserUploadIds;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.selfhelp.PushTrainContentContent;
import com.uyu.device.devicetraining.data.entity.trainback.ReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.ReversalTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumContentLoopMode;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumGlassType;
import com.uyu.device.devicetraining.data.entity.type.EnumPresAdjust;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.TurntableLocation;
import com.uyu.device.devicetraining.data.entity.type.EnumShowContentType;
import com.uyu.device.devicetraining.data.net.api.ApiService;
import com.uyu.device.devicetraining.data.net.api.ServiceFactory;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnDrawFinish;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.presenter.TrainPresenter;
import com.uyu.device.devicetraining.presentation.util.MyConfig;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.util.Util;
import com.uyu.device.devicetraining.presentation.view.widget.LineView;
import com.uyu.device.devicetraining.presentation.view.widget.SelectDropView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
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
public class ReversalTrainPresenter extends TrainPresenter<ReversalTrainPres, ReversalTrainBack, ReversalTrainNormal> implements OnDrawFinish, SelectDropView.SelectDropViewListener {
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

    public interface ReversalListener {

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
        listShowContent = new ArrayList<>();
        nowShowContentPos = 0;
        //训练完左眼，右眼从负片开始的问题，需要重置
        glassType = EnumGlassType.Zheng;

        super.initStartStatus();
    }

    @Override
    protected void newTrainBack() {
        trainBack = new ReversalTrainBack();
        trainNomal = new ReversalTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        trainBack.setEyeType(trainPres.getEyeType());

        trainBack.setLPositiveDegreeLevel(trainPres.getLPositiveDegreeLevel());
        trainBack.setLNegativeDegreeLevel(trainPres.getLNegativeDegreeLevel());
        trainBack.setRPositiveDegreeLevel(trainPres.getRPositiveDegreeLevel());
        trainBack.setRNegativeDegreeLevel(trainPres.getRNegativeDegreeLevel());

        Observable<List<ShowContent>> observableGetContent = getListShowContent(true);

        observableGetContent.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listShowContent -> {
                    if (listShowContent.size() > 0) {
                        this.listShowContent = listShowContent;
                    }
                    realStartTraining();
                });
    }

    @Override
    public void sendNowInfo() {
        trainNomal.setTrainContent(lineView.getNowShowContent());

        super.sendNowInfo();
    }

    @Override
    public void finishTrainViewStatus() {
//        stopTimer();
    }

    @Override
    public void prepareFinishData() {
        trainBack.setLPositiveDegreeLevel(glassLevelLeft);
        trainBack.setLNegativeDegreeLevel(glassLevelLeft);
        trainBack.setRPositiveDegreeLevel(glassLevelRight);
        trainBack.setRNegativeDegreeLevel(glassLevelRight);

        trainBack.setPrescriptionAdjustStatus(EnumPresAdjust.UNCHANGE);
        trainBack.setPositiveLetterSize(zhengLevel);
        trainBack.setNegativeLetterSize(fuLevel);

        trainBack.computeScore(configuration.oneTrainingSecond);

        super.prepareFinishData();
    }

    @Override
    public void finishTrain() {
        stopTimer();
        stopFindRightGlassTimer();
        stopGlassLookTimer();

        super.finishTrain();
    }

    @Override
    public void receiveFinishServerMessage() {
        stopTimer();
        stopFindRightGlassTimer();
        stopGlassLookTimer();

        super.receiveFinishServerMessage();
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
        stopFindRightGlassTimer();
        stopGlassLookTimer();
    }

    @Override
    public void resumeTrain() {
        super.resumeTrain();

        //恢复训练的时候重新开始计时，否则会出现暂停后，点击按键没有反应的情况
        glassUseSecond = 0;
        Calendar now = Calendar.getInstance();
        startDate = now.getTime();

        if (isOnFindGlassLevel) {
            startFindRightGlassTimer();
        } else {
            startTimer();
        }
        startGlassLookTimer();
    }

    /**
     * 每个项目额外可覆盖的父类的方法
     */

    @Override
    public void pressEnter() {
//        Timber.d("pressEnter");
//        //按键去抖
//        Calendar now = Calendar.getInstance();
//        long nowTime = now.getTime().getTime();
//        if(nowTime-pressTime > TrainConfig.ButtonPressShakeTime){
//            pressTime = nowTime;


        changeGlass();
//        }else{
//            showMsg("按键请不要太频繁");
//        }
    }

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

    @Override
    public void select(int pos) {
        contentLoopMode = EnumContentLoopMode.values()[pos];
        //循环模式变换，设置当前处方的循环模式
        trainPres.setTrainingContentLoopMode(contentLoopMode);
        SharePreferenceTool.setLastReversalTrainPres(context, trainPres);
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
    /**
     * 切换内容循环类型的控件
     */
    private SelectDropView viewLoopType;

    public void setLineView(LineView lineView) {
        this.lineView = lineView;
        this.lineView.setDrawFinishListener(this);
    }

    public void setViewLoopType(SelectDropView viewLoopType) {
        this.viewLoopType = viewLoopType;
        this.viewLoopType.setViewListener(this);

        this.viewLoopType.setSelectTip(context.getString(R.string.content_loop_mode));
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < EnumContentLoopMode.values().length; i++) {
            datas.add(EnumContentLoopMode.values()[i].getName());
        }
        this.viewLoopType.setDataList(datas);
    }

    /**
     * 内容循环方式
     */
    private EnumContentLoopMode contentLoopMode = EnumContentLoopMode.SEQUENCE;
    /**
     * 训练内容
     */
    private List<ShowContent> listShowContent = new ArrayList<>();
    /**
     * 当前训练内容的位置
     */
    private int nowShowContentPos = 0;

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
     * 镜片等级
     */
    private int glassLevel = 0;
    /**
     * 镜片等级-左眼
     */
    private int glassLevelLeft = 0;
    /**
     * 镜片等级-右眼
     */
    private int glassLevelRight = 0;
    /**
     * 是否处于找镜片等级
     * false不是，表示正式开始训练了
     */
    private boolean isOnFindGlassLevel = true;
    /**
     * 镜片变化的次数
     */
    private int glassChangeCount = 0;
    private ReversalTrainConfiguration configuration;

    private Observable<Long> observableTimer = null;
    private Subscriber<Long> subscriberTimer = null;
    private Subscription subscriptionTimer = null;

    private void startTimer() {
        stopTimer();

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
                Timber.d("训练时间: %s 秒", useSecond);

                //训练时间到
                if (useSecond >= configuration.oneTrainingSecond) {
                    finishTrain();
                }
            }
        };

        subscriptionTimer = observableTimer.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberTimer);
    }

    private void stopTimer() {
        Timber.d("stopTimmer");
        if (subscriptionTimer != null && !subscriptionTimer.isUnsubscribed()) {
            subscriptionTimer.unsubscribe();
        }
        if (subscriberTimer != null && !subscriberTimer.isUnsubscribed()) {
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
            //正式训练的时候增加
            if (!isOnFindGlassLevel) {
                // 如果是点击看过的，通过数加1
                trainBack.detailCountAdd(glassType, level);
            }

            // 如果是点击看过的，通过数加1
            trainBack.detailCountAdd(glassType, level);

            // 1变0，0变1
            int glassTypeValue = 1 - glassType.getValue();
            glassType = EnumGlassType.values()[glassTypeValue];

            if (isOnFindGlassLevel) {
                glassChangeCount++;
                int levelTryCompareCount = ReversalConfig.arrayLevelTryCount[glassLevel];
                if (glassChangeCount == levelTryCompareCount * 2) {
                    boolean isNeedStartRealTrain = false;
                    if (zhengLevel >= ReversalConfig.DefaultLetterSize && fuLevel >= ReversalConfig.DefaultLetterSize) {
                        if (glassLevel >= ReversalConfig.MaxGlassLevel - 1) {
                            //通过最后一级了，直接开始训练
                            isNeedStartRealTrain = true;
                        } else {
                            if (glassLevelLeft < ReversalConfig.MaxGlassLevel) {
                                glassLevelLeft++;
                            }
                            if (glassLevelRight < ReversalConfig.MaxGlassLevel) {
                                glassLevelRight++;
                            }
                            glassLevel++;
                            //每变一级就重新计算镜片变化的个数
                            glassChangeCount = 0;
                            //镜片下一等级以后，字体大小重新从13级开始，否则会出现到15级再往下降的情况
                            zhengLevel = 13;
                            fuLevel = 13;
                            Timber.d("镜片等级+1,为" + String.valueOf(glassLevel) + "级");
                        }
                    } else {
                        isNeedStartRealTrain = true;
                    }

                    if (isNeedStartRealTrain) {
                        Timber.d("正式开始训练");
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

//        initTraining();
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
            if (apiResult.getCode() == 0) {
                TrainingContent trainingContent = apiResult.getData();
                listShowContent.add(trainingContent.toShowContent());
            } else {
                showMsg(context.getString(R.string.get_article_fail));
                trainPres.setTrainingContentType(EnumContentType.LETTER);
            }

            realStartTraining();
        }
    }

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
            if (apiResult.getCode() == 0) {
                List<News> newsList = apiResult.getData();
                String finalContent = "";
                for (News news : newsList) {
                    news.save();
                    listShowContent.add(news.toShowContent());
                }
            } else {
                showMsg(context.getString(R.string.get_news_fail));
                trainPres.setTrainingContentType(EnumContentType.LETTER);
            }

            realStartTraining();
        }
    }

    public void realStartTraining() {
        if (ReversalTrainPres.isContentEqual(previousTrainPres, trainPres)) {
            //如果内容相同，则不需要刷新内容
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                contentLoopMode = trainPres.getTrainingContentLoopMode();
            } else {
                lineView.resetIndex();
            }
        } else {
            lineView.resetIndex();
        }

        if (getUserTrainMode() == EnumUserTrainMode.CONTROL) {
            //自助的不能切换循环模式
            viewLoopType.setVisibility(View.GONE);
            contentLoopMode = EnumContentLoopMode.SEQUENCE;
        } else {
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                viewLoopType.setVisibility(View.VISIBLE);
            } else {
                viewLoopType.setVisibility(View.GONE);
            }
            contentLoopMode = EnumContentLoopMode.SEQUENCE;
        }

        //循环模式变换，设置当前处方的循环模式
        trainPres.setTrainingContentLoopMode(contentLoopMode);
        SharePreferenceTool.setLastReversalTrainPres(context, trainPres);

        viewLoopType.setSelectContent(trainPres.getTrainingContentLoopMode().getName());

        if (getUserTrainMode() == EnumUserTrainMode.CONTROL) {
            glassLevel = trainPres.getLNegativeDegreeLevel();
            glassLevelLeft = trainPres.getLNegativeDegreeLevel();
            glassLevelRight = trainPres.getRNegativeDegreeLevel();

            zhengLevel = trainPres.getPositiveLetterSize();
            fuLevel = trainPres.getNegativeLetterSize();
            if (trainPres.getEyeType() == EnumEyeType.RIGHT) {
                zhengLevel = trainPres.getPositiveLetterSizeRight();
                fuLevel = trainPres.getNegativeLetterSizeRight();
            }

            isOnFindGlassLevel = false;
            glassChangeCount = 0;
            configuration = new ReversalTrainConfiguration.Builder()
                    .setOneTrainingSecond(ReversalConfig.oneTrainingSecondControl)
                    .build();
            //如果是双眼，练双眼的时间
            if (trainPres.getEyeType() == EnumEyeType.DOUBLE) {
                configuration.oneTrainingSecond = ReversalConfig.oneTrainingSecondDoubleControl;
            }
            startTimer();
        } else {
            glassLevel = trainPres.getLNegativeDegreeLevel();
            glassLevelLeft = trainPres.getLNegativeDegreeLevel();
            glassLevelRight = trainPres.getRNegativeDegreeLevel();
            //没有到最后一级往下减1级
            //解决亚平训练的时候优眼扫码报错的问题
            if (glassLevel > 0) {
                glassLevel--;
            }
            if (glassLevelLeft > 0) {
                glassLevelLeft--;
            }
            if (glassLevelRight > 0) {
                glassLevelRight--;
            }
            zhengLevel = 13;
            fuLevel = 13;

            isOnFindGlassLevel = true;
            glassChangeCount = 0;
            configuration = new ReversalTrainConfiguration.Builder()
                    .setOneTrainingSecond(ReversalConfig.oneTrainingSecond)
                    .build();
            //如果是双眼，练双眼的时间
            if (trainPres.getEyeType() == EnumEyeType.DOUBLE) {
                configuration.oneTrainingSecond = ReversalConfig.oneTrainingSecondDouble;
            }
            startFindRightGlassTimer();
        }

        startGlassLookTimer();

        trainStatus = TrainStatus.TRAINING;

        previousTrainPres = trainPres;

        firstSetContent();
        initTraining(false);
        changeToTrain();

        deviceAdjust();
    }

    public void changeLoopModeByTrainPres() {
        if (getUserTrainMode() == EnumUserTrainMode.CONTROL) {
            //自助的不能切换循环模式
            viewLoopType.setVisibility(View.GONE);
            contentLoopMode = EnumContentLoopMode.SEQUENCE;
        } else {
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                viewLoopType.setVisibility(View.VISIBLE);
            } else {
                viewLoopType.setVisibility(View.GONE);
            }
            contentLoopMode = EnumContentLoopMode.SEQUENCE;
        }

        //循环模式变换，设置当前处方的循环模式
        trainPres.setTrainingContentLoopMode(contentLoopMode);
        SharePreferenceTool.setLastReversalTrainPres(context, trainPres);

        viewLoopType.setSelectContent(trainPres.getTrainingContentLoopMode().getName());
    }

    /**
     * 第一次设置内容
     */
    public void firstSetContent() {
        //第一次的时候设置一下内容
        ShowContent showContent = listShowContent.get(nowShowContentPos);
        lineView.setShowContentType(showContent.getShowContentType());
        lineView.setSrcContent(showContent.getContent());
        lineView.invalidate();

        //训练内容变换，设置当前处方的训练内容
        trainPres.setTrainingContentType(showContent.getTrainingContentType());
        trainPres.setTrainingContentCategoryId(showContent.getTrainingContentCategoryId());
        trainPres.setTrainingContentArticleId(showContent.getTrainingContentArticleId());
        SharePreferenceTool.setLastReversalTrainPres(context, trainPres);
    }

    public void changeToTrain() {
        trainStatus = TrainStatus.TRAINING;
        tflistener.changeToStatus(trainStatus);
    }

    /**
     * 重新刷新显示视标
     */
    public void initTraining(boolean isContentNext) {
        computeTextSize();

        Calendar now = Calendar.getInstance();
        startDate = now.getTime();

        String glassLevelTip = "镜片" + String.valueOf(glassLevel) + "级";
        tflistener.setItemTip(glassType.getName() + Util.arrayVisionCardLevels[level].getName());
        //tflistener.setItemTip(glassType.getName() + String.valueOf(level) + "级"+","+glassLevelTip);
        refreshContent(isContentNext, level);

        startGlassLookTimer();
    }

    /**
     * 刷新内容显示显示
     *
     * @param isContentNext 是否内容换到下一个
     * @param level
     */
    public void refreshContent(boolean isContentNext, int level) {
        float trainingTextSize = Util.arrayVisionCardLevels[level].getTextSize();
        lineView.setMmSize(trainingTextSize);

        if (isContentNext) {
            if (lineView.isHasNextRow()) {
                lineView.nextRow();
                lineView.invalidate();
            } else {
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
                    //随机，可能随机到本身，这种情况是对的
//                    while (nowShowContentPos == nextPos) {
//                        nextPos = random.nextInt(listShowContent.size());
//                    }
                    nowShowContentPos = nextPos;
                }

                ShowContent showContent = listShowContent.get(nowShowContentPos);
                lineView.resetIndex();
                lineView.setShowContentType(showContent.getShowContentType());
                lineView.setSrcContent(showContent.getContent());
                lineView.invalidate();

                //训练内容变换，设置当前处方的训练内容
                trainPres.setTrainingContentType(showContent.getTrainingContentType());
                trainPres.setTrainingContentCategoryId(showContent.getTrainingContentCategoryId());
                trainPres.setTrainingContentArticleId(showContent.getTrainingContentArticleId());
                SharePreferenceTool.setLastReversalTrainPres(context, trainPres);
            }
        }
        lineView.invalidate();
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
     *
     * @return
     */
    public int computeGlassResult() {
        int glassResult = 3;
        long timeLong = endDate.getTime() - startDate.getTime();
        if (eyeType == EnumEyeType.DOUBLE) {
            if (timeLong <= ReversalConfig.SmallOverTimeDouble) {
                glassResult = 0;
            } else if (timeLong <= ReversalConfig.NoChangeOverTimeDouble) {
                glassResult = 1;
            } else if (timeLong <= ReversalConfig.BigOverTimeDouble) {
                glassResult = 2;
            } else {
                glassResult = 3;
            }
        } else {
            if (timeLong <= ReversalConfig.SmallOverTime) {
                glassResult = 0;
            } else if (timeLong <= ReversalConfig.NoChangeOverTime) {
                glassResult = 1;
            } else if (timeLong <= ReversalConfig.BigOverTime) {
                glassResult = 2;
            } else {
                glassResult = 3;
            }
        }

        return glassResult;
    }

    /**
     * 镜片切换
     */
    public void changeGlass() {
        if (trainStatus == TrainStatus.TRAINING) {
            //按键变成正在调整处理
            trainStatus = TrainStatus.ADJUST;

            stopGlassLookTimer();

            manualChange();

            deviceAdjust();
        } else {
            showMsg(context.getString(R.string.device_not_adjusted_well_msg));
        }
    }

    private void manualChange() {
        enterNextGlass();
        glassUseSecond = 0;
        //initTraining(true);
    }

    public void enterNextGlass() {
        Calendar now = Calendar.getInstance();
        endDate = now.getTime();

        //因为是按键的，所以肯定不能是3，肯定要正负片互换
        int glassResult = computeGlassResult();
        if (glassResult == 3) {
            glassResult = 2;
        }
        finishOneGlass(glassResult);
    }

    //镜片超时监听器
    private Observable<Long> observableGlassLook = null;
    private Subscriber<Long> subscriberGlassLook = null;
    private Subscription subscriptionGlassLook = null;

    private void startGlassLookTimer() {
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

    public void stopGlassLookTimer() {
        Timber.d("stopLockTimmer");
        if (subscriptionGlassLook != null && !subscriptionGlassLook.isUnsubscribed()) {
            subscriptionGlassLook.unsubscribe();
        }
        subscriptionGlassLook = null;
        subscriberGlassLook = null;
        observableGlassLook = null;
    }

    /**
     * 当前看超时
     */
    public void glassLookOver() {
        finishOneGlass(3);
        glassUseSecond = 0;

        initTraining(false);
    }

    //镜片超时监听器
    private Observable<Long> observableFindRightGlass = null;
    private Subscriber<Long> subscriberFindRightGlass = null;
    private Subscription subscriptionFindRightGlass = null;

    private void startFindRightGlassTimer() {
        stopFindRightGlassTimer();

        observableFindRightGlass = Observable.timer(ReversalConfig.FindRightGlassMaxSecond, TimeUnit.SECONDS);
        subscriberFindRightGlass = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                findRightGlassTimeOut();
            }
        };

        subscriptionFindRightGlass = observableFindRightGlass.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberFindRightGlass);
    }

    private void stopFindRightGlassTimer() {
        Timber.d("stopLockTimmer");
        if (subscriptionFindRightGlass != null && !subscriptionFindRightGlass.isUnsubscribed()) {
            subscriptionFindRightGlass.unsubscribe();
        }
        subscriptionFindRightGlass = null;
        subscriberFindRightGlass = null;
        observableFindRightGlass = null;
    }

    public void findRightGlassTimeOut() {
        finishTrain();
    }

    /**
     * 处理获取转盘位置
     */
    private void handleLoc() {
        leftFuLoc = TurntableLocation.getFuByDegreeLevel(glassLevelLeft);
        leftZhengLoc = TurntableLocation.getZhengByDegreeLevel(glassLevelLeft);

        rightFuLoc = TurntableLocation.getFuByDegreeLevel(glassLevelRight);
        rightZhengLoc = TurntableLocation.getZhengByDegreeLevel(glassLevelRight);
    }

    /**
     * 设备调整
     */
    public void deviceAdjust() {
        handleLoc();


        int leftLoc = -1;
        int rightLoc = -1;
        if (glassType == EnumGlassType.Zheng) {
            if (trainPres.getEyeType() == EnumEyeType.LEFT) {
                leftLoc = leftZhengLoc.getValue();
            } else if (trainPres.getEyeType() == EnumEyeType.RIGHT) {
                rightLoc = rightZhengLoc.getValue();
            } else if (trainPres.getEyeType() == EnumEyeType.DOUBLE) {
                leftLoc = leftZhengLoc.getValue();
                rightLoc = rightZhengLoc.getValue();
            }
        } else {
            if (trainPres.getEyeType() == EnumEyeType.LEFT) {
                leftLoc = leftFuLoc.getValue();
            } else if (trainPres.getEyeType() == EnumEyeType.RIGHT) {
                rightLoc = rightFuLoc.getValue();
            } else if (trainPres.getEyeType() == EnumEyeType.DOUBLE) {
                leftLoc = leftFuLoc.getValue();
                rightLoc = rightFuLoc.getValue();
            }
        }

        //ttsEngine.startSpeaking(R.raw.wait_adjust_device);
        ControlMessageSet messageSet = TrainUseCase.createReversalPrepare(leftLoc, rightLoc);
        //messageSet.setHandler(handlerDeviceAdjustFinish);
        //MotorBus.getInstance().sendMessageSet(messageSet);

        //转盘转动的时候隐藏，显示白的
        lineView.setVisibility(View.GONE);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Timber.d("device adjust finish");

                    lineView.setVisibility(View.VISIBLE);

                    //ADJUST调整中，代表调整完成需要显示
                    //PAUSE的时候，刷新内容即可
                    //解决结束后，刷新内容报错的问题
                    if (trainStatus == TrainStatus.ADJUST) {
                        trainStatus = TrainStatus.TRAINING;
                        //接收到转盘完成后刷新文字，并显示出来
                        initTraining(true);
                    } else if (trainStatus == TrainStatus.PAUSING) {
                        initTraining(true);
                    }
                });

//        Calendar now = Calendar.getInstance();
//        turnStartDate = now.getTime();
    }


    /**
     * 重置翻转拍镜片
     */
    @Override
    public void resetReersalLens() {
        super.resetReersalLens();
        int status = 0;
        if (trainPres.getEyeType() == EnumEyeType.LEFT) {
            status = 0;
        } else if (trainPres.getEyeType() == EnumEyeType.RIGHT) {
            status = 1;
        } else if (trainPres.getEyeType() == EnumEyeType.DOUBLE) {
            status = 2;
        }
        ControlMessageSet messageSet = TrainUseCase.resetGlass(status);
        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    ToastUtil.showShortToast(context, context.getString(R.string.reset_suc));
                });
    }
    //    private Date turnStartDate;
//    private Date turnEndDate;

    /**
     * 设备调整完成
     */
    private Handler handlerDeviceAdjustFinish = new Handler() {
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

    /**
     * 接收推送的训练内容
     *
     * @param pushTrainContentContent
     */
    public void receivePushTrainContentMsg(PushTrainContentContent pushTrainContentContent) {
        String json = "";
        try {
            json = pushTrainContentContent.toJson().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showMsg(context.getString(R.string.receive_push_msg_instructions) + json);
        syncUserContent(pushTrainContentContent);
    }

    public void syncUserContent(PushTrainContentContent pushTrainContentContent) {
        preparePause();

        ApiService apiService = ServiceFactory.create(ServiceFactory.TYPE_OBJECT);
        ContentUseCase contentUseCase = new ContentUseCase(apiService);

        trainPres.setTrainingContentType(EnumContentType.values()[pushTrainContentContent.getContentType()]);
        trainPres.setTrainingContentCategoryId(pushTrainContentContent.getCateId());
        trainPres.setTrainingContentArticleId(pushTrainContentContent.getContentId());
        trainPres.setTrainingContentLoopMode(EnumContentLoopMode.values()[pushTrainContentContent.getMode()]);

        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        int userId = SharePreferenceTool.getSharePreferenceValueInt(context, SharePreferenceTool.PREF_NOW_TRAIN_USER_ID);
        contentUseCase.getUploadIDs(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(apiResult -> getAllContents(apiResult))
                .flatMap(list -> {
                    return getListShowContent(false);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listShowContent -> {
                    Timber.d("Windern:listShowContent:size:%s", listShowContent.size());
                    this.listShowContent = listShowContent;

                    this.nowShowContentPos = 0;

                    firstSetContent();
                    //手机端用的文件，发送的是偏移量，所以这边的索引得加1
                    lineView.setPos(pushTrainContentContent.getContentPos() + 1);
                    lineView.invalidate();

                    if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                        viewLoopType.setVisibility(View.VISIBLE);
                        contentLoopMode = trainPres.getTrainingContentLoopMode();
                    } else {
                        viewLoopType.setVisibility(View.GONE);
                        //不管对方发什么都是顺序的
                        trainPres.setTrainingContentLoopMode(EnumContentLoopMode.SEQUENCE);
                        contentLoopMode = trainPres.getTrainingContentLoopMode();
                    }

                    viewLoopType.setSelectContent(trainPres.getTrainingContentLoopMode().getName());

                    SharePreferenceTool.setLastReversalTrainPres(context, trainPres);

                    //refreshDataView(list);
                    prepareResume();
                }, error -> {
                    showMsg(context.getString(R.string.get_user_sync_books_fail));
                });
    }

    public final class WebRequestCombin {
        public int needCount = 0;
        public int completeCount = 0;
    }

    public Observable<List<UserUploadIds>> getAllContents(ModelApiResult<List<UserUploadIds>> apiResult) {
        if (apiResult.getCode() == 0) {
            String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);

            final PublishSubject<List<UserUploadIds>> publishSubject = PublishSubject.create();
            final WebRequestCombin webRequestCombin = new WebRequestCombin();

            Gson gson = new Gson();
            String resultJson = gson.toJson(apiResult.getData(), apiResult.getData().getClass());
            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_NOW_TRAIN_USER_UPLOADIDS, resultJson);

            Timber.d("resultJson:%s", resultJson);

            //先计算请求数量
            for (UserUploadIds userUploadIds : apiResult.getData()) {
                if (userUploadIds.type == 0) {
                    for (Integer id : userUploadIds.ids) {
                        TrainingContent trainingContent = SQLite.select().from(TrainingContent.class).where(TrainingContent_Table.id.eq(id)).querySingle();
                        if (trainingContent == null) {
                            webRequestCombin.needCount++;
                        }
                    }
                } else if (userUploadIds.type == 1) {
                    webRequestCombin.needCount++;
                }
            }
            Timber.d("Windern:needCount:%s", webRequestCombin.needCount);

            //再去请求
            for (UserUploadIds userUploadIds : apiResult.getData()) {
                if (userUploadIds.type == 0) {
                    for (Integer id : userUploadIds.ids) {
                        TrainingContent trainingContent = SQLite.select().from(TrainingContent.class).where(TrainingContent_Table.id.eq(id)).querySingle();
                        if (trainingContent == null) {
                            contentUseCase.getTrainingContent(token, id)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe(trainingContentModelApiResult -> {
                                        if (trainingContentModelApiResult.getCode() == 0) {
                                            trainingContentModelApiResult.getData().save();
                                        }

                                        webRequestCombin.completeCount++;
                                        Timber.d("Windern:completeCount:%s", webRequestCombin.completeCount);
                                        if (webRequestCombin.needCount == webRequestCombin.completeCount) {
                                            publishSubject.onNext(apiResult.getData());
                                        }
                                    });
                        }
                    }
                } else if (userUploadIds.type == 1) {
                    contentUseCase.getNewsCategories(token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(newsCategoriesModelApiResult -> {
                                if (newsCategoriesModelApiResult.getCode() == 0) {
                                    for (NewsCategory newsCategory : newsCategoriesModelApiResult.getData()) {
                                        newsCategory.save();
                                    }
                                }

                                webRequestCombin.completeCount++;
                                Timber.d("Windern:completeCount:%s", webRequestCombin.completeCount);
                                if (webRequestCombin.needCount == webRequestCombin.completeCount) {
                                    publishSubject.onNext(apiResult.getData());
                                }
                            });
                }
            }


            return publishSubject;
        } else {
            return Observable.error(new Exception(apiResult.getMessage()));
        }
    }

    /**
     * 获取显示内容
     *
     * @param isStart 是不是处方刚开始的时候
     * @return
     */
    public Observable<List<ShowContent>> getListShowContent(boolean isStart) {
        Observable<List<ShowContent>> observableGetShowContent = Observable.just(new ArrayList<>());

        if (isStart && getUserTrainMode() == EnumUserTrainMode.CONTROL) {
            //如果是开始的时候需要考虑视光师控制，否则不用考虑
            //如果不是视光师控制也是走的下面
            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                //和上一个处方完全一样，不用重新取内容了
                if (ReversalTrainPres.isCompletedEqual(previousTrainPres, trainPres)) {
//                    realStartTraining();
                } else {
                    //先取文章
                    String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                    observableGetShowContent = contentUseCase.getTrainingContent(token, trainPres.getTrainingContentArticleId())
                            .subscribeOn(Schedulers.io())
                            .flatMap(apiResult -> {
                                List<ShowContent> listShowContent = new ArrayList<ShowContent>();

                                if (apiResult.getCode() == 0) {
                                    TrainingContent trainingContent = apiResult.getData();
                                    listShowContent.add(trainingContent.toShowContent());
                                } else {
                                    showMsg(context.getString(R.string.get_article_fail));
                                    trainPres.setTrainingContentType(EnumContentType.LETTER);

                                    listShowContent = ContentManager.createShowContentList(trainPres.getTrainingContentType(), ReversalConfig.MaxCacheLetterCount);
                                }

                                return Observable.just(listShowContent);
                            });
                }
            } else {
                List<ShowContent> listShowContent = ContentManager.createShowContentList(trainPres.getTrainingContentType(), ReversalConfig.MaxCacheLetterCount);
                observableGetShowContent = Observable.just(listShowContent);
//                realStartTraining();
            }
        } else {
            String resultUploads = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_NOW_TRAIN_USER_UPLOADIDS);
            List<UserUploadIds> userUploadIdsList = new ArrayList<>();
            Gson gson = new Gson();
            userUploadIdsList = gson.fromJson(resultUploads, new TypeToken<List<UserUploadIds>>() {
            }.getType());

            if (trainPres.getTrainingContentType() == EnumContentType.ARTICLE) {
                UserUploadIds userUploadIds = null;
                //查找本地文章的ids
                for (int i = 0; i < userUploadIdsList.size(); i++) {
                    if (userUploadIdsList.get(i).type == 0) {
                        userUploadIds = userUploadIdsList.get(i);
                    }
                }
                //如果用户没有选择本地文章
                if (userUploadIds != null && userUploadIds.ids != null && userUploadIds.ids.size() > 0) {
                    List<ShowContent> listShowContent = new ArrayList<>();

                    //选中文章之前内容
                    List<ShowContent> beforList = new ArrayList<>();
                    List<ShowContent> afterList = new ArrayList<>();
                    boolean isFindId = false;
                    for (int j = 0; j < userUploadIds.ids.size(); j++) {
                        int id = userUploadIds.ids.get(j);
                        TrainingContent trainingContent = SQLite.select().from(TrainingContent.class).where(TrainingContent_Table.id.eq(id)).querySingle();
                        if (trainingContent != null) {
                            if (id == trainPres.getTrainingContentArticleId()) {
                                isFindId = true;
                            }
                            if (isFindId) {
                                afterList.add(trainingContent.toShowContent());
                            } else {
                                beforList.add(trainingContent.toShowContent());
                            }
                        }
                    }
                    for (int m = 0; m < afterList.size(); m++) {
                        listShowContent.add(afterList.get(m));
                    }
                    for (int m = 0; m < beforList.size(); m++) {
                        listShowContent.add(beforList.get(m));
                    }

                    observableGetShowContent = Observable.just(listShowContent);
//                    realStartTraining();
                } else {
                    trainPres.setTrainingContentType(EnumContentType.LETTER);

                    List<ShowContent> listShowContent = ContentManager.createShowContentList(trainPres.getTrainingContentType(), ReversalConfig.MaxCacheLetterCount);
                    observableGetShowContent = Observable.just(listShowContent);
//                    realStartTraining();
                }
            } else if (trainPres.getTrainingContentType() == EnumContentType.NEWS) {
                //先取新闻
                String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                if (trainPres.getTrainingContentArticleId() == 0) {
                    observableGetShowContent = contentUseCase.getNewsOnce(token, trainPres.getTrainingContentCategoryId())
                            .subscribeOn(Schedulers.io())
                            .flatMap(apiResult -> {
                                List<ShowContent> listShowContent = new ArrayList<ShowContent>();

                                if (apiResult.getCode() == 0) {
                                    List<News> newsList = apiResult.getData();
                                    for (News news : newsList) {
                                        news.save();
                                        listShowContent.add(news.toShowContent());
                                    }
                                } else {
                                    showMsg(context.getString(R.string.get_news_fail));
                                    trainPres.setTrainingContentType(EnumContentType.LETTER);

                                    listShowContent = ContentManager.createShowContentList(trainPres.getTrainingContentType(), ReversalConfig.MaxCacheLetterCount);
                                }

                                return Observable.just(listShowContent);
                            });
                } else {
                    observableGetShowContent = contentUseCase.getNewsOnceFromId(token, trainPres.getTrainingContentCategoryId(), trainPres.getTrainingContentArticleId())
                            .subscribeOn(Schedulers.io())
                            .flatMap(apiResult -> {
                                List<ShowContent> listShowContent = new ArrayList<ShowContent>();

                                if (apiResult.getCode() == 0) {
                                    List<News> newsList = apiResult.getData();
                                    for (News news : newsList) {
                                        news.save();
                                        listShowContent.add(news.toShowContent());
                                    }
                                } else {
                                    showMsg(context.getString(R.string.get_news_fail));
                                    trainPres.setTrainingContentType(EnumContentType.LETTER);

                                    listShowContent = ContentManager.createShowContentList(trainPres.getTrainingContentType(), ReversalConfig.MaxCacheLetterCount);
                                }

                                return Observable.just(listShowContent);
                            });
                }
            } else {
                List<ShowContent> listShowContent = ContentManager.createShowContentList(trainPres.getTrainingContentType(), ReversalConfig.MaxCacheLetterCount);
                observableGetShowContent = Observable.just(listShowContent);
//                realStartTraining();
            }
        }

        return observableGetShowContent;
    }

    /**
     * 准备页面的暂停
     */
    public void preparePause() {
        stopTimer();
        stopFindRightGlassTimer();
        stopGlassLookTimer();

        trainStatus = TrainStatus.PREPARING;
        tflistener.changeToStatus(trainStatus);
    }

    /**
     * 准备页面的恢复
     */
    public void prepareResume() {
        //恢复训练的时候重新开始计时，否则会出现暂停后，点击按键没有反应的情况
        glassUseSecond = 0;
        Calendar now = Calendar.getInstance();
        startDate = now.getTime();

        if (isOnFindGlassLevel) {
            startFindRightGlassTimer();
        } else {
            startTimer();
        }
        startGlassLookTimer();

        trainStatus = TrainStatus.TRAINING;
        tflistener.changeToStatus(trainStatus);
    }
}
