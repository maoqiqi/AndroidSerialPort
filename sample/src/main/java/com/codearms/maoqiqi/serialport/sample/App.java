/*
 * Copyright 2019 maoqiqi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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