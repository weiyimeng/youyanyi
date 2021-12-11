package com.uyu.device.devicetraining.presentation.presenter.trial;

import android.content.Context;

import com.google.gson.Gson;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.ApiResult;
import com.uyu.device.devicetraining.data.entity.config.trial.ReversalConfig;
import com.uyu.device.devicetraining.data.entity.config.trial.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.other.ReceptionTrial;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;
import com.uyu.device.devicetraining.data.entity.trainpres.FracturedRulerTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGFixedVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGVariableVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;

import org.json.JSONObject;

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
public class TrialTrainManagerPresenter {
    private final TrainBackUseCase trainBackUseCase;
    private final Context context;

    public interface TrainManagerPresenterListener{
        /**
         * 开始训练
         * @param trainPres
         */
        void startTrain(TrainPres trainPres);

        /**
         * 开始训练跳过准备
         * @param trainPres
         */
        void startTrainSkipPrepare(TrainPres trainPres);

        /**
         * 所有训练结束
         */
        void finishAllTrain();

        /**
         * 显示dialog
         * @param title
         * @param content
         */
        void showPostDialog(String title, String content);

        /**
         * 隐藏dialog
         */
        void hidePostDialog();
    }

    private TrainManagerPresenterListener viewListener;

    public void setViewListener(TrainManagerPresenterListener viewListener) {
        this.viewListener = viewListener;
    }

    private EnumTrainItem trainItemNow = EnumTrainItem.REVERSAL;
    private EnumEyeType eyeType = EnumEyeType.LEFT;
    private EnumFusionTrain fusionTrain = EnumFusionTrain.BI;
    private TrainPres trainPres;

    @Inject
    public TrialTrainManagerPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , Context context) {
        this.trainBackUseCase = trainBackUseCase;
        this.context = context;
    }


    /**
     * 获取当前训练处方
     * @return
     */
    public TrainPres getNowTrainPres(){
        return trainPres;
    }


    /**
     * 获取下一个训练处方
     * @param trainBack
     * @param isUpdateNowTrainPres 是否更新当前的
     * @return
     */
    public TrainPres getNextTrainPres(TrainBack trainBack,boolean isUpdateNowTrainPres){
        TrainPres nextTrainPres = null;
        if(trainItemNow== EnumTrainItem.R_G_VARIABLE_VECTOR){
            nextTrainPres = null;
        }else{
            EnumEyeType eyeTypeTmp = eyeType;
            EnumFusionTrain fusionTrainTmp = fusionTrain;
            EnumTrainItem trainItemNowTmp = trainItemNow;

            if(trainItemNow==EnumTrainItem.REVERSAL){
                if(eyeType==EnumEyeType.LEFT){
                    eyeTypeTmp = EnumEyeType.RIGHT;
                }else if(eyeType==EnumEyeType.RIGHT){
                    eyeTypeTmp = EnumEyeType.DOUBLE;
                }else if(eyeType==EnumEyeType.DOUBLE){
                    fusionTrainTmp = EnumFusionTrain.BI;
                    trainItemNowTmp = EnumTrainItem.STEREOSCOPE;
                }
            }else{
                if(trainItemNow==EnumTrainItem.STEREOSCOPE){
                    trainItemNowTmp = EnumTrainItem.R_G_VARIABLE_VECTOR;
                    fusionTrainTmp = EnumFusionTrain.BO;
                }
            }
            nextTrainPres = createTrainPres(eyeTypeTmp,fusionTrainTmp,trainItemNowTmp);

            if(isUpdateNowTrainPres) {
                eyeType = eyeTypeTmp;
                fusionTrain = fusionTrainTmp;
                trainItemNow = trainItemNowTmp;
            }
        }
        return nextTrainPres;
    }

    /**
     * 第一次初始化处方
     */
    public void firstInitPres(){
        eyeType = EnumEyeType.LEFT;
        fusionTrain = EnumFusionTrain.BO;
        trainItemNow = EnumTrainItem.REVERSAL;
        trainPres = createTrainPres(eyeType,fusionTrain,trainItemNow);
    }

    public void startNowTrain(){
        viewListener.startTrain(trainPres);
    }

    public TrainPres createTrainPres(EnumEyeType eyeType,EnumFusionTrain fusionTrain,EnumTrainItem trainItemNow){
        TrainPres trainPres = null;
        switch (trainItemNow){
            case STEREOSCOPE:
                StereoscopeTrainPres stereoscopeTrainPres = new StereoscopeTrainPres();
                stereoscopeTrainPres.setTrainingType(fusionTrain);
                stereoscopeTrainPres.setScreenLocation(StereoscopeConfig.DefaultScreenLocation);
                trainPres = stereoscopeTrainPres;
                break;
            case FRACTURED_RULER:
                FracturedRulerTrainPres fracturedRulerTrainPres = new FracturedRulerTrainPres();
                fracturedRulerTrainPres.setTrainingType(fusionTrain);
                trainPres = fracturedRulerTrainPres;
                break;
            case REVERSAL:
                ReversalTrainPres reversalTrainPres = new ReversalTrainPres();
                reversalTrainPres.setId(0);
                reversalTrainPres.setLNegativeDegreeLevel(0);
                reversalTrainPres.setLPositiveDegreeLevel(0);
                reversalTrainPres.setRNegativeDegreeLevel(0);
                reversalTrainPres.setRPositiveDegreeLevel(0);
                reversalTrainPres.setNegativeLetterSize(ReversalConfig.DefaultLetterSize);
                reversalTrainPres.setNegativeLetterSizeRight(ReversalConfig.DefaultLetterSize);
                reversalTrainPres.setPositiveLetterSize(ReversalConfig.DefaultLetterSize);
                reversalTrainPres.setPositiveLetterSizeRight(ReversalConfig.DefaultLetterSize);
                reversalTrainPres.setTrainingContentType(EnumContentType.LETTER);
                reversalTrainPres.setTrainingContentCategoryId(0);
                reversalTrainPres.setTrainingContentArticleId(0);
                reversalTrainPres.setEyeType(eyeType);
                trainPres = reversalTrainPres;
                break;
            case R_G_VARIABLE_VECTOR:
                RGVariableVectorTrainPres rgVariableVectorTrainPres = new RGVariableVectorTrainPres();
                rgVariableVectorTrainPres.setTrainingType(fusionTrain);
                trainPres = rgVariableVectorTrainPres;
                break;
            case R_G_FIXED_VECTOR:
                RGFixedVectorTrainPres rgFixedVectorTrainPres = new RGFixedVectorTrainPres();
                rgFixedVectorTrainPres.setTrainingType(fusionTrain);
                trainPres = rgFixedVectorTrainPres;
                break;
            default:
                break;
        }
        return trainPres;
    }

    /**
     * 所有训练全部结束
     */
    public void allTrainFinish(){
        Gson gson = new Gson();
        String result = gson.toJson(trainBacks);
        receptionTrial.setResult(result);

        finishTrainPostData();
    }

    public void finishTrain(TrainBack trainBack){
        trainBacks.add(trainBack);

        if(trainPres.getTrainItemType()==EnumTrainItem.REVERSAL && ((ReversalTrainPres)trainPres).getEyeType() != EnumEyeType.DOUBLE){
            //如果是翻转拍左眼或右眼，直接跳过不显示准备页面
            trainPres = getNextTrainPres(trainBack,true);

            viewListener.startTrainSkipPrepare(trainPres);
        }else{
            trainPres = getNextTrainPres(trainBack,true);

            //下一个处方存在继续，不存在直接结束训练
            if(trainPres!=null) {
                startNowTrain();
            }else{
                allTrainFinish();
            }
        }
    }

//    public void finishTrain(TrainBack trainBack){
//        if(trainItemNow==EnumTrainItem.REVERSAL && eyeType==EnumEyeType.DOUBLE){
//            trainBacks.add(trainBack);
//
//            Gson gson = new Gson();
//            String result = gson.toJson(trainBacks);
//            receptionTrial.setResult(result);
//
//            finishTrainPostData();
//        }else{
//            if(trainItemNow==EnumTrainItem.REVERSAL){
//                trainBacks.add(trainBack);
//                if(eyeType==EnumEyeType.LEFT){
//                    eyeType = EnumEyeType.RIGHT;
//                }else if(eyeType==EnumEyeType.RIGHT){
//                    fusionTrain = EnumFusionTrain.BI;
//                    trainItemNow = EnumTrainItem.STEREOSCOPE;
//                }
//            }else{
//                //改融合项目是否找到可以结束
//                boolean isFusionEnd = false;
//                if(trainItemNow==EnumTrainItem.STEREOSCOPE){
//                    TrialStereoscopeTrainBack stereoscopeTrainBack = (TrialStereoscopeTrainBack)trainBack;
//                    if(stereoscopeTrainBack.getResultDifficulty()== StereoscopeConfig.Max_Level){
//                        trainItemNow = EnumTrainItem.FRACTURED_RULER;
//                    }else if(stereoscopeTrainBack.getResultDifficulty()<=0){
//                        trainItemNow = EnumTrainItem.R_G_VARIABLE_VECTOR;
//                    }else{
//                        isFusionEnd = true;
//                    }
//                }else if(trainItemNow==EnumTrainItem.FRACTURED_RULER){
//                    isFusionEnd = true;
//                }else if(trainItemNow==EnumTrainItem.R_G_VARIABLE_VECTOR){
//                    TrialRGVariableVectorTrainBack rgVariableVectorTrainBack = (TrialRGVariableVectorTrainBack)trainBack;
//                    if(rgVariableVectorTrainBack.getCriticalLocation()>= (RGVariableVectorConfig.Max_Distance*2)){
//                        trainItemNow = EnumTrainItem.R_G_FIXED_VECTOR;
//                    }else{
//                        isFusionEnd = true;
//                    }
//                }else if(trainItemNow==EnumTrainItem.R_G_FIXED_VECTOR){
//                    isFusionEnd = true;
//                }
//
//                if(isFusionEnd) {
////                    //裂隙尺，红绿固定取平均分，另外一个已经通关了
////                    //立体镜、红绿可变，不变，红绿可变是立体镜没变
////                    if(trainItemNow==EnumTrainItem.FRACTURED_RULER) {
////                        int itemSocre = ((TrialFracturedRulerTrainBack) trainBack).getScore();
////                        int realScore = (itemSocre+100)/2;
////                        ((TrialFracturedRulerTrainBack) trainBack).setScore(realScore);
////                    }else if(trainItemNow==EnumTrainItem.R_G_FIXED_VECTOR){
////                        int itemSocre = ((TrialRGFixedVectorTrainBack) trainBack).getScore();
////                        int realScore = (itemSocre+100)/2;
////                        ((TrialRGFixedVectorTrainBack) trainBack).setScore(realScore);
////                    }
//                    trainBacks.add(trainBack);
//
//                    if (fusionTrain == EnumFusionTrain.BI) {
//                        trainItemNow = EnumTrainItem.STEREOSCOPE;
//                        fusionTrain = EnumFusionTrain.BO;
//                    }else if (fusionTrain == EnumFusionTrain.BO) {
//                        trainItemNow = EnumTrainItem.REVERSAL;
//                        eyeType = EnumEyeType.DOUBLE;
//                    }
//                }
//            }
//        }
//        startNowTrain();
//    }

    /**
     * 提交数据之前的界面
     */
    public void postDataViewPre(String title,String content){
        viewListener.showPostDialog(title,content);
    }

    /**
     * 提交数据之后的界面
     */
    public void postDataViewAfter(){
        viewListener.hidePostDialog();
    }

    /**
     * 提交结果更新保存成功
     */
    public void postFinishSuccess(){
        postDataViewAfter();

        viewListener.finishAllTrain();
    }



    //体验训练模型
    private ReceptionTrial receptionTrial;

    private List<TrainBack> trainBacks = new ArrayList<>();

    public ReceptionTrial getReceptionTrial() {
        return receptionTrial;
    }

    public void setReceptionTrial(ReceptionTrial receptionTrial) {
        this.receptionTrial = receptionTrial;
    }

    public void resetData(){
        receptionTrial = null;
        trainBacks = new ArrayList<>();

        trainItemNow = EnumTrainItem.REVERSAL;
//        trainItemNow = EnumTrainItem.R_G_FIXED_VECTOR;
        eyeType = EnumEyeType.LEFT;
        fusionTrain = EnumFusionTrain.BI;
        trainPres = null;
    }

    public void finishTrainPostData(){
        postDataViewPre(context.getString(R.string.loading),context.getString(R.string.submit_data_please_wait));

        postData();
    }

    public void postData(){
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        int receptionTrialId = receptionTrial.getId();
        trainBackUseCase.exitTrainTrail(token, receptionTrialId, receptionTrial)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostData());
    }

    protected final class CallBackPostData extends Subscriber<ApiResult> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.d("postdata:onFailure:提交异常失败:%s",e.getMessage());
            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        postData();
                    });
        }

        @Override
        public void onNext(ApiResult apiResult) {
            if (apiResult.getCode() == 0) {
                ToastUtil.showLongToast(context, context.getString(R.string.self_help_experience_success));
            } else {
                ToastUtil.showLongToast(context, apiResult.getMessage());
            }
            postDataSuccess();
        }
    }

    public void postDataSuccess(){
        postDataViewAfter();

        if(viewListener!=null){
            viewListener.finishAllTrain();
        }
    }
}
