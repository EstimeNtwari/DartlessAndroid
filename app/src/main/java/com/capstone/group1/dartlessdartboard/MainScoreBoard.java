package com.capstone.group1.dartlessdartboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainScoreBoard extends AppCompatActivity {

    public String readBTData;
    TextView myScore;
    TextView myDarts ;
    TextView myScore2;
    TextView myDarts2;
    TextView turnID ;
    TextView throw1Score;
    TextView throw2Score;
    TextView throw3Score;
    GameData myGame;
    int incomingSeg=0;
    private Handler mHandler = new Handler();
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    int test=0;
    private int mInterval = 50;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_score_board);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myScore = (TextView) findViewById(R.id.Player1Score);
        myDarts = (TextView) findViewById(R.id.Player1Darts);
        myScore2 = (TextView) findViewById(R.id.Player2Score);
        myDarts2 = (TextView) findViewById(R.id.Player2Darts);
        turnID = (TextView) findViewById(R.id.TurnIndicator);
        throw1Score= (TextView) findViewById(R.id.Throw1);
        throw2Score= (TextView) findViewById(R.id.Throw2);
        throw3Score= (TextView) findViewById(R.id.Throw3);


        myGame = new GameData();

        updateUI();
        startRepeatingTask();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG.setAction("Action", null).show();

                myGame.subScore(myGame.getCurrentTurn(), 100);
                if(myGame.getDarts(myGame.getCurrentTurn())==3){
                    updateUI();
                    myGame.changeTurn();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            myGame.resetDarts();
                            updateUI();
                            Toast toast = Toast.makeText(getApplicationContext(), "PLAYER " + myGame.getCurrentTurn() + " TURN IS OVER", Toast.LENGTH_LONG);
                            toast.show();



                        }
                    }, 500);




                }else {

                    updateUI();
                }



            }
        });



        Button test = (Button) findViewById(R.id.ResetButton);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myGame.resetData();
                try {
                    ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.write((byte)5);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), "NOT LISTENING", Toast.LENGTH_LONG);
                    toast.show();
                }
                updateUI();

            }
        });

    }

    public void updateUI(){
        updateScores();
        turnID.setText("Player " + (myGame.currentTurn+1) + "s Turns");
    }

    public void updateScores(){
        myDarts.setText(String.valueOf(myGame.getDarts(0)));
        myScore.setText(String.valueOf(myGame.getScore(0)));
        myDarts2.setText(String.valueOf(myGame.getDarts(1)));
        myScore2.setText(String.valueOf(myGame.getScore(1)));
        if(myGame.getDartScore(0)==-1){
            throw1Score.setText("Over");
        }else {
            throw1Score.setText("" + myGame.getDartScore(0));
        }
        if(myGame.getDartScore(1)==-1){
            throw2Score.setText("Over");
        }else {
            throw2Score.setText("" + myGame.getDartScore(1));
        }
        if(myGame.getDartScore(2)==-1){
            throw3Score.setText("Over");
        }else {
            throw3Score.setText("" + myGame.getDartScore(2));
        }

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

    @Override
    public void onStop(){
        super.onStop();
        //stopRepeatingTask();

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            try {

                //this function can change value of mInterval.
                if(((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.readState==1 && ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.readBuffer[0]==2) {


                    incomingSeg= ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.readChar;
                    Log.w("MYGAME",  " SUBSCORE" +incomingSeg+"\n");
                    myGame.subScore(myGame.getCurrentTurn(), incomingSeg);

                    if(myGame.getDarts(myGame.getCurrentTurn())==3){
                        Log.w("MYGAME",  " LAST TURN\n");
                        updateUI();
                        myGame.changeTurn();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                myGame.resetDarts();
                                updateUI();
                                Toast toast = Toast.makeText(getApplicationContext(), "PLAYER " + myGame.getCurrentTurn() + " TURN IS OVER", Toast.LENGTH_LONG);
                                toast.show();



                            }
                        }, 500);




                    }else {
                        Log.w("MYGAME",  "MORE TURNS\n");
                        updateUI();
                    }

                    Toast toast = Toast.makeText(getApplicationContext(), "RECEIVED " +incomingSeg+ " from Bluetooth", Toast.LENGTH_SHORT);
                    toast.show();
                    ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.readState=0;

                }

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }



}
