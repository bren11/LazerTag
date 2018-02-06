package com.android.lazertag;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dphip on 6/02/2018.
 */

public class LobbyFrontend {
    String key;
    final int numPeople, ID;

    public LobbyFrontend(String key, int numPeople, int ID){
        this.key = key;
        this.numPeople = numPeople;
        this.ID = ID;
    }

    public void newAssignment( String newKey){
        this.key = newKey;
        DatabaseReference lobby = Network.getInstance().getLobby(newKey);
        lobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lobby lobby  = dataSnapshot.getValue(Lobby.class);
                //numPeople = lobby.players.size();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
