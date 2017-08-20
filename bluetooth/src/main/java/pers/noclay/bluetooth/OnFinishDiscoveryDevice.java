package pers.noclay.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by i-gaolonghai on 2017/8/18.
 */

public interface OnFinishDiscoveryDevice {
    void onFinish(List<BluetoothDevice> deviceList);
}
