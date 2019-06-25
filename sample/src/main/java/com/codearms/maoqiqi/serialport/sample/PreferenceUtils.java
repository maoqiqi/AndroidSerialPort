package com.codearms.maoqiqi.serialport.sample;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 存储数据
 * Link: https://github.com/maoqiqi/AndroidSerialPort
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019-06-18 10:06
 */
public class PreferenceUtils {

    private static PreferenceUtils instance;

    private SharedPreferences preferences;

    private PreferenceUtils() {
        preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
    }

    public static PreferenceUtils getInstance() {
        if (instance == null) {
            synchronized (PreferenceUtils.class) {
                if (instance == null) {
                    instance = new PreferenceUtils();
                }
            }
        }
        return instance;
    }

    public String getDevice() {
        return preferences.getString("device", "");
    }

    public void setDevice(String device) {
        preferences.edit().putString("device", device).apply();
    }

    public String getBaudRate() {
        return preferences.getString("baudRate", "");
    }

    public void setBaudRate(String baudRate) {
        preferences.edit().putString("baudRate", baudRate).apply();
    }

    public String getDataBits() {
        return preferences.getString("dataBits", "");
    }

    public void setDataBits(String dataBits) {
        preferences.edit().putString("dataBits", dataBits).apply();
    }

    public String getStopBits() {
        return preferences.getString("stopBits", "");
    }

    public void setStopBits(String stopBits) {
        preferences.edit().putString("stopBits", stopBits).apply();
    }

    public String getParity() {
        return preferences.getString("parity", "");
    }

    public void setParity(String parity) {
        preferences.edit().putString("parity", parity).apply();
    }

    public int getParityValue() {
        return preferences.getInt("parityValue", 0);
    }

    public void setParityValue(int parity) {
        preferences.edit().putInt("parityValue", parity).apply();
    }
}