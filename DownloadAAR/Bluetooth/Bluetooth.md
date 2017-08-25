[TOC]

# Bluetooth

这个SDK，封装了蓝牙通信相关的模块，使用了装饰者，单例，策略等设计模式，进行了抽象，蓝牙操作均通过Bluetooth进行，在UtilsDemo中利用SDK，实现了一个蓝牙聊天的功能，个人使用中，可以自由拓展。

# 涉及权限

蓝牙功能，需要申请系统的一些权限，这些已经在SDK的Manifest文件中进行了声明：

```xml
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.BLUETOOTH_PRIVILEGED"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

**由于Access_Coarse_Location是敏感权限，所以在Android6.0之后需要动态申请，在尝试搜索周围蓝牙设备的时候，应调用`Bluetooth.requestPermission()`进行动态申请，不过SDK中封装了调用操作，所以一般情况下你不需要自己去手动申请权限。✿✿ヽ(°▽°)ノ✿**

# 使用的服务

蓝牙通信的保持，需要以后后台的服务进行通信，为了方便使用，SDK采用进程间通信接口控制连接，发送和接收等动作。另外service采用了私有进程。

```xml
<service
     android:name=".BluetoothConnectionService"
     android:process=":bluetooth"/>
```

# 定义的抽象类

SDK中只定义了一个抽象类，具体为`ABSCreateBondStrategy`，你可以自己实现自己的配对策略，SDK中已经实现了自动配对策略`BluetoothAutoCreateBondStrategy`和弹出窗口手动配对`BluetoothCreateBondStrategy`策略。

```java
public abstract class ABSCreateBondStrategy implements CreateBondStrategy {
    OnCreateBondResultListener mOnCreateBondResultListener;
    public OnCreateBondResultListener getOnCreateBondResultListener() {
        return mOnCreateBondResultListener;
    }
    public void setOnCreateBondResultListener(OnCreateBondResultListener onCreateBondResultListener) {
        mOnCreateBondResultListener = onCreateBondResultListener;
    }
    @Override
    public void onBTDevicePairingRequest(Intent intent) {
    }
    @Override
    public void onBTDeviceBondStateChanged(Intent intent) {
    }
}
```

# 定义的接口

## CreateBondStrategy（包内私有）

这个接口是抽象配对策略来实现的，所以如果自定义配对策略，请继承抽象类`ABSCreateBondStrategy`

## OnBTBroadCastListener

```java
public interface OnBTBroadCastListener {
    void onBTBroadCastReceive(Intent intent);
}
```

SDK中默认监听了关于蓝牙的所有广播时间，但只对其中一些做了处理，如果你想处理某些广播，请实现该接口，并利用`Bluetooth.setBroadCastListener()`方法设置监听。

## OnBTDeviceDiscoveryListener

```java
public interface OnBTDeviceDiscoveryListener {
    void onBTAdapterDiscoveryFinished(Intent intent);
    void onBTAdapterDiscoveryStarted(Intent intent);
    void onFoundedBluetoothDevice(Intent intent);
    void onFoundedBluetoothDevice(BluetoothDevice device);
}
```

如果你想搜索周围的设备，并在搜索开始的时候做一些操作，请实现该接口，该接口会在不同的时机回调。当然也有另一种选择，就是下边的这个接口。

##OnFinishDiscoveryDevice

```java
public interface OnFinishDiscoveryDevice {
    void onFinish(List<BluetoothDevice> deviceList);
}
```

这个接口回调的时机就是在每次搜索周围设备完成后回调。

**注意：搜索比较耗时，所以这个接口回调会慢一些，建议加一个ProgressDialog**

## OnConnectListener

```
public interface OnConnectListener {
    void onConnectFail(int errorCode);
    void onConnectSuccess();
    void onConnectStart();
    void onReceiveMessage(byte[] bytes);
}
```

这个接口回调的时机是在准备连接到某个蓝牙设备的时候调用，所以如果你不是只有客户端的话，建议设置监听，因为蓝牙通信收到的数据会原封不动的发送过来。

**注意：这里的byte数组最大长度为1024， 是根据接收到的内容返回的，如果内容长度大于1024，可能会分多次传输过来。**

##OnCreateBondResultListener

```java
public interface OnCreateBondResultListener {
    void onCreateBondSuccess();
    void onCreateBondFail(int errorCode);
    void onCreateBonding();
}
```

这个接口的回调时机是在创建配对的时候调用的，一般情况下不需要设置，除非你想要监听配对的状况。

## OnPrepareBluetoothListener

```java
public interface OnPrepareBluetoothListener {
    void onNonSupportable(String tips);
    void onOpenBluetooth(boolean success);
    void onRequestPermission(boolean success, String[] permissions);
    void onOpenBluetoothDiscoverable();
    void onCloseBluetoothDiscoverable();
}
```

这个接口的回调时机是在准备阶段（即准备蓝牙通信的各种时机），当蓝牙设备不可用的时候，即您的设备不支持蓝牙的时候，会回调`OnNonSupportable`接口，在打开蓝牙的请求是也会进行对应的回调，由于搜索附近设备和配对可能会请求蓝牙可见性，所以这里也添加了这两个回调，这个接口也可以不进行实现，除非你需要监听它。

# 定义的AIDL接口：

## IBluetoothConnection

```java
import pers.noclay.bluetooth.IBluetoothReceiverListener;
interface IBluetoothConnection {
    void sendMessage(String message);
    void connect(String bluetoothAddress);
    void registerListener(IBluetoothReceiverListener listener);
    void unregisterListener(IBluetoothReceiverListener listener);
}
```

即Service持有的AIDL接口，通过这些接口可以与bluetooth后台服务通信，通过对应的接口可以实现对应的操作，不需要自己实现，由SDK自己协调。

## IBluetoothReceiverListener

```java
interface IBluetoothReceiverListener {
    void onConnectStart();
    void onConnectSuccess();
    void onConnectFailed(int errorCode);
    void onReceiveMessage(inout byte[] bytes);
}
```

供Service进行回调，在连接等时机调用，你不需要自己实现。

# 完成的类：

## Bluetooth

所有的SDK操作，都由它发起，后边会进行详细介绍。

## 其他类

1. BluetoothAutoCreateBondStrategy 自动配对的策略
2. BluetoothConfig 配置
3. BluetoothConnectionService 后台服务
4. BluetoothConstant 常量
5. BluetoothCreateBondStrategy 手动配对的策略类
6. BluetoothException 异常类，可能的有连接超时等
7. BluetoothReceiver 广播接收器，采用动态注册
8. BluetoothUtils 工具类
9. BluetoothWrapper 拓展后的配置
10. ClientThread 客户端线程类
11. ConnectThread 连接线程
12. ServerThread 服务端线程

# 可能抛出的异常:

```java
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
```

在使用过程中可能会抛出如上异常，mac地址错误，等等之类，在连接失败等回调的errorcode也可以作为判断依据。

# 使用：

## 初始化SDK：

```java
BluetoothConfig config = new BluetoothConfig.Builder(this)
                .setAutoPairAble(true)
                .setTargetAddress(TARGET)
                .build();
Bluetooth.initialize(config);
Bluetooth.setPrepareBluetoothListener(this);
Bluetooth.setOnConnectListener(this);
```

<u>请在onCreate方法中执行该初始化，Builder传入的是ActivityContext，请不要传入ApplicationContext，因为在SDK运行中，在某些时机需要使用ActivityContext。</u>

```java
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bluetooth.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bluetooth.onResult(requestCode, resultCode, data);
    }

```

<u>以上是为了动态注册广播接收器之后进行注销，以及打开蓝牙的结果回调，请务必添加。</u>

## 搜索周围的设备

```java

    private void showBluetoothSelect() {
        final ProgressDialog dialog = new ProgressDialog(WeChatActivity.this);
        dialog.setTitle("正在搜索附近的设备");
        dialog.show();

        Bluetooth.startSearch(new OnFinishDiscoveryDevice() {
            @Override
            public void onFinish(List<BluetoothDevice> deviceList) {
                dialog.dismiss();
                devices = deviceList;
              //do something
            }
        });
    }
```

最简单的去搜索即可，如果更详细的动态，请实现`OnBTDeviceDiscoveryListener`接口并设置之即可。

## 连接一个设备：

```java
Bluetooth.setTargetAddress(message.getAddress());
Bluetooth.startConnect();
```

首先请设置对应的目标设备的mac地址，否则可能会抛出异常。

## 发送一条消息：

```java
if (Bluetooth.isHasConnected()){
    try {
        Bluetooth.sendMessage(editText.getText().toString());
    } catch (BluetoothException e) {
        e.printStackTrace();
    }
}
```

发送消息的时候可能会抛出异常，请注意使用trycatch语句，记得判断连接状态哦✿✿ヽ(°▽°)ノ✿

## 接收消息：

```java
    @Override
    public void onReceiveMessage(byte[] bytes) {
        Message message = Message.obtain();
        message.what = 0;
        message.obj = new String(bytes);
        mHandler.sendMessage(message);
    }
```

接受到的消息，请自己添加解析，注意该方法在子线程中回调，请切回主线程中。

## 配置的属性

```java
    public Context mContext;  上下文
    public UUID mUUID;  连接使用的UUID
    public boolean mHoldLongConnectAble; 是否保持长连接，保持情况下，检测到断开，会回调onConnectFail(int errorCode)，错误码为连接超时
    public boolean mAutoPairAble; 是否自动配对，默认为false
    public String mPairPassword; 配对密码，不是自动配对情况下不需要设置，自动配对默认密码是0000, 1234
    public long mDiscoverableTimeThreshold; 蓝牙可见性默认的时间间隔为120秒
    public long mConnectTimeThreshold; 连接超时的默认时间间隔为60秒，60秒连接未成功会回调超时
    public String mTargetAddress; 目标的mac地址
    public boolean mServerEnable = true; 是否开启服务端线程，即是否接受其他设备的connect， 默认为true
```

# 效果

![效果](http://storage1.imgchr.com/VgJ1J.png)



