package com.uyu.device.devicetraining.presentation.internal.di.modules;

import android.support.v7.app.AppCompatActivity;

import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by windern on 2015/11/28.
 */
@Module
public class ActivityModule {
    private final AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity){
        this.activity = activity;
    }

    @Provides @PerActivity
    AppCompatActivity activity(){
        return this.activity;
    }
}
