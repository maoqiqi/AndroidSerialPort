package com.codearms.maoqiqi.serialport.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codearms.maoqiqi.serialport.SerialPortFinder;
import com.codearms.maoqiqi.serialport.utils.SerialPortManager;
import com.codearms.maoqiqi.utils.ToastUtils;

/**
 * 演示页面
 * Link: https://github.com/maoqiqi/AndroidSerialPort
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019-06-18 10:00
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] devices;
    private String[] baudRates;
    private String[] dataBits;
    private String[] stopBits;
    private String[] parities;
    private char[] paritiesValue = {'N', 'O', 'E'};

    // 串口设备
    private String device;
    // 波特率
    private String baudRate;
    // 数据位
    private String dataBit;
    // 停止位
    private String stopBit;
    private String parity;
    // 校验位
    private char parityValue;
    // 串口状态
    private boolean opened;

    private TextView tvSerialPort;
    private TextView tvBaudRate;
    // 这三个参数serial_port_extension才需要使用
    private TextView tvDataBits;
    private TextView tvStopBits;
    private TextView tvParity;
    private Button btnOpenSerialPort;
    private EditText etData;
    private Button btnSend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devices = new SerialPortFinder().getAllDevicesPath();
        baudRates = getResources().getStringArray(R.array.baud_rates);
        dataBits = getResources().getStringArray(R.array.data_bits);
        stopBits = getResources().getStringArray(R.array.stop_bits);
        parities = getResources().getStringArray(R.array.parities);

        tvSerialPort = findViewById(R.id.tv_serial_port);
        tvBaudRate = findViewById(R.id.tv_baud_rate);
        tvDataBits = findViewById(R.id.tv_data_bits);
        tvStopBits = findViewById(R.id.tv_stop_bits);
        tvParity = findViewById(R.id.tv_parity);
        Button btnDeviceInfo = findViewById(R.id.btn_device_info);
        btnOpenSerialPort = findViewById(R.id.btn_open_serial_port);
        etData = findViewById(R.id.et_data);
        btnSend = findViewById(R.id.btn_send);

        // 串口设备
        if (devices.length > 0) {
            device = PreferenceUtils.getInstance().getDevice();
            tvSerialPort.setText(device.equals("") ? devices[0] : device);
            tvSerialPort.setOnClickListener(this);
        } else {
            tvSerialPort.setText(R.string.no_device);
        }

        // 波特率
        baudRate = PreferenceUtils.getInstance().getBaudRate();
        if (baudRate.equals("")) baudRate = baudRates[0];
        tvBaudRate.setText(baudRate);
        tvBaudRate.setOnClickListener(this);

        // 数据位
        dataBit = PreferenceUtils.getInstance().getDataBits();
        if (dataBit.equals("")) dataBit = dataBits[0];
        tvDataBits.setText(dataBit);
        tvDataBits.setOnClickListener(this);

        // 停止位
        stopBit = PreferenceUtils.getInstance().getStopBits();
        if (stopBit.equals("")) stopBit = stopBits[0];
        tvStopBits.setText(stopBit);
        tvStopBits.setOnClickListener(this);

        // 校验位
        parity = PreferenceUtils.getInstance().getParity();
        if (parity.equals("")) parity = parities[0];
        tvParity.setText(parity);
        tvParity.setOnClickListener(this);
        int value = PreferenceUtils.getInstance().getParityValue();
        parityValue = value == 0 ? paritiesValue[0] : (char) value;

        btnDeviceInfo.setOnClickListener(this);

        if (devices.length > 0) {
            btnOpenSerialPort.setOnClickListener(this);
        }
        etData.setText(R.string.text);
        etData.setSelection(etData.getText().length());
        btnSend.setOnClickListener(this);

        updateViewState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_serial_port:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.serial_port)
                        .setItems(devices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                device = devices[which];
                                tvSerialPort.setText(device);
                            }
                        }).create().show();
                break;
            case R.id.tv_baud_rate:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.baud_rate)
                        .setItems(baudRates, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                baudRate = baudRates[which];
                                tvBaudRate.setText(baudRate);
                            }
                        }).create().show();
                break;
            case R.id.tv_data_bits:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.data_bits)
                        .setItems(dataBits, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBit = dataBits[which];
                                tvDataBits.setText(dataBit);
                            }
                        }).create().show();
                break;
            case R.id.tv_stop_bits:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.stop_bits)
                        .setItems(stopBits, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stopBit = stopBits[which];
                                tvStopBits.setText(stopBit);
                            }
                        }).create().show();
                break;
            case R.id.tv_parity:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.parity)
                        .setItems(parities, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                parityValue = paritiesValue[which];
                                tvParity.setText(parities[which]);
                            }
                        }).create().show();
                break;
            case R.id.btn_device_info:
                startActivity(new Intent(this, DeviceInfoActivity.class));
                break;
            case R.id.btn_open_serial_port:
                // 打开或关闭串口
                if (opened) {
                    SerialPortManager.getInstance().close();
                    opened = false;
                } else {
                    // 保存配置
                    PreferenceUtils.getInstance().setDevice(device);
                    PreferenceUtils.getInstance().setBaudRate(baudRate);
                    PreferenceUtils.getInstance().setDataBits(dataBit);
                    PreferenceUtils.getInstance().setStopBits(stopBit);
                    PreferenceUtils.getInstance().setParity(parity);
                    PreferenceUtils.getInstance().setParityValue(parityValue);

                    opened = SerialPortManager.getInstance().open(device, Integer.parseInt(baudRate),
                            Integer.parseInt(dataBit), Integer.parseInt(stopBit), parityValue, 0) != null;
                    if (opened) {
                        ToastUtils.show(getString(R.string.open_success));
                    } else {
                        ToastUtils.show(getString(R.string.open_fail));
                    }
                }
                updateViewState();
                break;
            case R.id.btn_send:
                String text = etData.getText().toString();
                SerialPortManager.getInstance().sendData(text);
                break;
        }
    }

    private void updateViewState() {
        btnOpenSerialPort.setText(opened ? R.string.close_serial_port : R.string.open_serial_port);

        tvSerialPort.setEnabled(!opened);
        tvBaudRate.setEnabled(!opened);
        tvDataBits.setEnabled(!opened);
        tvStopBits.setEnabled(!opened);
        tvParity.setEnabled(!opened);

        btnSend.setEnabled(opened);
    }

    @Override
    protected void onDestroy() {
        SerialPortManager.getInstance().close();
        super.onDestroy();
    }
}