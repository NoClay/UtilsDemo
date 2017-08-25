package pers.noclay.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Intent;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

interface CreateBondStrategy {
    void onBTDevicePairingRequest(Intent intent);
    void onBTDeviceBondStateChanged(Intent intent);
}
