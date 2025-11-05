package com.adr57.netdemo;

import android.app.Application;

import com.adr57.netdemo.network.ApiClient;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo ApiClient
        ApiClient.initialize(this);

    }
}
