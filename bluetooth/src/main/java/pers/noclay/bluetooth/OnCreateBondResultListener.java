package pers.noclay.bluetooth;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public interface OnCreateBondResultListener {
    void onCreateBondSuccess();
    void onCreateBondFail(int errorCode);
    void onCreateBonding();
}
