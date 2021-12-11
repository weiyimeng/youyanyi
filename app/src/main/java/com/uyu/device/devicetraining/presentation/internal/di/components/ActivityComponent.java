package com.uyu.device.devicetraining.presentation.internal.di.components;

import android.support.v7.app.AppCompatActivity;

import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.internal.di.modules.ActivityModule;
import com.uyu.device.devicetraining.presentation.internal.di.modules.ApplicationModule;

import dagger.Component;

/**
 * Created by windern on 2015/11/28.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {
    AppCompatActivity activity();
}
