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

package com.codearms.maoqiqi.serialport.utils;

import com.codearms.maoqiqi.serialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 串口管理类
 * Link: https://github.com/maoqiqi/AndroidSerialPort
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019-06-18 14:19
 */
public class SerialPortManager {

    private static final String TAG = SerialPortManager.class.getSimpleName();

    private static SerialPortManager instance;

    private SerialPort serialPort;
    private SerialReadThread readThread;
    private OutputStream outputStream;

    public static SerialPortManager getInstance() {
        if (instance == null) {
            synchronized (SerialPortManager.class) {
                if (instance == null) {
                    instance = new SerialPortManager();
                }
            }
        }
        return instance;
    }

    // 打开串口
    public SerialPort open(String path, int baudRate, int flags) {
        if (serialPort == null) {
            try {
                // 打开串口
                serialPort = new SerialPort(new File(path), baudRate, flags);

                // 创建一个接收线程
                readThread = new SerialReadThread(serialPort.getInputStream());
                // 开启接收消息的线程
                readThread.start();

                outputStream = serialPort.getOutputStream();
            } catch (SecurityException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "您没有串行端口的读/写权限。");
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "由于未知原因，串口无法打开。");
            }
        }
        return serialPort;
    }

    // 发送数据
    public void sendData(String data) {
        LogUtils.d(TAG, "发送数据：" + data + ",hex:" + StringUtils.str2HexStr(data));
        try {
            outputStream.write(StringUtils.str2HexStr(data).getBytes());
            LogUtils.i(TAG, "串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.i(TAG, "串口数据发送失败");
        }
    }

    // 关闭串口
    public void close() {
        // 停止接收消息的线程
        if (readThread != null) {
            readThread.interrupt();
            readThread.close();
            readThread = null;
        }

        if (outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }
}