package com.example.testmvp;

import android.app.Application;

import com.example.testmvp.generic.ConnectivityReceiver;

/**
 * Created by chayan
 */

public class TestMvp extends Application {
    private static TestMvp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }


    public static synchronized TestMvp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
