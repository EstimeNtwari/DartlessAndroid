package com.capstone.group1.dartlessdartboard;

/**
 * Created by estimentwari on 16-03-26.
 */
public class GameData {

    protected int[] score = new int[2];
    protected int[] darts = new int[2];
    protected int[] throwBuff = new int[6];
    protected int currentTurn;
    protected int gameType;
    private int [] segPoints;

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }


    GameData(int gData){

        gameType=gData;
        score[0]=gameType;
        score[1]=gameType;

        segPoints = new int[] {0,50,25,
                                20,1,18,4,13,6,10,15,2,17,3,19,7,16,8,11,14,9,12,5,
                                60,3,54,12,39,18,30,45,6,51,9,57,21,48,24,33,42,27,36,15,
                                20,1,18,4,13,6,10,15,2,17,3,19,7,16,8,11,14,9,12,5,
                                40,2,36,8,26,12,20,30,4,34,6,38,14,32,16,22,28,18,24,10};

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

    public int changeTurn(){
        if(currentTurn==1){
            currentTurn=0;
            return 0;
        }else{
            currentTurn=1;
            return 1;
        }

    }

    public int getScore(int playerID){
        return(score[playerID]);
    }
    public int getDarts(int playerID){return darts[playerID];}

    public boolean subScore(int playerID, int segment){
        if( (score[playerID] - segPoints[segment]) >=0) {
            score[playerID] -= segPoints[segment];
            setDartScore(segPoints[segment]);
            darts[playerID]++;
            return true;
        }else if(segment==0){
            setDartScore(-1);
            darts[playerID]++;
            return false;
        }else{
            setDartScore(-1); //represents score Overflow
            darts[playerID]++;
            return false;
        }
    }




    public void resetData(){

        score[0]=gameType;
        score[1]=gameType;
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
