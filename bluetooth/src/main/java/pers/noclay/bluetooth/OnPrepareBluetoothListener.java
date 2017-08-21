package pers.noclay.bluetooth;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public interface OnPrepareBluetoothListener {
    void onNonSupportable(String tips);
    void onOpenBluetooth(boolean success);
    void onRequestPermission(boolean success, String[] permissions);
    void onOpenBluetoothDiscoverable();
    void onCloseBluetoothDiscoverable();
}
