package com.uyu.device.devicetraining.data.net.api;

import com.uyu.device.devicetraining.data.entity.ApiResult;
import com.uyu.device.devicetraining.data.entity.DeviceEntity;
import com.uyu.device.devicetraining.data.entity.DeviceInfo;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.TokenEntity;
import com.uyu.device.devicetraining.data.entity.TrainBackApiResult;
import com.uyu.device.devicetraining.data.entity.content.CrashInfo;
import com.uyu.device.devicetraining.data.entity.content.News;
import com.uyu.device.devicetraining.data.entity.content.NewsCategory;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.content.UserUploadIds;
import com.uyu.device.devicetraining.data.entity.other.HardwareVersion;
import com.uyu.device.devicetraining.data.entity.other.Reception;
import com.uyu.device.devicetraining.data.entity.other.ReceptionStatus;
import com.uyu.device.devicetraining.data.entity.other.ReceptionTrial;
import com.uyu.device.devicetraining.data.entity.other.SoftwareVersion;
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
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.Category;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainingContentWeb;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by windern on 2015/11/28.
 */
public interface ApiService {
    @GET("device")
    Observable<DeviceEntity> login(@Query("duid") String duid, @Query("password") String password);

    @GET("getAccessToken")
    Observable<TokenEntity> getAccessToken(@Query("duid") String duid, @Query("code") String code);

    @GET("checkAuth")
    Observable<ApiResult> checkAuth(@Query("duid") String duid, @Query("tk") String tk);

    @PUT("device/{id}")
    Observable<ApiResult> updateDeviceInfo(@Path("id") int id, @Query("tk") String tk, @Body HashMap<String,String> deviceInfo);

    @GET("version/getNowSoftwareVersion")
    Observable<ModelApiResult<SoftwareVersion>> getNowSoftwareVersion(@Query("tk") String tk);

    @GET("version/getNowHardwareVersion")
    Observable<ModelApiResult<HardwareVersion>> getNowHardwareVersion(@Query("tk") String tk);

    //训练结束
    @PUT("visionTrainingFeedback/finish/{id}")
    Observable<ApiResult> finishAllTrain(@Path("id") int id, @Query("tk") String token);

    @GET("receptions/exitTrain")
    Observable<ApiResult> exitTrain(@Query("tk") String token, @Query("reception_id") int receptionId);

    @POST("receptions/exitTrainTrail")
    Observable<ApiResult> exitTrainTrail(@Query("tk") String token, @Query("reception_id") int receptionId, @Body ReceptionTrial receptionTrial);

    @POST("createTrainBack")
    <Tb extends TrainBack> Call<TrainBackApiResult<Tb>> createTrainBack(@Query("tk") String tk, @Body Tb trainBack);

    @POST("createStereoscope")
    Observable<TrainBackApiResult<StereoscopeTrainBack>> createStereoscope(@Query("tk") String tk, @Body StereoscopeTrainBack trainBack);

    @POST("createFracturedRuler")
    Observable<TrainBackApiResult<FracturedRulerTrainBack>> createFracturedRuler(@Query("tk") String tk, @Body FracturedRulerTrainBack trainBack);

    @POST("createReversal")
    Observable<TrainBackApiResult<ReversalTrainBack>> createReversal(@Query("tk") String tk, @Body ReversalTrainBack trainBack);

    @POST("createRedGreenRead")
    Observable<TrainBackApiResult<RedGreenReadTrainBack>> createRedGreenRead(@Query("tk") String tk, @Body RedGreenReadTrainBack trainBack);

    @POST("createApproach")
    Observable<TrainBackApiResult<ApproachTrainBack>> createApproach(@Query("tk") String tk, @Body ApproachTrainBack trainBack);

    @POST("createRGVariableVector")
    Observable<TrainBackApiResult<RGVariableVectorTrainBack>> createRGVariableVector(@Query("tk") String tk, @Body RGVariableVectorTrainBack trainBack);

    @POST("createRGFixedVector")
    Observable<TrainBackApiResult<RGFixedVectorTrainBack>> createRGFixedVector(@Query("tk") String tk, @Body RGFixedVectorTrainBack trainBack);

    @POST("createGlance")
    Observable<TrainBackApiResult<GlanceTrainBack>> createGlance(@Query("tk") String tk, @Body GlanceTrainBack trainBack);

    @POST("createFollow")
    Observable<TrainBackApiResult<FollowTrainBack>> createFollow(@Query("tk") String tk, @Body FollowTrainBack trainBack);

    //获取图书更目录
    @GET("Category")
    Observable<ModelApiResult<List<Category>>> getCategroyNew(@Query("ParentID") int parentId, @Query("tk") String token);

    //获取图书子目录
    @GET("TrainingContent")
    Observable<ModelApiResult<List<TrainingContentWeb>>> getItemBooksNew(@Query("CategoryID") int id, @Query("page") int page, @Query("pageSize") int pageSize, @Query("tk") String token);

    //获取图书内容
    @GET("TrainingContent/{id}?tk")
    Observable<ModelApiResult<TrainingContentWeb>> getArticleNew(@Path("id") int id, @Query("tk") String token);

    @GET("TrainingContent/{id}")
    Observable<ModelApiResult<TrainingContent>> getTrainingContent(@Path("id") int id, @Query("tk") String tk);

    @PUT("receptions/{id}")
    Observable<ModelApiResult<Reception>> updateReception(@Path("id") int id, @Query("tk") String tk, @Body ReceptionStatus receptionStatus);

    @GET("getTrainingScanQRCode")
    Observable<ModelApiResult<String>> getTrainingScanQRCode(@Query("tk") String tk);

    @GET("uyuuser/{user_id}/getUserTrainPresScheme")
    Observable<String> getUserTrainPresScheme(@Path("user_id") int user_id, @Query("tk") String tk);

    @GET("getNowReception")
    Observable<ModelApiResult<Reception>> getNowReception(@Query("tk") String tk);

    //获取用户下载书的编号
    @GET("uyuuser/content/uploadIDs/{userId}?json=[]")
    Observable<ModelApiResult<List<UserUploadIds>>> getUploadIDs(@Path("userId") int userId, @Query("tk") String tk);

    //获取类别列表
    @GET("content/getNewsCategories")
    Observable<ModelApiResult<List<NewsCategory>>> getNewsCategories(@Query("tk") String tk);

    //获取新闻列表
    @GET("content/getNews")
    Observable<ModelApiResult<List<News>>> getNews(@Query("tk") String tk, @Query("newsCategory") int newsCategory, @Query("newsDate") int newsDate);

    //获取新闻内容
    @GET("content/getNewsContent")
    Observable<ModelApiResult<News>> getNewsContent(@Query("tk") String tk, @Query("newsID") int newsID);

    //获取下一条新闻
    @GET("content/getNextNewsContent")
    Observable<ModelApiResult<News>> getNextNewsContent(@Query("tk") String tk, @Query("newsCategory") int newsCategory, @Query("newsDate") int newsDate);

    //获取上一条新闻
    @GET("content/getPreNewsContent")
    Observable<ModelApiResult<News>> getPreNewsContent(@Query("tk") String tk, @Query("newsCategory") int newsCategory, @Query("newsDate") int newsDate);

    @GET("content/getNewsOnce")
    Observable<ModelApiResult<List<News>>> getNewsOnce(@Query("tk") String tk, @Query("newsCategory") int newsCategory);

    @GET("content/getNewsOnceFromId")
    Observable<ModelApiResult<List<News>>> getNewsOnceFromId(@Query("tk") String tk, @Query("newsCategory") int newsCategory, @Query("newsid") int newsid);

    @POST("uploadLog")
    Observable<ModelApiResult<CrashInfo>> uploadLog(@Query("tk") String tk, @Body CrashInfo crashInfo);


}
