package com.android.lazertag;

import java.util.ArrayList;

/**
 * Created by dphip on 13/12/2017.
 */

public class Lobby {
    ArrayList<Player> players;
    boolean isPrivate;
    public Lobby(){
        players = new ArrayList<>();
        isPrivate = false;
    }
}
