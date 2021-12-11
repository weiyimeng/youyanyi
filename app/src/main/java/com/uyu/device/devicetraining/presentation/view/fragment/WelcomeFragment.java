package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.uyu.device.devicetraining.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2015/11/30.
 */
public class WelcomeFragment extends BaseFragment{
    @Bind(R.id.view_title)
    ImageView view_title;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_welcome, container, true);
        ButterKnife.bind(this, fragmentView);

        setAnimate();
        return fragmentView;
    }

    public void setAnimate(){
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.left_right_anim);
        view_title.startAnimation(anim);
    }
}
