package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

/**
 * Created by windern on 2016/1/8.
 */
public class SelectPrepareDesc extends PrepareDesc{
    protected String[] arrayOptions;

    public SelectPrepareDesc(int image, String desc) {
        super(image,desc);
        type = PrepareDescType.SELECT;
    }

    public SelectPrepareDesc(int image, String desc,String[] arrayOptions) {
        super(image,desc);
        this.arrayOptions = arrayOptions;
        type = PrepareDescType.SELECT;
    }

    public String[] getArrayOptions() {
        return arrayOptions;
    }

    public void setArrayOptions(String[] arrayOptions) {
        this.arrayOptions = arrayOptions;
    }
}
