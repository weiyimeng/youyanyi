package com.uyu.device.devicetraining.presentation.presenter.trial;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.EnumUserTrainMode;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.TrainBackApiResult;
import com.uyu.device.devicetraining.data.entity.message.BackTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.ExecutedTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.NormalTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainEmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainMessageType;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.other.Reception;
import com.uyu.device.devicetraining.data.entity.other.ReceptionStatus;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.TrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.RawProvider;
import com.uyu.device.devicetraining.presentation.adapter.TrainFragmentListener;
import com.uyu.device.devicetraining.presentation.adapter.TrainPresenterListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class TrialTrainPresenter<Tp extends TrainPres,Tb extends TrainBack,Tn extends TrainNormal> implements TrainPresenterListener{
    protected final TrainBackUseCase trainBackUseCase;
    protected final ContentUseCase contentUseCase;
    protected final Context context;
    protected final TtsEngine ttsEngine;

    @Inject
    public TrialTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        this.trainBackUseCase = trainBackUseCase;
        this.contentUseCase = contentUseCase;
        this.context = context;
        this.ttsEngine = ttsEngine;
    }

    /**
     * ?????????????????????
     */
    protected TrainEmqttMessage nowExecuteMessage;
    /**
     * ?????????????????????
     */
    protected TrainEmqttMessage lastTrainEmqttMessage;
    /**
     * ?????????????????????
     */
    protected TrainEmqttMessage presTrainEmqttMessage;
    /**
     * ?????????????????????
     */
    protected PresTrainMessageContent presTrainMessageContent;
    /**
     * ????????????
     */
    protected Tp trainPres;
    /**
     * ????????????
     */
    protected Tb trainBack;
    /**
     * ?????????????????????
     */
    protected Tn trainNomal;
    /**
     * ????????????
     */
    protected TrainStatus trainStatus = TrainStatus.WELCOME;
    /**
     * TrainFragment????????????
     */
    protected TrainFragmentListener tflistener;
    /**
     * ?????????????????????????????????
     */
    private int postUpdateReceptionStartCount=0;

    /**
     * ??????TrainFragment????????????
     * @param tflistener
     */
    public void setTflistener(TrainFragmentListener tflistener) {
        this.tflistener = tflistener;
    }

    /**
     * ?????????????????????
     */
    @Override
    public void initStartStatus() {
        trainStatus = TrainStatus.WELCOME;
        tflistener.changeToStatus(trainStatus);
    }

    /**
     * ????????????????????????????????????
     */
    public void preparePresBack(){
        newTrainBack();
        trainBack.setUserId(0);
        trainBack.setOptometristId(0);
        trainBack.setVisionTrainingFeedbackId(0);
        trainBack.setItemTrainPresId(0);
        trainBack.setCurrentRepeatTime(1);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        trainBack.setTrainingStartDate((int) (date.getTime() / 1000));
    }

    /**
     * ????????????TrainBack????????????
     */
    protected void newTrainBack(){

    }

    /**
     * ??????????????????
     * @param presTrainMessageContent
     */
    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        initStartStatus();
        trainStatus = TrainStatus.TRAINING;
        tflistener.startTrain();
    }

    /**
     * ??????????????????
     * @param trainPres
     */
    public void startTrain(Tp trainPres) {
        initStartStatus();
        trainStatus = TrainStatus.TRAINING;
        tflistener.startTrain();
    }

    @Override
    public void sendNowInfo() {
//        if(getUserTrainMode()==EnumUserTrainMode.CONTROL) {
//            sendNormalMsg();
//        }
    }

    /**
     * ???????????????view???????????????
     */
    public void finishTrainViewStatus(){

    }

    /**
     * ?????????????????????
     */
    public void finishTrainSpeakTip(){
        Timber.d("finishTrainSpeakTip");
        ttsEngine.startSpeaking(RawProvider.one_item_finish_tip);
    }

    @Override
    public void prepareFinishData() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        trainBack.setTrainingEndDate((int) (date.getTime() / 1000));
        trainBack.setTotalTime(trainBack.getTrainingEndDate() - trainBack.getTrainingStartDate());
    }

    @Override
    public void finishTrain() {
        trainStatus = TrainStatus.FINISHING;

        prepareFinishData();

//        postDataViewPre("????????????", "???????????????????????????????????????");
//        postData();

        finishTrainViewStatus();

        //????????????finishTrainViewStatus?????????????????????????????????????????????????????????
        finishTrainSpeakTip();

        finishDeviceResetGlass();

        initStartStatus();
        tflistener.finishTrain(trainBack);
    }

    //??????????????????????????????????????????????????????reception??????
    @Override
    public void receiveFinishServerMessage(){
        trainStatus = TrainStatus.FINISHING;

        prepareFinishData();

        postDataViewPre(context.getString(R.string.loading), "???????????????????????????????????????");
        postData(false);

        finishTrainViewStatus();

        //????????????finishTrainViewStatus?????????????????????????????????????????????????????????
        finishTrainSpeakTip();

        finishDeviceResetGlass();
    }

    /**
     * ??????-???????????????????????????????????????
     */
    public void postUpdateReceptionStart(){
        Timber.d("postUpdateReceptionStart");

        //????????????????????????????????????????????????????????????dialog
        if(postUpdateReceptionStartCount==1){
            postDataViewPre(context.getString(R.string.loading),context.getString(R.string.update_the_status_of_the_training_please_later));
        }
        postUpdateReceptionStartCount++;

        ReceptionStatus receptionStatus = new ReceptionStatus();
        receptionStatus.setId(presTrainMessageContent.getReceptionId());
        receptionStatus.setReceptStep(presTrainMessageContent.getTp().getSchemeProcessNum());
        receptionStatus.setReceptDetailStep(presTrainMessageContent.getCurrentRepeatTime());
        if(presTrainMessageContent.getTp().getTrainItemType()== EnumTrainItem.REVERSAL){
            receptionStatus.setReceptDetailStepParam(((ReversalTrainPres)presTrainMessageContent.getTp()).getEyeType().getValue());
        }else{
            receptionStatus.setReceptDetailStepParam(0);
        }
        receptionStatus.setReceptDetailStepStatus(1);

//        ???????????????????????????10??????????????????
//        .flatMap(result -> {
//            if (postUpdateReceptionStartCount > 10) {
//                return Observable.just(result);
//            } else {
//                return Observable.error(new Exception());
//            }
//        })
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.updateReception(token, receptionStatus)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackUpdateReceptionStartBack(handlerUpdateReceptionStart, presTrainEmqttMessage));
    }

    /**
     * ??????-????????????
     */
    public void postUpdateReceptionFinish(){
        ReceptionStatus receptionStatus = new ReceptionStatus();
        receptionStatus.setId(presTrainMessageContent.getReceptionId());
        receptionStatus.setReceptStep(presTrainMessageContent.getTp().getSchemeProcessNum());
        receptionStatus.setReceptDetailStep(presTrainMessageContent.getCurrentRepeatTime());
        if(presTrainMessageContent.getTp().getTrainItemType()== EnumTrainItem.REVERSAL){
            receptionStatus.setReceptDetailStepParam(((ReversalTrainPres)presTrainMessageContent.getTp()).getEyeType().getValue());
        }else{
            receptionStatus.setReceptDetailStepParam(0);
        }
        receptionStatus.setReceptDetailStepStatus(2);

        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.updateReception(token, receptionStatus)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackUpdateReceptionFinishBack(handlerUpdateReceptionFinish));
    }

    /**
     * ???????????????????????????
     */
    public void postDataViewPre(String title,String content){
        tflistener.showPostDialog(title,content);
    }

    /**
     * ???????????????????????????
     */
    public void postDataViewAfter(){
        tflistener.hidePostDialog();
    }

    @Override
    public void postData() {
//        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
//        Call<TrainBackApiResult<Tb>> call = trainBackUseCase.createTrainBack(token, trainBack);
//        call.enqueue(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception){

    }

    /**
     * ???????????????????????????
     */
    protected final class CallBackPostTrainingBack extends Subscriber<TrainBackApiResult<Tb>> {
        private boolean isUpdateReception = true;

        public CallBackPostTrainingBack(){
            isUpdateReception = true;
        }

        public CallBackPostTrainingBack(boolean isUpdateReception){
            this.isUpdateReception = isUpdateReception;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.d("postdata:onFailure:??????????????????:%s",e.getMessage());

            //10s?????????
            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        postData();
                    });
        }

        @Override
        public void onNext(TrainBackApiResult<Tb> apiResult) {
            if (apiResult.getCode() == 0) {
                Timber.d("postdata:????????????");

                trainBack = apiResult.getData();

                if(isUpdateReception) {
                    postUpdateReceptionFinish();
                }else{
                    postDataViewAfter();

                    //??????????????????????????????
                    initStartStatus();
                }
            } else {
                Timber.d("postdata:apicode:????????????:%s",apiResult.getMessage());
                showMsg(context.getString(R.string.failed_to_submit_data)+apiResult.getMessage());
            }
        }
    }

    /**
     * ??????????????????????????????
     */
    public void postFinishSuccess(){
        postDataViewAfter();

        initStartStatus();
        tflistener.finishTrain();
    }

    /**
     * ????????????????????????
     */
    protected final class CallBackUpdateReceptionStartBack extends Subscriber<ModelApiResult<Reception>> {
        private Handler handler;
        private TrainEmqttMessage trainEmqttMessage;
        public CallBackUpdateReceptionStartBack(Handler handler,TrainEmqttMessage trainEmqttMessage){
            this.handler = handler;
            this.trainEmqttMessage = trainEmqttMessage;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Timber.d("??????-??????????????????:??????????????????");

            //10s?????????
            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        postUpdateReceptionStart();
                    });
        }

        @Override
        public void onNext(ModelApiResult<Reception> apiResult) {
            if (apiResult.getCode() == 0) {
                Timber.d("??????-????????????????????????");

                Message message = new Message();
                Bundle bundle = new Bundle();
                //bundle.putString("msg", trainEmqttMessage.toJson());
                bundle.putString("msg", "");
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                showMsg(context.getString(R.string.start_failed_to_update_reception_information));
            }
        }
    }

    /**
     * ????????????????????????
     */
    protected final class CallBackUpdateReceptionBack extends Subscriber<ModelApiResult<Reception>> {
        private Handler handler;
        private TrainEmqttMessage trainEmqttMessage;
        public CallBackUpdateReceptionBack(Handler handler,TrainEmqttMessage trainEmqttMessage){
            this.handler = handler;
            this.trainEmqttMessage = trainEmqttMessage;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.d("??????-??????????????????:??????????????????");
        }

        @Override
        public void onNext(ModelApiResult<Reception> apiResult) {
            if (apiResult.getCode() == 0) {
                Timber.d("??????-????????????????????????");

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("msg", trainEmqttMessage.toJson());
                message.setData(bundle);
                handler.sendMessage(message);

            } else {
                Timber.d("??????-????????????????????????");
            }
        }
    }

    /**
     * ????????????????????????
     */
    protected final class CallBackUpdateReceptionFinishBack extends Subscriber<ModelApiResult<Reception>> {
        private Handler handler;
        public CallBackUpdateReceptionFinishBack(Handler handler){
            this.handler = handler;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.d("??????????????????:??????????????????");
            //10s?????????
            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        postUpdateReceptionFinish();
                    });
        }

        @Override
        public void onNext(ModelApiResult<Reception> apiResult) {
            if (apiResult.getCode() == 0) {
                Timber.d("??????-????????????????????????");

                handler.sendEmptyMessage(0);
            } else {
                Timber.d("??????-????????????????????????");

                showMsg(context.getString(R.string.end_failed_to_update_reception_information));
            }
        }
    }

    @Override
    public void sendFinishMsg() {
//        TrainEmqttMessage trainEmqttMessage = new TrainEmqttMessage();
//        trainEmqttMessage.setStp(presTrainEmqttMessage.getRtp());
//        trainEmqttMessage.setSid(presTrainEmqttMessage.getRid());
//        trainEmqttMessage.setRtp(presTrainEmqttMessage.getStp());
//        trainEmqttMessage.setRid(presTrainEmqttMessage.getSid());
//
//        BackTrainMessageContent backTrainMessageContent = new BackTrainMessageContent();
//        backTrainMessageContent.setTb(trainBack);
//
//        TrainMessage trainMessage = new TrainMessage();
//        trainMessage.setTmt(TrainMessageType.FINISH);
//        trainMessage.setCt(backTrainMessageContent);
//        trainEmqttMessage.setMsg(trainMessage);
//        sendMessage(trainEmqttMessage);
    }

    @Override
    public void sendNormalMsg(){
//        TrainEmqttMessage trainEmqttMessage = new TrainEmqttMessage();
//        trainEmqttMessage.setStp(presTrainEmqttMessage.getRtp());
//        trainEmqttMessage.setSid(presTrainEmqttMessage.getRid());
//        trainEmqttMessage.setRtp(presTrainEmqttMessage.getStp());
//        trainEmqttMessage.setRid(presTrainEmqttMessage.getSid());
//
//        NormalTrainMessageContent normalTrainMessageContent = new NormalTrainMessageContent();
//        normalTrainMessageContent.setTn(trainNomal);
//
//        TrainMessage trainMessage = new TrainMessage();
//        trainMessage.setTmt(TrainMessageType.NORMAL);
//        trainMessage.setCt(normalTrainMessageContent);
//        trainEmqttMessage.setMsg(trainMessage);
//        sendMessage(trainEmqttMessage);
    }

    /**
     * ???????????????????????????
     */
    public void sendExecutedMsg(){
//        TrainEmqttMessage trainEmqttMessage = new TrainEmqttMessage();
//        trainEmqttMessage.setStp(nowExecuteMessage.getRtp());
//        trainEmqttMessage.setSid(nowExecuteMessage.getRid());
//        trainEmqttMessage.setRtp(nowExecuteMessage.getStp());
//        trainEmqttMessage.setRid(nowExecuteMessage.getSid());
//
//        ExecutedTrainMessageContent executedTrainMessageContent = new ExecutedTrainMessageContent();
//        executedTrainMessageContent.setPretmt(nowExecuteMessage.getMsg().getTmt());
//
//        TrainMessage trainMessage = new TrainMessage();
//        trainMessage.setTmt(TrainMessageType.EXECUTED);
//        trainMessage.setCt(executedTrainMessageContent);
//        trainEmqttMessage.setMsg(trainMessage);
//        sendMessage(trainEmqttMessage);
    }

    @Override
    public void pauseTrain() {
        if(trainStatus == TrainStatus.TRAINING) {
            trainStatus = TrainStatus.PAUSING;
            tflistener.changeToStatus(trainStatus);
        }
    }

    @Override
    public void resumeTrain() {
        if(trainStatus == TrainStatus.PAUSING) {
            trainStatus = TrainStatus.TRAINING;
            tflistener.changeToStatus(trainStatus);
        }
    }

    @Override
    public void pressEnter() {

    }

    @Override
    public void pressUp() {

    }

    @Override
    public void pressDown() {

    }

    public Handler handlerUpdateReceptionStart = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            postDataViewAfter();

            trainPres = (Tp)presTrainMessageContent.getTp();
            tflistener.setItemTitle(trainPres.getShowName());
            tflistener.setItemTip("");
            preparePresBack();

            devicePrepare();
        }
    };

    public Handler handlerUpdateReception = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String msgString = bundle.getString("msg");
            TrainEmqttMessage trainEmqttMessage = TrainEmqttMessage.convert(msgString);
            TrainMessage trainMessage = trainEmqttMessage.getMsg();
            if(trainMessage.getTmt()== TrainMessageType.START){
                postDataViewAfter();

                presTrainMessageContent = (PresTrainMessageContent)trainMessage.getCt();
                trainPres = (Tp)presTrainMessageContent.getTp();
                tflistener.setItemTitle(trainPres.getShowName());
                tflistener.setItemTip("");
                preparePresBack();

                devicePrepare();
            }else if(trainMessage.getTmt()== TrainMessageType.PAUSE){
                pauseTrain();
            }else if(trainMessage.getTmt()== TrainMessageType.RESUME){
                resumeTrain();
            }
            sendExecutedMsg();
        }
    };

    public Handler handlerUpdateReceptionFinish = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            postFinishSuccess();
        }
    };

    public void receiveOptMsg(TrainEmqttMessage trainEmqttMessage) {
        TrainMessage trainMessage = trainEmqttMessage.getMsg();
        if(trainMessage.getTmt()== TrainMessageType.START){
            Timber.d("???????????????????????????");

            nowExecuteMessage = trainEmqttMessage;
            presTrainEmqttMessage = trainEmqttMessage;

            receivePresContent((PresTrainMessageContent)trainMessage.getCt());

//            showMsg("???????????????????????????");
//            presTrainMessageContent = (PresTrainMessageContent)trainMessage.getCt();
//            trainPres = (Tp)presTrainMessageContent.getTp();
//            tflistener.setItemTitle(trainPres.getShowName());
//            preparePresBack();
//
//            devicePrepare();
        }else if(trainMessage.getTmt()== TrainMessageType.PAUSE){
            if(trainStatus==TrainStatus.TRAINING) {
                Timber.d("???????????????????????????");

                nowExecuteMessage = trainEmqttMessage;

                ReceptionStatus receptionStatus = new ReceptionStatus();
                receptionStatus.setId(presTrainMessageContent.getReceptionId());
                receptionStatus.setReceptStep(presTrainMessageContent.getTp().getSchemeProcessNum());
                receptionStatus.setReceptDetailStep(presTrainMessageContent.getCurrentRepeatTime());
                if(presTrainMessageContent.getTp().getTrainItemType()== EnumTrainItem.REVERSAL){
                    receptionStatus.setReceptDetailStepParam(((ReversalTrainPres)presTrainMessageContent.getTp()).getEyeType().getValue());
                }else{
                    receptionStatus.setReceptDetailStepParam(0);
                }
                receptionStatus.setReceptDetailStepStatus(3);

                String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                trainBackUseCase.updateReception(token, receptionStatus)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CallBackUpdateReceptionBack(handlerUpdateReception, trainEmqttMessage));

//                pauseTrain();
            } else {
                Timber.d("??????????????????????????????????????????????????????");
            }
        }else if(trainMessage.getTmt()== TrainMessageType.RESUME){
            if(trainStatus==TrainStatus.PAUSING){
                Timber.d("??????????????????????????????");

                nowExecuteMessage = trainEmqttMessage;

                ReceptionStatus receptionStatus = new ReceptionStatus();
                receptionStatus.setId(presTrainMessageContent.getReceptionId());
                receptionStatus.setReceptStep(presTrainMessageContent.getTp().getSchemeProcessNum());
                receptionStatus.setReceptDetailStep(presTrainMessageContent.getCurrentRepeatTime());
                if(presTrainMessageContent.getTp().getTrainItemType()== EnumTrainItem.REVERSAL){
                    receptionStatus.setReceptDetailStepParam(((ReversalTrainPres)presTrainMessageContent.getTp()).getEyeType().getValue());
                }else{
                    receptionStatus.setReceptDetailStepParam(0);
                }
                receptionStatus.setReceptDetailStepStatus(1);

                String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
                trainBackUseCase.updateReception(token, receptionStatus)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CallBackUpdateReceptionBack(handlerUpdateReception, trainEmqttMessage));

//                resumeTrain();
            } else {
                Timber.d("??????????????????????????????????????????????????????");
            }
        }else if(trainMessage.getTmt()==TrainMessageType.STOP){
            //???????????????????????????????????????
            if(trainStatus!=TrainStatus.FINISHING){
                nowExecuteMessage = trainEmqttMessage;

                finishTrain();
            }
        }
    }

    @Override
    public void receiveMessage(EmqttMessage emqttMessage) {
        if(emqttMessage instanceof TrainEmqttMessage) {
            lastTrainEmqttMessage = (TrainEmqttMessage)emqttMessage;
            receiveOptMsg(lastTrainEmqttMessage);
        }
    }

    @Override
    public void sendMessage(EmqttMessage emqttMessage) {
        tflistener.sendMessage(emqttMessage);
    }

    protected void showMsg(String strmsg){
        //ttsEngine.startSpeaking(strmsg);

        Message msg = new Message();
        Bundle b = new Bundle();// ????????????
        b.putString("msg", strmsg);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String msgString = bundle.getString("msg");
            realShowMsg(msgString);
        }
    };

    private void realShowMsg(String msg) {
        ToastUtil.showShortToast(context, msg);
    }

    protected void devicePrepare(){
        Timber.d("devicePrepare");
        trainStatus = TrainStatus.PREPARING;
        tflistener.prepare();
        ttsEngine.startSpeaking(RawProvider.device_prepare);
        ControlMessageSet messageSet = TrainUseCase.create(trainPres);
        //messageSet.setHandler(handlerDevicePrepareFinish);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .flatMap(result -> ttsEngine.startSpeakingSubject(RawProvider.device_prepare_finish))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    startTrain(trainPres);
                });
    }

    //    protected Handler handlerDevicePrepareFinish = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            ttsEngine.startSpeaking(R.raw.device_prepare_finish,handlerFinishVoiceOver);
//        }
//    };
//
//    protected Handler handlerFinishVoiceOver = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            startTrain(presTrainMessageContent);
//        }
//    };
    public void selfhelpReceivePresContent(PresTrainMessageContent presTrainMessageContent){

    }

    public void receivePresContent(PresTrainMessageContent presTrainMessageContent){
        this.presTrainMessageContent = presTrainMessageContent;

        //??????????????????0
        postUpdateReceptionStartCount = 0;
        postUpdateReceptionStart();
    }

    public void receivePresContent(Tp trainPres){
        this.trainPres = trainPres;

        tflistener.setItemTitle(trainPres.getShowName());
        tflistener.setItemTip("");
        preparePresBack();

        devicePrepare();
    }

    /**
     * ????????????????????????
     * @return
     */
    public EnumUserTrainMode getUserTrainMode(){
        EnumUserTrainMode userTrainMode = EnumUserTrainMode.SELFHELP;
        String userTrainModeValue = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_USER_TRAIN_MODE);
        if(!userTrainModeValue.equals("")){
            userTrainMode = EnumUserTrainMode.valueOf(userTrainModeValue);
        }
        return userTrainMode;
    }

    /**
     * ????????????????????????
     */
    protected void finishDeviceResetGlass() {
        ControlMessageSet messageSet = TrainUseCase.resetGlass();

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                });
    }
}
