package com.capstone.group1.dartlessdartboard;

/**
 * Created by estimentwari on 16-03-26.
 */
public class GameData {

    protected int[] score = new int[2];
    protected int[] darts = new int[2];
    protected int[] throwBuff = new int[6];
    protected int currentTurn;
    protected int player_count;

    GameData(){
        score[0]=401;
        score[1]=401;

        darts[0]=0;
        darts[1]=0;


        currentTurn=0;
    }

    public int getDartScore(int dart){
        return throwBuff[dart +3*currentTurn];
    }

    public void setDartScore(int points){
        throwBuff[darts[currentTurn] + 3*currentTurn]= points;
    }




    public int getCurrentTurn(){
        return currentTurn;
    }

    public void changeTurn(){
        if(currentTurn==1){
            currentTurn=0;
        }else{
            currentTurn=1;
        }

    }

    public int getScore(int playerID){
        return(score[playerID]);
    }
    public int getDarts(int playerID){return darts[playerID];}

    public boolean subScore(int playerID, int points){
        if( (score[playerID] - points) >=0) {
            score[playerID] -= points;
            setDartScore(points);
            darts[playerID]++;
            return true;
        }else{
            setDartScore(-1); //represents score Overflow
            darts[playerID]++;
            return false;
        }

    }




    public void resetData(){

        score[0]=401;
        score[1]=401;
        currentTurn=0;

        darts[0]=0;
        darts[1]=0;

        throwBuff[0]=0;
        throwBuff[1]=0;
        throwBuff[2]=0;
        throwBuff[3]=0;
        throwBuff[4]=0;
        throwBuff[5]=0;


    }
    public void resetDarts(){
        darts[0]=0;
        darts[1]=0;
        throwBuff[0+3*currentTurn]=0;
        throwBuff[1+3*currentTurn]=0;
        throwBuff[2+3*currentTurn]=0;

    }

}
