package com.android.lazertag;

/**
 * Created by dphip on 6/11/2017.
 */

class NameStorer {
    private static final NameStorer ourInstance = new NameStorer();

    static NameStorer getInstance() {
        return ourInstance;
    }

    private NameStorer() {
        name = "Player";
    }

    private String name;
    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
