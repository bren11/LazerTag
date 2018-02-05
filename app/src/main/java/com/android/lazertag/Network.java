package com.android.lazertag;

import android.app.Activity;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
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

    private Network() { database = FirebaseDatabase.getInstance(); }

    public void addGame(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("nameData", MODE_PRIVATE);
        String name = prefs.getString("Name", "Guest");
        DatabaseReference gameRef = database.getReference(name);
        gameRef.setValue(new Lobby());
        DatabaseReference lobbyList = database.getReference("Lobby");
        lobbyList.child(name).setValue(name);
    }

    public DatabaseReference getTarget(){
        return database.getReference("target");
    }
    public DatabaseReference getLobbies(){ return database.getReference("Lobby");}
    public DatabaseReference getLobby(String key){return database.getReference(key);}
    /*public ArrayList<Player> getPlayersInLobby(String name) {
        DatabaseReference remoteLobby = database.getReference(name);


    }*/
}
