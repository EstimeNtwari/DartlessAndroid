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
import android.os.Handler;

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
    private InputStream mmInputStream;
    BluetoothSocket socket;
    public String readBTData;
    public int readState=0;

    Thread workerThread;
    byte[] readBuffer;
    public byte readChar;
    int readBufferPosition;
    int[] pointValue= new int[83];
    int counter;
    volatile boolean stopWorker;

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

        String address="30:14:12:12:10:85";

        HC06 = myRadio.getRemoteDevice(address);

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
        mmInputStream = socket.getInputStream();
        connected=true;
        beginListenForData();

    }

    public void disconnect() throws IOException {
        try {
            socket.close();
            connected=false;
        } catch (IOException e) {

        }

        //create subroutine to  disconnect socket
    }

    public void write(byte s) throws IOException {
        if(connected) {
            try {
                outputStream.write(s);
            } catch (IOException e) {
            }
        }
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 3; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                Log.w("MYAPP", "READ THREAD RUNNING \n");
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            readBufferPosition=0;
                            Log.w("MYAPP", ""+bytesAvailable+ " BYTES ARE AVAILABLE\n");
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);

                            for(int i=0;i<bytesAvailable;i++)
                            {

                                byte b = packetBytes[i];
                                if(b!=2 && b!=3 && b!=10){
                                    readChar=b;
                                }
                                Log.w("MYAPP", "BYTES ARE AVAILABLE 2: "+b+"\n");
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    Log.w("MYAPP", "READ VALUE:" +readBuffer[readBufferPosition]+"\n");


                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            readState=1;
                                            //do something with data. IE send to MAIN ACTIVITY
                                            readBTData= data;
                                        }
                                    });
                                }
                                else
                                {

                                    readBuffer[readBufferPosition++] = b;
                                    Log.w("MYAPP", "PLACED BYTE IN POSITION "+(readBufferPosition-1)+ "\n");
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }




}