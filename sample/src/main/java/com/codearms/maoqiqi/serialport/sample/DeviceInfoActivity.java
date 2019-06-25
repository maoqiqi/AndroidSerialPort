package com.codearms.maoqiqi.serialport.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codearms.maoqiqi.serialport.SerialPortFinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备信息
 * Link: https://github.com/maoqiqi/AndroidSerialPort
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019-06-21 17:36
 */
public class DeviceInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, new ArrayList<String>());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        try {
            List<String> data = new ArrayList<>();
            for (SerialPortFinder.Driver driver : new SerialPortFinder().getDrivers()) {
                String driverName = driver.getName();
                for (File file : driver.getDevices()) {
                    String deviceName = file.getName();
                    boolean canRead = file.canRead();
                    boolean canWrite = file.canWrite();
                    boolean canExecute = file.canExecute();
                    String path = file.getAbsolutePath();

                    StringBuilder permission = new StringBuilder();
                    permission.append("[");
                    permission.append(canRead ? " 可读" : "不可读");
                    permission.append(canWrite ? "  可写 " : " 不可写 ");
                    permission.append(canExecute ? "可执行" : "不可执行");
                    permission.append("]");

                    String msg = driverName + "-" + deviceName + "(" + path + ")" + " 权限:" + permission;
                    data.add(msg);
                }
            }
            adapter.replaceData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    static final class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Context context;
        private List<String> data;

        RecyclerViewAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(View.inflate(context, R.layout.item_device_info, null));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.tv.setText(data.get(i));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        void replaceData(List<String> list) {
            // 不是同一个引用才清空列表
            if (list != data) {
                data.clear();
                data.addAll(list);
            }
            notifyDataSetChanged();
        }

    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}