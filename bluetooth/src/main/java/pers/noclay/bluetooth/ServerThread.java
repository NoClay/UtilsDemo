package pers.noclay.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by i-gaolonghai on 2017/8/22.
 */

public class ServerThread extends Thread {
    private BluetoothServerSocket mBluetoothServerSocket;
    private Handler mHandler;
    private BluetoothSocket mBluetoothSocket;


    public BluetoothServerSocket getBluetoothServerSocket() {
        return mBluetoothServerSocket;
    }

    public void setBluetoothServerSocket(BluetoothServerSocket bluetoothServerSocket) {
        mBluetoothServerSocket = bluetoothServerSocket;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public BluetoothSocket getBluetoothSocket() {
        return mBluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        mBluetoothSocket = bluetoothSocket;
    }

    public ServerThread(Handler handler, UUID uuid) {
        mHandler = handler;
        try {
            mBluetoothServerSocket = BluetoothAdapter.getDefaultAdapter().
                    listenUsingRfcommWithServiceRecord(BluetoothConstant.getConnectName(), uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (mBluetoothServerSocket != null) {
            try {
                mBluetoothSocket = mBluetoothServerSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mBluetoothSocket != null) {
                Message message = Message.obtain();
                message.what = BluetoothConstant.MESSAGE_CONNECT_SUCCESS;
                message.obj = mBluetoothSocket.getRemoteDevice();
                message.arg1 = BluetoothConstant.ARG_FROM_SERVER;
                mHandler.sendMessage(message);
            }else{
                Message message = Message.obtain();
                message.what = BluetoothConstant.MESSAGE_CONNECT_FAILED;
                message.arg1 = BluetoothConstant.ARG_FROM_SERVER;
                mHandler.sendMessage(message);
            }
        }
    }
}
