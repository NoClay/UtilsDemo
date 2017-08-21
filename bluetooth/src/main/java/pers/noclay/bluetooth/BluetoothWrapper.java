package pers.noclay.bluetooth;

import android.app.Activity;
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
    private BluetoothReceiver mReceiver;
    private long mDiscoverableTime;
    private String mTargetAddress;

    static void config(BluetoothConfig config, OnBTDeviceDiscoveryListener listener){
        if (instance == null){
            synchronized (BluetoothWrapper.class){
                if (instance == null){
                    instance = new BluetoothWrapper(config, listener);
                }
            }
        }
    }

    public BluetoothWrapper(BluetoothConfig config, OnBTDeviceDiscoveryListener listener) {
        this.mContext = config.mContext;
        this.mUUID = config.mUUID == null ? UUID.fromString(BluetoothConstant.DEFAULT_UUID) : config.mUUID;
        this.mHoldLongConnectAble = config.mHoldLongConnectAble;
        this.mAutoPairAble = config.mAutoPairAble;
        this.mPairPassword = config.mPairPassword;
        this.mReceiver = listener == null ? new BluetoothReceiver() : new BluetoothReceiver(listener);
        this.mDiscoverableTime = config.mDiscoverableTime;
        this.mTargetAddress = config.mTargetAddress;
    }

    public Activity getActivity(){
        return (Activity) mContext;
    }

    public long getDiscoverableTime() {
        return mDiscoverableTime;
    }

    public Context getContext() {
        return mContext;
    }

    public BluetoothReceiver getReceiver() {
        return mReceiver;
    }

    public static BluetoothWrapper getInstance() {
        return instance;
    }

    public Context getApplicationContext() {
        return mContext.getApplicationContext();
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


    public String getTargetAddress() {
        return mTargetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        mTargetAddress = targetAddress;
    }

    public void setAutoPairAble(boolean autoPairAble) {
        mAutoPairAble = autoPairAble;
    }

    public static void setInstance(BluetoothWrapper instance) {
        BluetoothWrapper.instance = instance;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public void setHoldLongConnectAble(boolean holdLongConnectAble) {
        mHoldLongConnectAble = holdLongConnectAble;
    }

    public void setPairPassword(String pairPassword) {
        mPairPassword = pairPassword;
    }

    public void setReceiver(BluetoothReceiver receiver) {
        mReceiver = receiver;
    }

    public void setDiscoverableTime(long discoverableTime) {
        mDiscoverableTime = discoverableTime;
    }
}
