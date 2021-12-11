package com.uyu.device.devicetraining.domain.bluetooth.le;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 蓝牙连接监听服务，一直监听是否连着蓝牙
 * Created by windern on 2016/4/28.
 */
public class BltConnectService extends Service {
    public static class BltConnectServiceBinder extends Binder {

        private BltConnectService mService = null;

        public BltConnectServiceBinder(BltConnectService service) {
            mService = service;
        }

        public BltConnectService getService () {
            return mService;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new BltConnectServiceBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopListen();
        return super.onUnbind(intent);
    }

    public static int IntervalTime = 20;

    private Observable<Long> observable = null;
    private Subscriber<Long> subscriber = null;

    private void startTimer(){
        Timber.d("startTimer");
        observable = Observable.interval(0,IntervalTime, TimeUnit.SECONDS);
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                Timber.d("handleTask %s",aLong);
                handleTask();
            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }
    private void stopTimmer(){
        Timber.d("stopTimmer");
        if(subscriber!=null && !subscriber.isUnsubscribed()){
            subscriber.unsubscribe();
        }
        subscriber = null;
        observable = null;
    }

    private void handleTask(){
        MotorBluetoothAdapter motorBluetoothAdapter = MotorBluetoothAdapter.getInstance();
        if(!motorBluetoothAdapter.ismConnected()){
            Context context = getApplicationContext();
            String address = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_CONNECT_BLUTTOOTH_UID);
            if(!address.equals("")){
                boolean success = motorBluetoothAdapter.connect(address);
            }
        }
    }


    public void startListen() {
        Timber.d("startListen");
        startTimer();
    }

    public void stopListen(){
        stopTimmer();
    }
}
