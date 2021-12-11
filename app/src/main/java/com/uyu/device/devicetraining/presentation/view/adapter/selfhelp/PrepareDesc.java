package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import com.uyu.device.devicetraining.R;

/**
 * Created by windern on 2016/1/4.
 */
public class PrepareDesc {
    protected int image = R.mipmap.train_step_num1;
    protected String desc = "";
    protected PrepareDescType type = PrepareDescType.NORMAL;

    public PrepareDesc(int image, String desc) {
        this.image = image;
        this.desc = desc;
    }

    public PrepareDesc(int image, String desc,PrepareDescType type) {
        this.image = image;
        this.desc = desc;
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public PrepareDescType getType() {
        return type;
    }

    public void setType(PrepareDescType type) {
        this.type = type;
    }
}
