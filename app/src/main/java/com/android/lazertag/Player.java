package com.android.lazertag;

import android.graphics.drawable.Drawable;

/**
 * Created by dphip on 13/12/2017.
 */

public class Player {

    private String name;
    private Lobby lobby;
    private int hitsSent = 0;
    private int hitsRecieved = 0;
    private Drawable image;

    private static Player localPlayer;

    public static Player getLocalPlayer() {
        if(localPlayer == null) {
            localPlayer = new Player();
            localPlayer.image = Settings.datPicBro;
        }
        return  localPlayer;
    }

    public Player(){}

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

    public void setImage(Drawable image) {
        this.image = image;
    }
}
