package pers.noclay.bluetooth;

/**
 * Created by i-gaolonghai on 2017/8/24.
 */

public class BluetoothException extends Exception {
    public static final String ERROR = "ERROR";

    public BluetoothException(int errorCode) {
        this(BluetoothConstant.ERROR_TIPS[errorCode - BluetoothConstant.ERROR_NO_SUCH_MAC_ADDRESS], errorCode);
    }

    public BluetoothException(String message, int errorCode) {
        super(ERROR + " " + errorCode + " : " +
                BluetoothConstant.ERROR_TIPS[errorCode - BluetoothConstant.ERROR_NO_SUCH_MAC_ADDRESS]
                + message);
    }
}
