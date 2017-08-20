package pers.noclay.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by i-gaolonghai on 2017/8/18.
 */

public class BluetoothReceiver extends BroadcastReceiver{
    private static final String TAG = "BluetoothReceiver";
    private List<BluetoothDevice> deviceFounded;

    public BluetoothReceiver(List<BluetoothDevice> deviceFounded) {
        this.deviceFounded = deviceFounded;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {

            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED: {
                Log.d(TAG, "onReceive: 结束查找设备");
                break;
            }
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED: {
                Log.d(TAG, "onReceive: 开始查找设备");
                break;
            }
            case BluetoothDevice.ACTION_FOUND: {
                /* 从intent中取得搜索结果数据 */
                Log.d(TAG, "onReceive: 查找到设备");
                deviceFounded.add((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                break;
            }
        }
    }
}
