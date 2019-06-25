package com.codearms.maoqiqi.serialport.sample;

import android.app.Application;

import com.codearms.maoqiqi.utils.LogUtils;
import com.codearms.maoqiqi.utils.ToastUtils;

/**
 * Application
 * Link: https://github.com/maoqiqi/AndroidSerialPort
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019-06-18 10:10
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtils.init(BuildConfig.DEBUG);
        ToastUtils.init(this);
    }

    public static App getInstance() {
        return instance;
    }
}