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

package com.codearms.maoqiqi.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 获取串口的类(其实就是获取输入输出流)
 * Link: https://github.com/maoqiqi/AndroidSerialPort
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019-06-13 14:56
 */
public class SerialPort {

    private static final String TAG = "SerialPort";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    /**
     * 获得一个串口
     *
     * @param device   要操作的文件对象
     * @param baudRate 波特率
     * @param dataBits 数据位
     * @param stopBits 停止位
     * @param parity   校验位
     * @param flags    文件操作的标志
     * @throws SecurityException SecurityException
     * @throws IOException       IOException
     */
    public SerialPort(File device, int baudRate, int dataBits, int stopBits, int parity, int flags)
            throws SecurityException, IOException {

        /* Check access permission */
        // 检查访问权限,如果没有读写权限,进行文件操,修改文件访问权限
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                // 通过挂载到linux的方式,修改文件的操作权限
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());

                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        mFd = open(device.getAbsolutePath(), flags, baudRate, dataBits, stopBits, parity);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * JNI(调用java本地接口,实现串口的打开和关闭)
     *
     * @param path     串口设备的据对路径
     * @param baudRate 波特率
     * @param dataBits 数据位
     * @param stopBits 停止位(默认为1)
     * @param parity   校验位(一般默认为NONE)
     * @param flags    文件操作的标志
     * @return FileDescriptor
     */
    private native static FileDescriptor open(String path, int baudRate, int dataBits, int stopBits, int parity, int flags);

    public native void close();

    static {
        // 加载SO库
        System.loadLibrary("serial_port");
    }
}