package pers.noclay.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Random;
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

    private static final String CONNECT_NAME = "Bluetooth";

    public static final String VALUE_UUID = "valueUUID";
    public static final String VALUE_HOLD_LONG_CONNECT = "valueHoldLongConnectAble";
    public static final String VALUE_SERVER_ENABLE = "valueServerEnable";

    private static final int REQUEST_CODE = 2 << 10;
    public static final int REQUEST_OPEN_BLUETOOTH = REQUEST_CODE + 1;

    private static final int MESSAGE_CODE = 2 << 10;
    public static final int MESSAGE_CONNECT_SUCCESS = MESSAGE_CODE +1;
    public static final int MESSAGE_WAIT_CONNECT = MESSAGE_CODE + 2;
    public static final int MESSAGE_START_CONNECT = MESSAGE_CODE + 3;
    public static final int MESSAGE_READ_STRING = MESSAGE_CODE + 4;
    public static final int MESSAGE_CONNECT_FAILED = MESSAGE_CODE + 5;

    private static final int ARG_CODE = 2 << 10;
    public static final int ARG_FROM_SERVER = ARG_CODE + 1;
    public static final int ARG_FROM_CLIENT = ARG_CODE + 2;

    private static final int METHOD_CODE = 2 << 10;
    public static final int METHOD_ON_CONNECT_SUCCESS = METHOD_CODE + 1;
    public static final int METHOD_ON_CONNECT_FAILED = METHOD_CODE + 2;
    public static final int METHOD_ON_CONNECT_START = METHOD_CODE + 3;
    public static final int METHOD_ON_RECEIVE_MESSAGE = METHOD_CODE + 4;
    public static final int METHOD_ON_RECEIVE_FILE = METHOD_CODE + 5;

    public static final int FILE_CODE = 2 << 10;


    private static final int ERROR_CODE = 2 << 10;
    public static final int ERROR_NO_SUCH_MAC_ADDRESS = ERROR_CODE + 1;
    public static final int ERROR_NO_SUCH_DEVICE = ERROR_CODE + 2;
    public static final int ERROR_TIME_OUT = ERROR_CODE + 3;
    public static final int ERROR_AUTO_PAIR_NOT_MATCH = ERROR_CODE + 4;
    public static final int ERROR_NOT_CONNECTED = ERROR_CODE + 5;
    public static final int ERROR_BLUETOOTH_NOT_SUPPORTABLE = ERROR_CODE + 6;

    public static final String []ERROR_TIPS = {
            "No Such MAC Address : ",
            "No Such Bluetooth Device : ",
            "Connect or createBond time out!",
            "Auto pair failed ! Because the passwords to autoPair aren't matched!",
            "There is no connected device!",
            "Your device isn't support bluetooth!"
    };

    public static final int DEFAULT_DISCOVERABLE_TIME_THRESHOLD = 120;
    public static final int DEFAULT_CONNECT_TIME_THRESHOLD = 60;
    public static final int DEFAULT_CONNECT_CHECK = 300;
    public static String getConnectName() {
        return CONNECT_NAME + "_" + new Random().nextInt(1000);
    }
}
