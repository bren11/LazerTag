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

    /*public void setImage(String image) {
        this.image = image;
    }

    public void setCrossHair(Drawable crossHair) {
        this.crossHair = crossHair;
    }

    public Drawable[] getCrossHairs() {
        return crossHairs;
    }

    public void setCrossHairs(Drawable[] crossHairs) {
        this.crossHairs = crossHairs;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }*/

    private String name;
    private int hitsSent = 0;
    private int hitsRecieved = 0;
    private String image;

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
        this.image = "testing";
        this.setCurrentLobby("lobby");
        this.setTimeDisabled(0.0);
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
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void changeImage(int imageLocation) {
            this.image = GeneralPreferences.getInstance().getImages()[imageLocation];
    }
}
