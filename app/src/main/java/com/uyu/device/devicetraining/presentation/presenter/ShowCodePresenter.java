package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2015/11/30.
 */
@PerActivity
public class ShowCodePresenter {
    private final ContentUseCase contentUseCase;
    private final Context context;

    @Inject
    public ShowCodePresenter(@Named("contentUseCase") ContentUseCase contentUseCase, Context context) {
        this.contentUseCase = contentUseCase;
        this.context = context;
    }

    public interface ShowCodeListener{
        void setQrcode(Bitmap bitmap);
        void inNowPageTimeOut();
        void hidenMsg();
        void showMsg(String msg);
        void hidenQRCode();
        void showQRCode();
    }

    private ShowCodeListener itemListener;

    public void setItemListener(ShowCodeListener itemListener) {
        this.itemListener = itemListener;
    }


    public void startChangeQrcode(){
        stopTimmer();
        startTimer();
    }

    public void stopChangeQrcode(){
        stopTimmer();
    }

    private Observable<Long> observable = null;
    private Subscriber<Long> subscriber = null;
    private Subscription subscription = null;

    public void startTimer(){
        stopTimmer();
        observable = Observable.timer(TrainConfig.CodeShowTime,TimeUnit.SECONDS);
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                inNowPageTimeOut();
            }
        };

        subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void stopTimmer(){
        Timber.d("stopTimmer");
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

    private void inNowPageTimeOut(){
        if(itemListener!=null){
            itemListener.inNowPageTimeOut();
        }
    }
    /**
     * 检查验证
     */
    public void getQrcode(){
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        if(!token.equals("")) {
            itemListener.showMsg(context.getString(R.string.get_qrcode));
            itemListener.hidenQRCode();
            contentUseCase.getTrainingScanQRCode(token)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelApiResult<String>>() {

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d("checkAuth fail:%s", e.getMessage());
                            //error的时候说明网不好，不需要重新登录
                            //showMsg("获取训练二维码失败，请点击刷新重试");
                            itemListener.showMsg(context.getString(R.string.get_qrcode_fail));
                            itemListener.hidenQRCode();
                        }

                        @Override
                        public void onNext(ModelApiResult<String> stringModelApiResult) {
                            if (stringModelApiResult.getCode() == 0) {
                                String base64data = stringModelApiResult.getData();

                                if (base64data.length() > 0) {
                                    // 对Base64字符串进行解码
                                    byte[] data = Base64.decode(base64data, Base64.DEFAULT);
                                    // 转成Bitmap
                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    itemListener.setQrcode(bmp);
                                }
                                itemListener.hidenMsg();
                                itemListener.showQRCode();
                            } else {
                                //showMsg("获取训练二维码失败，请点击刷新重试");
                                itemListener.showMsg(context.getString(R.string.get_qrcode_fail));
                                itemListener.hidenQRCode();
                            }
                        }
                    });
        }
    }

    protected void showMsg(String strmsg){
        Observable.just(strmsg).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(inmsg->{
                    ToastUtil.showLongToast(context,inmsg);
                });
    }
}
