package pers.noclay.utiltool;

import android.util.Log;

/**
 * Created by clay on 2018/3/28.
 */

public class Logger {
    public static int LOGLEVEL = 0;
    public static boolean VERBOSE;
    public static boolean DEBUG;
    public static boolean INFO;
    public static boolean WARN;
    public static boolean ERROR;

    public Logger() {
    }

    public static void setDebugMode(boolean debugMode) {
        LOGLEVEL = debugMode?5:0;
        VERBOSE = LOGLEVEL > 4;
        DEBUG = LOGLEVEL > 3;
        INFO = LOGLEVEL > 2;
        WARN = LOGLEVEL > 1;
        ERROR = LOGLEVEL > 0;
    }

    public static void v(String tag, String msg) {
        if(DEBUG) {
            Log.v(tag, msg == null?"":msg);
        }

    }

    public static void v(String tag, String msg, Throwable tr) {
        if(DEBUG) {
            Log.v(tag, msg == null?"":msg, tr);
        }

    }

    public static void d(String tag, String msg) {
        if(DEBUG) {
            Log.d(tag, msg == null?"":msg);
        }

    }

    public static void d(String tag, String msg, Throwable tr) {
        if(DEBUG) {
            Log.d(tag, msg == null?"":msg, tr);
        }

    }


    public static void e(String tag, String msg) {
        if(ERROR) {
            Log.e(tag, msg == null?"":msg);
        }

    }

    public static void e(String tag, String msg, Throwable tr) {
        if(ERROR) {
            Log.e(tag, msg == null?"":msg, tr);
        }

    }


    public static void d_long(String tag, String str) {
        if(DEBUG) {
            str = str.trim();
            int index = 0;
            short maxLength = 4000;

            while(index < str.length()) {
                String sub;
                if(str.length() <= index + maxLength) {
                    sub = str.substring(index);
                } else {
                    sub = str.substring(index, index + maxLength);
                }

                index += maxLength;
                d(tag, sub.trim());
            }
        }

    }

    static {
        VERBOSE = LOGLEVEL > 4;
        DEBUG = LOGLEVEL > 3;
        INFO = LOGLEVEL > 2;
        WARN = LOGLEVEL > 1;
        ERROR = LOGLEVEL > 0;
    }
}
