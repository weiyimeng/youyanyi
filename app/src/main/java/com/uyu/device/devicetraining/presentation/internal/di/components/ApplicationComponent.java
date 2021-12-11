package com.uyu.device.devicetraining.presentation.internal.di.components;

import android.content.Context;

import com.uyu.device.devicetraining.data.net.api.ApiService;
import com.uyu.device.devicetraining.presentation.internal.di.modules.ApplicationModule;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.view.activity.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by windern on 2015/11/28.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    Context context();
    ApiService apiService();
    TtsEngine ttsEngine();
}
