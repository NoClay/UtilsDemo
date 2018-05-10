package pers.noclay.utiltool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by i-gaolonghai on 2017/8/8.
 */

public class SystemUtils {
    private static final String TAG = "SystemUtils";
    /**
     * 请求权限
     * @param activity
     * @param permission
     */
    public static void requestPermission(final Activity activity, String permission) {
        if (activity == null) {
            return;
        }
        int check = PackageManager.PERMISSION_DENIED;
        if (Build.VERSION.SDK_INT >= M) {
            check = activity.checkSelfPermission(permission);
        } else {
            check = activity.checkCallingOrSelfPermission(permission);
        }
        if (check != PackageManager.PERMISSION_GRANTED) {
            //没有获取该权限
            if (Build.VERSION.SDK_INT >= M) {
                activity.requestPermissions(new String[]{
                        permission
                }, 0);
            }
        }
    }

    /**
     * 判断一个应用是否已经安装在手机上，如果安装的话，返回当前的sourceDir，否则返回为null
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 返回sourceDir
     */
    public static String hasInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            if (info != null){
                return info.sourceDir;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判定一个应用是不是预装应用
     * @param app
     * @return
     */
    public static boolean isPresetApp(ApplicationInfo app) {
        // without system app
        if (app == null){
            return false;
        }
        if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
            String appPath = app.sourceDir;
            if (appPath.startsWith("/system")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个应用是不是预装应用
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPresetApp(Context context, String packageName) {
        // without system app
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo app = pm.getApplicationInfo(packageName, 0);
            if (app == null){
                return false;
            }
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                String appPath = app.sourceDir;
                if (appPath.startsWith("/system")) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }



    /**
     * 判断一个应用是不是系统应用
     * @param app
     * @return
     */
    public static boolean isSystemApp(ApplicationInfo app) {
        // 查看该应用是不是系统应用
        if (app == null){
            return false;
        }
        if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            String appPath = app.sourceDir;
            if (appPath.startsWith("/system")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个应用是不是系统应用
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSystemApp(Context context, String packageName) {
        // 查看该应用是不是系统应用
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo app = pm.getApplicationInfo(packageName, 0);
            if (app == null){
                return false;
            }
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                String appPath = app.sourceDir;
                if (appPath.startsWith("/system")) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 如果必要，销毁进程
     * @param process 进程对象
     */
    public static void destroyProcess(Process process) {
        try {
            if (null != process) {
                process.exitValue();
            }
        } catch (IllegalThreadStateException e) {
            try {
                if (null != process) {
                    process.destroy();
                    process.waitFor();
                    process = null;
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 执行shell命令，获取执行结果
     * @param cmd
     * @return
     */
    public static String execShell(String cmd) {
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = null;
            StringBuilder buffer = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            return buffer.toString().trim();
        } catch (Throwable t) {
            Log.e(TAG, "[" + cmd + "] exec shell failed(Throwable): " + t.getMessage());
        } finally {
            // 结束进程
            destroyProcess(process);
            // 关闭流对象
            try {
                if (null != reader) {
                    reader.close();
                    reader = null;
                }
            } catch (Throwable t) {
                Log.e(TAG, "close reader failed(Throwable): " + t.getMessage());
            }
        }

        return "";
    }

    /**
     * 检查网络状态，如果网络良好，则返回true
     * @param context
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null)
            return info.isConnected();
        return false;
    }


    /**
     * 检查是否有权限
     * @param activity
     * @param permission
     * @return
     */
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
}
