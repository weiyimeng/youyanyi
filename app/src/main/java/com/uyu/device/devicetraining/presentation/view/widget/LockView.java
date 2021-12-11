package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.presentation.AppProvider;
import com.uyu.device.devicetraining.presentation.util.DateUtil;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.FileUtils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2016/5/23.
 */
public class LockView extends RelativeLayout {
    public interface LockViewListener{
        void noOptFinishTrain();
    }

    private Context context;
    private View view;

    @Bind(R.id.iv_pause_qrcode)
    ImageView iv_pause_qrcode;

    @Bind(R.id.tv_pause_extra_desc)
    TextView tv_pause_extra_desc;

    private static final String extraDesc = AppProvider.getApplication().getString(R.string.pause_time_prompt);

    private LockViewListener mListener;

    public LockView(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initLayout();
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initLayout();
    }

    /**
     * 初始化布局
     */
    public void initLayout(){
        view = LayoutInflater.from(context).inflate(R.layout.layout_lock_view, this,
                true);
        ButterKnife.bind(this, view);

        initPauseQrcode();
    }

    public LockViewListener getmListener() {
        return mListener;
    }

    public void setmListener(LockViewListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 初始化暂停的二维码
     */
    public void initPauseQrcode(){
        String duid = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DUID);
        String QRStr = String.format(ServiceConfig.RESUME_TRAIN_URL, duid);
        Bitmap bitmap = FileUtils.bitMapGenerator(context, QRStr);
        iv_pause_qrcode.setImageBitmap(bitmap);
    }

    private Observable<Long> observable = null;
    private Subscriber<Long> subscriber = null;
    private Subscription subscription = null;

    public void startTimer(){
        Timber.d("startTimer");
        observable = Observable.interval(0,1, TimeUnit.SECONDS);
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                int pass = aLong.intValue();
                int left = TrainConfig.NoOptTrainFinishTime-pass;
                if(left<=0){
                    noOptFinishTrain();
                }else{
                    refreshFinishExtraDesc(left);
                }
            }
        };

        subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void stopTimer(){
        Timber.d("stopTimer");
        if(subscription!=null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        if(subscriber!=null && !subscriber.isUnsubscribed()){
            subscriber.unsubscribe();
        }
        subscription = null;
        subscriber = null;
        observable = null;
    }

    public void refreshFinishExtraDesc(int deadline){
        String strDeadline = DateUtil.showDate(deadline,DateUtil.TimeLevel_Second,DateUtil.TimeLevel_Second);
        String desc = String.format(extraDesc,strDeadline);
        tv_pause_extra_desc.setText(desc);
    }

    public void noOptFinishTrain(){
        stopTimer();

        if(mListener!=null){
            mListener.noOptFinishTrain();
        }
    }
}
