package pers.noclay.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Timer;

/**
 * Created by i-gaolonghai on 2017/8/22.
 */

public class ConnectThread extends Thread {

    private BluetoothSocket mBluetoothSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    byte[] bytes = new byte[1024];
    private int mArg;
    private Handler mHandler;

    public BluetoothSocket getBluetoothSocket() {
        return mBluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        mBluetoothSocket = bluetoothSocket;
    }

    public int getArg() {
        return mArg;
    }

    public void setArg(int arg) {
        mArg = arg;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public ConnectThread(BluetoothSocket bluetoothSocket, Handler handler, int arg) {
        mBluetoothSocket = bluetoothSocket;
        mArg = arg;
        mHandler = handler;
        try {
            mOutputStream = this.mBluetoothSocket.getOutputStream();
            mInputStream = this.mBluetoothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        int i = 0;
        if (mInputStream == null)
            return;
        do {
            try {
                i = mInputStream.read(bytes);
                Message message = new Message();
                message.what = BluetoothConstant.MESSAGE_READ_STRING;
                message.obj = Arrays.copyOf(bytes, i);
                mHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (i != 0);
    }

    public void write(String string) {
        byte[] bytes;
        bytes = string.getBytes();
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
