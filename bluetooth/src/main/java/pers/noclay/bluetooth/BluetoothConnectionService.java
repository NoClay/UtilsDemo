package pers.noclay.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.lang.ref.SoftReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static pers.noclay.bluetooth.BluetoothConstant.ARG_FROM_CLIENT;
import static pers.noclay.bluetooth.BluetoothConstant.ARG_FROM_SERVER;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_CONNECT_FAILED;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_CONNECT_SUCCESS;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_READ_STRING;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_START_CONNECT;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public class BluetoothConnectionService extends Service {
    RemoteCallbackList<IBluetoothReceiverListener> mListeners = new RemoteCallbackList<>();
    ClientThread mClientThread;
    ServerThread mServerThread;
    ConnectThread mConnectThread;
    boolean mHoldLongConnectAble = false;
    boolean mHasConnected = false;
    Timer mTimer;
    UUID mUUID;
    private static class BluetoothHandler extends Handler{
        SoftReference<? extends BluetoothConnectionService> service;
        BluetoothHandler mBluetoothHandler;

        public BluetoothHandler(BluetoothConnectionService service) {
            if (service != null){
                this.service = new SoftReference<BluetoothConnectionService>(service);
                mBluetoothHandler = this;
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CONNECT_SUCCESS: {
                    BluetoothSocket socket = null;
                    if (msg.arg1 == ARG_FROM_CLIENT) {
                        socket = service.get().mClientThread.getBluetoothSocket();
                    } else if (msg.arg1 == ARG_FROM_SERVER) {
                        socket = service.get().mServerThread.getBluetoothSocket();
                    }
                    service.get().mTimer = new Timer();
                    service.get().mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (service.get().mHasConnected
                                    && service.get().mHoldLongConnectAble
                                    && service.get().mConnectThread != null
                                    && service.get().mConnectThread.getBluetoothSocket() != null){
                                if (!service.get().mConnectThread.getBluetoothSocket().isConnected()){
                                    service.get().beginBroadcast(BluetoothConstant.METHOD_ON_CONNECT_FAILED, null, BluetoothConstant.ERROR_NOT_CONNECTED);
                                }
                            }
                        }
                    }, 0, BluetoothConstant.DEFAULT_CONNECT_CHECK);
                    service.get().mConnectThread = new ConnectThread(socket, mBluetoothHandler, msg.arg1);
                    service.get().mConnectThread.start();
                    service.get().mHasConnected = true;
                    service.get().beginBroadcast(BluetoothConstant.METHOD_ON_CONNECT_SUCCESS, null, -1);
                    break;
                }
                case MESSAGE_START_CONNECT: {
                    service.get().beginBroadcast(BluetoothConstant.METHOD_ON_CONNECT_START, null, -1);
                    break;
                }
                case MESSAGE_CONNECT_FAILED: {
                    service.get().beginBroadcast(BluetoothConstant.METHOD_ON_CONNECT_FAILED, null, -1);
                    break;
                }
                case MESSAGE_READ_STRING: {
                    service.get().beginBroadcast(BluetoothConstant.METHOD_ON_RECEIVE_MESSAGE, ((byte[]) msg.obj), -1);
                    //针对收到的信息进行判定
                    break;
                }
            }
        }
    }
    private BluetoothHandler mHandler = new BluetoothHandler(BluetoothConnectionService.this);

    private Binder mBinder = new IBluetoothConnection.Stub() {
        @Override
        public void connect(String bluetoothAddress) throws RemoteException{
            mClientThread = new ClientThread(bluetoothAddress, mUUID, mHandler);
            mClientThread.start();
        }
        @Override
        public void sendMessage(String message) throws RemoteException {
            if (mConnectThread != null && mConnectThread.isAlive()){
                mConnectThread.write(message);
            }
        }


        @Override
        public void registerListener(IBluetoothReceiverListener listener) throws RemoteException {
            mListeners.register(listener);
        }

        @Override
        public void unregisterListener(IBluetoothReceiverListener listener) throws RemoteException {
            mListeners.unregister(listener);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mUUID = UUID.fromString(intent.getStringExtra(BluetoothConstant.VALUE_UUID));
        mHoldLongConnectAble = intent.getBooleanExtra(BluetoothConstant.VALUE_HOLD_LONG_CONNECT, false);
        if (intent.getBooleanExtra(BluetoothConstant.VALUE_SERVER_ENABLE, true)){
            mServerThread = new ServerThread(mHandler, mUUID);
            mServerThread.start();
        }
        return mBinder;
    }


    public void beginBroadcast(int method, byte []bytes, int type) {
        if (mListeners == null) {
            return;
        }
        final int size = mListeners.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                IBluetoothReceiverListener listener = mListeners.getBroadcastItem(i);
                switch (method) {
                    case BluetoothConstant.METHOD_ON_CONNECT_FAILED: {
                        listener.onConnectFailed(type);
                        break;
                    }
                    case BluetoothConstant.METHOD_ON_CONNECT_START: {
                        listener.onConnectStart();
                        break;
                    }
                    case BluetoothConstant.METHOD_ON_CONNECT_SUCCESS: {
                        listener.onConnectSuccess();
                        break;
                    }
                    case BluetoothConstant.METHOD_ON_RECEIVE_MESSAGE: {
                        listener.onReceiveMessage(bytes);
                        break;
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mListeners.finishBroadcast();
    }
}
