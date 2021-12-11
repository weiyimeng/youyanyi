package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.trainpres.FracturedRulerTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGFixedVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGVariableVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;
import com.uyu.device.devicetraining.presentation.view.thirdly.convenientbanner.transforms.DepthPageTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jwc on 2016/7/17.
 */
public class TrainPrepareViewA extends RelativeLayout implements ViewPager.OnPageChangeListener{

    public static final String PREPARE = "prepare";

    public String train_type = "";

    Context mContext;

    int mSelectedFontColor;
    int mUnSelectedFontColor;
    int mTransparentColor;
    int mUnpassUnSelectedFontColor;

    @Bind(R.id.container_menu)
    LinearLayout container_menu;

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.iv_left)
    ImageView iv_left;
    @Bind(R.id.iv_right)
    ImageView iv_right;


    @Bind(R.id.ll_circle)
    LinearLayout ll_circle;

    @Bind(R.id.ll_left_guide)
    LinearLayout ll_left_guide;


    private Map<String,String> prepare_items = null;

    private Map<String,TextView> tv_items = new HashMap<>();

    private List<TextView> tvCircles = new ArrayList<>();

    private List<PrepareDesc> descList;
    private TrainPres trainPres;

    private PreparePagerAdapter preparePagerAdapter;

    private OnStartTrainListener listener;
    private OnSelectContentListener selectContentListener;
    private OnSelectUserContentListener selectUserContentListener;
    private ReversalPresChangeListener reversalPresChangeListener;
    private OnTextSizeChangeListener onTextSizeChangeListener;


    public TrainPrepareViewA(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public TrainPrepareViewA(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public TrainPrepareViewA(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
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
        initSelected();
        initCircle();
        preparePagerAdapter = new PreparePagerAdapter(mContext, descList, listener, selectContentListener, reversalPresChangeListener, trainPres, onTextSizeChangeListener, selectUserContentListener);
        viewpager.setPageTransformer(true, new DepthPageTransformer());
        viewpager.setAdapter(preparePagerAdapter);
        viewpager.addOnPageChangeListener(this);
    }

    /**
     * 设置左边菜单显示
     * @param visible
     */
    public void setContainerMenuVisible(int visible){
        container_menu.setVisibility(visible);
    }

    /**
     * 初始化
     */
    private void init(){
        /**
         * 初始化所需颜色
         */
        mUnSelectedFontColor = Color.parseColor("#9CC829");
        mSelectedFontColor = mContext.getResources().getColor(R.color.white);
        mTransparentColor = mContext.getResources().getColor(R.color.transparent);
        mUnpassUnSelectedFontColor = mContext.getResources().getColor(R.color.content_gray);

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_pre_training_a,this,true);
        ButterKnife.bind(this,view);
    }



    private void initLeftGuide(){
        if(ll_left_guide.getChildCount()>0){
            ll_left_guide.removeAllViews();
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,88);
        for (String key : prepare_items.keySet()){
            TextView tvItem = new TextView(mContext);
            tvItem.setText(prepare_items.get(key));
            tvItem.setLayoutParams(lp);
            tvItem.setGravity(Gravity.CENTER_VERTICAL);
            tvItem.setTextSize(14);
            tvItem.setPadding(50,0,0,0);

            ll_left_guide.addView(tvItem);
            tv_items.put(key,tvItem);
        }

        initSelected();
    }

    private void initSelected(){
        String strName = "";
        if(trainPres == null){
            setLeftGuideSelectedIndex(PREPARE);
        }else {
            EnumTrainItem trainItem = trainPres.getTrainItemType();
            if (trainItem == EnumTrainItem.REVERSAL) {
                setLeftGuideSelectedIndex(EnumTrainItem.REVERSAL.getName());
            } else if (trainItem == EnumTrainItem.STEREOSCOPE) {
                if(train_type.equals("youyan")) {
                    strName = ((StereoscopeTrainPres) trainPres).getTrainingType().getName();
                }
                setLeftGuideSelectedIndex(EnumTrainItem.STEREOSCOPE.getName()+strName);
            } else if (trainItem == EnumTrainItem.R_G_VARIABLE_VECTOR) {
                if(train_type.equals("youyan")) {
                    strName = ((RGVariableVectorTrainPres) trainPres).getTrainingType().getName();
                }
                setLeftGuideSelectedIndex(EnumTrainItem.R_G_VARIABLE_VECTOR.getName()+strName);
            } else if (trainItem == EnumTrainItem.FRACTURED_RULER) {
                if(train_type.equals("youyan")) {
                    strName = ((FracturedRulerTrainPres) trainPres).getTrainingType().getName();
                }
                setLeftGuideSelectedIndex(EnumTrainItem.FRACTURED_RULER.getName()+strName);
            }else if (trainItem == EnumTrainItem.RED_GREEN_READ) {
                setLeftGuideSelectedIndex(EnumTrainItem.RED_GREEN_READ.getName());
            }else if (trainItem == EnumTrainItem.APPROACH) {
                setLeftGuideSelectedIndex(EnumTrainItem.APPROACH.getName());
            }else if (trainItem == EnumTrainItem.R_G_FIXED_VECTOR) {
                if(train_type.equals("youyan")) {
                    strName = ((RGFixedVectorTrainPres) trainPres).getTrainingType().getName();
                }
                setLeftGuideSelectedIndex(EnumTrainItem.R_G_FIXED_VECTOR.getName()+strName);
            } else if (trainItem == EnumTrainItem.GLANCE) {
                setLeftGuideSelectedIndex(EnumTrainItem.GLANCE.getName());
            } else if (trainItem == EnumTrainItem.FOLLOW) {
                setLeftGuideSelectedIndex(EnumTrainItem.FOLLOW.getName());
            } else {
                setLeftGuideSelectedIndex(PREPARE);
            }
        }
    }

    @OnClick(R.id.iv_left)
    public void before() {
        viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
    }

    @OnClick(R.id.iv_right)
    public void next() {
        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
    }


    /**
     * 重置回到第一页
     */
    public void reset() {
        viewpager.setCurrentItem(0);
    }

    public void go2NextStage(){
        int currentItem = viewpager.getCurrentItem();
        if(descList!=null) {
            //desclist可能为空，开始的时候还没刷新回来
            if (currentItem < descList.size() - 1) {
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            } else {
                listener.onStartTrain();
            }
        }
    }

    public void setPrepareItems(Map<String,String> items){
        if(items != null){
            prepare_items = items;
            initLeftGuide();
        }
    }

    private void setCircleSelected(int index){
        //推进的时候会出现角标越界 这里做一下判断,估计出现错误的原因是ViewPager没有控制好选中时的角标位置
        if (index < tvCircles.size()-1){
            tvCircles.get(index).setSelected(true);
        }
        for (int i = 0 ;i < tvCircles.size();i++){
            if(i != index){
                tvCircles.get(i).setSelected(false);
            }
        }
    }

    private void initCircle(){
        int count = descList.size();
        if(ll_circle.getChildCount() > 0){
            ll_circle.removeAllViews();
            tvCircles.clear();
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15,15);
        lp.setMargins(10,10,10,10);
        for (int i = 0 ;i < count;i++){
            TextView tvCircle = new TextView(mContext);
            tvCircle.setLayoutParams(lp);
            tvCircle.setBackgroundResource(R.drawable.selector_prepare_bottom_circle_bgbg);
            tvCircle.setSelected(false);
            ll_circle.addView(tvCircle);
            tvCircles.add(tvCircle);
        }
        setCircleSelected(0);
    }


    /**
     * 设置选中控件的样式
     * @param key
     */
    private void setLeftGuideSelectedIndex(String key){
        if(prepare_items != null) {
            for (String k : prepare_items.keySet()) {
                if (k.equals(key)) {
                    tv_items.get(k).setBackgroundResource(R.drawable.guide_item_bg);
                    tv_items.get(k).setTextColor(mSelectedFontColor);
                } else {
                    tv_items.get(k).setBackgroundColor(mTransparentColor);
                    tv_items.get(k).setTextColor(mUnpassUnSelectedFontColor);

//                tv_items.get(k).setBackgroundColor(mTransparentColor);
//                tv_items.get(k).setTextColor(mUnSelectedFontColor);
                }
            }
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCircleSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
