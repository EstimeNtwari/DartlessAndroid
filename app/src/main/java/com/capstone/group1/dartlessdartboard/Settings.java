package com.capstone.group1.dartlessdartboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class Settings extends AppCompatActivity {


    Intent intent;
    String dataOut;
    SharedPreferences.Editor prefEditor;
    SharedPreferences gamePref;
    SoundPool mySounds;
    SoundPool.Builder soundPoolBuilder;

    AudioAttributes attributes;
    AudioAttributes.Builder attributesBuilder;
    private int soundID_BTConn;

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
        Intent intent = getIntent();
        gamePref = getSharedPreferences("prefGameType", MODE_PRIVATE);
        prefEditor = gamePref.edit();

        attributesBuilder = new AudioAttributes.Builder();
        attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        attributes= attributesBuilder.build();

        soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setAudioAttributes(attributes);
        mySounds= soundPoolBuilder.build();
        soundID_BTConn= mySounds.load(this, R.raw.bluetooth_connect, 1);


        int gametype= getGamePref();

        //String gametype= intent.getStringExtra("CallerClass");

        Log.w("MYAPP", "" + gametype + "");
        RadioButton b3 = (RadioButton) findViewById(R.id.threeLabel);
        RadioButton b5 = (RadioButton) findViewById(R.id.fiveLabel);
        RadioButton b8 = (RadioButton) findViewById(R.id.eightLabel);

        if(gametype==301){
            b3.setChecked(true);
            Log.w("MYAPP", "SET3CHECK : " + gametype + "");

        }else if(gametype==501){
            b5.setChecked(true);
            Log.w("MYAPP", "SET5CHECK : " + gametype + "");

        }else if(gametype==801){
            b8.setChecked(true);
            Log.w("MYAPP", "SET8CHECK : " + gametype + "");

        }



        if(((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.getConnected()){
            conStatus.setText("Connected");
            conStatus.setTextColor(Color.GREEN);
            conButton.setText("Disconnect from Dartboard");
        }else{
            conStatus.setText("Disconnected");
            conStatus.setTextColor(Color.RED);
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
                            conStatus.setTextColor(Color.GREEN);
                            conButton.setText("Disconnect from Dartboard");
                            mySounds.play(soundID_BTConn, 1, 1, 0, 0, 1);
                            Toast toast = Toast.makeText(getApplicationContext(), "Succesfully Connected", Toast.LENGTH_SHORT);
                            toast.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), "Error Connecting. Please check dartboard and try again", Toast.LENGTH_LONG);
                            toast.show();


                        }
                    } else {
                        try {
                            ((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.disconnect();
                            conStatus.setText("Disconnected");
                            conStatus.setTextColor(Color.RED);
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
                    ((cBaseApplication) Settings.this.getApplicationContext()).myBlueComms.write(brightness);
                    Toast toast = Toast.makeText(getApplicationContext(), "Brightness set to level "+brightness, Toast.LENGTH_SHORT);
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

    public int getGamePref(){
        return gamePref.getInt("Game1", 0);
    }

    public void setGamePref(int perf){
        prefEditor.putInt("Game1", perf);
        prefEditor.commit();
    }


    public void onRadioButtonClicked(View view) {
            // Is the button now checked?
            intent = new Intent();
            boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.eightLabel:
                    if (checked){
                        // Pirates are the best
                        Toast toast = Toast.makeText(getApplicationContext(), "EIGHT", Toast.LENGTH_SHORT);
                        toast.show();
                        setGamePref(801);
                    break;
                }
            case R.id.fiveLabel:
                if (checked) {
                    // Ninjas rule
                    Toast toast = Toast.makeText(getApplicationContext(), "FIVE", Toast.LENGTH_SHORT);
                    toast.show();
                    setGamePref(501);
                    break;
                }
            case R.id.threeLabel:

                if(checked) {
                    Toast toast = Toast.makeText(getApplicationContext(), "THREE", Toast.LENGTH_SHORT);
                    toast.show();
                    setGamePref(301);
                    break;
                }
        }
    }

}
