package pers.noclay.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Created by i-gaolonghai on 2017/8/18.
 <uses-permission android:name="android.permission.BLUETOOTH"/>
 <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
 <uses-permission android:name="android.permission.BLUETOOTH_PRIVILEGED"/>
 <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 */

public class BluetoothConstant {
    public static final String DEFAULT_UUID = "fa87c0d0-afac-11de-8a39-0800200c9a66";
    public static final String DEFAULT_UUID_EMBEDDED = "00001101-0000-1000-8000-00805F9B34FB";
    public static final String []DEFAULT_BROADCAST_ACTIONS = {
            BluetoothDevice.ACTION_ACL_CONNECTED,
            BluetoothDevice.ACTION_ACL_DISCONNECTED,
            BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED,
            BluetoothDevice.ACTION_BOND_STATE_CHANGED,
            BluetoothDevice.ACTION_CLASS_CHANGED,
            BluetoothDevice.ACTION_FOUND,
            BluetoothDevice.ACTION_NAME_CHANGED,
            BluetoothDevice.ACTION_PAIRING_REQUEST,
            BluetoothDevice.ACTION_UUID,
            BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED,
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED,
            BluetoothAdapter.ACTION_DISCOVERY_STARTED,
            BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED,
            BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE,
            BluetoothAdapter.ACTION_REQUEST_ENABLE,
            BluetoothAdapter.ACTION_SCAN_MODE_CHANGED,
            BluetoothAdapter.ACTION_STATE_CHANGED
    };
    public static final String []DEFAULT_PIN = {"1234", "0000"};
    public static final String NON_SUPPORT_BLUETOOTH = "本机不支持蓝牙功能";

    public static final String PERMISSION_ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String PERMISSION_BLUETOOTH = "android.permission.BLUETOOTH";
    public static final String PERMISSION_BLUETOOTH_ADMIN = "android.permission.BLUETOOTH_ADMIN";
    public static final String PERMISSION_BLUETOOTH_PRIVILEGED = "android.permission.BLUETOOTH_PRIVILEGED";
    private static final int REQUEST_CODE = 2 << 10;
    public static final int REQUEST_OPEN_BLUETOOTH = REQUEST_CODE + 1;
    private static final int MESSAGE_CODE = 2 << 10;

    private static final int ERROR_CODE = 2 << 10;

    public static final int DEFAULT_DISCOVERABLE_TIME = 120;
}
