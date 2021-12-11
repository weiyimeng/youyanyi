package com.uyu.device.devicetraining.presentation.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;
import com.uyu.device.devicetraining.presentation.adapter.TrainFragmentListener;
import com.uyu.device.devicetraining.presentation.adapter.ViewTitleTipListener;
import com.uyu.device.devicetraining.presentation.presenter.TrainPresenter;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windern on 2015/12/4.
 */
public class TrainFragment<T extends TrainPresenter> extends BaseFragment implements TrainFragmentListener {
    @Inject
    T trainPresenter;

    @Bind(R.id.ll_welcome_container)
    RelativeLayout ll_welcome_container;

    @Bind(R.id.ll_preparing_container)
    RelativeLayout ll_preparing_container;

    @Bind(R.id.ll_pausing_container)
    RelativeLayout ll_pausing_container;

    @Bind(R.id.ll_training_container)
    LinearLayout ll_training_container;

    @Bind(R.id.tv_welcome)
    TextView tv_welcome;

    @Bind(R.id.tv_pausing_welcome)
    TextView tv_pausing_welcome;

    protected ViewTitleTipListener mlistener;
    private TrainStatus trainStatus = TrainStatus.WELCOME;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        View fragmentView = inflater.inflate(layoutId, container, true);
        ButterKnife.bind(this, fragmentView);
        setupUI();

        return fragmentView;
    }

    public int getLayoutId() {
        return R.layout.fragment_train;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        setItemInfo();
    }

    protected void initialize() {

    }

    public void setupUI(){
        changeToStatus(TrainStatus.WELCOME);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mlistener = (ViewTitleTipListener)activity;
    }

    /**
     * 设置训练项目名称
     */
    public void setItemInfo(){
        String trainItemName = getTrainItemName();
        tv_welcome.setText(trainItemName);
        tv_pausing_welcome.setText(trainItemName);
    }

    /**
     * 获取训练项目名称
     * @return
     */
    public String getTrainItemName(){
        return getActivity().getResources().getString(R.string.title_sub_item);
    }

    /**
     * 设置标题
     * @param itemTitle
     */
    public void setItemTitle(String itemTitle){
        if(mlistener!=null) {
            mlistener.setItemTitle(itemTitle);
        }
    }

    /**
     * 设置item提示
     * 如果为空，则自动隐藏
     * @param itemTip
     */
    public void setItemTip(String itemTip){
        if(mlistener!=null) {
            mlistener.setItemTip(itemTip);
        }
    }

    @Override
    public void changeToStatus(TrainStatus trainStatus) {
        this.trainStatus = trainStatus;
        ll_welcome_container.setVisibility(trainStatus == TrainStatus.WELCOME ? View.VISIBLE : View.GONE);
        ll_preparing_container.setVisibility((trainStatus == TrainStatus.PREPARING || trainStatus == TrainStatus.ADJUST) ? View.VISIBLE : View.GONE);
        ll_pausing_container.setVisibility(trainStatus == TrainStatus.PAUSING ? View.VISIBLE : View.GONE);
        ll_training_container.setVisibility(trainStatus == TrainStatus.TRAINING ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                trainPresenter.pressEnter();
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                trainPresenter.pressUp();
                break;
            case KeyEvent.KEYCODE_BACK:
                trainPresenter.pressDown();
                break;
        }
        return true;
    }

    public void onSelfKeyDown(int keyCode){
        handlerKeyDown.sendEmptyMessage(0);
    }

    public Handler handlerKeyDown = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            trainPresenter.pressEnter();
        }
    };

    /**
     * loading Dialog
     */
    protected ProgressDialog progressDialog;

    @Override
    public void showPostDialog(String title,String content) {
        //title、content作为final变量传进来可以直接用，任何在lamada表达式用的变量都是，可以再函数中定义其他变量也可以直接引入
        Observable.just(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if(progressDialog!=null){
                        //如果存在先取消掉原来的，然后显示新的
                        hidePostDialog();
                    }
                    progressDialog = ProgressDialog.show(getActivity(), title, content, true, false);
                },throwable -> {
                    throwable.printStackTrace();
                });
    }


    @Override
    public void hidePostDialog(){
        if(progressDialog!=null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 显示网络超时错误的
     */
    @Override
    public void showNetworkExceptionDialog(DialogInterface.OnClickListener retryClickListener){
        Dialog alertDialog = new AlertDialog.Builder(getActivity()).
                setTitle(R.string.prompt).
                setMessage(R.string.network_timeout).
                setIcon(R.mipmap.ic_launcher).
                setPositiveButton(R.string.reconnect, retryClickListener).
                create();
        alertDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        alertDialog.show();
    }

    @Override
    public void prepare() {
        changeToStatus(TrainStatus.PREPARING);
    }

    @Override
    public void startTrain() {
        changeToStatus(TrainStatus.TRAINING);
    }

    @Override
    public void finishTrain() {
        ((WelcomeActivity)getActivity()).finishTrain();
    }

    @Override
    public void finishTrain(TrainBack trainBack) {

    }

    @Override
    public void pauseTrain() {
        if(trainStatus== TrainStatus.TRAINING) {
            trainPresenter.pauseTrain();
        }
    }

    @Override
    public void resumeTrain() {
        if(trainStatus== TrainStatus.PAUSING) {
            trainPresenter.resumeTrain();
        }
    }

    @Override
    public void receiveMessage(EmqttMessage emqttMessage) {
        trainPresenter.receiveMessage(emqttMessage);
    }

    @Override
    public void sendMessage(EmqttMessage emqttMessage) {
        ((WelcomeActivity)getActivity()).sendMessage(emqttMessage);
    }

    ////自助训练后加的

    public void receivePresContent(PresTrainMessageContent presTrainMessageContent){
        trainPresenter.receivePresContent(presTrainMessageContent);
    }

    /**
     * 获取到服务器结束消息
     */
    public void receiveFinishServerMessage(){
        trainPresenter.receiveFinishServerMessage();
    }

    public TrainPresenter getTrainPresenter(){
        return trainPresenter;
    }
}
