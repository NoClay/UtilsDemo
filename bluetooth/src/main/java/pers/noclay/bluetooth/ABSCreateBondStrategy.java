package pers.noclay.bluetooth;

import android.content.Intent;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public abstract class ABSCreateBondStrategy implements CreateBondStrategy {
    OnCreateBondResultListener mOnCreateBondResultListener;

    public OnCreateBondResultListener getOnCreateBondResultListener() {
        return mOnCreateBondResultListener;
    }


    public void setOnCreateBondResultListener(OnCreateBondResultListener onCreateBondResultListener) {
        mOnCreateBondResultListener = onCreateBondResultListener;
    }

    @Override
    public void onBTDevicePairingRequest(Intent intent) {

    }

    @Override
    public void onBTDeviceBondStateChanged(Intent intent) {

    }
}
