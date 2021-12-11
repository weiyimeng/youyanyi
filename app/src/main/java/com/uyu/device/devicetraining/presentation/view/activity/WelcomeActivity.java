package com.uyu.device.devicetraining.presentation.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.example.BalloonActivity;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.entity.content.NewsCategory;
import com.uyu.device.devicetraining.data.entity.ApiResult;
import com.uyu.device.devicetraining.data.entity.EnumUserTrainMode;
import com.uyu.device.devicetraining.data.entity.config.FinishDescManager;
import com.uyu.device.devicetraining.data.entity.config.TrainConfig;
import com.uyu.device.devicetraining.data.entity.message.ConnectTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.ControlsetTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainEmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainMessageType;
import com.uyu.device.devicetraining.data.entity.message.VersionEmqtt;
import com.uyu.device.devicetraining.data.entity.message.VersionEmqttMessage;
import com.uyu.device.devicetraining.data.entity.other.Reception;
import com.uyu.device.devicetraining.data.entity.other.ReceptionTrial;
import com.uyu.device.devicetraining.data.entity.selfhelp.ControlExitTrainContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.PushTrainContentContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.SelfhelpCreateTrainContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.SelfhelpCreateTrainTrialContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.SelfhelpFinishTrainContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.SelfhelpUnlockTrainContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.ServerEmqttMessage;
import com.uyu.device.devicetraining.data.entity.selfhelp.ServerEmqttMessageContent;
import com.uyu.device.devicetraining.data.entity.selfhelp.TrainPresScheme;
import com.uyu.device.devicetraining.data.entity.trainpres.FollowTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.GlanceTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RedGreenReadTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumButtonType;
import com.uyu.device.devicetraining.data.entity.type.EnumLineType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.bluetooth.tradition.MotorBluetoothAdapter;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.LoginUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.adapter.ViewTitleTipListener;
import com.uyu.device.devicetraining.presentation.internal.di.HasComponent;
import com.uyu.device.devicetraining.presentation.internal.di.components.DaggerLoginComponent;
import com.uyu.device.devicetraining.presentation.internal.di.components.DaggerUserComponent;
import com.uyu.device.devicetraining.presentation.internal.di.components.LoginComponent;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.ButtonPressListenPresenter;
import com.uyu.device.devicetraining.presentation.presenter.CheckAuthPresenter;
import com.uyu.device.devicetraining.presentation.presenter.LoginPresenter;
import com.uyu.device.devicetraining.presentation.presenter.TrainManagerPresenter;
import com.uyu.device.devicetraining.presentation.presenter.VersionPresenter;
import com.uyu.device.devicetraining.presentation.presenter.WelcomePresenter;
import com.uyu.device.devicetraining.presentation.presenter.quick.ReversalTrainPresenter;
import com.uyu.device.devicetraining.presentation.type.EnumDeviceStatus;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.adapter.MqttAdatper;
import com.uyu.device.devicetraining.presentation.view.adapter.BatteryReceiver;
import com.uyu.device.devicetraining.presentation.view.adapter.OnButtonPressListener;
import com.uyu.device.devicetraining.presentation.view.adapter.OnChangeChargeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.FileUtils;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnSelectContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnTextSizeChangeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDescManager;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.ReversalPresChangeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainAllFinishView;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainItemTimeType;
import com.uyu.device.devicetraining.presentation.view.fragment.ApproachTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.CheckAuthFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.FollowTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.FracturedRulerTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.GlanceTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.IntroductionFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.LockFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.LoginFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.RGFixedVectorTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.RGVariableVectorTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.RedGreenReadTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.ReversalTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.ShowCodeFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.StereoscopeTrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.TrainFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.TrainStageFragment;
import com.uyu.device.devicetraining.presentation.view.fragment.trial.WholeTrialTrainFragment;
import com.uyu.device.devicetraining.presentation.view.widget.BannerBarView;
import com.uyu.device.devicetraining.presentation.view.widget.ShowItemIntroPopupWindow;
import com.uyu.device.devicetraining.presentation.view.widget.ShowItemIntroView;
import com.uyu.device.devicetraining.presentation.view.widget.ShowUpgradeView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.uyu.device.library.BluetoothSPP;
import com.uyu.device.library.BluetoothState;
import com.uyu.device.library.DeviceList;

import app.update.UpdateAgent;
import app.update.utils.PackageUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public class WelcomeActivity extends BaseActivity implements HasComponent<UserComponent>
        , MqttAdatper
        , OnButtonPressListener, OnChangeChargeListener
        , TrainManagerPresenter.TrainManagerPresenterListener
        , TrainAllFinishView.TrainAllFinishViewListener
        , ReversalPresChangeListener
        , OnSelectContentListener
        , OnSelectUserContentListener
        , OnTextSizeChangeListener
        , ViewTitleTipListener
        , BannerBarView.BannerBarViewListener
        , VersionPresenter.VersionPresenterListener
        , LoginPresenter.LoginPresenterListener
        , ShowItemIntroView.OnCloseItemIntroListener {
    @Bind(R.id.banner_bar_view)
    public BannerBarView banner_bar_view;

    @Bind(R.id.container_check_auth)
    RelativeLayout container_check_auth;

    @Bind(R.id.container_introduction)
    RelativeLayout container_introduction;

    @Bind(R.id.container_login)
    RelativeLayout container_login;

    @Bind(R.id.container_welcome)
    RelativeLayout container_welcome;

    @Bind(R.id.container_lock)
    RelativeLayout container_lock;

    @Bind(R.id.container_train_trial)
    RelativeLayout container_train_trial;

    @Bind(R.id.container_time_training)
    RelativeLayout container_time_training;

    @Bind(R.id.container_train)
    RelativeLayout container_train;

    IntroductionFragment introductionFragment;
    CheckAuthFragment checkAuthFragment;
    LoginFragment loginFragment;
    ShowCodeFragment showCodeFragment;
    LockFragment lockFragment;
    WholeTrialTrainFragment wholeTrialTrainFragment;

    @Bind(R.id.fl_fragment_train_stage)
    FrameLayout fl_fragment_train_stage;

    TrainStageFragment trainStageFragment;

    @Bind(R.id.fl_fragment_stereoscope)
    FrameLayout fl_fragment_stereoscope;

    StereoscopeTrainFragment stereoscopeTrainFragment;

    @Bind(R.id.fl_fragment_fractured_ruler)
    FrameLayout fl_fragment_fractured_ruler;

    FracturedRulerTrainFragment fracturedRulerTrainFragment;

    @Bind(R.id.fl_fragment_reversal)
    FrameLayout fl_fragment_reversal;

    ReversalTrainFragment reversalTrainFragment;

    @Bind(R.id.fl_fragment_red_green_read)
    FrameLayout fl_fragment_red_green_read;

    RedGreenReadTrainFragment redGreenReadTrainFragment;

    @Bind(R.id.fl_fragment_approach)
    FrameLayout fl_fragment_approach;

    ApproachTrainFragment approachTrainFragment;

    @Bind(R.id.fl_fragment_r_g_variable_vector)
    FrameLayout fl_fragment_r_g_variable_vector;

    RGVariableVectorTrainFragment rgVariableVectorTrainFragment;

    @Bind(R.id.fl_fragment_r_g_fixed_vector)
    FrameLayout fl_fragment_r_g_fixed_vector;

    RGFixedVectorTrainFragment rgFixedVectorTrainFragment;

    @Bind(R.id.fl_fragment_glance)
    FrameLayout fl_fragment_glance;

    GlanceTrainFragment glanceTrainFragment;

    @Bind(R.id.fl_fragment_follow)
    FrameLayout fl_fragment_follow;

    FollowTrainFragment followTrainFragment;

    FrameLayout fl_fragmentTrainNow;

    //状态变量
    private EnumUserTrainMode userTrainMode = EnumUserTrainMode.SELFHELP;
    //状态，流程先校验，然后登录，在校验，校验通过到欢迎界面
    private EnumDeviceStatus deviceStatus = EnumDeviceStatus.CHECK_AUTH;
    private TrainItemTimeType itemTimeType = TrainItemTimeType.ALL_PRES;
    private EnumTrainItem trainItemNow = EnumTrainItem.STEREOSCOPE;
    private PresTrainMessageContent presTrainMessageContent;

    //优眼扫码考虑这种情况，判断连续两个项目是否相同
    /**
     * 当前训练处方
     */
    private TrainPres trainPres;
    /**
     * 上一个训练处方
     */
    private TrainPres trainPresPrevious;

    private TrainFragment fragmentNow;

    private EmqttMessage connectEmqttMessage;

    private UserComponent userComponent;
    private LoginComponent loginComponent;

    @Inject
    VersionPresenter versionPresenter;

    @Inject
    WelcomePresenter welcomePresenter;

    @Inject
    TrainManagerPresenter trainManagerPresenter;

    @Inject
    TtsEngine ttsEngine;

    @Inject
    TrainBackUseCase trainBackUseCase;

    @Inject
    LoginUseCase loginUseCase;

    @Inject
    ContentUseCase contentUseCase;

    private long pressTime = 0;
    private int pressCode = 0;

    private static int PupilDefaultDistance = 640;

    ButtonPressListenPresenter buttonPressListenPresenter;
    private boolean isFirstIn = true;
    private boolean isFirstStart = true;

    private BluetoothSPP bt;
    private BatteryReceiver batteryReceiver;

    /**
     * 当前设备处于训练中才会把升级消息储存
     * 是否存在升级的消息(0-不存在,1-存在)
     */
    private int upgradeMsg = 0;
    /**
     * 升级url
     */
    private String versionUrl = "";
    /**
     * 升级版本
     */
    private int versionCode = 0;

    private ShowUpgradeView showUpgradeView = null;

    /**
     * 是否第一次连上蓝牙
     */
    private boolean isConnectBltFirst = true;

    @Bind(R.id.root)
    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 屏幕高亮不变暗(放在setContentView()之前)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                , WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        bluetoothConfigure();

        banner_bar_view.setmListener(this);

        //初始化显示的内容
        setItemTitle("使用优眼APP或,微信扫描二维码");
        setItemTip("");
        banner_bar_view.setFinishType(EnumButtonType.REFRESH_CODE);

        isFirstIn = true;
        userTrainMode = SharePreferenceTool.getUserTrainMode(WelcomeActivity.this);

        this.initializeInjector();

        MotorBus.getInstance().setOnButtonPressListener(this);
        MotorBus.getInstance().setOnChangeChargeListener(this);

        introductionFragment = (IntroductionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_introduction);
        loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_login);
        checkAuthFragment = (CheckAuthFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_check_auth);
        showCodeFragment = (ShowCodeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_show_code);
        lockFragment = (LockFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_lock);
        wholeTrialTrainFragment = (WholeTrialTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_whole_trial_train);
        trainStageFragment = (TrainStageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_train_stage);
        stereoscopeTrainFragment = (StereoscopeTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_stereoscope);
        fracturedRulerTrainFragment = (FracturedRulerTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_fractured_ruler);
        reversalTrainFragment = (ReversalTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_reversal);
        redGreenReadTrainFragment = (RedGreenReadTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_red_green_read);
        approachTrainFragment = (ApproachTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_approach);
        rgVariableVectorTrainFragment = (RGVariableVectorTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_r_g_variable_vector);
        rgFixedVectorTrainFragment = (RGFixedVectorTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_r_g_fixed_vector);
        glanceTrainFragment = (GlanceTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_glance);
        followTrainFragment = (FollowTrainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_follow);

        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        batteryReceiver = new BatteryReceiver();
        //注册receiver
        registerReceiver(batteryReceiver, intentFilter);

        //第一次启动就复位
        reset();

        //版本更新
        checkVersionUpdate();
    }

    /**
     * 检查版本更新
     */
    public void checkVersionUpdate() {
        String channel_id = null;
        try {
            channel_id = this.getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder(ServiceConfig.CHEACK_VERSION);
        sb.append("package_name=").append(getPackageName());
        sb.append("&channel_id=").append(channel_id);
        sb.append("&version_code=").append(PackageUtils.getVersionCode(this));
        UpdateAgent agent = new UpdateAgent(this)
                .setAppIcon(R.mipmap.ic_launcher)
                .setCleanMode(true)
                .setSavePath(FileUtils.getAppPath() + "/yyy.apk")
                .setUpdateURL(sb.toString())
                .setUpdateOnlyWifi(true);
        agent.update();
    }

    /**
     * 充电初始化
     */
    public void chargeInit() {
        //保证开机的时候充电的程序关闭，保证状态一致
        //一上来保证关闭的状态，能保证一定能从30%到50%的循环
        //一上来保证开启的状态，可能能保证50%-100%的循环，取决了上一次用的电量
        //考虑特殊异常，第一次就保证充电上是最合理的
        ControlMessageSet messageSet = TrainUseCase.changeChargeStatus(1);
        MotorBus.getInstance().sendMessageSetDirect(messageSet);
    }

    /**
     * 蓝牙配置
     */
    public void bluetoothConfigure() {
        bt = new BluetoothSPP(this);

        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , R.string.bluetool_not_available
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                MotorBluetoothAdapter.getInstance().receiveFromBluetooth(data);
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                MotorBluetoothAdapter.getInstance().connectedSuccess();
                if (isConnectBltFirst) {
                    isConnectBltFirst = false;
                    //第一次连接上蓝牙以后做处理
                    chargeInit();
                }
            }

            public void onDeviceDisconnected() {
                MotorBluetoothAdapter.getInstance().connectLost();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        MotorBluetoothAdapter.getInstance().setBt(bt);
    }

    public void checkChooseBluetooth() {
        String bltUid = SharePreferenceTool.getSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_CONNECT_BLUTTOOTH_UID);
        Timber.d("windern:bltUid:%s", bltUid);
        //如果没有选择设置蓝牙，选择蓝牙
        if (bltUid.equals("")) {
            //不需要连接了，checkauth失败会让重新匹配蓝牙
            //go2ConnectBt();
        } else {
            MotorBluetoothAdapter.getInstance().connect(bltUid);
            //updateBtidInfo(bltUid);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isFirstStart) {
            changeStatus(EnumDeviceStatus.CHECK_AUTH);

            isFirstStart = false;
        }
    }

    //连接蓝牙
    public void connectBt() {
        if (!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }

    private void changeStatus(EnumDeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
        container_check_auth.setVisibility(this.deviceStatus == EnumDeviceStatus.CHECK_AUTH ? View.VISIBLE : View.GONE);
        container_login.setVisibility(this.deviceStatus == EnumDeviceStatus.LOGIN ? View.VISIBLE : View.GONE);
        container_introduction.setVisibility(this.deviceStatus == EnumDeviceStatus.INTRODUCTION ? View.VISIBLE : View.GONE);
        container_welcome.setVisibility(this.deviceStatus == EnumDeviceStatus.WELCOME ? View.VISIBLE : View.GONE);
        container_lock.setVisibility(this.deviceStatus == EnumDeviceStatus.LOCK ? View.VISIBLE : View.GONE);
        container_train.setVisibility(this.deviceStatus == EnumDeviceStatus.TRAINING ? View.VISIBLE : View.GONE);
        container_train_trial.setVisibility(this.deviceStatus == EnumDeviceStatus.TRAINING_TRIAL ? View.VISIBLE : View.GONE);

        if (!(this.deviceStatus == EnumDeviceStatus.TRAINING
                || this.deviceStatus == EnumDeviceStatus.TRAINING_TRIAL)) {
            // 不在训练中,则检查有没有升级消息
            if (upgradeMsg == 1 && versionCode != 0) {
                versionPresenter.checkSoftwareVersion(versionUrl, versionCode);
            }
        }

        if (!(this.deviceStatus == EnumDeviceStatus.TRAINING)) {
            setShowItemIntroBtnVisible(View.GONE);
        }

        if (this.deviceStatus == EnumDeviceStatus.CHECK_AUTH) {
            setItemTitle(null);
        } else if (this.deviceStatus == EnumDeviceStatus.LOGIN) {
            setItemTitle("登录");
            setItemTip("");
            banner_bar_view.setFinishType(null);

            //直接登录
            String duid = SharePreferenceTool.getSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_DUID);
            loginFragment.loginDirect(duid);
        } else if (this.deviceStatus == EnumDeviceStatus.INTRODUCTION) {
            setItemTitle(null);
        } else if (this.deviceStatus == EnumDeviceStatus.WELCOME) {
            setItemTitle(" ");
            setItemTip("");
            banner_bar_view.setFinishType(EnumButtonType.REFRESH_CODE);
        } else if (this.deviceStatus == EnumDeviceStatus.LOCK) {
            setItemTitle("锁定界面");
            setItemTip("");
            banner_bar_view.setFinishType(EnumButtonType.FINISH_ALL);
        } else if (this.deviceStatus == EnumDeviceStatus.TRAINING) {
            setItemTitle("训练");
            setItemTip("");
            banner_bar_view.setFinishType(null);
        } else if (this.deviceStatus == EnumDeviceStatus.TRAINING_TRIAL) {
            setItemTitle("体验训练");
            setItemTip("");
            banner_bar_view.setFinishType(null);
        }

        //如果是显示欢迎页面，开始扫描二维码
        if (this.deviceStatus == EnumDeviceStatus.WELCOME) {
            showCodeFragment.getQrcode();
        }

        if (this.deviceStatus == EnumDeviceStatus.CHECK_AUTH) {
            checkAuthFragment.checkAuth();
        }

        if (this.deviceStatus == EnumDeviceStatus.LOCK) {
            lockFragment.refreshCode();
            lockFragment.startTimer();
        } else {
            lockFragment.stopTimer();
        }

        if (this.deviceStatus == EnumDeviceStatus.INTRODUCTION) {
            introductionFragment.refreshStatusAndView();
        } else {
            introductionFragment.changeDeviceStatusStopSpeak();
        }
    }


    private void initializeInjector() {
        this.userComponent = DaggerUserComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();

        this.loginComponent = DaggerLoginComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();

        loginComponent.inject(this);

//        buttonPressListenPresenter = new ButtonPressListenPresenter();
//        buttonPressListenPresenter.setListener(this);
//        buttonPressListenPresenter.startListen();

        trainManagerPresenter.setViewListener(this);

        versionPresenter.setmListener(this);
//        versionPresenter.checkVersion();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ttsEngine.release();
        bt.stopService();
        unregisterReceiver(batteryReceiver);
    }


    @Override
    public UserComponent getComponent() {
        return userComponent;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
//            onButtonPress();
//        }
//        return true;
//    }

    public void showMsg(String msg) {
        Observable.just(msg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> ToastUtil.showShortToast(WelcomeActivity.this, msg));
    }


    /**
     * 接收消息做处理：两个流程
     * 1、设备欢迎页-视光师连接消息-项目欢迎页-训练消息-训练结束消息-退出消息-设备欢迎页
     * 2、设备欢迎页-扫码-自助训练消息-训练-全部训练结束设备欢迎页
     *
     * @param emqttMessage
     */
    @Override
    public void receiveMessage(EmqttMessage emqttMessage) {
        if (emqttMessage instanceof TrainEmqttMessage) {
            if (deviceStatus == EnumDeviceStatus.INTRODUCTION || deviceStatus == EnumDeviceStatus.WELCOME) {
                if (((TrainEmqttMessage) emqttMessage).getMsg().getTmt() == TrainMessageType.CONNECT) {
                    connectEmqttMessage = emqttMessage;
                    changeStatus(EnumDeviceStatus.TRAINING);
                    changeItemTimeType(TrainItemTimeType.WELCOME);

                    //设置用户训练模式
                    userTrainMode = EnumUserTrainMode.CONTROL;
                    SharePreferenceTool.setSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_USER_TRAIN_MODE, userTrainMode.toString());
                }
            } else {
                if (((TrainEmqttMessage) emqttMessage).isHeaderEqual(((TrainEmqttMessage) connectEmqttMessage))) {
                    //占用的人发送的消息
                    if (itemTimeType == TrainItemTimeType.WELCOME) {
                        if (((TrainEmqttMessage) emqttMessage).getMsg().getTmt() == TrainMessageType.START) {
                            EnumTrainItem trainItem = ((PresTrainMessageContent) ((TrainEmqttMessage) emqttMessage).getMsg().getCt()).getTp().getTrainItemType();
                            showNowFragment(trainItem);
                            changeItemTimeType(TrainItemTimeType.TRAINING);
                            fragmentNow.receiveMessage(emqttMessage);
                        } else if (((TrainEmqttMessage) emqttMessage).getMsg().getTmt() == TrainMessageType.STOP) {
                            showMsg(getString(R.string.no_train_cannot_end));
                        } else if (((TrainEmqttMessage) emqttMessage).getMsg().getTmt() == TrainMessageType.EXIT) {
                            controlExitTrain();
                        } else {
                            String str = String.format(getString(R.string.welcome_page_not_handle_msg), ((TrainEmqttMessage) emqttMessage).getMsg().getTmt().getName());
                            showMsg(str);
                        }
                    } else if (itemTimeType == TrainItemTimeType.TRAINING) {
                        if (((TrainEmqttMessage) emqttMessage).getMsg().getTmt() == TrainMessageType.START) {
                            showMsg(getString(R.string.train_not_complete_not_start_new_train));
                        } else {
                            fragmentNow.receiveMessage(emqttMessage);
                        }
                    } else {
                        showMsg(getString(R.string.abnormal_state));
                    }
                } else {
                    showMsg(getString(R.string.equipment_is_occupied));
                }
            }
        } else if (emqttMessage instanceof ServerEmqttMessage) {
            Timber.d("收到服务器消息:%s", emqttMessage.toJson());

            ServerEmqttMessageContent serverEmqttMessageContent = ((ServerEmqttMessage) emqttMessage).getMsg().getMsgContent();
            if (serverEmqttMessageContent instanceof SelfhelpCreateTrainContent) {
                if (deviceStatus == EnumDeviceStatus.TRAINING) {
                    showMsg(getString(R.string.train_not_complete_not_start_new_train));
                } else {
                    //设置用户训练模式
                    userTrainMode = EnumUserTrainMode.SELFHELP;
                    SharePreferenceTool.setSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_USER_TRAIN_MODE, userTrainMode.toString());

                    trainManagerPresenter.startWithSelfhelpCreateTrainMessage((SelfhelpCreateTrainContent) serverEmqttMessageContent);

                    changeStatus(EnumDeviceStatus.TRAINING);

                    //showMsg("收到服务器开始消息");
                }
            } else if (serverEmqttMessageContent instanceof SelfhelpFinishTrainContent) {
                //结束的时候必须是训练或者锁定状态
                if (deviceStatus == EnumDeviceStatus.TRAINING || deviceStatus == EnumDeviceStatus.LOCK) {
                    if (trainManagerPresenter.isNowTrainReception((SelfhelpFinishTrainContent) serverEmqttMessageContent)) {
                        if (itemTimeType == TrainItemTimeType.TRAINING) {
                            //在训练阶段的时候才需要结束，如果不在训练阶段，不需要处理，否则可能出现fragmentNow为null
                            fragmentNow.receiveFinishServerMessage();

                        }
                        setItemTip(" ");
                        banner_bar_view.setBtnFinishCurrentVisibility(View.GONE);

                        finishAllTrain();

                        //showMsg("收到服务器结束消息");
                    } else {
                        showMsg(getString(R.string.receive_end_msg_but_not_current_train));
                    }
                } else {
                    showMsg(getString(R.string.receive_end_msg_not_start_train));
                }
            } else if (serverEmqttMessageContent instanceof SelfhelpUnlockTrainContent) {
                if (deviceStatus == EnumDeviceStatus.LOCK) {
                    if (trainManagerPresenter.isNowTrainReception(((SelfhelpUnlockTrainContent) serverEmqttMessageContent).getReceptionId())) {
                        unlockTrain();
                        //showMsg("收到服务器解锁训练消息");
                    } else {
                        showMsg(getString(R.string.receive_unlock_msg_not_current_train));
                    }
                } else {
                    showMsg(getString(R.string.receive_unlock_msg_not_lock_status));
                }
            } else if (serverEmqttMessageContent instanceof ControlExitTrainContent) {
                Reception reception = ((ControlExitTrainContent) serverEmqttMessageContent).getReception();
                if (((TrainEmqttMessage) connectEmqttMessage).getSid().equals(reception.getOptometristId().toString())) {
                    finishAllTrain();
                } else {
                    showMsg(getString(R.string.receive_optometrist_exit_msg_not_exit_train));
                }
            } else if (serverEmqttMessageContent instanceof SelfhelpCreateTrainTrialContent) {
                if (deviceStatus == EnumDeviceStatus.INTRODUCTION || deviceStatus == EnumDeviceStatus.WELCOME) {
                    //先切换，在去执行开始
                    //解决微信体验准备界面，全部结束按钮不出现的问题
                    changeStatus(EnumDeviceStatus.TRAINING_TRIAL);

                    ReceptionTrial receptionTrial = ((SelfhelpCreateTrainTrialContent) serverEmqttMessageContent).getReception();
                    wholeTrialTrainFragment.startTrain(receptionTrial);
                } else {
                    showMsg(getString(R.string.receive_create_self_train_not_start));
                }
            } else if (serverEmqttMessageContent instanceof PushTrainContentContent) {
                if (deviceStatus == EnumDeviceStatus.TRAINING && itemTimeType == TrainItemTimeType.TRAINING) {
                    if (trainItemNow == EnumTrainItem.REVERSAL && fragmentNow instanceof ReversalTrainFragment) {
                        //在训练阶段的时候才需要结束，如果不在训练阶段，不需要处理，否则可能出现fragmentNow为null
                        ((ReversalTrainFragment) fragmentNow).receivePushTrainContentMsg((PushTrainContentContent) serverEmqttMessageContent);
                    } else {
                        showMsg(getString(R.string.current_train_not_handle_instructions));
                    }
                } else {
                    showMsg(getString(R.string.push_train_but_not_start));
                }
            }
        } else if (emqttMessage instanceof VersionEmqtt) {
            VersionEmqtt versionEmqtt = (VersionEmqtt) emqttMessage;
            if (versionEmqtt.getMsg().getMsgType().equals("PUSH_LATEST_VERSIONS")) {
                versionUrl = versionEmqtt.getMsg().getMsgContent().getSoftwareVersionUrl();
                if (!TextUtils.isEmpty(versionEmqtt.getMsg().getMsgContent().getSoftwareVersionCode())) {
                    versionCode = Integer.valueOf(versionEmqtt.getMsg().getMsgContent().getSoftwareVersionCode());
                }
                if (this.deviceStatus == EnumDeviceStatus.TRAINING
                        || this.deviceStatus == EnumDeviceStatus.TRAINING_TRIAL) {
                    // 设备处于训练中,标识存在升级消息
                    upgradeMsg = 1;
                } else {
                    versionPresenter.checkSoftwareVersion(versionUrl, versionCode);
                }

            }
        }
    }


    /**
     * 控制结束训练
     */
    public void controlExitTrain() {
        showPostDialog(getString(R.string.loading), getString(R.string.end_train_plese_wait));
        postControlExitTrainData();
    }

    /**
     * 提交结束训练的数据
     */
    public void postControlExitTrainData() {
        ConnectTrainMessageContent connectTrainMessageContent = (ConnectTrainMessageContent) (((TrainEmqttMessage) connectEmqttMessage).getMsg().getCt());
        String token = SharePreferenceTool.getSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_DEVICE_TOKEN);
        int receptionId = connectTrainMessageContent.getReceptionId();
        trainBackUseCase.exitTrain(token, receptionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostControlExitTrain());
    }

    /**
     * 提交训练结果的监听
     */
    protected final class CallBackPostControlExitTrain extends Subscriber<ApiResult> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.d("postdata:onFailure:提交异常失败:%s", e.getMessage());
            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        postControlExitTrainData();
                    });
        }

        @Override
        public void onNext(ApiResult apiResult) {
            if (apiResult.getCode() == 0) {
                Timber.d("postdata:提交成功");

                ToastUtil.showLongToast(WelcomeActivity.this, getString(R.string.end_suc));
                postControlExitTrainSuccess();
            } else if (apiResult.getCode() == 1) {
                ToastUtil.showLongToast(WelcomeActivity.this, apiResult.getMessage());
                postControlExitTrainSuccess();
            } else {
                Timber.d("postdata:apicode:提交失败:%s", apiResult.getMessage());
                Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            postControlExitTrainData();
                        });
            }
        }
    }

    /**
     * 提交结果更新保存成功
     */
    public void postControlExitTrainSuccess() {
        hidePostDialog();

        changeStatus(EnumDeviceStatus.INTRODUCTION);
        finishTrainChangeView();
        userTrainMode = EnumUserTrainMode.CONTROL;
        connectEmqttMessage = null;
    }

    public void hideAllTrainFragment() {
        fl_fragment_stereoscope.setVisibility(View.GONE);
        fl_fragment_fractured_ruler.setVisibility(View.GONE);
        fl_fragment_reversal.setVisibility(View.GONE);
        fl_fragment_red_green_read.setVisibility(View.GONE);
        fl_fragment_approach.setVisibility(View.GONE);
        fl_fragment_r_g_variable_vector.setVisibility(View.GONE);
        fl_fragment_r_g_fixed_vector.setVisibility(View.GONE);
        fl_fragment_glance.setVisibility(View.GONE);
        fl_fragment_follow.setVisibility(View.GONE);
    }

    public void showNowFragment(EnumTrainItem trainItem) {
        if (userTrainMode == EnumUserTrainMode.SELFHELP) {
            if (trainItem == EnumTrainItem.REVERSAL) {
                banner_bar_view.setBtnFinishCurrentVisibility(View.VISIBLE);
            } else {
                banner_bar_view.setBtnFinishCurrentVisibility(View.GONE);
            }
        }
        hideAllTrainFragment();
        changeNowFragment(trainItem);
        fl_fragmentTrainNow.setVisibility(View.VISIBLE);
    }

    public void changeNowFragment(EnumTrainItem trainItem) {
        trainItemNow = trainItem;
        switch (trainItem) {
            case STEREOSCOPE:
                fl_fragmentTrainNow = fl_fragment_stereoscope;
                fragmentNow = stereoscopeTrainFragment;
                break;
            case FRACTURED_RULER:
                fl_fragmentTrainNow = fl_fragment_fractured_ruler;
                fragmentNow = fracturedRulerTrainFragment;
                break;
            case REVERSAL:
                fl_fragmentTrainNow = fl_fragment_reversal;
                fragmentNow = reversalTrainFragment;
                break;
            case RED_GREEN_READ:
                fl_fragmentTrainNow = fl_fragment_red_green_read;
                fragmentNow = redGreenReadTrainFragment;
                break;
            case APPROACH:
                fl_fragmentTrainNow = fl_fragment_approach;
                fragmentNow = approachTrainFragment;
                break;
            case R_G_VARIABLE_VECTOR:
                fl_fragmentTrainNow = fl_fragment_r_g_variable_vector;
                fragmentNow = rgVariableVectorTrainFragment;
                break;
            case R_G_FIXED_VECTOR:
                fl_fragmentTrainNow = fl_fragment_r_g_fixed_vector;
                fragmentNow = rgFixedVectorTrainFragment;
                break;
            case GLANCE:
                fl_fragmentTrainNow = fl_fragment_glance;
                fragmentNow = glanceTrainFragment;
                break;
            case FOLLOW:
                fl_fragmentTrainNow = fl_fragment_follow;
                fragmentNow = followTrainFragment;
                break;
            default:
                break;
        }
    }

    @Override
    public void sendMessage(EmqttMessage emqttMessage) {
        welcomePresenter.sendMessage(emqttMessage);
    }

    public void finishTrain() {
        if (userTrainMode == EnumUserTrainMode.CONTROL) {
            changeItemTimeType(TrainItemTimeType.WELCOME);
        } else {
            itemTrainFinish();
        }
        banner_bar_view.setBtnFinishCurrentVisibility(View.GONE);
    }

    /**
     * 刷新显示训练状态的更新
     */
    public void changeItemTimeType(TrainItemTimeType itemTimeType) {
        this.itemTimeType = itemTimeType;
        trainStageFragment.setTrainItemTimeType(this.itemTimeType);
        if (fl_fragmentTrainNow != null) {
            fl_fragmentTrainNow.setVisibility(this.itemTimeType == TrainItemTimeType.TRAINING ? View.VISIBLE : View.GONE);
        }
        if (this.itemTimeType == TrainItemTimeType.TRAINING) {
            container_time_training.setVisibility(View.VISIBLE);
            fl_fragment_train_stage.setVisibility(View.GONE);

            banner_bar_view.setFinishType(null);

            if (container_train_trial.getVisibility() == View.VISIBLE) {
                setShowItemIntroBtnVisible(View.VISIBLE);
            } else {
                setShowItemIntroBtnVisible(View.GONE);
            }

            if (SharePreferenceTool.getUserTrainMode(WelcomeActivity.this) == EnumUserTrainMode.SELFHELP) {
                //在跳转到训练的界面的时候停止监听
                stopLockTimer();
            }

        } else {
            setShowItemIntroBtnVisible(View.GONE);
            container_time_training.setVisibility(View.GONE);
            fl_fragment_train_stage.setVisibility(View.VISIBLE);

            if (this.itemTimeType == TrainItemTimeType.ALL_PRES) {
                setItemTitle("本次训练处方");
                setItemTip("");
                banner_bar_view.setFinishType(EnumButtonType.FINISH_ALL);
            } else if (this.itemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE) {
                setItemTitle("训练开始前整体准备");
                setItemTip("");
                banner_bar_view.setFinishType(EnumButtonType.FINISH_ALL);
            } else if (this.itemTimeType == TrainItemTimeType.PREPARE) {
                setItemTitle(trainPres.getShowName() + "准备");
                setItemTip("");
                banner_bar_view.setFinishType(EnumButtonType.FINISH_ALL);
            } else if (this.itemTimeType == TrainItemTimeType.WELCOME) {
                setItemTitle("训练等待准备");
                setItemTip("");
                banner_bar_view.setFinishType(null);
            } else if (this.itemTimeType == TrainItemTimeType.ONE_END || this.itemTimeType == TrainItemTimeType.ALL_END) {
                setItemTitle("训练结束休息");
                setItemTip("");
                banner_bar_view.setFinishType(EnumButtonType.FINISH_ITEM);
            }

            //跳到非训练界面就监听，如果在训练界面，超时都会自动跳转到该项目结束的，这是再监听计时
            if (SharePreferenceTool.getUserTrainMode(WelcomeActivity.this) == EnumUserTrainMode.SELFHELP) {
                //自助的时候需要监听
                stopLockTimer();
                startLockTimer();
            }
        }
    }

    @Override
    public void getTrainPresSchemeBack(TrainPresScheme strainPresScheme, boolean isSwitchTrainStatus) {
        trainStageFragment.setTrainPresScheme(strainPresScheme);
        if (isSwitchTrainStatus) {
            changeItemTimeType(TrainItemTimeType.DEVICE_OVERALL_PREPARE);
        }
    }

    /**
     * 所有用户处方展示完成
     */
    public void stepUserAllPresShowFinish() {
        changeItemTimeType(TrainItemTimeType.DEVICE_OVERALL_PREPARE);
    }

    /**
     * 设备整体准备完成
     */
    public void stepDeviceOverallPrepareFinish() {
        trainManagerPresenter.startNowTrain();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        //开始下个项目前存上上个项目
        this.trainPresPrevious = trainPres;

        this.presTrainMessageContent = presTrainMessageContent;
        this.trainPres = this.presTrainMessageContent.getTp();
        //只有优眼扫码自助开始前进入这里
        //
        if (this.trainPres instanceof ReversalTrainPres) {
            ReversalTrainPres lastReversalTrainPres = SharePreferenceTool.getLastReversalTrainPres(WelcomeActivity.this);
            if (lastReversalTrainPres != null
                    && this.trainPres != null
                    && this.trainPres.getTrainingPrescriptionSchemeId().intValue() == lastReversalTrainPres.getTrainingPrescriptionSchemeId().intValue()) {
                //如果最后一个翻转拍处方和当前翻转拍处方处于一个方案，就用上一个处方的训练结果
                ((ReversalTrainPres) this.trainPres).setTrainingContentType(lastReversalTrainPres.getTrainingContentType());
                ((ReversalTrainPres) this.trainPres).setTrainingContentCategoryId(lastReversalTrainPres.getTrainingContentCategoryId());
                ((ReversalTrainPres) this.trainPres).setTrainingContentArticleId(lastReversalTrainPres.getTrainingContentArticleId());
                ((ReversalTrainPres) this.trainPres).setTrainingContentLoopMode(lastReversalTrainPres.getTrainingContentLoopMode());
            }
        }

        itemTrainPrepare();
    }

    @Override
    public void finishAllTrain() {
        showMsg(getString(R.string.train_all_end));
        changeStatus(EnumDeviceStatus.INTRODUCTION);
        finishTrainChangeView();
        trainManagerPresenter.finishAllReception();
    }

    public void reset() {
        Timber.d("设备电机复位完成");
        ControlMessageSet messageSet = TrainUseCase.reset();

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    // adjustPupilDistance();
                    ToastUtil.showShortToast(WelcomeActivity.this, getString(R.string.reset_completion));
                });
    }

    protected Handler handlerResetFinish = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Timber.d("设备上电重置完成");
            //重置完后调整瞳距，先不重置，瞳距电机有问题
            // adjustPupilDistance();
            handlerResetFinishTip.sendEmptyMessage(0);
        }
    };

    protected Handler handlerResetFinishTip = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ToastUtil.showShortToast(WelcomeActivity.this, getString(R.string.reset_completion));
        }
    };

    public void adjustPupilDistance() {
        ControlMessageSet messageSet = TrainUseCase.adjustPupileDistance(PupilDefaultDistance);
//        messageSet.setHandler(handlerAdjustPupilDistanceFinish);
//        MotorBus.getInstance().sendMessageSet(messageSet);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    ToastUtil.showShortToast(WelcomeActivity.this, getString(R.string.interpupillary_distance_adjustment_complete));
                });
    }

    protected Handler handlerAdjustPupilDistanceFinish = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Timber.d("调整瞳距完成");
            handlerAdjustPupilDistanceFinishTip.sendEmptyMessage(0);
        }
    };

    protected Handler handlerAdjustPupilDistanceFinishTip = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ToastUtil.showShortToast(WelcomeActivity.this, getString(R.string.interpupillary_distance_adjustment_complete));
        }
    };
    boolean flag;

//    public void onButtonPress1() {
//        if (!flag) {
//            startActivity(new Intent(this, BalloonActivity.class));
//            flag = true;
//        } else
//            BalloonActivity.test();
//    }

    public void onButtonPress() {
        Timber.d("onButtonPress");

        int keyCode = KeyEvent.KEYCODE_VOLUME_UP;
        //welcome扫码界面按键无效,只考虑训练中的按键
        if (deviceStatus == EnumDeviceStatus.TRAINING) {
            if (itemTimeType == TrainItemTimeType.TRAINING) {
                if (isOnItemIntro) {
                    showItemIntroGo2NextStage();
                } else {
                    fragmentNow.onSelfKeyDown(keyCode);
                }
            } else {
                //解决按钮不在主线程刷新的问题
                Observable.just(1).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            trainStageFragment.pressEnter();
                        });
            }
        } else if (deviceStatus == EnumDeviceStatus.INTRODUCTION) {
            afterIntroductionStart();
        } else if (deviceStatus == EnumDeviceStatus.TRAINING_TRIAL) {
            wholeTrialTrainFragment.pressEnter();
        }
    }

    @Override
    public void onChangeCharge(int chargeStatus) {
        MotorBus.getInstance().chargeMotCtrl.getMotor().setChargeStatus(chargeStatus);
        Observable.just(1).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    //ToastUtil.showShortToast(WelcomeActivity.this,"充电状态改变成功："+String.valueOf(chargeStatus));
                });
    }

    /**
     * loading Dialog
     */
    protected ProgressDialog progressDialog;

    @Override
    public void showPostDialog(String title, String content) {
        //title、content作为final变量传进来可以直接用，任何在lamada表达式用的变量都是，可以再函数中定义其他变量也可以直接引入
        Observable.just(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (progressDialog != null) {
                        //如果存在先取消掉原来的，然后显示新的
                        hidePostDialog();
                    }
                    progressDialog = ProgressDialog.show(WelcomeActivity.this, title, content, true, false);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    @Override
    public void hidePostDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 显示对话框
     *
     * @param title
     * @param message
     */
    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builer = new AlertDialog.Builder(WelcomeActivity.this);
        builer.setTitle(title);
        builer.setMessage(message);
        // 当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /**
     * 单项训练准备
     */
    public void itemTrainPrepare() {
        if (trainPres != null) {
            switch (trainPres.getTrainItemType()) {
                case REVERSAL:
                    //监听依次是训练种类（文章，字母，数字），文章种类，字体大小监听
                    trainStageFragment.initPrepareData(trainPresPrevious, trainPres, this, this, null, this);
                    break;
                case RED_GREEN_READ:
                    trainStageFragment.initPrepareData(trainPresPrevious, trainPres, null, this, this, this);
                    break;
                case FOLLOW:
                case GLANCE:
                    trainStageFragment.initPrepareData(trainPresPrevious, trainPres, null, this, null, this);
                    break;
                default:
                    trainStageFragment.initPrepareData(trainPresPrevious, trainPres, null, null, null, this);
                    break;
            }
        }
        changeItemTimeType(TrainItemTimeType.PREPARE);
        prepareItemIntroView();
    }

    public void itemTrainTraining() {
        changeItemTimeType(TrainItemTimeType.TRAINING);

        showNowFragment(presTrainMessageContent.getTp().getTrainItemType());
        fragmentNow.receivePresContent(presTrainMessageContent);
//        String showName = String.format("%s 第%s遍",presTrainMessageContent.getTp().getShowName(),presTrainMessageContent.getCurrentRepeatTime());
//        if(presTrainMessageContent.getTp() instanceof ReversalTrainPres){
//            showName += " " + ((ReversalTrainPres)presTrainMessageContent.getTp()).getEyeType().getName() + "眼";
//        }
//
//        testTrainFragment.setItemTrainDesc(showName);
//        fl_fragment_test_train.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishViewClick() {
        trainManagerPresenter.startNextTrain();
    }

    /**
     * 项目结束
     */
    public void itemTrainFinish() {
        Boolean isItemAllEnd = trainManagerPresenter.itemTrainFinish();
        if (isItemAllEnd == null) {
            //如果是null，只有用户自助远程结束训练，不用处理，前面已经先跳到扫码页面了
        } else if (isItemAllEnd) {
            TrainPres trainPresPre = trainManagerPresenter.getNowTrainPres();
            TrainPres trainPresNext = trainManagerPresenter.getNextTrainPres();
            String[] finishDescList = FinishDescManager.getFinishDescList(trainPresPre, trainPresNext);
            String finishContent = TextUtils.join("\n\n", finishDescList);
            trainStageFragment.refreshAllFinishView(finishContent);

            itemTrainAllEnd();
        } else {
            itemTrainOneEnd();
        }
    }

    /**
     * 小项单次结束，
     */
    public void itemTrainOneEnd() {
        //如果是翻转拍，直接开始一个新的训练，到新的prepare界面，这样就不会出现one_end界面显示的还是右眼
        //而不是到one_end的界面
        if (trainPres.getTrainItemType() == EnumTrainItem.REVERSAL) {
            onFinishViewClick();
        } else {
            changeItemTimeType(TrainItemTimeType.ONE_END);
        }
    }

    /**
     * 小项全部结束
     */
    public void itemTrainAllEnd() {
        changeItemTimeType(TrainItemTimeType.ALL_END);
    }

    /**
     * 准备界面点击完成
     */
    public void prepareFinish() {
        itemTrainTraining();
    }

    /**
     * 小项单次结束的准备界面完成
     */
    public void oneEndFinish() {
        trainManagerPresenter.startNextTrain();
        itemTrainTraining();
    }

    @Override
    public void changeLeft(int level) {
        ((ReversalTrainPres) trainPres).setLNegativeDegreeLevel(level);
        ((ReversalTrainPres) trainPres).setLPositiveDegreeLevel(level);
    }

    @Override
    public void changeRight(int level) {
        ((ReversalTrainPres) trainPres).setRNegativeDegreeLevel(level);
        ((ReversalTrainPres) trainPres).setRPositiveDegreeLevel(level);
    }

    @Override
    public void onSelectContent(int trainingContentType, int trainingContentArticleId, String title) {
        if (trainPres != null) {
            switch (trainPres.getTrainItemType()) {
                case REVERSAL:
                    ((ReversalTrainPres) trainPres).setTrainingContentType(EnumContentType.values()[trainingContentType]);
                    ((ReversalTrainPres) trainPres).setTrainingContentArticleId(trainingContentArticleId);
                    break;
                case RED_GREEN_READ:
                    ((RedGreenReadTrainPres) trainPres).setTrainingContentType(EnumContentType.values()[trainingContentType]);
                    ((RedGreenReadTrainPres) trainPres).setTrainingContentArticleId(trainingContentArticleId);
                    break;
                case FOLLOW:
                    ((FollowTrainPres) trainPres).setLineType(EnumLineType.values()[trainingContentType]);
                    break;
                case GLANCE:
                    ((GlanceTrainPres) trainPres).setTrainingContentType(EnumContentType.values()[trainingContentType]);
                    ((GlanceTrainPres) trainPres).setTrainingContentArticleId(trainingContentArticleId);
                    break;

            }
        }
    }

    @Override
    public void onSelectUserContent(EnumContentType trainingContentType, int trainingContentCategoryId, int trainingContentArticleId, String showTitle) {
        if (trainPres != null) {
            switch (trainPres.getTrainItemType()) {
                case REVERSAL:
                    ((ReversalTrainPres) trainPres).setTrainingContentType(trainingContentType);
                    ((ReversalTrainPres) trainPres).setTrainingContentCategoryId(trainingContentCategoryId);
                    ((ReversalTrainPres) trainPres).setTrainingContentArticleId(trainingContentArticleId);
                    break;
            }
        }
    }

    @Override
    public void onTxtSizeChange(int size) {
        ((RedGreenReadTrainPres) trainPres).setLetterSize(size);
    }

    /**
     * 设置标题
     *
     * @param itemTitle
     */
    @Override
    public void setItemTitle(String itemTitle) {
        if (itemTitle == null || itemTitle.equals("")) {
            banner_bar_view.setVisibility(View.GONE);
        } else {
            banner_bar_view.setItemTitle(itemTitle);
            banner_bar_view.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 设置item提示
     * 如果为空，则自动隐藏
     *
     * @param itemTip
     */
    @Override
    public void setItemTip(String itemTip) {
        banner_bar_view.setItemTip(itemTip);
    }

    /**
     * 设置banner的finishTyp
     *
     * @param buttonType
     */
    public void setFinishType(EnumButtonType buttonType) {
        banner_bar_view.setFinishType(buttonType);
    }

    /**
     * 设置显示项目介绍的按钮是否显示
     *
     * @param visible
     */
    public void setShowItemIntroBtnVisible(int visible) {
        banner_bar_view.setShowItemIntroBtnVisible(visible);
    }

    @Override
    public void finish(EnumButtonType finishType) {
        Timber.d("finishType:%s", finishType);
        if (finishType != null) {
            if (finishType == EnumButtonType.FINISH_ALL) {
                if (deviceStatus == EnumDeviceStatus.TRAINING_TRIAL) {
                    wholeTrialTrainFragment.munualFinishTrain();
                } else {
                    trainManagerPresenter.finishTrain();
                }
            } else if (finishType == EnumButtonType.FINISH_ITEM) {
                if (deviceStatus == EnumDeviceStatus.TRAINING_TRIAL) {
                    wholeTrialTrainFragment.onFinishViewClick();
                } else {
                    trainManagerPresenter.finishItemTrain();
                }
            } else if (finishType == EnumButtonType.REFRESH_CODE) {
                showCodeFragment.getQrcode();
            }
        }
    }

    /**
     * 获取当前接待
     */
    public void getNowReception() {
        String token = SharePreferenceTool.getSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_DEVICE_TOKEN);
        loginUseCase.getNowReception(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        getNowReceptionBack(apiResult.getData());
                    } else {
                        showMsg(getString(R.string.get_msg_abnormal));
                    }
                }, throwable -> {
                    //网络异常，需要重新连接，隔1s尝试重连
                    Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                getNowReception();
                            });
                });
    }

    public void getNowReceptionBack(Reception reception) {
        if (isFirstIn) {
            //第一次进入
            isFirstIn = false;
            //不等于空，代表当前有接待
            //未结束的训练
            if (reception != null && reception.getReceptStep() != -1) {
                if (reception.getOptometristId() == 0) {
                    //设置用户训练模式
                    userTrainMode = EnumUserTrainMode.SELFHELP;
                    SharePreferenceTool.setSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_USER_TRAIN_MODE, userTrainMode.toString());

                    //属于自助的没有训练师
                    //之前存的enumusermode，还是自助
                    trainManagerPresenter.startWithReception(reception);
                    changeStatus(EnumDeviceStatus.TRAINING);
                } else if (reception.getOptometristId() != 0) {
                    //设置用户训练模式
                    userTrainMode = EnumUserTrainMode.CONTROL;
                    SharePreferenceTool.setSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_USER_TRAIN_MODE, userTrainMode.toString());

                    //有视光师该控制的，回到welcome页面，重新发送处方
                    changeStatus(EnumDeviceStatus.TRAINING);
                    changeItemTimeType(TrainItemTimeType.WELCOME);

                    //根据reception的信息重新初始化connectEmqttMessage的信息，解决视光师重新发送消息的问题
                    ConnectTrainMessageContent connectTrainMessageContent = new ConnectTrainMessageContent();
                    connectTrainMessageContent.setReceptionId(reception.getId());

                    TrainMessage trainMessage = new TrainMessage();
                    trainMessage.setTmt(TrainMessageType.CONNECT);
                    trainMessage.setCt(connectTrainMessageContent);

                    TrainEmqttMessage trainEmqttMessage = new TrainEmqttMessage();
                    trainEmqttMessage.setStp("1");
                    trainEmqttMessage.setSid(reception.getOptometristId().toString());
                    trainEmqttMessage.setRtp("2");
                    trainEmqttMessage.setRid(reception.getDeviceId().toString());
                    trainEmqttMessage.setMsg(trainMessage);
                    connectEmqttMessage = trainEmqttMessage;
                }
            }
        } else {
            //以后掉线重新进入,只需要判断结束的情况
            if (reception.getReceptStep() == -1) {
                if (itemTimeType == TrainItemTimeType.TRAINING) {
                    //在训练阶段的时候才需要结束，如果不在训练阶段，不需要处理，否则可能出现fragmentNow为null
                    fragmentNow.receiveFinishServerMessage();
                }
                finishAllTrain();
            }
        }
    }

    /**
     * 普通安装
     *
     * @param file
     */
    @Override
    public void installSoftware(File file) {
        Intent intent = new Intent();
        //安装完后自动打开，不加的话，不会
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                String address = data.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS);
                SharePreferenceTool.setSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_CONNECT_BLUTTOOTH_UID, address);
                SharePreferenceTool.setSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_DUID, address);

                chooseBtSuccessBack();

                //updateBtidInfo(address);
                //MotorBluetoothAdapter.getInstance().connect(address);
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 更新蓝牙地址
     *
     * @param address
     */
    public void updateBtidInfo(String address) {
        String device_id = SharePreferenceTool.getSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_DEVICE_ID);
        String device_token = SharePreferenceTool.getSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_DEVICE_TOKEN);
        if (!device_id.equals("")) {
            HashMap<String, String> hashMapDeviceInfo = new HashMap<>();
            hashMapDeviceInfo.put("btid", address);
            loginUseCase.updateDeviceInfo(Integer.valueOf(device_id), device_token, hashMapDeviceInfo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(apiResult -> {
                        if (apiResult.getCode() == 0) {
                            Timber.d("提交蓝牙信息成功");
                        }
                    }, error -> {
                        Timber.d("提交蓝牙信息失败：%s", error.getMessage());
                    });
        }
    }

    public void setup() {
        checkChooseBluetooth();
    }

    /**
     * 静默安装
     */
    @Override
    public void silentInstallSoftware(File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process process = null;
                OutputStream out = null;
                try {
                    // 请求root
                    process = Runtime.getRuntime().exec("su");
                    out = process.getOutputStream();
                    // 调用安装
                    out.write(("pm install -r " + file.getAbsolutePath() + "\n").getBytes());
                    // 打开软件
                    out.write(("am start -n com.uyu.device.devicetraining/com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void loginSuccess() {
        changeStatus(EnumDeviceStatus.CHECK_AUTH);
    }

    public void go2Login() {
        changeStatus(EnumDeviceStatus.LOGIN);
    }

    public void mqttLost() {
        if (deviceStatus != EnumDeviceStatus.CHECK_AUTH) {
            checkAuthFragment.checkAuth();
        }
        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                banner_bar_view.setLogoStatus(1);
            }
        }, 0);

    }

    public void mqttConnectSuccess() {
        String deviceAlias = SharePreferenceTool.getSharePreferenceValue(WelcomeActivity.this, SharePreferenceTool.PREF_DEVICE_ALIAS);
        banner_bar_view.setDeviceName(deviceAlias);

        if (isFirstIn) {
            //第一次进，校验通过连接蓝牙
            connectBt();

            changeStatus(EnumDeviceStatus.INTRODUCTION);
            getNowReception();
        } else {
            //当前不在自助训练，才需要获取，否则不需要会自动停止
            if (deviceStatus != EnumDeviceStatus.TRAINING_TRIAL) {
                getNowReception();
            }
        }
        banner_bar_view.setLogoStatus(0);
    }

    public void mqttConnectFail() {
        if (deviceStatus == EnumDeviceStatus.CHECK_AUTH) {
            checkAuthFragment.showNetworkExceptionDialog();
        } else {
            //网络异常，需要重新连接，隔1s尝试重连
            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        checkAuthFragment.checkAuth();
                    });
        }
        banner_bar_view.setLogoStatus(1);
    }

    public void networkException() {
        if (deviceStatus == EnumDeviceStatus.CHECK_AUTH) {
            checkAuthFragment.showNetworkExceptionDialog();
        } else {
            //网络异常，需要重新连接，隔1s尝试重连
            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        checkAuthFragment.checkAuth();
                    });
        }
    }

    public void checkAuthFail() {
        if (deviceStatus == EnumDeviceStatus.CHECK_AUTH) {
            go2ConnectBt();
        } else {
            ToastUtil.showLongToast(WelcomeActivity.this, getString(R.string.other_pad_connect_this_device));
            introductionFragment.changeDeviceStatusStopSpeak();
            finish();
        }
    }

    /**
     * 去连接蓝牙
     */
    public void go2ConnectBt() {
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    /**
     * 选择蓝牙蓝牙成功回来
     */
    public void chooseBtSuccessBack() {
        changeStatus(EnumDeviceStatus.LOGIN);
    }

    public void afterIntroductionStart() {
        changeStatus(EnumDeviceStatus.WELCOME);
    }

    public void inNowPageTimeOut() {
        if (deviceStatus == EnumDeviceStatus.WELCOME) {
            changeStatus(EnumDeviceStatus.INTRODUCTION);
        }
    }

    private Observable<Long> observableLock = null;
    private Subscriber<Long> subscriberLock = null;
    private Subscription subscriptionLock = null;

    private void startLockTimer() {
        Timber.d("startLockTimer");
        observableLock = Observable.timer(TrainConfig.NoOptTrainLockTime, TimeUnit.SECONDS);
        subscriberLock = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                lockTrain();
            }
        };

        subscriptionLock = observableLock.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberLock);
    }

    private void stopLockTimer() {
        Timber.d("stopLockTimmer");
        if (subscriptionLock != null && !subscriptionLock.isUnsubscribed()) {
            subscriptionLock.unsubscribe();
        }
        if (subscriberLock != null && !subscriberLock.isUnsubscribed()) {
            subscriberLock.unsubscribe();
        }
        subscriptionLock = null;
        subscriberLock = null;
        observableLock = null;
    }

    public void lockTrain() {
        if (deviceStatus == EnumDeviceStatus.TRAINING && SharePreferenceTool.getUserTrainMode(WelcomeActivity.this) == EnumUserTrainMode.SELFHELP) {
            if (itemTimeType == TrainItemTimeType.TRAINING) {
                fragmentNow.pauseTrain();
            }
            changeStatus(EnumDeviceStatus.LOCK);
        }
    }

    public void unlockTrain() {
        if (deviceStatus == EnumDeviceStatus.LOCK && SharePreferenceTool.getUserTrainMode(WelcomeActivity.this) == EnumUserTrainMode.SELFHELP) {
            if (itemTimeType == TrainItemTimeType.TRAINING) {
                fragmentNow.resumeTrain();
            }
            changeStatus(EnumDeviceStatus.TRAINING);

            //解锁以后继续监听-否则会出现解锁后不会继续锁定了
            //跳到非训练界面就监听，如果在训练界面，超时都会自动跳转到该项目结束的，这是再监听计时
            if (SharePreferenceTool.getUserTrainMode(WelcomeActivity.this) == EnumUserTrainMode.SELFHELP) {
                //自助的时候需要监听
                stopLockTimer();
                startLockTimer();
            }
        }
    }

    public void noOptFinishTrain() {
        trainManagerPresenter.finishTrain();
    }

    public void finishTrialTrain() {
        showMsg("体验训练全部结束");
        changeStatus(EnumDeviceStatus.INTRODUCTION);
        finishTrainChangeView();
    }

    /**
     * 结束以后更新界面
     */
    public void finishTrainChangeView() {
        //showMsg("结束后更新界面-调整电机");
        reset();
    }

    /**
     * 显示网络超时错误的
     */
    @Override
    public void showNetworkExceptionDialog(DialogInterface.OnClickListener retryClickListener) {
        Dialog alertDialog = new AlertDialog.Builder(WelcomeActivity.this).
                setTitle(R.string.prompt).
                setMessage(R.string.network_timeout).
                setIcon(R.mipmap.ic_launcher).
                setPositiveButton(R.string.reconnect
                        , retryClickListener).
                create();
        alertDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        alertDialog.show();
    }

    /**
     * 是否在显示项目介绍的界面
     */
    private boolean isOnItemIntro = false;
    private PopupWindow showItemIntroPopupWindow;
    private ShowItemIntroView showItemIntroView;

    /**
     * 准备项目准备的view
     * 解决点击后延迟的问题
     */
    public void prepareItemIntroView() {
        showItemIntroPopupWindow = new PopupWindow(WelcomeActivity.this);
        showItemIntroPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        showItemIntroPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        showItemIntroPopupWindow.setOutsideTouchable(true);
        showItemIntroPopupWindow.setTouchable(true);
        showItemIntroPopupWindow.setFocusable(true);
        showItemIntroPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));// 响应返回键必须的语句。

        showItemIntroView = new ShowItemIntroView(WelcomeActivity.this);
        //准备数据
        List<PrepareDesc> descList = PrepareDescManager.getPrepareDescList(trainPresPrevious, trainPres);
        showItemIntroView.setDescList(descList, trainPres);
        showItemIntroView.setmListener(this);

        //用这种写法才行，popupwindow需要监听按键，同时rl_root在xml里面也必须设置fucusable
        //返回键不知道为什么不起作用
        //在develop版本中不用加这个
//        RelativeLayout showItemIntroViewRoot = (RelativeLayout) showItemIntroView.findViewById(R.id.rl_root);
//        showItemIntroViewRoot.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent event) {
//                //必须是按下，否则会发两次，up一次down一次
//                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//                    onButtonPress();
//                    return true;
//                }else if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
//                    onCloseItemIntro();
//                    return true;
//                }
//                return true;
//            }
//        });

        showItemIntroPopupWindow.setContentView(showItemIntroView);
    }

    /**
     * 显示当前项目的使用介绍
     */
    @Override
    public void showItemIntro() {
        if (deviceStatus == EnumDeviceStatus.TRAINING_TRIAL) {
            wholeTrialTrainFragment.showItemIntro();
        } else if (deviceStatus == EnumDeviceStatus.TRAINING) {
            if (itemTimeType == TrainItemTimeType.TRAINING) {
                fragmentNow.pauseTrain();

                isOnItemIntro = true;
                showItemIntroView.reset();
                showItemIntroPopupWindow.showAtLocation(container_train, Gravity.CENTER, 0, 0);
            }
        }
    }

    @Override
    public void onCloseItemIntro() {
        if (deviceStatus == EnumDeviceStatus.TRAINING_TRIAL) {
            wholeTrialTrainFragment.onCloseItemIntro();
        } else if (deviceStatus == EnumDeviceStatus.TRAINING) {
            if (itemTimeType == TrainItemTimeType.TRAINING) {
                fragmentNow.resumeTrain();
                isOnItemIntro = false;
                showItemIntroPopupWindow.dismiss();
            }
        }
    }

    /**
     * 显示项目介绍的控件跳转到下一个步骤
     */
    public void showItemIntroGo2NextStage() {
        showItemIntroView.go2NextStage();
    }


    @Override
    public void showUpgradeView(int status) {
        if (showUpgradeView == null) {
            showUpgradeView = new ShowUpgradeView(this);
        }
        if (status == 1) {
            showUpgradeView.showAtLocation(root, Gravity.CENTER, 0, 0);
        } else if (status == 0) {
            showUpgradeView.dismiss();
        }
    }

    @Override
    public void finishCurrent() {
        if (presTrainMessageContent.getTp().getTrainItemType() == EnumTrainItem.REVERSAL) {
            ((ReversalTrainPresenter) fragmentNow.getTrainPresenter()).finishTrain();
            setItemTip("");
        }
    }
}
