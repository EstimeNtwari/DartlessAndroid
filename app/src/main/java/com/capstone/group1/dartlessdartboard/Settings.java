package com.capstone.group1.dartlessdartboard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Switch btSwitch = (Switch) findViewById(R.id.BluetoothSW);

        //automatically set the switch based on bluetooth.
        //btSwitch.setChecked();

        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if(!isChecked){
                    ((cBaseApplication)Settings.this.getApplicationContext()).myBlueComms.TurnOffBT();
                }else{
                    ((cBaseApplication)Settings.this.getApplicationContext()).myBlueComms.TurnBTon();
                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}
