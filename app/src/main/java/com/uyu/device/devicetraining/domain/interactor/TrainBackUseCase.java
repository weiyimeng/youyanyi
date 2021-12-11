package com.uyu.device.devicetraining.domain.interactor;

import com.uyu.device.devicetraining.data.entity.ApiResult;
import com.uyu.device.devicetraining.data.entity.DeviceEntity;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.TokenEntity;
import com.uyu.device.devicetraining.data.entity.TrainBackApiResult;
import com.uyu.device.devicetraining.data.entity.other.Reception;
import com.uyu.device.devicetraining.data.entity.other.ReceptionStatus;
import com.uyu.device.devicetraining.data.entity.other.ReceptionTrial;
import com.uyu.device.devicetraining.data.entity.trainback.ApproachTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.FollowTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.FracturedRulerTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.GlanceTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RGFixedVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RGVariableVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.RedGreenReadTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.ReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.StereoscopeTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.net.api.ApiService;

import javax.inject.Inject;

import retrofit2.Call;
import rx.Observable;

/**
 * Created by windern on 2015/11/30.
 */
public class TrainBackUseCase {
    private final ApiService apiService;

    @Inject
    public TrainBackUseCase(ApiService apiService){
        this.apiService = apiService;
    }

    public Observable<ModelApiResult<Reception>> updateReception(String tk,ReceptionStatus receptionStatus){
        return apiService.updateReception(receptionStatus.getId(), tk, receptionStatus);
    }

    public Observable<ApiResult> finishAllTrain(String tk,int id){
        return apiService.finishAllTrain(id, tk);
    }

    public Observable<ApiResult> exitTrain(String tk,int receptionId){
        return apiService.exitTrain(tk, receptionId);
    }

    public Observable<ApiResult> exitTrainTrail(String tk, int receptionId, ReceptionTrial receptionTrial){
        return apiService.exitTrainTrail(tk, receptionId, receptionTrial);
    }


    public <Tb extends TrainBack> Call<TrainBackApiResult<Tb>> createTrainBack(String tk,Tb trainBack){
        return apiService.createTrainBack(tk, trainBack);
    }

    public Observable<TrainBackApiResult<StereoscopeTrainBack>> createStereoscope(String tk,StereoscopeTrainBack trainBack){
        return apiService.createStereoscope(tk, trainBack);
    }

    public Observable<TrainBackApiResult<FracturedRulerTrainBack>> createFracturedRuler(String tk,FracturedRulerTrainBack trainBack){
        return apiService.createFracturedRuler(tk, trainBack);
    }

    public Observable<TrainBackApiResult<ReversalTrainBack>> createReversal(String tk,ReversalTrainBack trainBack){
        return apiService.createReversal(tk, trainBack);
    }

    public Observable<TrainBackApiResult<RedGreenReadTrainBack>> createRedGreenRead(String tk,RedGreenReadTrainBack trainBack){
        return apiService.createRedGreenRead(tk, trainBack);
    }

    public Observable<TrainBackApiResult<ApproachTrainBack>> createApproach(String tk,ApproachTrainBack trainBack){
        return apiService.createApproach(tk, trainBack);
    }

    public Observable<TrainBackApiResult<RGVariableVectorTrainBack>> createRGVariableVector(String tk,RGVariableVectorTrainBack trainBack){
        return apiService.createRGVariableVector(tk, trainBack);
    }

    public Observable<TrainBackApiResult<RGFixedVectorTrainBack>> createRGFixedVector(String tk,RGFixedVectorTrainBack trainBack){
        return apiService.createRGFixedVector(tk, trainBack);
    }

    public Observable<TrainBackApiResult<GlanceTrainBack>> createGlance(String tk,GlanceTrainBack trainBack){
        return apiService.createGlance(tk, trainBack);
    }

    public Observable<TrainBackApiResult<FollowTrainBack>> createFollow(String tk,FollowTrainBack trainBack){
        return apiService.createFollow(tk, trainBack);
    }
}
