package com.android.lazertag;

import android.app.Activity;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ryan Whiting on 12/11/2017.
 */

class Network {

    public FirebaseDatabase database;
    public DataSnapshot currentLobby;

    private static final Network ourInstance = new Network();

    static Network getInstance() {
        return ourInstance;
    }

    private Network() { database = FirebaseDatabase.getInstance(); }

    public void addGame(){
        String name = Player.getLocalPlayer().getName();
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

    public void addPlayer(String key){
        ourInstance.database.getReference(key).child("players").child(Player.getLocalPlayer().getName()).setValue(Player.getLocalPlayer());
        Player.getLocalPlayer().setCurrentLobby(key);
    }
}
