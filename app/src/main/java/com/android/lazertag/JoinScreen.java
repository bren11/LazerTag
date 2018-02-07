package com.android.lazertag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JoinScreen extends AppCompatActivity {
    LobbyFrontend[] visLobbies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visLobbies = new LobbyFrontend[8];
        visLobbies[0] = new LobbyFrontend("none", 0, R.id.n0);
        visLobbies[1] = new LobbyFrontend("none", 0, R.id.n1);
        visLobbies[2] = new LobbyFrontend("none", 0, R.id.n2);
        visLobbies[3] = new LobbyFrontend("none", 0, R.id.n3);
        visLobbies[4] = new LobbyFrontend("none", 0, R.id.n4);
        visLobbies[5] = new LobbyFrontend("none", 0, R.id.n5);
        visLobbies[6] = new LobbyFrontend("none", 0, R.id.n6);
        visLobbies[7] = new LobbyFrontend("none", 0, R.id.n7);
        Network database = Network.getInstance();
        database.getLobbies().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int j = 0;
                for (DataSnapshot lobbyName: dataSnapshot.getChildren()){
                    String name = lobbyName.getValue(String.class);
                    visLobbies[j].newAssignment(name);
                    j++;
                }
                for(int i = 0; i < j && i < 8; i++){
                    final Button button = (Button) findViewById(visLobbies[i].ID);
                    String text = "" + visLobbies[i].key + " (" + visLobbies[i].numPeople + ")";
                    button.setText(text);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setContentView(R.layout.activity_join_screen);
        Button test = (Button)findViewById(R.id.button);
        test.setBackgroundColor(Color.BLUE);

    }
    public void goToMain(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
    public void joinLobby(int index){
        Network database = Network.getInstance();
        DatabaseReference ref = database.getLobby(visLobbies[index].key);

    }
    public void joinLobby1(View view) {
        joinLobby(0);
    }

    public void joinLobby2(View view) {
        joinLobby(1);
    }
    public void joinLobby3(View view) {
        joinLobby(2);
    }
    public void joinLobby4(View view) {
        joinLobby(3);
    }
    public void joinLobby5(View view) {
        joinLobby(4);
    }
    public void joinLobby6(View view) {
        joinLobby(5);
    }
    public void joinLobby7(View view) {
        joinLobby(6);
    }

    public void joinLobby8(View view) {
        joinLobby(7);
    }


}
