package pers.noclay.bluetooth;

import android.content.Context;

import java.util.UUID;

/**
 * Created by i-gaolonghai on 2017/8/18.
 */

public class BluetoothConfig {
    public Context mContext;
    public UUID mUUID;
    public boolean mHoldLongConnectAble;
    public boolean mAutoPairAble;
    public String mPairPassword;
    public long mDiscoverableTimeThreshold;
    public long mConnectTimeThreshold;
    public String mTargetAddress;
    public Class<? extends BluetoothConnectionService> mConnectionServiceClass;
    public boolean mServerEnable = true;

    private BluetoothConfig(Context context, UUID UUID, boolean holdLongConnectAble,
                            boolean autoPairAble, String pairPassword, long discoverableTimeThreshold,
                            String targetAddress, long connectTimeThreshold, boolean serverEnable,
                            Class<? extends BluetoothConnectionService> connectionServiceClass) {
        mContext = context;
        mUUID = UUID;
        mHoldLongConnectAble = holdLongConnectAble;
        mAutoPairAble = autoPairAble;
        mPairPassword = pairPassword;
        mDiscoverableTimeThreshold = discoverableTimeThreshold;
        mTargetAddress = targetAddress;
        mConnectTimeThreshold = connectTimeThreshold;
        mServerEnable = serverEnable;
        mConnectionServiceClass = connectionServiceClass;
    }


    public static final class Builder{
        private Context mContext;
        private UUID mUUID;
        private boolean mHoldLongConnectAble;
        private boolean mAutoPairAble;
        private String mPairPassword;
        private long mDiscoverableTime;
        private String mTargetAddress;
        private long mConnectTime;
        private boolean mServerEnable = true;
        private Class<? extends BluetoothConnectionService> mConnectionServiceClass;


        public Builder(Context context) {
            mContext = context;
            this.mDiscoverableTime = BluetoothConstant.DEFAULT_DISCOVERABLE_TIME_THRESHOLD;
            this.mConnectTime = BluetoothConstant.DEFAULT_CONNECT_TIME_THRESHOLD;
        }


        public Builder setUUID(String uuid){
            this.mUUID = UUID.fromString(uuid);
            return this;
        }

        public Builder setUUID(UUID uuid){
            this.mUUID = uuid;
            return this;
        }

        public Builder setServerEnable(boolean serverEnable) {
            mServerEnable = serverEnable;
            return this;
        }

        public Builder setHoldLongConnectAble(boolean holdLongConnectAble){
            this.mHoldLongConnectAble = holdLongConnectAble;
            return this;
        }

        public Builder setAutoPairAble(boolean autoPairAble){
            this.mAutoPairAble = autoPairAble;
            return this;
        }

        public Builder setPairPassword(String pairPassword){
            this.mPairPassword = pairPassword;
            return this;
        }

        public Builder setDiscoverableTime(long time){
            if (time > 0){
                this.mDiscoverableTime = time;
            }
            return this;
        }

        public Builder setTargetAddress(String targetAddress){
            this.mTargetAddress = targetAddress;
            return this;
        }
        
        public Builder setConnectTime(long connectTime){
            this.mConnectTime = connectTime;
            return this;
        }

        public Builder setConnectionServiceClass(Class<? extends BluetoothConnectionService> connectionServiceClass) {
            this.mConnectionServiceClass = connectionServiceClass;
            return this;
        }

        public BluetoothConfig build(){
            return new BluetoothConfig(mContext, mUUID, mHoldLongConnectAble,
                    mAutoPairAble, mPairPassword, mDiscoverableTime, mTargetAddress,
                    mConnectTime, mServerEnable, mConnectionServiceClass);
        }

    }
}
