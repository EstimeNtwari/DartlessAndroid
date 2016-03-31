package com.capstone.group1.dartlessdartboard;

import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
    TextView t1Sign;
    TextView t2Sign;
    TextView t3Sign;
    TextView sSign;
    TextView scoreID1;
    TextView scoreID2;

    GameData myGame;
    int incomingSeg=0;
    private Handler mHandler = new Handler();
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    int test=0;
    private int mInterval = 250;

    SoundPool mySounds;
    SoundPool.Builder soundPoolBuilder;

    AudioAttributes attributes;
    AudioAttributes.Builder attributesBuilder;

    int soundID_dart;

    Typeface chalk_font;
    Typeface bubble_font;
    Typeface block_font;
    Typeface thin_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_score_board);
        chalk_font = Typeface.createFromAsset(getAssets(),  "fonts/PWChalk.ttf");
        bubble_font = Typeface.createFromAsset(getAssets(),  "fonts/Snacker.ttf");
        block_font = Typeface.createFromAsset(getAssets(),  "fonts/Blocky.ttf");
        thin_font = Typeface.createFromAsset(getAssets(),  "fonts/Thin.otf");

        attributesBuilder = new AudioAttributes.Builder();
        attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        attributes= attributesBuilder.build();

        soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setAudioAttributes(attributes);
        mySounds= soundPoolBuilder.build();

        soundID_dart = mySounds.load(this, R.raw.voice, 1);

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
        t1Sign= (TextView) findViewById(R.id.T1Sign);
        t2Sign= (TextView) findViewById(R.id.T2Sign);
        t3Sign= (TextView) findViewById(R.id.T3Sign);
        sSign = (TextView) findViewById(R.id.ScoreTitle);
        scoreID1= (TextView) findViewById(R.id.scoreID1);
        scoreID2= (TextView) findViewById(R.id.scoreID2);
        throw1Score.setTypeface(chalk_font);
        throw2Score.setTypeface(chalk_font);
        throw3Score.setTypeface(chalk_font);
        scoreID1.setTypeface(thin_font);
        scoreID2.setTypeface(thin_font);


        sSign.setTypeface(chalk_font);
        turnID.setTypeface(block_font);


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
                    }, 2000);




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
                mySounds.play(soundID_dart, 1, 1, 0, 0, 2);
                Toast toast = Toast.makeText(getApplicationContext(), "NEW GAME STARTED ", Toast.LENGTH_LONG);
                toast.show();
                try {
                    ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.write((byte)5);
                } catch (IOException e) {
                    e.printStackTrace();
                    toast = Toast.makeText(getApplicationContext(), "NOT LISTENING", Toast.LENGTH_LONG);
                    toast.show();
                }
                updateUI();

            }
        });

    }

    public void updateUI(){
        updateScores();
        turnID.setText("PLAYER " + (myGame.currentTurn+1) + "s TURN");
    }

    public void updateScores(){
        myDarts.setText(String.valueOf(myGame.getDarts(0)));
        myScore.setText(String.valueOf(myGame.getScore(0)));
        myDarts2.setText(String.valueOf(myGame.getDarts(1)));
        myScore2.setText(String.valueOf(myGame.getScore(1)));
        if(myGame.getDartScore(0)==-1){
            throw1Score.setText("OVER");
        }else if(myGame.getDartScore(0)==-2){
            throw2Score.setText("MISS");
        }else {
            throw1Score.setText("" + myGame.getDartScore(0));
        }
        if(myGame.getDartScore(1)==-1){
            throw2Score.setText("OVER");
        }else if(myGame.getDartScore(1)==-2){
            throw2Score.setText("MISS");
        }else {
            throw2Score.setText("" + myGame.getDartScore(1));
        }
        if(myGame.getDartScore(2)==-1){
            throw3Score.setText("OVER");
        }else if(myGame.getDartScore(2)==-2){
            throw2Score.setText("MISS");
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
                        }, 2000);




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
