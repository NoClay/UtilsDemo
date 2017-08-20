package pers.noclay.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.List;
import java.util.UUID;

/**
 * Created by i-gaolonghai on 2017/8/18.
 */

public class Bluetooth {
    public static void initialize(BluetoothConfig config){
        BluetoothWrapper.config(config);
    }

    public static void registerBluetoothReceiver(){
        
    }

}
