package pers.noclay.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Intent;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public interface CreateBondStrategy {
    void onBTDevicePairingReQuest(Intent intent);
    void onBTDeviceBondStateChanged(Intent intent);
}
