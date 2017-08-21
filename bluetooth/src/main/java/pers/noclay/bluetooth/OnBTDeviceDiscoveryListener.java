package pers.noclay.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

/**
 * Created by i-gaolonghai on 2017/8/20.
 */

public interface OnBTDeviceDiscoveryListener {
    void onBTAdapterDiscoveryFinished(Intent intent);
    void onBTAdapterDiscoveryStarted(Intent intent);
    void onFoundedBluetoothDevice(Intent intent);
    void onFoundedBluetoothDevice(BluetoothDevice device);
}
