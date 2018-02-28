package com.android.lazertag;

/**
 * Created by Ryan on 1/17/2018.
 */

public class Hit {

    public Hit(Player receiver, Player sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    private Player receiver;
    private Player sender;

    public Player getReceiver() {
        return receiver;
    }

    public void setReceiver(Player receiver) {
        this.receiver = receiver;
    }

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }

}
