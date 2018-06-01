package com.example.testmvp.detail;

public class DetailsCommunicatorImpl implements DetailsCommunicator {
    @Override
    public void openDetailsScreen(final DetailsCommunicator listener) {
                listener.openDetailsScreen(listener);
    }
}
