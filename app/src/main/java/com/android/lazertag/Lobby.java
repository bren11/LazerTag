package com.android.lazertag;

import java.util.HashMap;

public class Lobby {
    HashMap<String, Player> players;
    boolean isPrivate;
    String key;
    int state;
    public Lobby(){
        players = new HashMap<>();
        isPrivate = false;
        String name = Player.getLocalPlayer().getName();
        Player player = Player.getLocalPlayer();
        players.put(name, player);
        key = Player.getLocalPlayer().getName();
        state = 0;
    }
}
