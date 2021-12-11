/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uyu.device.devicetraining.presentation.internal.di.components;

import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.internal.di.modules.ActivityModule;
import com.uyu.device.devicetraining.presentation.internal.di.modules.TrainBackModule;
import com.uyu.device.devicetraining.presentation.internal.di.modules.UserModule;
import com.uyu.device.devicetraining.presentation.view.fragment.ApproachTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.CheckAuthFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.FollowTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.FracturedRulerTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.GlanceTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.IntroductionFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.LoginFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.RGFixedVectorTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.RGVariableVectorTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.RedGreenReadTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.ReversalTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.ShowCodeFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.StereoscopeTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.TrainStageFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.trial.TrialTrainStageFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.WelcomeFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.trial.TrialFracturedRulerTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.trial.TrialRGFixedVectorTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.trial.TrialRGVariableVectorTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.trial.TrialReversalTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.trial.TrialStereoscopeTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.trial.WholeTrialTrainFragment;

import dagger.Component;

/**
 * A scope {@link com.uyu.device.devicetraining.presentation.internal.di.PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, UserModule.class, TrainBackModule.class})
public interface UserComponent extends ActivityComponent {
    void inject(ShowCodeFragment showCodeFragment);
    void inject(TrainStageFragment trainStageFragment);
    void inject(WelcomeFragment userListFragment);
    void inject(LoginFragment loginFragment);
    void inject(CheckAuthFragment checkAuthFragment);
    void inject(IntroductionFragment introductionFragment);
    void inject(StereoscopeTrainFragment stereoscopeTrainFragment);
    void inject(FracturedRulerTrainFragment fracturedRulerTrainFragment);
    void inject(ReversalTrainFragment reversalTrainFragment);
    void inject(RedGreenReadTrainFragment redGreenReadTrainFragment);
    void inject(ApproachTrainFragment approachTrainFragment);
    void inject(RGVariableVectorTrainFragment rgVariableVectorTrainFragment);
    void inject(RGFixedVectorTrainFragment rgFixedVectorTrainFragment);
    void inject(GlanceTrainFragment glanceTrainFragment);
    void inject(FollowTrainFragment followTrainFragment);

    void inject(WholeTrialTrainFragment wholeTrialTrainFragment);
    void inject(TrialTrainStageFragment trainTrialStageFragment);
    void inject(TrialStereoscopeTrainFragment trialStereoscopeTrainFragment);
    void inject(TrialFracturedRulerTrainFragment trialFracturedRulerTrainFragment);
    void inject(TrialReversalTrainFragment trialReversalTrainFragment);
    void inject(TrialRGVariableVectorTrainFragment trialRGVariableVectorTrainFragment);
    void inject(TrialRGFixedVectorTrainFragment trialRGFixedVectorTrainFragment);
}
