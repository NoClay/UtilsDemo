package pers.noclay.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by i-gaolonghai on 2017/8/22.
 */

public class ClientThread extends Thread {
    private BluetoothSocket mBluetoothSocket;
    private Handler mHandler;

    public BluetoothSocket getBluetoothSocket() {
        return mBluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        mBluetoothSocket = bluetoothSocket;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public ClientThread(String address, UUID uuid, Handler handler) {
        mHandler = handler;
        try {
            mBluetoothSocket = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address)
                    .createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mBluetoothSocket == null){
                    return;
                }else{
                    try {
                        mBluetoothSocket.connect();
                        if(mBluetoothSocket.isConnected()){
                            Message message = Message.obtain();
                            message.what = BluetoothConstant.MESSAGE_CONNECT_SUCCESS;
                            message.obj = mBluetoothSocket.getRemoteDevice();
                            message.arg1 = BluetoothConstant.ARG_FROM_CLIENT;
                            mHandler.sendMessage(message);
                        }else{
                            Message message = Message.obtain();
                            message.what = BluetoothConstant.MESSAGE_CONNECT_FAILED;
                            message.arg1 = BluetoothConstant.ARG_FROM_CLIENT;
                            mHandler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
