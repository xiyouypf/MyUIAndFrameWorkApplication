package com.ypf.myunique;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

public class DeviceIdUtil {
    public static String getDeviceId(Context context) {
        StringBuilder builder = new StringBuilder();
        String imei = getImei(context);
        String androidId = getAndroidId(context);//手机型号 +手机
        String serial = getSerial();//         唯一
        String id = getDeviceUUID().replace("-", "");
        //追加imei
        if (!TextUtils.isEmpty(imei)) {
            builder.append(imei);
            builder.append("|");
        }
        //追加androidid
        if (!TextUtils.isEmpty(androidId)) {
            builder.append(androidId);
            builder.append("|");
        }
        //追加serial
        if (!TextUtils.isEmpty(serial)) {
            builder.append(serial);
            builder.append("|");
        }
        //追加硬件uuid
        if (!TextUtils.isEmpty(id)) {
            builder.append(id);
        }
        //        一系列的字符串  ----11 硬件标识有关   手机
        //生成SHA1，统一DeviceId长度
        if (builder.length() > 0) {
            try {
                byte[] hash = getHashByString(builder.toString());
                String sha1 = bytesToHex(hash);
                if (!TextUtils.isEmpty(sha1)) {
                    //返回最终的DeviceId
                    return sha1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转16进制字符串
     *
     * @param data 数据
     * @return 16进制字符串
     */
    private static String bytesToHex(byte[] data) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 0; i < data.length; i++) {
            temp = (Integer.toHexString(data[i] & 0xff));
            if (temp.length() == 1) {
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString().toUpperCase(Locale.CHINA);
    }

    /**
     * 取SHA1
     *
     * @param data 数据
     * @return 对应的hash值
     */
    private static byte[] getHashByString(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));
            return messageDigest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "".getBytes();
    }

    //获得硬件uuid（根据硬件相关属性，生成uuid）（无需权限）  数字  0   -10
    private static String getDeviceUUID() {
        String dev = "1001" +
                Build.BOARD +
                Build.BRAND +
                Build.DEVICE +
                Build.HARDWARE +
                Build.ID +
                Build.MODEL +
                Build.PRODUCT +
                Build.SERIAL;
        return new UUID(dev.hashCode(), Build.SERIAL.hashCode()).toString();
    }

    private static String getSerial() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Build.getSerial();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得设备的AndroidId
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    private static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //需要获得READ_PHONE_STATE权限，>=6.0，默认返回null
    private static String getImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getImei();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
