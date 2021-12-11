package com.uyu.device.devicetraining.domain.interactor;

import com.uyu.device.devicetraining.data.entity.content.News;
import com.uyu.device.devicetraining.data.entity.content.NewsCategory;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.content.UserUploadIds;
import com.uyu.device.devicetraining.data.net.api.ApiService;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by windern on 2015/11/30.
 */
public class ContentUseCase {
    private final ApiService apiService;

    @Inject
    public ContentUseCase(ApiService apiService){
        this.apiService = apiService;
    }

    public Observable<ModelApiResult<TrainingContent>> getTrainingContent(String tk, int id){
        return apiService.getTrainingContent(id,tk);
    }

    public Observable<ModelApiResult<String>> getTrainingScanQRCode(String tk){
        return apiService.getTrainingScanQRCode(tk);
    }

    public Observable<ModelApiResult<List<UserUploadIds>>> getUploadIDs(String tk,int userId){
        return apiService.getUploadIDs(userId,tk);
    }

    public Observable<ModelApiResult<List<NewsCategory>>> getNewsCategories(String tk){
        return apiService.getNewsCategories(tk);
    }

    public Observable<ModelApiResult<List<News>>> getNews(String tk,int newsCategory,int newsDate){
        return apiService.getNews(tk,newsCategory,newsDate);
    }

    public Observable<ModelApiResult<News>> getNewsContent(String tk,int newsID){
        return apiService.getNewsContent(tk,newsID);
    }

    public Observable<ModelApiResult<News>> getNextNewsContent(String tk,int newsCategory,int newsDate){
        return apiService.getNextNewsContent(tk,newsCategory,newsDate);
    }

    public Observable<ModelApiResult<News>> getPreNewsContent(String tk,int newsCategory,int newsDate){
        return apiService.getPreNewsContent(tk,newsCategory,newsDate);
    }

    public Observable<ModelApiResult<List<News>>> getNewsOnce(String tk,int newsCategory){
        return apiService.getNewsOnce(tk,newsCategory);
    }

    public Observable<ModelApiResult<List<News>>> getNewsOnceFromId(String tk,int newsCategory,int newsid){
        return apiService.getNewsOnceFromId(tk,newsCategory,newsid);
    }
}
