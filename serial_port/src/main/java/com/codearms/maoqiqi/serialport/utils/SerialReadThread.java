package com.codearms.maoqiqi.serialport.utils;

import android.os.SystemClock;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取串口数据线程
 * Link: https://github.com/maoqiqi/AndroidSerialPort
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019-06-18 14:25
 */
public class SerialReadThread extends Thread {

    private static final String TAG = SerialReadThread.class.getSimpleName();

    private BufferedInputStream bufferedInputStream;

    public SerialReadThread(InputStream inputStream) {
        bufferedInputStream = new BufferedInputStream(inputStream);
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int size;

        LogUtils.d(TAG, "-------开始读取串口数据-------");

        while (!isInterrupted()) {
            try {
                int available = bufferedInputStream.available();
                if (available > 0) {
                    size = bufferedInputStream.read(buffer);
                    if (size > 0) {
                        onReceive(buffer, size);
                    }
                } else {
                    // 暂停一点时间,免得一直循环造成CPU占用率过高
                    SystemClock.sleep(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.d(TAG, "-------读取串口数据失败-------");
                break;
            }
        }

        LogUtils.d(TAG, "-------结束读取串口数据-------");
    }

    // 处理获取到的数据
    private void onReceive(final byte[] buffer, final int size) {
        LogUtils.d(TAG, "onReceive:" + new String(buffer, 0, size));
    }

    // 关闭读取串口数据线程
    public void close() {
        try {
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            interrupt();
        }
    }
}