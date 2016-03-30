package com.capstone.group1.dartlessdartboard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class Settings extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Switch btSwitch = (Switch) findViewById(R.id.BluetoothSW);
        final TextView conStatus= (TextView) findViewById(R.id.ConnectionStatus);
        final Button conButton = (Button) findViewById(R.id.ConnectButton);
        final Button brightButton = (Button) findViewById(R.id.SetBrightness);
        final SeekBar brightBar = (SeekBar) findViewById(R.id.BrightBar);




        if(((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.getConnected()){
            conStatus.setText("Connected");
            conButton.setText("Disconnect from Dartboard");
        }else{
            conStatus.setText("Disconnected");
            conButton.setText("Connect to Dartboard");
        }


        btSwitch.setChecked(((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.BTstatus());

        setSupportActionBar(toolbar);


        //automatically set the switch based on bluetooth.
        //btSwitch.setChecked();

        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (!isChecked) {
                    ((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.TurnOffBT();

                } else {
                    ((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.TurnBTon();

                }
            }
        });

        conButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.BTstatus()) {

                    if (!((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.getConnected()) {

                        try {
                            ((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.connectDevice();
                            conStatus.setText("Connected");
                            conButton.setText("Disconnect from Dartboard");
                            Toast toast = Toast.makeText(getApplicationContext(), "Succesfully Connected", Toast.LENGTH_SHORT);
                            toast.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), "Error Connecting. Please check Dartboard", Toast.LENGTH_SHORT);
                            toast.show();


                        }
                    } else {
                        try {
                            ((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.disconnect();
                            conStatus.setText("Disconnected");
                            conButton.setText("Connect to Dartboard");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Turn on Bluetooth", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });

        brightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte brightness= (byte) brightBar.getProgress();


                try {

                    ((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.write((byte)'[');
                    ((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.write(brightness);
                    Toast toast = Toast.makeText(getApplicationContext(), ""+brightness, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Connect to the Dartboard", Toast.LENGTH_SHORT);
                    toast.show();

                }


            }


            });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}
