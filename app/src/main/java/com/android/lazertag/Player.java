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
    private Lobby lobby;
    private int hitsSent = 0;
    private int hitsRecieved = 0;
    private Drawable image;
    private Drawable crossHair;
    private Location location;

    private static Player localPlayer;

    public static Player getLocalPlayer() {
        if(localPlayer == null) {
            localPlayer = new Player();
            //localPlayer.image = Settings.datPicBro;
        }
        return  localPlayer;
    }

    public Player(){}

    public Lobby getLobby() { return lobby; }

    public void setLobby(Lobby lobby) { this.lobby = lobby; }
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

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Drawable getCrossHair() {
        return crossHair;
    }

    public void setCrossHair(Drawable image) {
        this.crossHair = image;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location whereareyou) {
        this.location = whereareyou;
    }

}
