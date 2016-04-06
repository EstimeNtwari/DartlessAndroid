package com.capstone.group1.dartlessdartboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class MainScoreBoard extends AppCompatActivity {


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
    Animation shake;
    ScaleAnimation grow;
    ScaleAnimation shrink;
    Button newGame ;
    RadioGroup group;
    SharedPreferences gamePref;
    SharedPreferences.Editor prefEditor;
    Vibrator v;

    static GameData myGame;
    int incomingSeg=0;
    private Handler mHandler = new Handler();
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    int test=0;
    private int mInterval = 2;

    SoundPool mySounds;
    SoundPool.Builder soundPoolBuilder;

    AudioAttributes attributes;
    AudioAttributes.Builder attributesBuilder;

    int soundID_dart;
    int soundID_newgame;

    Typeface chalk_font;
    Typeface bubble_font;
    Typeface block_font;
    Typeface thin_font;
    private int soundID_BTConn;
    private int soundID_P1;
    private int soundID_P2;
    private int soundID_P1win;
    private int soundID_P2win;
    private int soundID_miss;
    private int soundID_over;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initMain();



        updateUI();







        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.getConnected()) {
                    shrinkNewGame();
                    try {
                        ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.write((byte) 20);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    turnID.startAnimation(shake);
                    myGame.resetData();
                    mySounds.play(soundID_newgame, 1, 1, 0, 0, 1);
                    Toast toast = Toast.makeText(getApplicationContext(), "NEW GAME STARTED ", Toast.LENGTH_LONG);
                    toast.show();

                    updateUI();
                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "PLEASE CONNECT TO THE DARTBOARD ", Toast.LENGTH_LONG);
                    toast.show();
                    growNewGame();
                }

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

    @Override
    public void onStop(){
        super.onStop();
        stopRepeatingTask();

    }


    @Override
    public void onStart(){
        super.onStart();
        startRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            try {
                //if (((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.getConnected()){

                    //this function can change value of mInterval.
                    if (((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.readState ==1) {
                        mySounds.play(soundID_dart, 1, 1, 0, 0, 1);
                        v.vibrate(250);
                        incomingSeg = ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.readChar;
                        Log.w("MYGAME", " SUBSCORE" + incomingSeg + "\n");
                        myGame.subScore(myGame.getCurrentTurn(), incomingSeg);


                        if (myGame.getDarts(myGame.getCurrentTurn()) == 3) {
                            Log.w("MYGAME", " LAST TURN\n");
                            updateUI();
                            myGame.changeTurn();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    turnID.startAnimation(shake);
                                    myGame.resetDarts();
                                    try {
                                        ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.write((byte) 20);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if(myGame.currentTurn==0){
                                        if(myGame.getScore(1)==0) {
                                            mySounds.play(soundID_P2win,1,1,0,0,1);
                                            growNewGame();
                                        }else{
                                            mySounds.play(soundID_P1, 1, 1, 0, 0, 1);
                                        }
                                    }else{
                                        if(myGame.getScore(0)==0) {
                                            mySounds.play(soundID_P2win,1,1,0,0,1);
                                            growNewGame();
                                        }else{
                                            mySounds.play(soundID_P2, 1, 1, 0, 0, 1);
                                        }
                                    }
                                    updateUI();
                                    Toast toast = Toast.makeText(getApplicationContext(), "PLAYER " + myGame.getCurrentTurn() + " TURN IS OVER", Toast.LENGTH_LONG);
                                    toast.show();

                                }
                            }, 5000);


                        } else {
                            Log.w("MYGAME", "MORE TURNS\n");
                            updateUI();
                        }

                        Toast toast = Toast.makeText(getApplicationContext(), "RECEIVED " + incomingSeg + " from Bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                        ((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.readState = 0;


                    }




            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };


    public void updateUI(){
        updateScores();
        turnID.setText("PLAYER " + (myGame.currentTurn + 1) + "s TURN");
    }

    public void growNewGame(){
        grow =  new ScaleAnimation(1f, 3.65f, 1f, 10.5f, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
        grow.setFillAfter(true);
        grow.setFillEnabled(true);
        grow.setDuration(500);
        grow.setInterpolator(new OvershootInterpolator(3f));
        newGame.setAnimation(grow);
    }
    public void shrinkNewGame(){
        shrink = new ScaleAnimation(3.65f, 1f, 10.5f, 1f, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
        shrink.setFillAfter(true);
        shrink.setFillEnabled(true);
        shrink.setDuration(500);
        shrink.setInterpolator(new OvershootInterpolator(3f));
        newGame.setAnimation(shrink);
    }

    public void initMain(){

        setContentView(R.layout.activity_main_score_board);
        newGame = (Button) findViewById(R.id.ResetButton);
        chalk_font = Typeface.createFromAsset(getAssets(), "fonts/PWChalk.ttf");
        bubble_font = Typeface.createFromAsset(getAssets(), "fonts/Snacker.ttf");
        block_font = Typeface.createFromAsset(getAssets(), "fonts/Blocky.ttf");
        thin_font = Typeface.createFromAsset(getAssets(), "fonts/Thin.otf");

        shake= AnimationUtils.loadAnimation(this, R.anim.shake);

        gamePref = getSharedPreferences("prefGameType", MODE_PRIVATE);

        prefEditor = gamePref.edit();

        if(getGamePref()==0){
            setGamePref(501);
        }
        if(myGame == null){
            myGame = new GameData(getGamePref());
        }
        if(getGamePref()!=myGame.gameType){
            Log.w("MYAPP","GT="+myGame.gameType+ " GPREF = "+getGamePref()+  ". CHANGED GAMETYPE FROM SETTINGS.\n");
            myGame = new GameData(getGamePref());
            Log.w("MYAPP","NEW GT="+myGame.gameType+".\n");
        }


        v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        attributesBuilder = new AudioAttributes.Builder();
        attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        attributes= attributesBuilder.build();

        soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setAudioAttributes(attributes);
        mySounds= soundPoolBuilder.build();

        soundID_dart = mySounds.load(this, R.raw.darthit, 1);
        soundID_newgame= mySounds.load(this, R.raw.gamestart, 1);

        soundID_P1= mySounds.load(this, R.raw.player1,1);
        soundID_P2= mySounds.load(this, R.raw.player2,1);
        soundID_P1win= mySounds.load(this, R.raw.player_1_wins,1);
        soundID_P2win= mySounds.load(this, R.raw.player_2_wins,1);
        soundID_miss= mySounds.load(this, R.raw.miss_sound,1);
        soundID_over= mySounds.load(this, R.raw.miss_sound_2,1);


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

        if(!((cBaseApplication) MainScoreBoard.this.getApplicationContext()).myBlueComms.getConnected()){
            growNewGame();
        }

    }

    public int getGamePref(){
        return gamePref.getInt("Game1", 0);
    }

    public void setGamePref(int perf){
        prefEditor.putInt("Game1", perf);
        prefEditor.commit();
    }


    public void updateScores(){


        myDarts.setText(String.valueOf(myGame.getDarts(0)));
        myScore.setText(String.valueOf(myGame.getScore(0)));
        myDarts2.setText(String.valueOf(myGame.getDarts(1)));
        myScore2.setText(String.valueOf(myGame.getScore(1)));

        if(myGame.getDartScore(0)==-1 ){
            throw1Score.setText("OVER");
            mySounds.play(soundID_over, 1, 1, 0, 0, 1);
            throw1Score.setAnimation(shake);
        }else if(myGame.getDartScore(0)==-2){
            throw1Score.setText("MISS");
            mySounds.play(soundID_miss, 1, 1, 0, 0, 1);
            throw1Score.setAnimation(shake);
        } else {
            throw1Score.setText("" + myGame.getDartScore(0));

        }
        if(myGame.getDartScore(1)==-1){
            throw2Score.setText("OVER");
            mySounds.play(soundID_over, 1, 1, 0, 0, 1);
            throw2Score.setAnimation(shake);
        }else if(myGame.getDartScore(1)==-2){
            throw2Score.setText("MISS");
            mySounds.play(soundID_miss, 1, 1, 0, 0, 1);
            throw2Score.setAnimation(shake);
        } else {
            throw2Score.setText("" + myGame.getDartScore(1));

        }
        if(myGame.getDartScore(2)==-1){
            throw3Score.setText("OVER");
            mySounds.play(soundID_over, 1, 1, 0, 0, 1);
            throw3Score.setAnimation(shake);
        }else if(myGame.getDartScore(2)==-2){
            throw3Score.setText("MISS");
            mySounds.play(soundID_miss, 1, 1, 0, 0, 1);
            throw3Score.setAnimation(shake);
        } else {
            throw3Score.setText("" + myGame.getDartScore(2));

        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);

    }



}
