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

    private BluetoothConfig(Context context, UUID UUID, boolean holdLongConnectAble,
                            boolean autoPairAble, String pairPassword) {
        mContext = context;
        mUUID = UUID;
        mHoldLongConnectAble = holdLongConnectAble;
        mAutoPairAble = autoPairAble;
        mPairPassword = pairPassword;
    }

    public static final class Builder{
        private Context mContext;
        private UUID mUUID;
        private boolean mHoldLongConnectAble;
        private boolean mAutoPairAble;
        private String mPairPassword;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setUUID(String uuid){
            this.mUUID = UUID.fromString(uuid);
            return this;
        }

        public Builder setUUID(UUID uuid){
            this.mUUID = uuid;
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

        public BluetoothConfig build(){
            return new BluetoothConfig(mContext, mUUID, mHoldLongConnectAble, mAutoPairAble, mPairPassword);
        }

    }
}
