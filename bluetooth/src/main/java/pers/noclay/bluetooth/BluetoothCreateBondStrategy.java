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
    public void onBTDevicePairingRequest(Intent intent) {

    }

    @Override
    public void onBTDeviceBondStateChanged(Intent intent) {
        mTheDestDevice = Bluetooth.getBluetoothAdapter().getRemoteDevice(mTheDestDevice.getAddress());
        switch (mTheDestDevice.getBondState()){
            case BluetoothDevice.BOND_BONDED:{
                if (mOnCreateBondResultListener != null){
                    mOnCreateBondResultListener.onCreateBondSuccess();
                }
                break;
            }
            case BluetoothDevice.BOND_BONDING:{
                if (mOnCreateBondResultListener != null){
                    mOnCreateBondResultListener.onCreateBonding();
                }
                break;
            }
            case BluetoothDevice.BOND_NONE:{
                if (mOnCreateBondResultListener != null){
                    mOnCreateBondResultListener.onCreateBondFail(BluetoothConstant.ERROR_TIME_OUT);
                }
                break;
            }
        }
    }
}
