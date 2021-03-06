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
         * ????????????
         * @param trainPres
         */
        void startTrain(TrainPres trainPres);

        /**
         * ????????????????????????
         * @param trainPres
         */
        void startTrainSkipPrepare(TrainPres trainPres);

        /**
         * ??????????????????
         */
        void finishAllTrain();

        /**
         * ??????dialog
         * @param title
         * @param content
         */
        void showPostDialog(String title, String content);

        /**
         * ??????dialog
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
     * ????????????????????????
     * @return
     */
    public TrainPres getNowTrainPres(){
        return trainPres;
    }


    /**
     * ???????????????????????????
     * @param trainBack
     * @param isUpdateNowTrainPres ?????????????????????
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
     * ????????????????????????
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
     * ????????????????????????
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
            //?????????????????????????????????????????????????????????????????????
            trainPres = getNextTrainPres(trainBack,true);

            viewListener.startTrainSkipPrepare(trainPres);
        }else{
            trainPres = getNextTrainPres(trainBack,true);

            //?????????????????????????????????????????????????????????
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
//                //???????????????????????????????????????
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
////                    //??????????????????????????????????????????????????????????????????
////                    //??????????????????????????????????????????????????????????????????
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
     * ???????????????????????????
     */
    public void postDataViewPre(String title,String content){
        viewListener.showPostDialog(title,content);
    }

    /**
     * ???????????????????????????
     */
    public void postDataViewAfter(){
        viewListener.hidePostDialog();
    }

    /**
     * ??????????????????????????????
     */
    public void postFinishSuccess(){
        postDataViewAfter();

        viewListener.finishAllTrain();
    }



    //??????????????????
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
            Timber.d("postdata:onFailure:??????????????????:%s",e.getMessage());
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
