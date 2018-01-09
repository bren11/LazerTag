package com.android.lazertag;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ryan Whiting on 12/11/2017.
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

    public void addGame(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("nameData", MODE_PRIVATE);
        DatabaseReference gameRef = database.getReference(prefs.getString("Name", "Guest"));
        gameRef.setValue(new Lobby());

    }

    public DatabaseReference getTarget(){
        return database.getReference("target");
    }

    /*public ArrayList<Player> getPlayersInLobby(String name) {
        DatabaseReference remoteLobby = database.getReference(name);


    }*/
}
