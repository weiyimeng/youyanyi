package com.uyu.device.devicetraining.presentation.view.adapter;

import com.uyu.device.devicetraining.data.entity.type.EnumContentLoopMode;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;

/**
 * Created by windern on 2016/5/26.
 */
public interface OnSelectUserContentListener {
    void onSelectUserContent(EnumContentType trainingContentType,int trainingContentCategoryId,int trainingContentArticleId,String showTitle);
}
