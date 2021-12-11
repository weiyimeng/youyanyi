package com.uyu.device.devicetraining.presentation.util;

import android.content.Context;

/**
 * Created by windern on 2015/12/28.
 */
public class BaseModule {

    protected Context context;

    public BaseModule(Context context) {
        this.context = context;
    }

    public void setup(Context context) {
        this.context = context;
    }

    public void release() {
        context = null;
    }

    public Context getContext() {
        return context;
    }
}
