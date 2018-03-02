 package com.android.lazertag;

import android.graphics.drawable.Drawable;
import android.location.Location;

/**
 * Created by dphip on 13/12/2017.
 */

public class Player {


    public static void setLocalPlayer(Player localPlayer) {
        Player.localPlayer = localPlayer;
    }
    private String name;
    private int hitsSent = 0;
    private int hitsRecieved = 0;
    private String imageTarget;



    private String currentLobby;
    private double timeDisabled;

    private static Player localPlayer;

    public static Player getLocalPlayer() {
        if(localPlayer == null) {
            localPlayer = new Player();
        }
        return  localPlayer;
    }

    public Player(){
        this.setName("player");
        this.imageTarget = "testing";
        this.setCurrentLobby("lobby");
    }

    public double getTimeDisabled(){
        return timeDisabled;
    }

    public void setTimeDisabled(double timeDisabled){
        this.timeDisabled = timeDisabled;
    }

    public String getCurrentLobby() {
        return currentLobby;
    }

    public void setCurrentLobby(String currentLobby) {
        this.currentLobby = currentLobby;
    }

    public void setHitsSent(int hitsSent) {
        this.hitsSent = hitsSent;
    }

    public void setHitsRecieved(int hitsRecieved) {
        this.hitsRecieved = hitsRecieved;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHitsSent() {
        return hitsSent;
    }

    public void onHitSent() {
        hitsSent++;
    }

    public int getHitsRecieved() {
        return hitsRecieved;
    }

    public void onHitRecieved() { hitsRecieved++; }

    public String getImage() {
        return imageTarget;
    }
/*
    public void setImage(int imageLocation) {
            this.imageTarget = GeneralPreferences.getInstance().getImages()[imageLocation];
    }
*/

}
