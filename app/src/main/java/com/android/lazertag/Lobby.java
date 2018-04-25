package com.android.lazertag;

import java.util.HashMap;

public class Lobby {

    HashMap<String, Player> players;

    boolean isPrivate;
    String key;
    int state;
    Boolean toDeleteSoon;

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Boolean isToDeleteSoon() {
        return toDeleteSoon;
    }

    public void setToDeleteSoon(Boolean toDeleteSoon) {
        this.toDeleteSoon = toDeleteSoon;
    }


    public Lobby(){
        players = new HashMap<>();
        isPrivate = false;
        String name = Player.getLocalPlayer().getName();
        Player.getLocalPlayer().setCurrentLobby(Player.getLocalPlayer().getName());
        Player player = Player.getLocalPlayer();
        players.put(name, player);
        GeneralPreferences.getInstance().setCurrentKey(Player.getLocalPlayer().getName());
        key = Player.getLocalPlayer().getName();
        state = 0;
        toDeleteSoon = false;
    }
}
