package com.uyu.device.devicetraining.domain.bluetooth.tradition;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.domain.bluetooth.ComConfig;
import com.uyu.device.devicetraining.domain.bluetooth.ComFrame;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.presentation.AppProvider;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.library.BluetoothSPP;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import timber.log.Timber;

/**
 * 蓝牙中间连接器，管理蓝牙的状态，发送、接收消息
 * Created by windern on 2016/3/22.
 */
public class MotorBluetoothAdapter {
    private static MotorBluetoothAdapter instatnce = null;

    public static MotorBluetoothAdapter getInstance() {
        if (instatnce == null) {
            instatnce = new MotorBluetoothAdapter();
        }
        return instatnce;
    }

    private MotorBluetoothAdapter() {

    }

    private boolean mConnected = false;

    public boolean ismConnected() {
        return mConnected;
    }

    private BluetoothSPP bt;
    private BltConnectService bltConnectService;

    public BluetoothSPP getBt() {
        return bt;
    }

    public void setBt(BluetoothSPP bt) {
        this.bt = bt;
    }

    public BltConnectService getBltConnectService() {
        return bltConnectService;
    }

    public void setBltConnectService(BltConnectService bltConnectService) {
        this.bltConnectService = bltConnectService;
    }

    /**
     * 连接蓝牙
     *
     * @param address
     */
    public void connect(String address) {
        Timber.d("windern:connect:%s", address);
        if (bt != null) {
            bt.connect(address);
        }
    }

    /**
     * 蓝牙连接成功
     */
    public void connectedSuccess() {
        Timber.d("windern:connectedSuccess");
        mConnected = true;
        if (bltConnectService != null) {
            ToastUtil.showShortToast(bltConnectService.getApplicationContext(), AppProvider.getApplication().getString(R.string.Bluetooth_connection_success));
            bltConnectService.stopListen();
        }

        //连接成功后，直接发送当前要处理的消息
        MotorBus.getInstance().handleNowMessageSet();
    }

    /**
     * 蓝牙连接失败
     */
    public void connectLost() {
        Timber.d("windern:connectLost");
        mConnected = false;
        if (bltConnectService != null) {
            ToastUtil.showShortToast(bltConnectService.getApplicationContext(), AppProvider.getApplication().getString(R.string.bluetooth_lost));
            bltConnectService.startListen();
        }
    }

    /**
     * 发送蓝牙字节消息
     * 如果超过20个字节，分多次发送
     *
     * @param WriteBytes
     * @return
     */
    public boolean sendFromMotorBus(byte[] WriteBytes) {
        if (ismConnected()) {
            byte[] value = new byte[20];
            value[0] = (byte) 0x00;

            int loopCount = (WriteBytes.length % 20 == 0) ? (WriteBytes.length / 20) : (WriteBytes.length / 20 + 1);

            for (int i = 0; i < loopCount; i++) {
                byte[] copywrite = Arrays.copyOfRange(WriteBytes, i * 20, (i + 1) * 20);
                if (i == loopCount - 1) {
                    int leftCount = WriteBytes.length % 20;
                    if (leftCount > 0) {
                        copywrite = Arrays.copyOfRange(WriteBytes, i * 20, i * 20 + leftCount);
                    }
                }

                Timber.d("windern:send:copywrite:%s", ComConfig.bytesToString(copywrite));
                bt.send(copywrite, false);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 接收蓝牙字节消息
     *
     * @param bytes
     */
    public void receiveFromBluetooth(byte[] bytes) {
        Timber.d("windern:receiveFromBluetooth:%s", ComConfig.bytesToString(bytes));
        ComFrame.getInstance().receiveBytes(bytes);
    }
}
