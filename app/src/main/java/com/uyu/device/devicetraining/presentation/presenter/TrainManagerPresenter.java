package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;
import android.content.DialogInterface;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.ApiResult;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.other.Reception;
import com.uyu.device.devicetraining.data.entity.other.ReceptionStatus;
import com.uyu.device.devicetraining.data.entity.other.Reception_Table;
import com.uyu.device.devicetraining.data.entity.selfhelp.SelfhelpCreateTrainContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.SelfhelpFinishTrainContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.TrainPresScheme;
import com.uyu.device.devicetraining.data.entity.trainback.ApproachTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.FollowTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.FracturedRulerTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.GlanceTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RGFixedVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RGVariableVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RedGreenReadTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.ReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.StereoscopeTrainBack;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.net.api.ApiService;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.net.api.ServiceFactory;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;

import org.json.JSONObject;

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
public class TrainManagerPresenter {
    private final TrainBackUseCase trainBackUseCase;
    private final Context context;

    public interface TrainManagerPresenterListener{
        /**
         * ????????????????????????
         * @param trainPresScheme
         */
        void getTrainPresSchemeBack(TrainPresScheme trainPresScheme,boolean isSwitchTrainStatus);

        /**
         * ????????????
         * @param presTrainMessageContent
         */
        void startTrain(PresTrainMessageContent presTrainMessageContent);

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

        /**
         * ??????????????????dialog
         * @param retryClickListener
         */
        void showNetworkExceptionDialog(DialogInterface.OnClickListener retryClickListener);
    }

    private TrainManagerPresenterListener viewListener;

    public void setViewListener(TrainManagerPresenterListener viewListener) {
        this.viewListener = viewListener;
    }

    private SelfhelpCreateTrainContent selfhelpCreateTrainContent;
    private TrainPresScheme trainPresScheme;
    private Reception reception;
    private ReceptionStatus receptionStatus;

    public ReceptionStatus getReceptionStatus() {
        return receptionStatus;
    }

    @Inject
    public TrainManagerPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , Context context) {
        this.trainBackUseCase = trainBackUseCase;
        this.context = context;
    }

    /**
     * ???????????????????????????
     * @param selfhelpCreateTrainContent
     */
    public void startWithSelfhelpCreateTrainMessage(SelfhelpCreateTrainContent selfhelpCreateTrainContent){
        this.selfhelpCreateTrainContent = selfhelpCreateTrainContent;
        reception = this.selfhelpCreateTrainContent.getReception();

        //?????????????????????????????????????????????????????????????????????
        reception.save();

        initReceptionStatus();
        getUserTrainPresScheme(false);
    }

    /**
     * ???????????????
     * @param reception
     */
    public void startWithReception(Reception reception){
        Reception localReception = new SQLite().select()
                .from(Reception.class)
                .where(Reception_Table.id.eq(reception.getId()))
                .querySingle();
        if(localReception!=null){
            this.reception = localReception;
        }else{
            this.reception = reception;
            this.reception.save();
        }

        //????????????????????????????????????????????????????????????????????????
        if(localReception.getReceptStep()==-1){
            finishPostData();
        }else{
            initReceptionStatus();
            getUserTrainPresScheme(true);
        }
    }

    /**
     * ???????????????????????????????????????
     * @param isResume
     */
    public void getUserTrainPresScheme(final boolean isResume){
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        ApiService apiService = ServiceFactory.create(ServiceFactory.TYPE_STRING);
        Observable<String> trainPresSchemeService = apiService.getUserTrainPresScheme(reception.getUserId(),token);
        trainPresSchemeService.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    getUserTrainPresScheme(isResume);
                                });
                    }

                    @Override
                    public void onNext(String apiResult) {
                        try {
                            Timber.d("scheme:%s",apiResult);
                            JSONObject jsonObject = new JSONObject(apiResult);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                trainPresScheme = TrainPresScheme.convert(data);
                                if (isResume) {
                                    getUserTrainPresSchemeSuccess(false);
                                    startNowTrain();
                                } else {
                                    getUserTrainPresSchemeSuccess(true);
                                }
                            } else {
                                getUserTrainPresScheme(isResume);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            getUserTrainPresScheme(isResume);
                        }
                    }
                });
    }

    private void initReceptionStatus(){
        receptionStatus = new ReceptionStatus();
        receptionStatus.setId(reception.getId());
        receptionStatus.setReceptStep(reception.getReceptStep());
        receptionStatus.setReceptDetailStep(1);
        receptionStatus.setReceptDetailStepParam(reception.getReceptDetailStepParam());
        receptionStatus.setReceptDetailStepStatus(reception.getReceptDetailStepStatus());

        //??????????????????reception???????????????????????????
        SharePreferenceTool.setSharePreferenceValueInt(context, SharePreferenceTool.PREF_NOW_TRAIN_USER_ID,reception.getUserId());
    }

    public void getUserTrainPresSchemeSuccess(boolean isSwitchTrainStatus){
        viewListener.getTrainPresSchemeBack(trainPresScheme,isSwitchTrainStatus);
    }

    public void startNowTrain(){
        ReceptionStatus nextReceptionStatus = receptionStatus;
        //?????????????????????????????????
        if(nextReceptionStatus!=null) {
            PresTrainMessageContent presTrainMessageContent = new PresTrainMessageContent();
            presTrainMessageContent.setUserId(reception.getUserId());
            presTrainMessageContent.setOptometristId(reception.getOptometristId());
            presTrainMessageContent.setReceptionId(reception.getId());
            presTrainMessageContent.setVisionTrainingFeedbackId(reception.getResultId());
            presTrainMessageContent.setCurrentRepeatTime(nextReceptionStatus.getReceptDetailStep());
            TrainPres nextTrainPres = trainPresScheme.getTrainingPresList().get(nextReceptionStatus.getReceptStep());
            if(nextTrainPres instanceof ReversalTrainPres
                    && ((ReversalTrainPres)nextTrainPres).getEyeType() != EnumEyeType.DOUBLE){
                ((ReversalTrainPres)nextTrainPres).setEyeType(EnumEyeType.values()[nextReceptionStatus.getReceptDetailStepParam()]);
            }
            presTrainMessageContent.setTp(nextTrainPres);
            viewListener.startTrain(presTrainMessageContent);
        }else{
            viewListener.finishAllTrain();
        }
    }

    public void startNextTrain(){
        ReceptionStatus nextReceptionStatus = trainPresScheme.getNextTrainPres(receptionStatus);
        //?????????????????????????????????
        if(nextReceptionStatus!=null) {
            PresTrainMessageContent presTrainMessageContent = new PresTrainMessageContent();
            presTrainMessageContent.setUserId(reception.getUserId());
            presTrainMessageContent.setOptometristId(reception.getOptometristId());
            presTrainMessageContent.setReceptionId(reception.getId());
            presTrainMessageContent.setVisionTrainingFeedbackId(reception.getResultId());
            presTrainMessageContent.setCurrentRepeatTime(nextReceptionStatus.getReceptDetailStep());
            TrainPres nextTrainPres = trainPresScheme.getTrainingPresList().get(nextReceptionStatus.getReceptStep());
            if(nextTrainPres.getTrainItemType()== EnumTrainItem.REVERSAL && ((ReversalTrainPres)nextTrainPres).getEyeType()== EnumEyeType.LEFT){
                ((ReversalTrainPres)nextTrainPres).setEyeType(EnumEyeType.values()[nextReceptionStatus.getReceptDetailStepParam()]);
            }
            presTrainMessageContent.setTp(nextTrainPres);
            receptionStatus = nextReceptionStatus;
            viewListener.startTrain(presTrainMessageContent);
        }else{
            finishTrain();
        }
    }

    /**
     * ????????????????????????
     * @return
     */
    public TrainPres getNowTrainPres(){
        TrainPres nextTrainPres = null;
        ReceptionStatus nextReceptionStatus = receptionStatus;
        //?????????????????????????????????
        if(nextReceptionStatus!=null) {
            nextTrainPres = trainPresScheme.getTrainingPresList().get(nextReceptionStatus.getReceptStep());
            if(nextTrainPres instanceof ReversalTrainPres){
                ((ReversalTrainPres)nextTrainPres).setEyeType(EnumEyeType.values()[nextReceptionStatus.getReceptDetailStepParam()]);
            }
        }
        return nextTrainPres;
    }

    /**
     * ???????????????????????????
     * @return
     */
    public TrainPres getNextTrainPres(){
        TrainPres nextTrainPres = null;
        ReceptionStatus nextReceptionStatus = trainPresScheme.getNextTrainPres(receptionStatus);
        //?????????????????????????????????
        if(nextReceptionStatus!=null) {
            nextTrainPres = trainPresScheme.getTrainingPresList().get(nextReceptionStatus.getReceptStep());
            if(nextTrainPres.getTrainItemType()== EnumTrainItem.REVERSAL && ((ReversalTrainPres)nextTrainPres).getEyeType()== EnumEyeType.LEFT){
                ((ReversalTrainPres)nextTrainPres).setEyeType(EnumEyeType.values()[nextReceptionStatus.getReceptDetailStepParam()]);
            }
        }
        return nextTrainPres;
    }

    /**
     * ????????????
     */
    public void finishTrain(){
        reception.setReceptStep(-1);
        reception.save();

        finishPostData();
    }

    /**
     * ??????????????????
     */
    public void finishItemTrain(){
        ReceptionStatus nextReceptionStatus = trainPresScheme.getNextItemTrainPres(receptionStatus);
        //?????????????????????????????????
        if(nextReceptionStatus!=null) {
            PresTrainMessageContent presTrainMessageContent = new PresTrainMessageContent();
            presTrainMessageContent.setUserId(reception.getUserId());
            presTrainMessageContent.setOptometristId(reception.getOptometristId());
            presTrainMessageContent.setReceptionId(reception.getId());
            presTrainMessageContent.setVisionTrainingFeedbackId(reception.getResultId());
            presTrainMessageContent.setCurrentRepeatTime(nextReceptionStatus.getReceptDetailStep());
            TrainPres nextTrainPres = trainPresScheme.getTrainingPresList().get(nextReceptionStatus.getReceptStep());
            if(nextTrainPres.getTrainItemType()== EnumTrainItem.REVERSAL && ((ReversalTrainPres)nextTrainPres).getEyeType()== EnumEyeType.LEFT){
                ((ReversalTrainPres)nextTrainPres).setEyeType(EnumEyeType.values()[nextReceptionStatus.getReceptDetailStepParam()]);
            }
            presTrainMessageContent.setTp(nextTrainPres);
            receptionStatus = nextReceptionStatus;
            viewListener.startTrain(presTrainMessageContent);
        }else{
            finishTrain();
        }
    }

    /**
     * ????????????????????????????????????????????????
     * null??????????????????trainPresScheme???????????????null???
     * @return
     */
    public Boolean itemTrainFinish() {
        Boolean isItemAllEnd = null;
        if(trainPresScheme!=null) {
            ReceptionStatus nextReceptionStatus = trainPresScheme.getNextTrainPres(receptionStatus);
            if (nextReceptionStatus != null && nextReceptionStatus.getReceptStep() == receptionStatus.getReceptStep()) {
                isItemAllEnd = false;
            }else{
                isItemAllEnd = true;
            }
        }else{
            isItemAllEnd = null;
        }
        return isItemAllEnd;
    }

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
     * ???????????????????????????
     */
    public void postFinishTrainData() {
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        int feedbackId = reception.getResultId();
        trainBackUseCase.finishAllTrain(token, feedbackId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostFinishTrain());
    }

    /**
     * ???????????????????????????
     */
    protected final class CallBackPostFinishTrain extends Subscriber<ApiResult> {
        @Override
        public void onCompleted() {
            postDataViewAfter();
        }

        @Override
        public void onError(Throwable e) {
            //onError??????onCompleted
            postDataViewAfter();


            viewListener.showNetworkExceptionDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    postFinishTrainData();
                }
            });
        }

        @Override
        public void onNext(ApiResult apiResult) {
            if (apiResult.getCode() == 0) {
                Timber.d("postdata:????????????");

                postFinishSuccess();
            } else {
                viewListener.showNetworkExceptionDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postFinishTrainData();
                    }
                });
            }
        }

        @Override
        public void onStart() {
            postDataViewPre(context.getString(R.string.loading),context.getString(R.string.update_the_status_of_the_training_please_later));
        }
    }

    /**
     * ??????????????????????????????
     */
    public void postFinishSuccess(){
        viewListener.finishAllTrain();
    }

    /**
     * ??????????????????????????????????????????
     */
    public void finishAllReception(){
        selfhelpCreateTrainContent=null;
        trainPresScheme=null;
        reception=null;
        receptionStatus=null;
    }

    /**
     * ??????????????????????????????
     * @param selfhelpFinishTrainContent
     * @return
     */
    public boolean isNowTrainReception(SelfhelpFinishTrainContent selfhelpFinishTrainContent){
        boolean isRight = false;
        if(reception!=null
                && reception.getId()!=null
                && reception.getId()!=0
                && selfhelpFinishTrainContent!=null
                && selfhelpFinishTrainContent.getReception()!=null
                && selfhelpFinishTrainContent.getReception().getId()!=null
                && selfhelpFinishTrainContent.getReception().getId()!=0
                && reception.getId().intValue()==selfhelpFinishTrainContent.getReception().getId().intValue()){
            //???intvalue??????????????????????????????????????????????????????????????????????????????
            isRight = true;
        }
        return isRight;
    }

    /**
     * ????????????????????????
     * @param receptionId
     * @return
     */
    public boolean isNowTrainReception(int receptionId){
        boolean isRight = false;
        if(reception!=null
                && reception.getId()!=null
                && reception.getId()!=0
                && reception.getId().intValue()==receptionId){
            //???intvalue??????????????????????????????????????????????????????????????????????????????
            isRight = true;
        }
        return isRight;
    }

    /**
     * ??????????????????????????????????????????
     */
    private boolean isOnPostAllLocalBacks = false;
    /**
     * ?????????????????????????????????
     */
    private int sum = 0;
    /**
     * ???????????????????????????
     */
    private int nowFinishCount = 0;
    /**
     * ???????????????
     */
    private int successCount = 0;

    /**
     * ??????????????????
     */
    public void finishPostData(){
        isOnPostAllLocalBacks = false;
        postAllLocalBacks();
    }
    /**
     * ???????????????????????????????????????
     */
    public void postAllLocalBacks(){
        if(!isOnPostAllLocalBacks) {
            postDataViewPre("????????????","????????????????????????????????????");

            isOnPostAllLocalBacks = true;
            sum = 0;
            nowFinishCount = 0;
            successCount = 0;

            String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);

            List<StereoscopeTrainBack> listStereoscopeTrainBack = SQLite.select().from(StereoscopeTrainBack.class).queryList();
            List<FracturedRulerTrainBack> listFracturedRulerTrainBack = SQLite.select().from(FracturedRulerTrainBack.class).queryList();
            List<ReversalTrainBack> listReversalTrainBack = SQLite.select().from(ReversalTrainBack.class).queryList();
            List<RedGreenReadTrainBack> listRedGreenReadTrainBack = SQLite.select().from(RedGreenReadTrainBack.class).queryList();
            List<ApproachTrainBack> listApproachTrainBack = SQLite.select().from(ApproachTrainBack.class).queryList();
            List<RGVariableVectorTrainBack> listRGVariableVectorTrainBack = SQLite.select().from(RGVariableVectorTrainBack.class).queryList();
            List<RGFixedVectorTrainBack> listRGFixedVectorTrainBack = SQLite.select().from(RGFixedVectorTrainBack.class).queryList();
            List<GlanceTrainBack> listGlanceTrainBack = SQLite.select().from(GlanceTrainBack.class).queryList();
            List<FollowTrainBack> listFollowTrainBack = SQLite.select().from(FollowTrainBack.class).queryList();

            if (listStereoscopeTrainBack != null && listStereoscopeTrainBack.size() > 0) {
                sum += listStereoscopeTrainBack.size();
            }
            if (listFracturedRulerTrainBack != null && listFracturedRulerTrainBack.size() > 0) {
                sum += listFracturedRulerTrainBack.size();
            }
            if (listReversalTrainBack != null && listReversalTrainBack.size() > 0) {
                sum += listReversalTrainBack.size();
            }
            if (listRedGreenReadTrainBack != null && listRedGreenReadTrainBack.size() > 0) {
                sum += listRedGreenReadTrainBack.size();
            }
            if (listApproachTrainBack != null && listApproachTrainBack.size() > 0) {
                sum += listApproachTrainBack.size();
            }
            if (listRGVariableVectorTrainBack != null && listRGVariableVectorTrainBack.size() > 0) {
                sum += listRGVariableVectorTrainBack.size();
            }
            if (listRGFixedVectorTrainBack != null && listRGFixedVectorTrainBack.size() > 0) {
                sum += listRGFixedVectorTrainBack.size();
            }
            if (listGlanceTrainBack != null && listGlanceTrainBack.size() > 0) {
                sum += listGlanceTrainBack.size();
            }
            if (listFollowTrainBack != null && listFollowTrainBack.size() > 0) {
                sum += listFollowTrainBack.size();
            }

            if(sum==0){
                //?????????????????????????????????
                isOnPostAllLocalBacks = false;
                postFinishTrainData();
            }else{
                if (listStereoscopeTrainBack != null && listStereoscopeTrainBack.size() > 0) {
                    for (StereoscopeTrainBack trainBack : listStereoscopeTrainBack) {
                        trainBackUseCase.createStereoscope(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }

                if (listFracturedRulerTrainBack != null && listFracturedRulerTrainBack.size() > 0) {
                    for (FracturedRulerTrainBack trainBack : listFracturedRulerTrainBack) {
                        trainBackUseCase.createFracturedRuler(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }

                if (listReversalTrainBack != null && listReversalTrainBack.size() > 0) {
                    for (ReversalTrainBack trainBack : listReversalTrainBack) {
                        trainBackUseCase.createReversal(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }

                if (listRedGreenReadTrainBack != null && listRedGreenReadTrainBack.size() > 0) {
                    for (RedGreenReadTrainBack trainBack : listRedGreenReadTrainBack) {
                        trainBackUseCase.createRedGreenRead(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }

                if (listApproachTrainBack != null && listApproachTrainBack.size() > 0) {
                    for (ApproachTrainBack trainBack : listApproachTrainBack) {
                        trainBackUseCase.createApproach(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }

                if (listRGVariableVectorTrainBack != null && listRGVariableVectorTrainBack.size() > 0) {
                    for (RGVariableVectorTrainBack trainBack : listRGVariableVectorTrainBack) {
                        trainBackUseCase.createRGVariableVector(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }

                if (listRGFixedVectorTrainBack != null && listRGFixedVectorTrainBack.size() > 0) {
                    for (RGFixedVectorTrainBack trainBack : listRGFixedVectorTrainBack) {
                        trainBackUseCase.createRGFixedVector(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }

                if (listGlanceTrainBack != null && listGlanceTrainBack.size() > 0) {
                    for (GlanceTrainBack trainBack : listGlanceTrainBack) {
                        trainBackUseCase.createGlance(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }

                if (listFollowTrainBack != null && listFollowTrainBack.size() > 0) {
                    for (FollowTrainBack trainBack : listFollowTrainBack) {
                        trainBackUseCase.createFollow(token, trainBack)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(apiResult -> {
                                    nowFinishCount++;
                                    successCount++;

                                    trainBack.delete();

                                    checkPostAllLocalBacksFinish();
                                }, throwable -> {
                                    nowFinishCount++;

                                    checkPostAllLocalBacksFinish();
                                });
                    }
                }
            }

        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    public void checkPostAllLocalBacksFinish(){
        //???????????????????????????????????????????????????
        if(nowFinishCount==sum){
            postDataViewAfter();

            isOnPostAllLocalBacks = false;

            //?????????????????????????????????????????????????????????
            if(successCount==sum){
                postFinishTrainData();
            }else{
                viewListener.showNetworkExceptionDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postAllLocalBacks();
                    }
                });
            }
        }
    }
}
