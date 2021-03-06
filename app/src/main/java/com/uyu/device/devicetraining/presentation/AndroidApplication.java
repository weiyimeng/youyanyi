package com.uyu.device.devicetraining.presentation;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.emqtt.SslUtility;
import com.uyu.device.devicetraining.domain.bluetooth.tradition.BltConnectService;
import com.uyu.device.devicetraining.domain.bluetooth.tradition.MotorBluetoothAdapter;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.presentation.internal.di.components.ApplicationComponent;
import com.uyu.device.devicetraining.presentation.internal.di.components.DaggerApplicationComponent;
import com.uyu.device.devicetraining.presentation.internal.di.modules.ApplicationModule;
import com.uyu.device.devicetraining.presentation.util.ConstantValues;
import com.uyu.device.devicetraining.presentation.util.CrashHandler;
import com.uyu.device.devicetraining.presentation.util.FakeCrashLibrary;
import com.uyu.device.devicetraining.presentation.localvoice.SpeechService;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.FileUtils;

import timber.log.Timber;

/**
 * Created by windern on 2015/11/28.
 */
public class AndroidApplication extends Application implements ServiceConnection {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //??????????????????
        FlowManager.init(new FlowConfig.Builder(this).build());

        if (ConstantValues.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
        Timber.tag("DeviceTraining");//????????????????????????Tag

        MotorBus.getInstance();

        //?????????????????????
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_xf_releaseId));//
        Setting.setShowLog(true);

        startSpeechService();
        bindSpeechService();

        startBltConnectService();
        bindBltConnectService();

        SslUtility.newInstance(this);

        initializeInjector();

        //??????crash????????????
        CrashHandler handler = CrashHandler.getInstance();
        handler.regiestExceptionTracker(this);
        //????????????????????????
        LeakCanary.install(this);
        initImageLoadGlobalConfig();


        AppProvider.init(this);
        RawProvider.init(this);
    }

    private void initImageLoadGlobalConfig() {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
        DiskCache cache = new UnlimitedDiscCache(FileUtils.getImageCache());
        //???????????????????????????
        builder.diskCache(cache);
        //?????????????????????????????????
        builder.diskCacheFileCount(500);
        //?????????????????????????????????????????????
        builder.diskCacheSize(50 * 1024 * 1024);
        // ????????????????????????????????????????????????
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 8;
        //???????????????????????????
        builder.memoryCacheSize(maxSize);
        //??????????????????????????????
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        ImageLoader.getInstance().init(builder.build());
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent(){
        return this.applicationComponent;
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

    //??????????????????????????????
    private SpeechService speechService;
    public void startSpeechService () {
        Intent it = new Intent(this, SpeechService.class);
        startService(it);
    }
    public void stopSpeechService () {
        Intent it = new Intent(this, SpeechService.class);
        stopService(it);
    }
    private void bindSpeechService () {
        Intent it = new Intent (this, SpeechService.class);
        this.bindService(it, this, Service.BIND_AUTO_CREATE);
    }
    private void unbindSpeechService () {
        this.unbindService(this);
    }

    //BltConnect??????
    private BltConnectService bltConnectService;
    public void startBltConnectService () {
        Intent it = new Intent(this, BltConnectService.class);
        startService(it);
    }
    public void stopBltConnectService () {
        Intent it = new Intent(this, BltConnectService.class);
        stopService(it);
    }
    private void bindBltConnectService () {
        Intent it = new Intent (this, BltConnectService.class);
        this.bindService(it, this, Service.BIND_AUTO_CREATE);
    }
    private void unbindBltConnectService () {
        this.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (service instanceof SpeechService.SpeechServiceBinder) {
            SpeechService.SpeechServiceBinder binder = (SpeechService.SpeechServiceBinder)service;
            speechService = binder.getService();
        }

        if (service instanceof BltConnectService.BltConnectServiceBinder) {
            Timber.d("windern:onServiceConnected BltConnectService");
            BltConnectService.BltConnectServiceBinder binder = (BltConnectService.BltConnectServiceBinder)service;
            bltConnectService = binder.getService();
            MotorBluetoothAdapter.getInstance().setBltConnectService(bltConnectService);
            bltConnectService.startListen();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Toast.makeText(this, "onServiceDisconnected name=" + name, Toast.LENGTH_LONG).show();
    }

    public SpeechService getSpeechService(){
        return speechService;
    }

    public BltConnectService getBltConnectService(){
        return bltConnectService;
    }

}
