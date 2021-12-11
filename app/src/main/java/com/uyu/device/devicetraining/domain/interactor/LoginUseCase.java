package com.uyu.device.devicetraining.domain.interactor;

import com.uyu.device.devicetraining.data.entity.ApiResult;
import com.uyu.device.devicetraining.data.entity.DeviceEntity;
import com.uyu.device.devicetraining.data.entity.DeviceInfo;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.TokenEntity;
import com.uyu.device.devicetraining.data.entity.other.HardwareVersion;
import com.uyu.device.devicetraining.data.entity.other.Reception;
import com.uyu.device.devicetraining.data.entity.other.SoftwareVersion;
import com.uyu.device.devicetraining.data.net.api.ApiService;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by windern on 2015/11/30.
 */
public class LoginUseCase {
    private final ApiService apiService;

    @Inject
    public LoginUseCase(ApiService apiService){
        this.apiService = apiService;
    }

    public Observable<DeviceEntity> login(String duid,String password) {
        return apiService.login(duid,password);
    }

    public Observable<TokenEntity> getToken(String duid,String code){
        return apiService.getAccessToken(duid, code);
    }

    public Observable<ApiResult> checkAuth(String duid,String tk){
        return apiService.checkAuth(duid, tk);
    }

    public Observable<ApiResult> updateDeviceInfo(int deviceId,String tk, HashMap<String,String> deviceInfo){
        return apiService.updateDeviceInfo(deviceId,tk,deviceInfo);
    }

    public Observable<ModelApiResult<SoftwareVersion>> getNowSoftwareVersion(String tk){
        return apiService.getNowSoftwareVersion(tk);
    }

    public Observable<ModelApiResult<HardwareVersion>> getNowHardwareVersion(String tk){
        return apiService.getNowHardwareVersion(tk);
    }

    public Observable<ModelApiResult<Reception>> getNowReception(String tk){
        return apiService.getNowReception(tk);
    }
}
