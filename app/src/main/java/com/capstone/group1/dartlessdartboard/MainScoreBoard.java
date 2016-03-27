package com.capstone.group1.dartlessdartboard;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.app.Activity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;


public class MainScoreBoard extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_score_board);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        final GameData myGame = new GameData();
        final TextView myScore = (TextView) findViewById(R.id.CurrentScore);
        final TextView myDarts = (TextView) findViewById(R.id.Darts);
        final TextView turnID = (TextView) findViewById(R.id.TurnIndicator);
        myScore.setText(String.valueOf(myGame.getScore(0)));
        myDarts.setText(String.valueOf(myGame.getDarts(0)));
        turnID.setText("Player " + String.valueOf(myGame.getCurrentTurn() + 1) + "s Turns");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG.setAction("Action", null).show();


                myGame.subScore(myGame.getCurrentTurn(), 10);
                myDarts.setText(String.valueOf(myGame.getDarts(myGame.getCurrentTurn())));
                myScore.setText(String.valueOf(myGame.getScore(myGame.getCurrentTurn())));
                turnID.setText("Player " + String.valueOf(myGame.getCurrentTurn() + 1) + "s Turns");

                if(myGame.getDarts(myGame.getCurrentTurn())==0){
                    myGame.resetDarts();
                    myGame.changeTurn();

                }
            }
        });



        Button test = (Button) findViewById(R.id.ResetButton);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myGame.resetData();
                myDarts.setText(String.valueOf(myGame.getDarts(myGame.getCurrentTurn())));
                myScore.setText(String.valueOf(myGame.getScore(myGame.getCurrentTurn())));
                turnID.setText("Player " + String.valueOf(myGame.getCurrentTurn() + 1) + "s Turns");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_score_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //SWITCH TO SETTING ACTIVITY
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
