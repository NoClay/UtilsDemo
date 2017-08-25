// IBluetoothConnection.aidl
package pers.noclay.bluetooth;

// Declare any non-default types here with import statements
import pers.noclay.bluetooth.IBluetoothReceiverListener;
interface IBluetoothConnection {
    void sendMessage(String message);
    void connect(String bluetoothAddress);
    void registerListener(IBluetoothReceiverListener listener);
    void unregisterListener(IBluetoothReceiverListener listener);
}
