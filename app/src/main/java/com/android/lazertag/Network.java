package com.android.lazertag;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by meme on 12/11/2017.
 */

class Network {

    public FirebaseDatabase database;

    private static final Network ourInstance = new Network();

    static Network getInstance() {
        return ourInstance;
    }

    private Network() {
        database = FirebaseDatabase.getInstance();
    }
}
