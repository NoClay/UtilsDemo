package pers.noclay.bluetooth;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public interface OnConnectListener {
    void onConnectFail();
    void onConnectSuccess();
    void onConnectStart();
    void onReceiveMessage(byte[] bytes);
}
