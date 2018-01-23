package com.android.lazertag;


public class Player {
    public static Player ownPlayer;
    public String name;
    public Player(){
        name = "bob";
    }

    public void changeName(String name){
        name = this.name;
    }

    public static Player getOwnPlayer(){
        if(ownPlayer == null)
            ownPlayer = new Player();
        return ownPlayer;
    }
}
