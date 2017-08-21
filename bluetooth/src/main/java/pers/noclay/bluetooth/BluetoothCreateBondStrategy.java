package pers.noclay.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public class BluetoothCreateBondStrategy extends ABSCreateBondStrategy {
    private BluetoothDevice mTheDestDevice;

    public BluetoothCreateBondStrategy(BluetoothDevice theDestDevice, OnCreateBondResultListener onCreateBondResultListener) {
        mTheDestDevice = theDestDevice;
        mOnCreateBondResultListener = onCreateBondResultListener;
    }

    @Override
    public void onBTDevicePairingReQuest(Intent intent) {

    }

    @Override
    public void onBTDeviceBondStateChanged(Intent intent) {
        mTheDestDevice = Bluetooth.getBluetoothAdapter().getRemoteDevice(mTheDestDevice.getAddress());
        switch (mTheDestDevice.getBondState()){
            case BluetoothDevice.BOND_BONDED:{
                mOnCreateBondResultListener.onCreateBondSuccess();
                break;
            }
            case BluetoothDevice.BOND_BONDING:{
                mOnCreateBondResultListener.onCreateBonding();
                break;
            }
            case BluetoothDevice.BOND_NONE:{
                mOnCreateBondResultListener.onCreateBondFail();
                break;
            }
        }
    }
}
