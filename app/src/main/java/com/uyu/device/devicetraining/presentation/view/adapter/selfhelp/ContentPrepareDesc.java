package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;


/**
 * Created by windern on 2016/1/8.
 */
public class ContentPrepareDesc extends SelectPrepareDesc {
    public ContentPrepareDesc(int image, String desc) {
        super(image, desc);
        arrayOptions = new String[]{"数字", "字母", "文章"};
        type = PrepareDescType.CONTENT;
    }


}
