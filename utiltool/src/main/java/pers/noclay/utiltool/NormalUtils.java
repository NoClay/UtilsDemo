package pers.noclay.utiltool;

/**
 * Created by i-gaolonghai on 2017/7/21.
 */

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

/**
 * Created by 82661 on 2016/11/6.
 */

public class NormalUtils {


    /**
     * 将[1.23, 1.34, 2.3]转换为对应的FloatList
     * @param data 形如[1.23, 1.34, 2.3]的Float型数据封装的String类型
     * @return List\<Float> 对应的FloatList,格式错误则返回的是null
     */
    public static List<Float> asFloatList(String data){
        if (data.startsWith("[") && data.endsWith("]")){
            int length = data.length();
            data = data.substring(1, length - 1);
            System.out.println(data);
            String[] datas = data.split(", ");
            List<Float> result = new ArrayList<>();
            for (int i = 0; i < datas.length; i++) {
                result.add(new Float(datas[i]));
            }
            return result;
        }
        return null;
    }
    /**
     * 将形如[1, 2, 3, 4]的字符串转换为字符List
     * @param data 形如[1, 2, 2]的Integer型数据封装的String类型
     * @return List\<Integer> 对应的IntegerList,格式错误则返回的是null
     */
    public static List<Integer> asIntegerList(String data){
        if (data.startsWith("[") && data.endsWith("]")){
            int length = data.length();
            data = data.substring(1, length - 1);
            System.out.println(data);
            String[] datas = data.split(", ");
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < datas.length; i++) {
                result.add(Integer.getInteger(datas[i]));
            }
            return result;
        }
        return null;
    }

    /**
     * 判断一个字符串是否是一个浮点型的数据
     * @param floatString 字符串
     * @return 如果是则返回true，否则为false
     */
    public static boolean isFloatString(String floatString){
        return floatString.matches("^([+-]?)\\d+(\\.\\d+)$");
    }

    /**
     * 判断一个字符串是否是一个整型数据
     * @param integer 字符串
     * @return 如果是则返回true，否则返回false
     */
    public static boolean isIntegerString(String integer){
        return integer.matches("^([+-]?)\\d+$");
    }

    /**
     * 检查某一年的某一天存在，避免如2017.2.29
     * @param year 年份
     * @param month 月份
     * @param day 日期
     * @return 如果这一天存在，则返回true，如果这一天在未来，返回false
     */
    public static boolean isDateExistPast(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH) + 1;
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        if (year > year1) {
            return false;
        } else if (year == year1) {
            if (month > month1) {
                return false;
            } else if (month == month1) {
                if (day > day1) {
                    return false;
                }
            }
        }
        return isDateExist(year, month, day);
    }
    /**
     * 检查某一年的某一天存在，避免如2017.2.29
     * @param year 年份
     * @param month 月份
     * @param day 日期
     * @return 如果这一天存在，则返回true，如果这一天在未来，返回false
     */
    public static boolean isDateExist(int year, int month, int day) {
        if (year <= 0 || month <= 0 || day <= 0) {
            return false;
        }
        String monthString = String.valueOf(month);
        if (monthString.matches("^1|3|5|7|8|10|12$")) {
            if (day <= 31) {
                return true;
            }
            return false;
        } else if (monthString.matches("^4|6|9|11$")) {
            if (day <= 30) {
                return true;
            }
            return false;
        } else {
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
                if (day <= 29) {
                    return true;
                }
                return false;
            }
            if (day <= 28) {
                return true;
            }
            return false;
        }
    }

    /**
     * 判断一个字符串可能是手机号码
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        String regex = "1[3|5|7|8|][0-9]{9}";
        return mobiles.matches(regex);
    }

    /**
     * 判断一个字符串是不是数串
     * @param data
     * @return
     */
    public static boolean isAllNumber(String data) {
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c < '0' && c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串编码转换的实现方法
     * @param str        待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            //用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            //用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }
    /**
     * 字符串编码转换的实现方法
     * @param str        待转换编码的字符串
     * @param oldCharset 原编码
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            //用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = str.getBytes(oldCharset);
            //用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 获取html超文本语言中的内容
     * @param html html字符串Eg:String html = "<ul><li>1.hehe</li><li>2.hi</li><li>3.hei</li></ul>";
     * @return 获得的内容集合Eg:[1.hehe, 2.hi, 3.hei]
     */
    public static List<String> getContent(String html) {
        String ss = ">[^<]+<";
        String temp = null;
        Pattern pa = Pattern.compile(ss);
        Matcher ma = null;
        ma = pa.matcher(html);
        List<String> result = new ArrayList<>();
        while (ma.find()) {
            temp = ma.group();
            if (temp != null) {
                if (temp.startsWith(">")) {
                    temp = temp.substring(1);
                }
                if (temp.endsWith("<")) {
                    temp = temp.substring(0, temp.length() - 1);
                }
                if (!temp.equalsIgnoreCase("")) {
                    result.add(temp);
                }
            }
        }
        return result;
    }

    /**
     * 计算已经过去的某一年的某一个月的天数
     * @param year 年份
     * @param month 月份
     * @return 如果计算的是未来的某一个月，则返回为0
     */
    public static int getDayOfMonthPast(int year, int month) {
        if (year <= 0 || month <= 0 || month > 12) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH) + 1;
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        if (year > year1) {
            return 0;
        } else if (year == year1 && month > month1) {
            return 0;
        }
        return getDayOfMonth(year, month);
    }

    /**
     * 返回某一年某一个月的天数
     * @param year 年份
     * @param month 月份
     * @return 如果计算的是未来的某一个月，则返回为0
     */
    public static int getDayOfMonth(int year, int month) {
        if (year <= 0 || month <= 0 || month > 12) {
            return 0;
        }
        String monthString = String.valueOf(month);
        if (monthString.matches("^1|3|5|7|8|10|12$")){
            return 31;
        }else if (monthString.equals("2")){
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
                return 29;
            } else {
                return 28;
            }
        }else if (monthString.matches("^4|6|9|11$")){
            return 30;
        }
        return 0;
    }

    /**
     * 获取某一年的天数
     * @param year 年份
     * @return 天数
     */
    public static int getDayOfYear(int year){
        if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
            return 366;
        }else{
            return 365;
        }
    }

    /**
     * 比较两个日期在天数的大小
     * @param date1 日期1
     * @param date2 日期2
     * @return 返回日期1与日期2相比较的结果，如果日期相同，怎返回0，如果date1小于date2，返回差的天数（负数）
     */
    public static int compareDate(Date date1, Date date2) {
        long mills = date1.getTime() - date2.getTime();
        return (int) ((mills / 1000) / 3600) / 24;
    }
    /**
     * 比较两个日期在天数的大小
     * @param year1
     * @param month1
     * @param day1
     * @param year2
     * @param month2
     * @param day2
     * @return 返回日期1与日期2相比较的结果，如果日期相同，怎返回0，如果date1小于date2，返回（负数）
     */
    public static int compareDate(int year1, int month1, int day1,
                                  int year2, int month2, int day2) {
        Date date1 = new Date(year1, month1 - 1, day1);
        Date date2 = new Date(year2, month2 - 1, day2);
        return compareDate(date1, date2);
    }

    /**
     * 比较两个double类型变量的大小，相等返回0，大于返回1，小于返回-1
     * @param d1
     * @param d2
     * @return
     */
    public static int compareDouble(double d1, double d2) {
        if (Math.abs(d1 - d2) < 1e-6) {
            return 0;
        } else if ((d1 - d2) > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 将整型转换为布尔型,如果为0，则为false，否则为true
     * @param integer
     * @return
     */
    public static boolean booleanValueOfInteger(Integer integer) {
        if (integer == null) {
            return false;
        }
        if (integer.equals(0)) {
            return false;
        }
        return true;
    }

    /**
     * 将Calendar转为时间字符串
     * @param calendar
     * @param timeFormat 为null则采用格式："yyyy-MM-dd HH:mm:00"
     * @return
     */
    public static String valueOfCalendar(Calendar calendar, String timeFormat) {
        if (calendar == null) {
            return null;
        }
        return valueOfDate(calendar.getTime(), timeFormat);
    }

    /**
     * 将Date转为时间字符串
     * @param date
     * @param timeFormat 为null则采用格式："yyyy-MM-dd HH:mm:00"
     * @return
     */
    public static String valueOfDate(Date date, String timeFormat) {
        if (date == null) {
            return null;
        }
        if (timeFormat == null) {
            timeFormat = "yyyy-MM-dd HH:mm:00";
        }
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(date);
    }


    /**
     * 解析时间字符串成为日期
     * @param date
     * @param timeFormat 为null则默认格式为"yyyy-MM-dd HH:mm:00"
     * @return date
     */
    public static Date getTimeFromString(String date, String timeFormat) {
        if (date == null) {
            return null;
        }
        if (timeFormat == null) {
            timeFormat = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保留两位小数，并返回字符串
     * @param value
     * @return
     */
    public static String getTwoShortValue(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(value);//format 返回的是字符串
    }

    /**
     * 字节流转换为十六进制字符串
     * @param data
     * @return
     */
    public static String hexValueOfBytes(byte[] data) {
        StringBuilder buf = new StringBuilder(data.length * 2);
        for (int i = 0; i < data.length; i++) {
            buf.append(String.format("%02x", data[i]));
        }
        return buf.toString();
    }

    /**
     * 十六进制字符串转换为bytes
     * @param hexString
     * @return
     */
    public static byte[] byteValueOfHexString(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase().replace(" ", "");
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToHexByte(hexChars[pos]) << 4 | charToHexByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToHexByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 判断一个字符串是不是一个十六进制的字符串
     * @param src
     * @return
     */
    public static boolean checkHexString(String src) {
        for (char c : src.toCharArray()) {
            if ("0123456789AaBbCcDdEeFf".indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }


}
