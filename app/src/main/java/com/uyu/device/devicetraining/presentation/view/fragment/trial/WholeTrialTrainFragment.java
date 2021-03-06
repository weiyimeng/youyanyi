package com.uyu.device.devicetraining.presentation.view.fragment.trial;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.trial.FinishDescManager;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.entity.other.ReceptionTrial;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.PrepareDescManager;
import com.uyu.device.devicetraining.data.entity.trainpres.FollowTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.GlanceTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RedGreenReadTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumButtonType;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumLineType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.trial.TrialTrainManagerPresenter;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnSelectContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnTextSizeChangeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.ReversalPresChangeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainItemTimeType;
import com.uyu.device.devicetraining.presentation.view.fragment.BaseFragment;
import com.uyu.device.devicetraining.presentation.view.widget.ShowItemIntroPopupWindow;
import com.uyu.device.devicetraining.presentation.view.widget.ShowItemIntroView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ?????????fragment??????WelcomeActivity???????????????
 * Created by windern on 2016/6/2.
 */
public class WholeTrialTrainFragment extends BaseFragment implements TrialTrainManagerPresenter.TrainManagerPresenterListener
        , TrialTrainStageFragment.TrialTrainStageFragmentListener
        , TrialTrainFragment.TrialTrainFragmentListener
        , ReversalPresChangeListener
        , OnSelectContentListener
        , OnSelectUserContentListener
        , OnTextSizeChangeListener
        , ShowItemIntroView.OnCloseItemIntroListener{
    @Inject
    TrialTrainManagerPresenter trainManagerPresenter;

    @Bind(R.id.fl_fragment_stereoscope)
    FrameLayout fl_fragment_stereoscope;

    TrialStereoscopeTrainFragment stereoscopeTrainFragment;

    @Bind(R.id.fl_fragment_fractured_ruler)
    FrameLayout fl_fragment_fractured_ruler;

    TrialFracturedRulerTrainFragment fracturedRulerTrainFragment;

    @Bind(R.id.fl_fragment_reversal)
    FrameLayout fl_fragment_reversal;

    TrialReversalTrainFragment reversalTrainFragment;

//    @Bind(R.id.fl_fragment_red_green_read)
//    FrameLayout fl_fragment_red_green_read;
//
//    TrialRedGreenReadTrainFragment redGreenReadTrainFragment;
//
//    @Bind(R.id.fl_fragment_approach)
//    FrameLayout fl_fragment_approach;
//
//    TrialApproachTrainFragment approachTrainFragment;

    @Bind(R.id.fl_fragment_r_g_variable_vector)
    FrameLayout fl_fragment_r_g_variable_vector;

    TrialRGVariableVectorTrainFragment rgVariableVectorTrainFragment;

    @Bind(R.id.fl_fragment_r_g_fixed_vector)
    FrameLayout fl_fragment_r_g_fixed_vector;

    TrialRGFixedVectorTrainFragment rgFixedVectorTrainFragment;

//    @Bind(R.id.fl_fragment_glance)
//    FrameLayout fl_fragment_glance;
//
//    TrialGlanceTrainFragment glanceTrainFragment;
//
//    @Bind(R.id.fl_fragment_follow)
//    FrameLayout fl_fragment_follow;
//
//    TrialFollowTrainFragment followTrainFragment;

    FrameLayout fl_fragmentTrainNow;
    private TrialTrainFragment fragmentNow;

    private TrainItemTimeType itemTimeType = TrainItemTimeType.ALL_PRES;
    private EnumTrainItem trainItemNow = EnumTrainItem.REVERSAL;
    //?????????????????????????????????????????????????????????????????????
    /**
     * ??????????????????
     */
    private TrainPres trainPres;
    /**
     * ?????????????????????
     */
    private TrainPres trainPresPrevious;
    private TrainBack trainBack;

    @Bind(R.id.fl_fragment_train_stage)
    FrameLayout fl_fragment_train_stage;

    @Bind(R.id.container_time_training)
    RelativeLayout container_time_training;

    TrialTrainStageFragment trainStageFragment;

    public void setReceptionTrial(ReceptionTrial receptionTrial) {
        trainManagerPresenter.setReceptionTrial(receptionTrial);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_whole_trial_train, container, true);
        ButterKnife.bind(this, fragmentView);

        initFragment();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainManagerPresenter.setViewListener(this);
    }

    public void initFragment(){
        trainStageFragment = (TrialTrainStageFragment) getChildFragmentManager().findFragmentById(R.id.fragment_trial_train_stage);

        stereoscopeTrainFragment = (TrialStereoscopeTrainFragment) getChildFragmentManager().findFragmentById(R.id.fragment_trial_stereoscope);
        fracturedRulerTrainFragment = (TrialFracturedRulerTrainFragment) getChildFragmentManager().findFragmentById(R.id.fragment_trial_fractured_ruler);
        reversalTrainFragment = (TrialReversalTrainFragment) getChildFragmentManager().findFragmentById(R.id.fragment_trial_reversal);
//        redGreenReadTrainFragment = (TrialRedGreenReadTrainFragment) getFragmentManager().findFragmentById(R.id.fragment_trial_red_green_read);
//        approachTrainFragment = (TrialApproachTrainFragment) getFragmentManager().findFragmentById(R.id.fragment_trial_approach);
        rgVariableVectorTrainFragment = (TrialRGVariableVectorTrainFragment) getChildFragmentManager().findFragmentById(R.id.fragment_trial_r_g_variable_vector);
        rgFixedVectorTrainFragment = (TrialRGFixedVectorTrainFragment) getChildFragmentManager().findFragmentById(R.id.fragment_trial_r_g_fixed_vector);
//        glanceTrainFragment = (TrialGlanceTrainFragment) getFragmentManager().findFragmentById(R.id.fragment_trial_glance);
//        followTrainFragment = (TrialFollowTrainFragment) getFragmentManager().findFragmentById(R.id.fragment_trial_follow);

        trainStageFragment.setListener(this);
        stereoscopeTrainFragment.setListener(this);
        fracturedRulerTrainFragment.setListener(this);
        reversalTrainFragment.setListener(this);
        rgVariableVectorTrainFragment.setListener(this);
        rgFixedVectorTrainFragment.setListener(this);
    }

    public void changeNowFragment(EnumTrainItem trainItem) {
        switch (trainItem) {
            case STEREOSCOPE:
                fl_fragmentTrainNow = fl_fragment_stereoscope;
                fragmentNow = stereoscopeTrainFragment;
                break;
            case FRACTURED_RULER:
                fl_fragmentTrainNow = fl_fragment_fractured_ruler;
                fragmentNow = fracturedRulerTrainFragment;
                break;
            case REVERSAL:
                fl_fragmentTrainNow = fl_fragment_reversal;
                fragmentNow = reversalTrainFragment;
                break;
//            case RED_GREEN_READ:
//                fl_fragmentTrainNow = fl_fragment_red_green_read;
//                fragmentNow = redGreenReadTrainFragment;
//                break;
//            case APPROACH:
//                fl_fragmentTrainNow = fl_fragment_approach;
//                fragmentNow = approachTrainFragment;
//                break;
            case R_G_VARIABLE_VECTOR:
                fl_fragmentTrainNow = fl_fragment_r_g_variable_vector;
                fragmentNow = rgVariableVectorTrainFragment;
                break;
            case R_G_FIXED_VECTOR:
                fl_fragmentTrainNow = fl_fragment_r_g_fixed_vector;
                fragmentNow = rgFixedVectorTrainFragment;
                break;
//            case GLANCE:
//                fl_fragmentTrainNow = fl_fragment_glance;
//                fragmentNow = glanceTrainFragment;
//                break;
//            case FOLLOW:
//                fl_fragmentTrainNow = fl_fragment_follow;
//                fragmentNow = followTrainFragment;
//                break;
            default:
                break;
        }
    }

    public void hideAllTrainFragment() {
        fl_fragment_stereoscope.setVisibility(View.GONE);
        fl_fragment_fractured_ruler.setVisibility(View.GONE);
        fl_fragment_reversal.setVisibility(View.GONE);
//        fl_fragment_red_green_read.setVisibility(View.GONE);
//        fl_fragment_approach.setVisibility(View.GONE);
        fl_fragment_r_g_variable_vector.setVisibility(View.GONE);
        fl_fragment_r_g_fixed_vector.setVisibility(View.GONE);
//        fl_fragment_glance.setVisibility(View.GONE);
//        fl_fragment_follow.setVisibility(View.GONE);
    }

    public void showNowFragment(EnumTrainItem trainItem) {
        hideAllTrainFragment();
        changeNowFragment(trainItem);
        fl_fragmentTrainNow.setVisibility(View.VISIBLE);
    }

    public void startTrain(TrainPres trainPres){
        //???????????????????????????????????????
        this.trainPresPrevious = this.trainPres;

        showNowFragment(trainPres.getTrainItemType());

        this.trainPres = trainPres;
        itemTrainPrepare();
    }

    /**
     * ??????????????????????????????
     * @param trainPres
     */
    public void startTrainSkipPrepare(TrainPres trainPres){
        //???????????????????????????????????????
        this.trainPresPrevious = this.trainPres;

        showNowFragment(trainPres.getTrainItemType());

        this.trainPres = trainPres;
        itemTrainPrepare();

        itemTrainTraining();
    }

    /**
     * ??????????????????
     */
    public void itemTrainPrepare() {
        if (trainPres != null) {
            switch (trainPres.getTrainItemType()) {
                case REVERSAL:
                    //?????????????????????????????????????????????????????????????????????????????????????????????
                    trainStageFragment.initPrepareData(trainPresPrevious, trainPres, this, this, null,this);
                    break;
                case RED_GREEN_READ:
                    trainStageFragment.initPrepareData(trainPresPrevious, trainPres, null, this, this,this);
                    break;
                case FOLLOW:
                case GLANCE:
                    trainStageFragment.initPrepareData(trainPresPrevious, trainPres, null, this, null,this);
                    break;
                default:
                    trainStageFragment.initPrepareData(trainPresPrevious, trainPres, null, null, null,this);
                    break;
            }
        }
        changeItemTimeType(TrainItemTimeType.PREPARE);
        prepareItemIntroView();
    }

    public void itemTrainTraining() {
        changeItemTimeType(TrainItemTimeType.TRAINING);

        showNowFragment(trainPres.getTrainItemType());
        fragmentNow.receiveTrainPres(trainPres);
//        String showName = String.format("%s ???%s???",presTrainMessageContent.getTp().getShowName(),presTrainMessageContent.getCurrentRepeatTime());
//        if(presTrainMessageContent.getTp() instanceof ReversalTrainPres){
//            showName += " " + ((ReversalTrainPres)presTrainMessageContent.getTp()).getEyeType().getName() + "???";
//        }
//
//        testTrainFragment.setItemTrainDesc(showName);
//        fl_fragment_test_train.setVisibility(View.VISIBLE);
    }

    /**
     * ?????????????????????????????????
     */
    public void changeItemTimeType(TrainItemTimeType itemTimeType) {
        this.itemTimeType = itemTimeType;
        trainStageFragment.setTrainItemTimeType(this.itemTimeType);
        if (fl_fragmentTrainNow != null) {
            fl_fragmentTrainNow.setVisibility(this.itemTimeType == TrainItemTimeType.TRAINING ? View.VISIBLE : View.GONE);
        }
        if (this.itemTimeType == TrainItemTimeType.TRAINING) {
            container_time_training.setVisibility(View.VISIBLE);
            fl_fragment_train_stage.setVisibility(View.GONE);

            setFinishType(null);
            setShowItemIntroBtnVisible(View.VISIBLE);

            //????????????????????????????????????????????????
            stopLockTimer();
        } else {
            setShowItemIntroBtnVisible(View.GONE);

            container_time_training.setVisibility(View.GONE);
            fl_fragment_train_stage.setVisibility(View.VISIBLE);

            if(this.itemTimeType == TrainItemTimeType.ALL_PRES){
                setItemTitle("??????????????????");
                setItemTip("");
                setFinishType(EnumButtonType.FINISH_ALL);
            }else if(this.itemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE){
                setItemTitle("???????????????????????????");
                setItemTip("");
                setFinishType(EnumButtonType.FINISH_ALL);
            }else if(this.itemTimeType == TrainItemTimeType.PREPARE){
                setItemTitle(trainPres.getShowName()+"??????");
                setItemTip("");
                setFinishType(EnumButtonType.FINISH_ALL);
            }else if(this.itemTimeType == TrainItemTimeType.WELCOME){
                setItemTitle("??????????????????");
                setItemTip("");
                setFinishType(null);
            }else if(this.itemTimeType == TrainItemTimeType.ONE_END || this.itemTimeType == TrainItemTimeType.ALL_END){
                setItemTitle("??????????????????");
                setItemTip("");
                setFinishType(EnumButtonType.FINISH_ITEM);
            }else if(this.itemTimeType == TrainItemTimeType.SHOWRESULT){
                setItemTitle("??????????????????");
                setItemTip("");
                setFinishType(null);
            }

            //???????????????????????????
            startLockTimer();
        }
    }

    /**
     * ????????????
     * @param itemTitle
     */
    public void setItemTitle(String itemTitle){
        ((WelcomeActivity)getActivity()).setItemTitle(itemTitle);
    }

    /**
     * ??????item??????
     * ??????????????????????????????
     * @param itemTip
     */
    public void setItemTip(String itemTip){
        ((WelcomeActivity)getActivity()).setItemTip(itemTip);
    }

    /**
     * ??????banner???finishTyp
     * @param buttonType
     */
    public void setFinishType(EnumButtonType buttonType){
        ((WelcomeActivity)getActivity()).setFinishType(buttonType);
    }

    /**
     * ?????????????????????????????????????????????
     * @param visible
     */
    public void setShowItemIntroBtnVisible(int visible){
        ((WelcomeActivity)getActivity()).setShowItemIntroBtnVisible(visible);
    }

    private Observable<Long> observableLock = null;
    private Subscriber<Long> subscriberLock = null;
    private Subscription subscriptionLock = null;

    private void startLockTimer(){
        Log.d("aaa","=====startLockTimer======");
        stopLockTimer();

        Timber.d("startLockTimer");
        observableLock = Observable.timer(TrainConfig.NoOptTrainLockTime, TimeUnit.SECONDS);
        subscriberLock = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                lockTrain();
            }
        };

        subscriptionLock = observableLock.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberLock);
    }
    private void stopLockTimer(){
        Timber.d("stopLockTimmer");
        if(subscriptionLock!=null && !subscriptionLock.isUnsubscribed()){
            subscriptionLock.unsubscribe();
        }
        if(subscriberLock!=null && !subscriberLock.isUnsubscribed()){
            subscriberLock.unsubscribe();
        }
        subscriptionLock = null;
        subscriberLock = null;
        observableLock = null;
    }

    public void lockTrain(){
        if(itemTimeType!= TrainItemTimeType.TRAINING) {
            //??????????????????????????????
            munualFinishTrain();
        }
    }

    /**
     * ??????????????????
     */
    public void munualFinishTrain(){
        trainManagerPresenter.allTrainFinish();
    }

    public void finishTrain(){
        itemTrainFinish();
    }

    @Override
    public void finishTrain(TrainBack trainBack){
        this.trainBack = trainBack;

        if(trainPres.getTrainItemType()==EnumTrainItem.REVERSAL && ((ReversalTrainPres)trainPres).getEyeType() != EnumEyeType.DOUBLE){
            showMsg(getActivity().getResources().getString(R.string.title_reversal)+((ReversalTrainPres)trainPres).getEyeType().getName()+getActivity().getString(R.string.train_end));
            //?????????????????????????????????????????????????????????ALL_END??????
            trainManagerPresenter.finishTrain(trainBack);
        }else{
            TrainPres trainPresPre = trainManagerPresenter.getNowTrainPres();
            TrainPres trainPresNext = trainManagerPresenter.getNextTrainPres(trainBack,false);
            String[] finishDescList = FinishDescManager.getFinishDescList(trainPresPre,trainPresNext);
            String finishContent = TextUtils.join("\n\n",finishDescList);
            trainStageFragment.refreshAllFinishView(finishContent);

            changeItemTimeType(TrainItemTimeType.ALL_END);
        }
//        trainManagerPresenter.finishTrain(trainBack);
    }

    /**
     * ????????????
     */
    public void itemTrainFinish() {
//        Boolean isItemAllEnd = trainManagerPresenter.itemTrainFinish();
//        if(isItemAllEnd==null){
//            //?????????null?????????????????????????????????????????????????????????????????????????????????????????????
//        } else if (isItemAllEnd) {
//            itemTrainAllEnd();
//        } else {
//            itemTrainOneEnd();
//        }
    }

    /**
     * ?????????????????????
     */
    public void itemTrainOneEnd() {
        //???????????????????????????????????????????????????????????????prepare??????????????????????????????one_end???????????????????????????
        //????????????one_end?????????
        if(trainPres.getTrainItemType()== EnumTrainItem.REVERSAL){
            onFinishViewClick();
        }else {
            changeItemTimeType(TrainItemTimeType.ONE_END);
        }
    }

    /**
     * ??????????????????
     */
    public void itemTrainAllEnd() {
        changeItemTimeType(TrainItemTimeType.ALL_END);
    }

    /**
     * ????????????????????????
     */
    public void prepareFinish() {
        itemTrainTraining();
    }

    /**
     * ???????????????????????????????????????
     */
    public void oneEndFinish() {
//        trainManagerPresenter.startNextTrain();
//        itemTrainTraining();
    }

    public void onFinishViewClick() {
        trainManagerPresenter.finishTrain(trainBack);
    }

    @Override
    public void onSelectUserContent(EnumContentType trainingContentType, int trainingContentCategoryId, int trainingContentArticleId, String showTitle) {
        if (trainPres != null) {
            switch (trainPres.getTrainItemType()) {
                case REVERSAL:
                    ((ReversalTrainPres) trainPres).setTrainingContentType(trainingContentType);
                    ((ReversalTrainPres) trainPres).setTrainingContentCategoryId(trainingContentCategoryId);
                    ((ReversalTrainPres) trainPres).setTrainingContentArticleId(trainingContentArticleId);
                    break;
            }
        }
    }

    @Override
    public void onSelectContent(int trainingContentType, int trainingContentArticleId, String title) {
        if (trainPres != null) {
            switch (trainPres.getTrainItemType()) {
                case REVERSAL:
                    ((ReversalTrainPres) trainPres).setTrainingContentType(EnumContentType.values()[trainingContentType]);
                    ((ReversalTrainPres) trainPres).setTrainingContentArticleId(trainingContentArticleId);
                    break;
                case RED_GREEN_READ:
                    ((RedGreenReadTrainPres) trainPres).setTrainingContentType(EnumContentType.values()[trainingContentType]);
                    ((RedGreenReadTrainPres) trainPres).setTrainingContentArticleId(trainingContentArticleId);
                    break;
                case FOLLOW:
                    ((FollowTrainPres) trainPres).setLineType(EnumLineType.values()[trainingContentType]);
                    break;
                case GLANCE:
                    ((GlanceTrainPres) trainPres).setTrainingContentType(EnumContentType.values()[trainingContentType]);
                    ((GlanceTrainPres) trainPres).setTrainingContentArticleId(trainingContentArticleId);
                    break;

            }
        }
    }

    @Override
    public void onTxtSizeChange(int size) {
        ((RedGreenReadTrainPres) trainPres).setLetterSize(size);
    }

    @Override
    public void changeLeft(int level) {
        ((ReversalTrainPres) trainPres).setLNegativeDegreeLevel(level);
        ((ReversalTrainPres) trainPres).setLPositiveDegreeLevel(level);
    }

    @Override
    public void changeRight(int level) {
        ((ReversalTrainPres) trainPres).setRNegativeDegreeLevel(level);
        ((ReversalTrainPres) trainPres).setRPositiveDegreeLevel(level);
    }


    @Override
    public void finishAllTrain() {
        Log.d("aaa","======finishAllTrain=======");
        trainStageFragment.show_result_view.voicePrompt();
        changeItemTimeType(TrainItemTimeType.SHOWRESULT);
        stopLockTimer();

//        ((WelcomeActivity)getActivity()).finishAllTrain();
    }

    @Override
    public void showPostDialog(String title, String content) {
        ((WelcomeActivity)getActivity()).showPostDialog(title,content);
    }

    @Override
    public void hidePostDialog() {
        ((WelcomeActivity)getActivity()).hidePostDialog();
    }

//    @OnClick(R.id.btn_start_trial_train)
//    public void munualFinishTrialTrain(){
//        trainManagerPresenter.finishTrainPostData();
//    }


    public void startTrain(ReceptionTrial receptionTrial){
        trainManagerPresenter.resetData();
        trainManagerPresenter.setReceptionTrial(receptionTrial);

        changeItemTimeType(TrainItemTimeType.DEVICE_OVERALL_PREPARE);
    }

    /**
     * ????????????????????????
     */
    public void stepDeviceOverallPrepareFinish() {
        trainManagerPresenter.firstInitPres();
        trainManagerPresenter.startNowTrain();
    }

    @Override
    public void stepUserAllPresShowFinish() {

    }

    public void pressEnter(){
        Timber.d("pressEnter:isOnItemIntro:%s",isOnItemIntro);
        if(itemTimeType== TrainItemTimeType.TRAINING) {
            if(isOnItemIntro){
                showItemIntroGo2NextStage();
            }else {
                fragmentNow.pressEnter();
            }
        }else{
            trainStageFragment.pressEnter();
        }
    }

    /**
     * ????????????????????????????????????
     */
    private boolean isOnItemIntro = false;
    private PopupWindow showItemIntroPopupWindow;
    private ShowItemIntroView showItemIntroView;

    /**
     * ?????????????????????view
     * ??????????????????????????????
     */
    public void prepareItemIntroView(){
        showItemIntroPopupWindow = new PopupWindow(getActivity());
        showItemIntroPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        showItemIntroPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        showItemIntroPopupWindow.setOutsideTouchable(true);
        showItemIntroPopupWindow.setTouchable(true);
        showItemIntroPopupWindow.setFocusable(true);
        showItemIntroPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));// ?????????????????????????????????

        showItemIntroView = new ShowItemIntroView(getActivity());
        //????????????
        List<PrepareDesc> descList = PrepareDescManager.getPrepareDescList(trainPresPrevious, trainPres);
        showItemIntroView.setDescList(descList, trainPres);
        showItemIntroView.setmListener(this);

        //????????????????????????popupwindow???????????????????????????rl_root???xml?????????????????????fucusable
        //???????????????????????????????????????
        //???develop????????????????????????
//        RelativeLayout showItemIntroViewRoot = (RelativeLayout) showItemIntroView.findViewById(R.id.rl_root);
//        showItemIntroViewRoot.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent event) {
//                //???????????????????????????????????????up??????down??????
//                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//                    pressEnter();
//                    return true;
//                }else if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
//                    onCloseItemIntro();
//                    return true;
//                }
//                return true;
//            }
//        });

        showItemIntroPopupWindow.setContentView(showItemIntroView);
    }

    /**
     * ?????????????????????????????????
     */
    public void showItemIntro(){
        if(itemTimeType==TrainItemTimeType.TRAINING) {
            if(itemTimeType==TrainItemTimeType.TRAINING) {
                fragmentNow.pauseTrain();

                isOnItemIntro = true;
                showItemIntroView.reset();
                showItemIntroPopupWindow.showAtLocation(container_time_training, Gravity.CENTER, 0, 0);
            }
        }
    }

    @Override
    public void onCloseItemIntro() {
        if(itemTimeType==TrainItemTimeType.TRAINING) {
            if(itemTimeType==TrainItemTimeType.TRAINING) {
                fragmentNow.resumeTrain();
                isOnItemIntro = false;
                showItemIntroPopupWindow.dismiss();
            }
        }
    }

    /**
     * ???????????????????????????????????????????????????
     */
    public void showItemIntroGo2NextStage(){
        showItemIntroView.go2NextStage();
    }

    public void showMsg(String msg) {
        Observable.just(msg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> ToastUtil.showShortToast(getActivity(), msg));
    }
}
