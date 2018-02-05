package com.android.lazertag;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dphip on 13/12/2017.
 */

public class Player {

    private String name;
    private Lobby lobby;
    private int hitsSent = 0;
    private int hitsRecieved = 0;
    private Drawable image;
    private Drawable crossHair;
    private Location location;
    private Drawable[] crossHairs;
    private Drawable[] images;

    private static Player localPlayer;

    public static Player getLocalPlayer() {
        if(localPlayer == null) {
            localPlayer = new Player();
        }
        return  localPlayer;
    }

    public Player(){
    }

    public Lobby getLobby() { return lobby; }

    public void setLobby(Lobby lobby) { this.lobby = lobby; }

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

    public Drawable getImage() {
        return image;
    }

    public void setImage(int imageLocation) {
            this.image = this.images[imageLocation];
    }

    public Drawable getCrossHair() {
        return crossHair;
    }

    public void setCrossHair(int hairLocation) {
        this.crossHair = this.crossHairs[hairLocation];
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location whereareyou) {
        this.location = whereareyou;
    }

    public void setcrossHairs(Drawable[] crossHairs) {
        this.crossHairs = crossHairs;
    }

    public void setimages(Drawable[] images) {
        this.images = images;
    }

}
