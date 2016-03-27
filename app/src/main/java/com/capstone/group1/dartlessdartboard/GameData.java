package com.capstone.group1.dartlessdartboard;

/**
 * Created by estimentwari on 16-03-26.
 */
public class GameData {

    protected int[] score = new int[2];
    protected int[] darts = new int[2];
    protected int currentTurn;
    protected int player_count;

    GameData(){
        score[0]=401;
        score[1]=401;

        darts[0]=3;
        darts[1]=3;


        currentTurn=0;
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
    public int getDarts(int playerID){
        return darts[playerID];
    }

    public void subScore(int playerID, int points){
        score[playerID]-=points;
        darts[playerID]--;
    }

    public void newTurn(int playerID){
        darts[playerID]=3;

    }


    public void resetData(){

        score[0]=401;
        score[1]=401;

        darts[0]=3;
        darts[1]=3;

    }
    public void resetDarts(){
        darts[0]=3;
        darts[1]=3;
    }

}
