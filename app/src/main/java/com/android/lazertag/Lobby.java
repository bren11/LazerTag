package com.android.lazertag;

import java.util.ArrayList;

/**
 * Created by dphip on 13/12/2017.
 */

public class Lobby {
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    ArrayList<Player> players;
    boolean isPrivate;
    public Lobby(){
        players = new ArrayList<>();
        isPrivate = false;
    }
}
