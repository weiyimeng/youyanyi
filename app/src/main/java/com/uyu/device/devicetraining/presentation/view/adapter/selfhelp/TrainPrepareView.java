package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;
import com.uyu.device.devicetraining.presentation.view.thirdly.convenientbanner.transforms.DepthPageTransformer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by windern on 2016/1/4.
 */
public class TrainPrepareView extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private Context context;


    @Bind(R.id.single_viewpager)
    ViewPager viewPager;
    @Bind(R.id.right_next)
    ImageView nextBtn;
    @Bind(R.id.left_before)
    ImageView beforeBtn;
    @Bind(R.id.step_nums)
    ImageView stepNums;

    public int[] tipsNums = {
            R.mipmap.train_step_num1,
            R.mipmap.train_step_num2,
            R.mipmap.train_step_num3,
            R.mipmap.train_step_num4,
            R.mipmap.train_step_num5,
            R.mipmap.train_step_num6,
            R.mipmap.train_step_num7,
            R.mipmap.train_step_num8,
            R.mipmap.train_step_num8,
    };

    public PreparePagerAdapter preparePagerAdapter;

    private OnStartTrainListener listener;
    private OnSelectContentListener selectContentListener;
    private OnSelectUserContentListener selectUserContentListener;
    private List<PrepareDesc> descList;
    private ReversalPresChangeListener reversalPresChangeListener;
    private TrainPres trainPres;
    private OnTextSizeChangeListener onTextSizeChangeListener;

    public TrainPrepareView(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    public TrainPrepareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public TrainPrepareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
       View view = LayoutInflater.from(context).inflate(R.layout.fragment_pre_training, this, true);
        ButterKnife.bind(this, view);
    }

    public void setDescList(List<PrepareDesc> descList, OnStartTrainListener listener, TrainPres trainPres) {
        this.descList = descList;
        this.trainPres = trainPres;
        this.listener = listener;
        initViews();
    }

    public void setDescList(List<PrepareDesc> descList){
        this.descList = descList;
        initViews();
    }

    public void setReversalPresChangeListener(ReversalPresChangeListener reversalPresChangeListener) {
        this.reversalPresChangeListener = reversalPresChangeListener;
    }

    public void setSelectContentListener(OnSelectContentListener selectContentListener) {
        this.selectContentListener = selectContentListener;
    }

    public void setOnTextSizeChangeListener(OnTextSizeChangeListener onTextSizeChangeListener) {
        this.onTextSizeChangeListener = onTextSizeChangeListener;
    }

    public void setListener(OnStartTrainListener listener) {
        this.listener = listener;
    }

    public void setSelectUserContentListener(OnSelectUserContentListener selectUserContentListener) {
        this.selectUserContentListener = selectUserContentListener;
    }

    /**
     * 初始化View
     */
    private void initViews() {
        preparePagerAdapter = new PreparePagerAdapter(context, descList, listener, selectContentListener, reversalPresChangeListener, trainPres,onTextSizeChangeListener,selectUserContentListener);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setAdapter(preparePagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @OnClick(R.id.left_before)
    void before() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    @OnClick(R.id.right_next)
    void next() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }


    /**
     * 重置回到第一页
     */
    public void reset() {
        viewPager.setCurrentItem(0);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        stepNums.setImageResource(tipsNums[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void go2NextStage(){
        int currentItem = viewPager.getCurrentItem();
        if(descList!=null) {
            //desclist可能为空，开始的时候还没刷新回来
            if (currentItem < descList.size() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                listener.onStartTrain();
            }
        }
    }
}



