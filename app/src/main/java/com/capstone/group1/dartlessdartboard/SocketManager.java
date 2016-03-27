package com.capstone.group1.dartlessdartboard;

import android.app.Application;
import android.content.Context;

public class SocketManager extends Application {
    private static SocketManager singleton;
    public int mBluetoothState;


    public synchronized static SocketManager getInstance(Context context) {
        if (null == singleton) {
            singleton = new SocketManager();
        }
        return singleton;
    }

    public synchronized void setState(int state) {
        mBluetoothState = state;
    }

    public synchronized int getState() {
        return mBluetoothState;
    }
}