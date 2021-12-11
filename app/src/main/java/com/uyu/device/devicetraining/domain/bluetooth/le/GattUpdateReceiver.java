package com.uyu.device.devicetraining.domain.bluetooth.le;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

/**
 * Created by windern on 2016/3/22.
 */
public class GattUpdateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Timber.d("GattUpdateReceiver:action:%s",action);
        if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            MotorBluetoothAdapter.getInstance().connectedSuccess();
        } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
            MotorBluetoothAdapter.getInstance().connectLost();
        } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            // Show all the supported services and characteristics on the user interface.
            MotorBluetoothAdapter.getInstance().displayGattServices();
        } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
            boolean isValid = intent.getBooleanExtra(BluetoothLeService.DATA_AVAILABLE_FLAG,false);
            if(isValid) {
                byte[] bytes = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                if(bytes!=null && bytes.length>0) {
                    MotorBluetoothAdapter.getInstance().receiveFromBluetooth(bytes);
                }
            }
        }
    }
}
