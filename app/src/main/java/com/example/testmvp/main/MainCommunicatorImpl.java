package com.example.testmvp.main;

import android.os.Handler;

/**
 * Created by Praveen
 */

public class MainCommunicatorImpl  implements MainCommunicator {
    @Override
    public void getData(final MainCommunicator listener) {
        listener.getData(listener);
    }
}
