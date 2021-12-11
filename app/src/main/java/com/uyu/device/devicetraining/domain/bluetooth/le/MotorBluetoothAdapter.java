package com.uyu.device.devicetraining.domain.bluetooth.le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.uyu.device.devicetraining.domain.bluetooth.ComConfig;
import com.uyu.device.devicetraining.domain.bluetooth.ComFrame;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * 蓝牙中间连接器，管理蓝牙的状态，发送、接收消息
 * Created by windern on 2016/3/22.
 */
public class MotorBluetoothAdapter {
    //可以通信的通道的uuid
    private final static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private static MotorBluetoothAdapter instatnce = null;
    public static MotorBluetoothAdapter getInstance(){
        if(instatnce==null){
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

    private BluetoothGattCharacteristic ableGattCharacteristic;
    private BluetoothLeService bluetoothLeService;
    private BltConnectService bltConnectService;

    public void setBluetoothLeService(BluetoothLeService bluetoothLeService) {
        this.bluetoothLeService = bluetoothLeService;
    }

    public BluetoothLeService getBluetoothLeService() {
        return bluetoothLeService;
    }

    public BltConnectService getBltConnectService() {
        return bltConnectService;
    }

    public void setBltConnectService(BltConnectService bltConnectService) {
        this.bltConnectService = bltConnectService;
    }

    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        b = null;
        return b2;
    }

    public void displayGattServices() {
        List<BluetoothGattService> gattServices = bluetoothLeService.getSupportedGattServices();
        if (gattServices == null) return;

        for (BluetoothGattService gattService : gattServices) {
            //-----Service的字段信息-----//  
            int type = gattService.getType();
            Timber.d("-->service type:" + type);
            Timber.d("-->includedServices size:" + gattService.getIncludedServices().size());
            Timber.d("-->service uuid:" + gattService.getUuid());

            //-----Characteristics的字段信息-----//  
            List<BluetoothGattCharacteristic> gattCharacteristics =gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic  gattCharacteristic: gattCharacteristics) {
                Timber.d("---->char uuid:" + gattCharacteristic.getUuid());

                int permission = gattCharacteristic.getPermissions();
                Timber.d("---->char permission:" + permission);

                int property = gattCharacteristic.getProperties();
                Timber.d("---->char property:" + property);

                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Timber.d("---->char value:" + new String(data));
                }

                //UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic  
                if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA)){
                    ableGattCharacteristic = gattCharacteristic;

                    //监听这个通信的口的回复
                    bluetoothLeService.setCharacteristicNotification(
                            ableGattCharacteristic, true);
                }
            }
        }//  

        MotorBus.getInstance().handleNowMessageSet();
    }

    public boolean connect(String address){
        return bluetoothLeService.connect(address);
    }

    public void connectedSuccess(){
        ToastUtil.showShortToast(bluetoothLeService.getApplicationContext(),"蓝牙连接成功");
        Timber.d("connectedSuccess");
        mConnected = true;
        if(bltConnectService!=null) {
            bltConnectService.stopListen();
        }
    }

    public void connectLost(){
        ToastUtil.showShortToast(bluetoothLeService.getApplicationContext(),"蓝牙丢失");
        Timber.d("connectLost");
        mConnected = false;
        if(bltConnectService!=null) {
            bltConnectService.startListen();
        }
    }

    public boolean sendFromMotorBus(byte[] WriteBytes){
        if(bluetoothLeService!=null && ableGattCharacteristic!=null && mConnected) {
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

                ableGattCharacteristic.setValue(value[0],
                        BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                ableGattCharacteristic.setValue(copywrite);

                Timber.d("windern:send:copywrite:%s", ComConfig.bytesToString(copywrite));

                bluetoothLeService.writeCharacteristic(ableGattCharacteristic);
            }
            return true;
        }else{
            return false;
        }
    }

    public void receiveFromBluetooth(byte[] bytes){
        Timber.d("windern:receiveFromBluetooth:%s",ComConfig.bytesToString(bytes));
        ComFrame.getInstance().receiveBytes(bytes);
    }
}
