package pers.noclay.bluetooth;

import android.content.Context;

import java.util.UUID;

/**
 * Created by i-gaolonghai on 2017/8/18.
 */

public class BluetoothWrapper {
    private static BluetoothWrapper instance;
    private Context mContext;
    private UUID mUUID;
    private boolean mHoldLongConnectAble;
    private boolean mAutoPairAble;
    private String mPairPassword;

    static void config(BluetoothConfig config){
        if (instance == null){
            synchronized (BluetoothWrapper.class){
                if (instance == null){
                    instance = new BluetoothWrapper(config);
                }
            }
        }
    }

    public BluetoothWrapper(BluetoothConfig config) {
        this.mContext = config.mContext;
        this.mUUID = config.mUUID == null ? UUID.fromString(BluetoothConstant.DEFAULT_UUID) : config.mUUID;
        this.mHoldLongConnectAble = config.mHoldLongConnectAble;
        this.mAutoPairAble = config.mAutoPairAble;
        this.mPairPassword = config.mPairPassword;
    }

    public static BluetoothWrapper getInstance() {
        return instance;
    }

    public Context getApplicationContext() {
        return mContext;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public boolean isHoldLongConnectAble() {
        return mHoldLongConnectAble;
    }

    public boolean isAutoPairAble() {
        return mAutoPairAble;
    }

    public String getPairPassword() {
        return mPairPassword;
    }
}
