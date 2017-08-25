package pers.noclay.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static pers.noclay.bluetooth.BluetoothConstant.ARG_FROM_CLIENT;
import static pers.noclay.bluetooth.BluetoothConstant.ARG_FROM_SERVER;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_CONNECT_FAILED;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_CONNECT_SUCCESS;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_READ_STRING;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_START_CONNECT;
import static pers.noclay.bluetooth.BluetoothConstant.MESSAGE_WAIT_CONNECT;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public class BluetoothConnectionService extends Service {
    private RemoteCallbackList<IBluetoothReceiverListener> mListeners = new RemoteCallbackList<>();
    private ClientThread mClientThread;
    private ServerThread mServerThread;
    private ConnectThread mConnectThread;
    private boolean mHoldLongConnectAble = false;
    private boolean mHasConnected = false;
    private Timer mTimer;
    private UUID mUUID;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CONNECT_SUCCESS: {
                    BluetoothSocket socket = null;
                    if (msg.arg1 == ARG_FROM_CLIENT) {
                        socket = mClientThread.getBluetoothSocket();
                    } else if (msg.arg1 == ARG_FROM_SERVER) {
                        socket = mServerThread.getBluetoothSocket();
                    }
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (mHasConnected && mHoldLongConnectAble && mConnectThread != null && mConnectThread.getBluetoothSocket() != null){
                                if (!mConnectThread.getBluetoothSocket().isConnected()){
                                    beginBroadcast(BluetoothConstant.METHOD_ON_CONNECT_FAILED, null, BluetoothConstant.ERROR_NOT_CONNECTED);
                                }
                            }
                        }
                    }, 0, BluetoothConstant.DEFAULT_CONNECT_CHECK);
                    mConnectThread = new ConnectThread(socket, mHandler, msg.arg1);
                    mConnectThread.start();
                    mHasConnected = true;
                    beginBroadcast(BluetoothConstant.METHOD_ON_CONNECT_SUCCESS, null, -1);
                    break;
                }
                case MESSAGE_START_CONNECT: {
                    beginBroadcast(BluetoothConstant.METHOD_ON_CONNECT_START, null, -1);
                    break;
                }
                case MESSAGE_CONNECT_FAILED: {
                    beginBroadcast(BluetoothConstant.METHOD_ON_CONNECT_FAILED, null, -1);
                    break;
                }
                case MESSAGE_READ_STRING: {
                    beginBroadcast(BluetoothConstant.METHOD_ON_RECEIVE_MESSAGE, ((byte[]) msg.obj), -1);
                    //针对收到的信息进行判定
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };

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
