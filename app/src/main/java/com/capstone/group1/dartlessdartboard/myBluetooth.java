package com.capstone.group1.dartlessdartboard;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Desktop on 12/15/2014.
 */
public class myBluetooth  {

    private final static int REQUEST_ENABLE_BT = 1;
    public static BluetoothAdapter myRadio ;
    public BluetoothDevice device, HC06;

    private OutputStream outputStream;
    private InputStream inStream;
    BluetoothSocket socket;

    public boolean connected, ScanDone, mControl;

    myBluetooth(){
       connected=false;
        socket = null;
        myRadio = BluetoothAdapter.getDefaultAdapter();
    }



    public boolean getConnected(){return connected;}
    public void setConnected(boolean c){connected=c;}

    public boolean BTstatus(){
        if (myRadio == null) {
            // Device does not support Bluetooth
            return false;
        }

        return myRadio.isEnabled();
    }

    public boolean discover(){
        return myRadio.startDiscovery();
    }


    public boolean TurnBTon(){
        if (myRadio == null) {
            // Device does not support Bluetooth
            return false;
        }

        if (!myRadio.isEnabled()) {
            return myRadio.enable();
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        return false;
    }
    public boolean TurnOffBT(){

        if(myRadio.isEnabled()){
            //run code to disable bluetooth
            return myRadio.disable();
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        return false;
    }

    public void connectDevice() throws IOException {
        int pin=1234;

        Log.w("MYAPP", "SOC" + HC06.getAddress());
        Log.w("MYAPP", "SOC"+HC06.getName());
        final UUID MY_UUID =UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        ParcelUuid[] uuids = HC06.getUuids(); //create unique identifier for device


        //Log.w("MYAPP", "UUID"+uuids[1].getUuid());

        //create connection socket and set to blank
        socket = HC06.createInsecureRfcommSocketToServiceRecord(MY_UUID);
        //socket = HC06.createRfcommSocketToServiceRecord(MY_UUID); //set socket to connected device
        socket.connect(); //establish connection
        outputStream = socket.getOutputStream();
        inStream = socket.getInputStream();
        connected=true;

    }




}