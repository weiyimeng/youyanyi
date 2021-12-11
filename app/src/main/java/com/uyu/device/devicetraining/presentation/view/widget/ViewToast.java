package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uyu.device.devicetraining.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/5/17 0017.
 */
public class ViewToast {
    private static ViewToast instance = null;
    private static int time = 9;
    private static int firstTime;
    private Timer timer;
    private TextView mainView;


    private ViewToast() {

    }

    public static ViewToast getInstance() {
        if (instance == null) {
            instance = new ViewToast();
        }
        firstTime = 9;
        return instance;
    }

    public static ViewToast getInstance(int timeCount) {
        if (instance == null) {
            instance = new ViewToast();
        }
        firstTime = timeCount;
        return instance;
    }

    public void showTime(Context context, int gravity, int xOffset, int yOffset) {
        Toast toast = new Toast(context);
        TextView textView = new TextView(context);
        timer = new Timer();
        mainView = textView;
        mainView.setVisibility(View.VISIBLE);
        toast.setGravity(gravity, xOffset, yOffset);
        textView.setTextSize(50);
        textView.setText("");
//        textView.setTextColor(context.getResources().getColor(R.color.black));
        textView.setTextColor(Color.RED);
//        textView.setTextScaleX(3);
//        ShowTimeTask timeTask = new ShowTimeTask(textView, toast);
//        timer.schedule(timeTask, 0, 1000);
        startTime(textView,toast);
        resTime();
    }

    private void startTime(TextView textView,Toast toast){

        Observable.interval(1,TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        textView.setText(""+time);
                        toast.setView(textView);
                        toast.show();
                        time--;
                        if(time<1){
                            unsubscribe();
                        }
                    }
                });
    }

    private void resTime() {
        time = firstTime;
    }

    public void removeTime() {
        time = firstTime;
        if (mainView != null) mainView.setVisibility(View.GONE);
        if (timer != null) timer.cancel();
    }

    class ShowTimeTask extends TimerTask {
        TextView textView;
        Toast toast;

        public ShowTimeTask(TextView textView, Toast toast) {
            this.textView = textView;
            this.toast = toast;
        }

        @Override
        public void run() {
            textView.post(new TimerTask() {
                @Override
                public void run() {
                    if (time < 0)
                        return;
                    textView.setText("" + (time--));
                }
            });
            toast.setView(textView);
            if (time > 0) toast.show();
        }
    }
}
