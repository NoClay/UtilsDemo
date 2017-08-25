package pers.noclay.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i-gaolonghai on 2017/8/18.
 */

public final class BluetoothReceiver extends BroadcastReceiver{
    private static final String TAG = "BluetoothReceiver";
    private List<BluetoothDevice> mOnlineDevices;
    private OnBTDeviceDiscoveryListener mOnBTDeviceDiscoveryListener;
    private OnFinishDiscoveryDevice mOnFinishDiscoveryDevice;
    private OnBTBroadCastListener mBroadCastListener;
    private ABSCreateBondStrategy mCreateBondStrategy;

    public OnBTDeviceDiscoveryListener getOnBTDeviceDiscoveryListener() {
        return mOnBTDeviceDiscoveryListener;
    }

    public void setOnBTDeviceDiscoveryListener(OnBTDeviceDiscoveryListener onBTDeviceDiscoveryListener) {
        mOnBTDeviceDiscoveryListener = onBTDeviceDiscoveryListener;
    }

    public OnBTBroadCastListener getBroadCastListener() {
        return mBroadCastListener;
    }

    public void setBroadCastListener(OnBTBroadCastListener broadCastListener) {
        mBroadCastListener = broadCastListener;
    }

    public ABSCreateBondStrategy getCreateBondStrategy() {
        return mCreateBondStrategy;
    }

    public void setCreateBondStrategy(ABSCreateBondStrategy createBondStrategy) {
        mCreateBondStrategy = createBondStrategy;
    }

    public OnFinishDiscoveryDevice getOnFinishDiscoveryDevice() {
        return mOnFinishDiscoveryDevice;
    }

    public void setOnFinishDiscoveryDevice(OnFinishDiscoveryDevice onFinishDiscoveryDevice) {
        mOnFinishDiscoveryDevice = onFinishDiscoveryDevice;
    }

    public BluetoothReceiver() {
        mOnlineDevices = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: action = " + action);
        if (mBroadCastListener != null){
            mBroadCastListener.onBTBroadCastReceive(intent);
        }
        switch (action) {
            case BluetoothDevice.ACTION_PAIRING_REQUEST: {
                if (mCreateBondStrategy != null){
                    mCreateBondStrategy.onBTDevicePairingRequest(intent);
                }
                break;
            }
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED: {
                if (mCreateBondStrategy != null){
                    mCreateBondStrategy.onBTDeviceBondStateChanged(intent);
                }
                break;
            }
            case BluetoothDevice.ACTION_FOUND: {
                /* 从intent中取得搜索结果数据 */
                if (mOnBTDeviceDiscoveryListener != null){
                    mOnBTDeviceDiscoveryListener.onFoundedBluetoothDevice(intent);
                    mOnBTDeviceDiscoveryListener.onFoundedBluetoothDevice((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                }
                mOnlineDevices.add((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                break;
            }
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED: {
                if (mOnBTDeviceDiscoveryListener != null){
                    mOnBTDeviceDiscoveryListener.onBTAdapterDiscoveryFinished(intent);
                }
                if (mOnFinishDiscoveryDevice != null){
                    mOnFinishDiscoveryDevice.onFinish(mOnlineDevices);
                }
                break;
            }
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED: {
                if (mOnBTDeviceDiscoveryListener != null){
                    mOnBTDeviceDiscoveryListener.onBTAdapterDiscoveryStarted(intent);
                }
                break;
            }
        }
    }
}
