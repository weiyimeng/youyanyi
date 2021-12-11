package com.uyu.device.devicetraining.presentation.internal.di.modules;

import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.LoginUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by windern on 2015/11/28.
 */
@Module
public class TrainBackModule {
    @Provides
    @PerActivity
    @Named("trainBackUseCase")
    TrainBackUseCase provideTrainBackUseCase(
            TrainBackUseCase trainBackUseCase) {
        return trainBackUseCase;
    }

    @Provides
    @PerActivity
    @Named("contentUseCase")
    ContentUseCase provideContentUseCase(
            ContentUseCase contentUseCase) {
        return contentUseCase;
    }
}
