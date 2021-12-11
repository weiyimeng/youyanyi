package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumButtonType;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windern on 2015/12/19.
 */
public class BannerBarView extends RelativeLayout{
    public interface BannerBarViewListener{
        /**
         * 结束
         * @param finishType
         */
        void finish(EnumButtonType finishType);

        /**
         * 结束当前训练
         */
        void finishCurrent();

        /**
         * 显示当前的项目介绍
         */
        void showItemIntro();
    }

    private Context context;
    private View view;

    @Bind(R.id.rl)
    RelativeLayout rl;

    @Bind(R.id.tv_item_title)
    TextView tv_item_title;

    @Bind(R.id.tv_item_tip)
    TextView tv_item_tip;

    @Bind(R.id.btn_finish)
    Button btn_finish;

    @Bind(R.id.tv_device_name)
    TextView tv_device_name;

    @Bind(R.id.tv_time)
    TextView tv_time;

    @Bind(R.id.ll_youyan_download)
    LinearLayout ll_youyan_download;

    @Bind(R.id.ll_instructions)
    LinearLayout ll_instructions;

    @Bind(R.id.iv_logo)
    ImageView iv_logo;

    @Bind(R.id.btn_finish_current)
    Button btn_finish_current;

    private BannerBarViewListener mListener;
    private EnumButtonType finishType=null;

    public BannerBarView(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    public BannerBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initLayout();
    }

    public BannerBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initLayout();
    }

    /**
     * 初始化布局
     */
    public void initLayout(){
        view = LayoutInflater.from(context).inflate(R.layout.item_banner_layout, this,
                true);
        ButterKnife.bind(this, view);
    }

    public void setmListener(BannerBarViewListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 设置标题
     * @param itemTitle
     */
    public void setItemTitle(String itemTitle){
//        if(itemTitle==null || itemTitle.equals("")){
//            ll_banner_container.setVisibility(View.GONE);
//        }else {
//            ll_banner_container.setVisibility(View.VISIBLE);
            tv_item_title.setText(itemTitle);
            tv_item_tip.setVisibility(View.VISIBLE);
            tv_item_title.setVisibility(View.GONE);
//        }
    }

    /**
     * 设置item提示
     * 如果为空，则自动隐藏
     * @param itemTip
     */
    public void setItemTip(String itemTip){
        if(itemTip==null || itemTip.equals("")){
            tv_item_tip.setVisibility(View.GONE);
        }else {
            tv_item_tip.setText(itemTip);
            tv_item_tip.setVisibility(View.VISIBLE);
            btn_finish.setVisibility(View.GONE);
        }
    }

    /**
     * 设置logo显示的状态
     * @param status
     */
    public void setLogoStatus(int status){
        if(status == 0){
            iv_logo.setImageResource(R.drawable.logo);
        }else if(status == 1){
            iv_logo.setImageResource(R.drawable.logo_abnormal);
        }
        Log.d("aaa","=====status======="+status);
    }

    public EnumButtonType getFinishType() {
        return finishType;
    }

    /**
     * 设置按钮状态
     * @param finishType
     */
    public void setFinishType(EnumButtonType finishType) {
        this.finishType = finishType;
        if(this.finishType==null){
            btn_finish.setVisibility(View.GONE);
        }else{
            int tipid = finishType.getTipid();
            btn_finish.setText(tipid);
            btn_finish.setVisibility(View.VISIBLE);
            tv_item_tip.setText("");
            tv_item_tip.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_finish)
    public void finish(){
        if(mListener!=null){
            mListener.finish(finishType);
        }
    }

    ShowQRCodeView showQRCodeView ;

    @OnClick(R.id.ll_youyan_download)
    public void youyanDownload(){
        if(showQRCodeView == null) {
            showQRCodeView = new ShowQRCodeView(context);
        }
        showQRCodeView.showAtLocation(rl,Gravity.CENTER,0,0);
    }

    @OnClick(R.id.ll_instructions)
    public void clickItemIntro(){
        if(mListener!=null){
            mListener.showItemIntro();
        }
    }

    @OnClick(R.id.btn_finish_current)
    public void finishCurrentTrain(){
        if(mListener != null){
            mListener.finishCurrent();
        }
    }

    /**
     * 设置结束当前训练按钮的隐藏和显示
     * @param i
     */
    public void setBtnFinishCurrentVisibility(int i){
        btn_finish_current.setVisibility(i);
    }

    /**
     * 设置显示项目介绍的按钮是否显示
     * @param visible
     */
    public void setShowItemIntroBtnVisible(int visible){
        ll_instructions.setVisibility(visible);
    }

    public void setDeviceName(String deviceName){
        tv_device_name.setText(deviceName);
    }

    Observable<Long> observalbleTime;
    Subscriber<Long> subscriberTime;
    Subscription subscriptionTime;
    long time = 0;
    long seconds = 0;
    long minutes = 0;

    private void showTimeText(){
        this.time++;
        if(time >= 60){
            minutes = time / 60;
            seconds = time % 60;
        }else{
            seconds = time;
        }
        tv_time.setText(getContext().getString(R.string.time)+minutes+getContext().getString(R.string.minutes)+seconds+getContext().getString(R.string.seconds));
    }

    public void startTime(){
        time = 0;
        seconds = 0;
        minutes = 0;
        showTimeText();
        observalbleTime = Observable.interval(1,TimeUnit.SECONDS);
        subscriberTime = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                showTimeText();
            }
        };
        subscriptionTime =
                observalbleTime.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriberTime);
    }
    public void stopTime(){
        if(subscriptionTime != null){
            subscriptionTime.unsubscribe();
        }
        if(subscriberTime != null){
            subscriberTime.unsubscribe();
        }
        subscriptionTime = null;
        subscriberTime = null;
        observalbleTime = null;
    }

    public void hiddenTime(){
        tv_time.setVisibility(View.GONE);
    }
    public void showTime(){
        tv_time.setVisibility(View.VISIBLE);
    }
}
