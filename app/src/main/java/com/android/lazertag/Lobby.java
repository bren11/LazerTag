package com.android.lazertag;

import java.util.HashMap;

public class Lobby {
    HashMap<String, Player> players;
    boolean isPrivate;
    String key;
    public Lobby(){
        players = new HashMap<>();
        isPrivate = false;
        players.put(Player.getLocalPlayer().getName(), Player.getLocalPlayer());
        key = Player.getLocalPlayer().getName();
    }
}
