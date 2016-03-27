package com.capstone.group1.dartlessdartboard;

/**
 * Created by estimentwari on 16-03-26.
 */
import android.app.Application;

public class cBaseApplication extends Application {

    public myBluetooth myBlueComms;

    @Override
    public void onCreate()
    {
        super.onCreate();
        myBlueComms = new myBluetooth();
    }

}