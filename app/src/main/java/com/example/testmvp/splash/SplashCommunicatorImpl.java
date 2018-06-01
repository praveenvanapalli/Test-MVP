package com.example.testmvp.splash;

import android.os.Handler;

public class SplashCommunicatorImpl implements SplashCommunicator {
    @Override
    public void openSpalshScreen(final SplashCommunicator listener, final String key) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                listener.openSpalshScreen(listener, key);
            }
        }, 2000);
    }
}
