// IBluetoothReceiverListener.aidl
package pers.noclay.bluetooth;

// Declare any non-default types here with import statements

interface IBluetoothReceiverListener {
    void onConnectStart();
    void onConnectSuccess();
    void onConnectFailed(int errorCode);
    void onReceiveMessage(inout byte[] bytes);
}
