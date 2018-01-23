package com.android.lazertag;

import java.util.ArrayList;

public class Lobby {
    ArrayList<Player> players;
    boolean isPrivate;
    public Lobby(){
        players = new ArrayList<>();
        isPrivate = false;
        players.add(Player.getOwnPlayer());
    }
}
