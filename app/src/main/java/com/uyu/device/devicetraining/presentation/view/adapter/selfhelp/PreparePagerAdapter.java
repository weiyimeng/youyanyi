package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windern on 2016/1/4.
 */
public class PreparePagerAdapter extends PagerAdapter {
    private Context context;
    private List<PrepareDesc> descList = new ArrayList<>();
    private List<PrepareDescView> viewList = new ArrayList<>();
    private OnStartTrainListener listener;
    private OnSelectContentListener selectContentListener;
    private ReversalPresChangeListener reversalPresChangeListener;
    private OnTextSizeChangeListener onTextSizeChangeListener;
    private TrainPres trainPres;


    public PreparePagerAdapter(Context context
            , List<PrepareDesc> descList
            , OnStartTrainListener listener
            , OnSelectContentListener selectContentListener
            , ReversalPresChangeListener reversalPresChangeListener
            , TrainPres trainPres
            , OnTextSizeChangeListener onTextSizeChangeListener
            , OnSelectUserContentListener selectUserContentListener) {
        this.context = context;
        this.descList = descList;
        this.trainPres = trainPres;
        this.listener = listener;
        this.selectContentListener = selectContentListener;
        this.reversalPresChangeListener = reversalPresChangeListener;
        this.onTextSizeChangeListener = onTextSizeChangeListener;
        for (int i = 0; i < descList.size(); i++) {
            PrepareDescView prepareDescView = new PrepareDescView(context);
            prepareDescView.setParam((i+1),descList.get(i), listener, selectContentListener, reversalPresChangeListener, trainPres, onTextSizeChangeListener, selectUserContentListener);
            if (i == descList.size() - 1) {
                prepareDescView.setStartBtnVisible();
            }
            viewList.add(prepareDescView);
        }
    }

    @Override
    public int getCount() {
        return descList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(viewList.get(position), 0);
        return viewList.get(position);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(viewList.get(position));

    }

    /**
     * 改变内容
     *
     * @param paramOneValue
     * @param paramTwoValue
     * @param title
     */
    public void changeContent(int paramOneValue, int paramTwoValue, String title) {
        for (int i = 0; i < viewList.size(); i++) {
            viewList.get(i).changeContent(paramOneValue, paramTwoValue, title);
        }
    }
}
