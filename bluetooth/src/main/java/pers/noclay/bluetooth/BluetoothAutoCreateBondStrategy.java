package pers.noclay.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public class BluetoothAutoCreateBondStrategy extends ABSCreateBondStrategy {
    private BluetoothDevice mTheDestDevice;
    private Context mContext;
    private BroadcastReceiver mBroadcastReceiver;
    private int index = 0;
    private static final String TAG = "BluetoothAutoCreateBond";



    public BluetoothAutoCreateBondStrategy(BluetoothDevice theDestDevice,
                                           Context context,
                                           BroadcastReceiver broadcastReceiver,
                                           OnCreateBondResultListener onCreateBondResultListener) {
        mTheDestDevice = theDestDevice;
        mContext = context;
        mBroadcastReceiver = broadcastReceiver;
        mOnCreateBondResultListener = onCreateBondResultListener;
    }

    @Override
    public void onBTDevicePairingRequest(Intent intent) {
        try {
            Log.d(TAG, "onBTDevicePairingReQuest: password = " + BluetoothConstant.DEFAULT_PIN[index]);
            if (index < BluetoothConstant.DEFAULT_PIN.length){
                BluetoothUtils.setPin(mTheDestDevice.getClass(), mTheDestDevice, BluetoothConstant.DEFAULT_PIN[index]);
            }else if (!TextUtils.isEmpty(Bluetooth.getPairPassword())){
                BluetoothUtils.setPin(mTheDestDevice.getClass(), mTheDestDevice, Bluetooth.getPairPassword());
            } else if (mOnCreateBondResultListener != null){
                mOnCreateBondResultListener.onCreateBondFail(BluetoothConstant.ERROR_AUTO_PAIR_NOT_MATCH);
            }
            //1.确认配对
            BluetoothUtils.setPairingConfirmation(mTheDestDevice.getClass(), mTheDestDevice, true);
            //2.终止有序广播
            mBroadcastReceiver.abortBroadcast();
            //如果没有将广播终止，则会出现一个一闪而过的配对框。
            //3.调用setPin方法进行配对...
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mBroadcastReceiver.abortBroadcast();
        //终止广播
    }

    @Override
    public void onBTDeviceBondStateChanged(Intent intent) {
        mTheDestDevice = Bluetooth.getBluetoothAdapter().getRemoteDevice(mTheDestDevice.getAddress());
        switch (mTheDestDevice.getBondState()){
            case BluetoothDevice.BOND_BONDED:{
                Log.d(TAG, "onBTDeviceBondStateChanged: 配对成功");
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
                index ++;
                if (index < BluetoothConstant.DEFAULT_PIN.length){
                    try {
                        BluetoothUtils.createBond(mTheDestDevice.getClass(), mTheDestDevice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mOnCreateBondResultListener != null) {
                        mOnCreateBondResultListener.onCreateBonding();
                    }
                }else if (index == BluetoothConstant.DEFAULT_PIN.length && !TextUtils.isEmpty(Bluetooth.getPairPassword())){
                    try {
                        BluetoothUtils.createBond(mTheDestDevice.getClass(), mTheDestDevice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mOnCreateBondResultListener != null) {
                        mOnCreateBondResultListener.onCreateBonding();
                    }
                }else{
                    Bluetooth.setAutoPairAble(false);
                    if (mOnCreateBondResultListener != null) {
                        Bluetooth.createBond(mOnCreateBondResultListener);
                    }
                }
                break;
            }
        }
    }

}
