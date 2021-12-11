package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.uyu.device.devicetraining.presentation.internal.di.HasComponent;

/**
 * Created by windern on 2015/11/28.
 */
public abstract class BaseFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    protected void showToastMessage(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    protected <C> C getComponent(Class<C> componentType){
        return componentType.cast(((HasComponent<C>)getActivity()).getComponent());
    }
}
