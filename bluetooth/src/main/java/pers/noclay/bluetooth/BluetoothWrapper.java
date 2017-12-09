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
    private long mDiscoverableTimeThreshold;
    private String mTargetAddress;
    private long mConnectTimeThreshold;
    private boolean mServerEnable;
    private Class<? extends BluetoothConnectionService> mConnectionServiceClass;


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
        this.mReceiver = new BluetoothReceiver();
        this.mDiscoverableTimeThreshold = config.mDiscoverableTimeThreshold;
        this.mTargetAddress = config.mTargetAddress;
        this.mConnectTimeThreshold = config.mConnectTimeThreshold;
        this.mServerEnable = config.mServerEnable;

        if (config.mConnectionServiceClass != null){
            this.mConnectionServiceClass = config.mConnectionServiceClass;
        }else {
            this.mConnectionServiceClass = BluetoothConnectionService.class;
        }
    }

    public Activity getActivity(){
        return (Activity) mContext;
    }

    public long getDiscoverableTimeThreshold() {
        return mDiscoverableTimeThreshold;
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

    public long getConnectTimeThreshold() {
        return mConnectTimeThreshold;
    }

    public void setConnectTimeThreshold(long connectTimeThreshold) {
        mConnectTimeThreshold = connectTimeThreshold;
    }

    public void setDiscoverableTimeThreshold(long discoverableTimeThreshold) {
        mDiscoverableTimeThreshold = discoverableTimeThreshold;
    }

    public boolean isServerEnable() {
        return mServerEnable;
    }

    public void setServerEnable(boolean serverEnable) {
        mServerEnable = serverEnable;
    }


    public Class<? extends BluetoothConnectionService> getConnectionServiceClass() {
        return mConnectionServiceClass;
    }
}
