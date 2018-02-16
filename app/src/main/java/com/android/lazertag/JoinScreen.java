package com.android.lazertag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                    Button button = (Button) findViewById(visLobbies[i].ID);
                    String text = "" + visLobbies[i].key + " (" + visLobbies[i].numPeople + ")";
                    button.setText(text);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setContentView(R.layout.activity_join_screen);

    }
    public void goToMain(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
    public void joinLobby(int index){
        if(!visLobbies[index].key.equals("none")) {
            Network database = Network.getInstance();
            database.addPlayer(visLobbies[index].key);
            Intent intent = new Intent(this, InLobby.class);
            startActivity(intent);
        }
    }
    public void goToGame(View view){
        if (android.os.Build.VERSION.SDK_INT < 23) {
            Toast.makeText(this, "Your operating system is not compatible with our proprietary BLASTING technology", Toast.LENGTH_SHORT).show();
        } else if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "The app needs certain permissions to run. To give permissions, go to settings>apps>laser tag>permissions.", Toast.LENGTH_SHORT).show();
        } else{
            Intent intent = new Intent(this, Screen2.class);
            startActivity(intent);
        }
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
