package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

/**
 * Created by Administrator on 2016/2/3 0003.
 */
public class ReversalPrepareDesc extends PrepareDesc {
    PrepareDescType type = PrepareDescType.EYELEVEL;

    public ReversalPrepareDesc(int image, String desc) {
        super(image, desc);
    }

    public ReversalPrepareDesc(int image, String desc, PrepareDescType type) {
        super(image, desc, type);
    }
}
