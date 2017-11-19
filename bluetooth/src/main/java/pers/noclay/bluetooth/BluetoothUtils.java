package pers.noclay.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by i-gaolonghai on 2017/8/21.
 */

public class BluetoothUtils {
    /**
     * String 形如 "98:D3:32:70:5A:44"
     * @return
     */
    public static String getMacAddress(String address) {
        if (address == null || address.length() <= 0) {
            return null;
        }
        if (address.length() > 17) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < address.length(); i++) {
            if ((i + 1) % 3 == 0 && address.charAt(i) == ':'){
                builder.append(":");
            }else if ((i + 1) %3 != 0){
                char temp = address.charAt(i);
                if (temp >= '0' && temp <= '9'){
                    builder.append(temp);
                }else if (temp >= 'a' && temp <= 'z'){
                    builder.append((char) ('A' + temp - 'a'));
                }else if (temp >= 'A' && temp <= 'Z'){
                    builder.append(temp);
                }else{
                    return null;
                }
            }
        }
        return builder.toString();
    }
    /**
     * 检查是否有权限
     * @param activity
     * @param permission
     * @return
     */
    @SuppressLint("WrongConstant")
    public static boolean hasPermission(Activity activity, String permission){
        if (activity == null) {
            return false;
        }
        int check = 0;
        if (Build.VERSION.SDK_INT >= M) {
            check = activity.checkSelfPermission(permission);
        } else {
            check = activity.checkCallingOrSelfPermission(permission);
        }
        if (check != PackageManager.PERMISSION_GRANTED) {
            //没有获取该权限
            return false;
        }
        return true;
    }

    /**
     * 请求权限
     * @param activity
     * @param permission
     */
    public static void requestPermission(final Activity activity, String permission) {
        if (activity == null) {
            return;
        }
        int check = 0;
        if (Build.VERSION.SDK_INT >= M) {
            check = activity.checkSelfPermission(permission);
        } else {
            check = activity.checkCallingOrSelfPermission(permission);
        }
        if (check != PackageManager.PERMISSION_GRANTED) {
            //没有获取该权限
            if (Build.VERSION.SDK_INT >= M) {
                activity.requestPermissions(new String[]{permission}, 0);
            }
        }
    }



    //确认配对

    public static void setPairingConfirmation(Class<?> btClass, BluetoothDevice device, boolean isConfirm) throws Exception {
        Method setPairingConfirmation = btClass.getDeclaredMethod("setPairingConfirmation", boolean.class);
        setPairingConfirmation.invoke(device, isConfirm);
    }

    /**
     * 与设备配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    public static boolean createBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        return (boolean) createBondMethod.invoke(btDevice);
    }

    /**
     * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    public static boolean removeBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        return (boolean) removeBondMethod.invoke(btDevice);
    }

    public static boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]{str.getBytes()});
            Log.e("returnValue", "" + returnValue);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // 取消用户输入
    public static boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        return (boolean) createBondMethod.invoke(device);
    }

    // 取消配对
    public static boolean cancelBondProcess(Class btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        return (boolean) createBondMethod.invoke(device);
    }

    /**
     * @param clsShow
     */
    public static void printAllInform(Class clsShow) {
        try {
            // 取得所有方法
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {
                Log.e("method name", hideMethod[i].getName() + ";and the i is:" + i);
            }
            // 取得所有常量
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++) {
                Log.e("Field name", allFields[i].getName());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean pair(String strAddr, String strPsw) {
        boolean result = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // 检查蓝牙地址是否有效
            Log.d("mylog", "devAdd un effient!");
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            try {
                Log.d("mylog", "NOT BOND_BONDED");
                BluetoothUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                BluetoothUtils.createBond(device.getClass(), device);
//                remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
                result = true;
            } catch (Exception e) {
                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
            }
        } else {
            Log.d("mylog", "HAS BOND_BONDED");
            try {
                BluetoothUtils.createBond(device.getClass(), device);
                BluetoothUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                BluetoothUtils.createBond(device.getClass(), device);
//                remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
                result = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
            }
        }
        return result;
    }
}
