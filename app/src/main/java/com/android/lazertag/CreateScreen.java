package com.android.lazertag;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.opencv.dnn.Net;

import java.util.ArrayList;

public class CreateScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Network network = Network.getInstance();
        network.addGame(this);
        setContentView(R.layout.activity_create_screen);
        Player.getLocalPlayer().setCurrentLobby(Player.getLocalPlayer().getName());
        final int[] ids = new int[]{R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7};
        Network database = Network.getInstance();
        database.getLobby(Player.getLocalPlayer().getName()).child("players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                ArrayList<String> names = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Player player = data.getValue(Player.class);
                    names.add(player.getName());
                    i++;
                }
                for(int j = 0; j < i && j < 8; j++){
                    Button button = (Button) findViewById(ids[j]);
                    button.setText(names.get(j));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void goToMain(View view){
        Network database = Network.getInstance();
        database.getLobby(Player.getLocalPlayer().getName()).removeValue();
        database.getLobbies().child(Player.getLocalPlayer().getName()).removeValue();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void goToScreen(View view){
        if (android.os.Build.VERSION.SDK_INT < 23){
            Toast.makeText(this, "Your operating system is not compatible with our proprietary BLASTING technology", Toast.LENGTH_SHORT).show();
        } else if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "The app needs certain permissions to run. To give permissions, go to settings>apps>laser tag>permissions.", Toast.LENGTH_SHORT).show();
        } else {
            Network database = Network.getInstance();
            database.getLobby(Player.getLocalPlayer().getName()).child("state").setValue(1);
            Intent intent = new Intent(this, Screen2.class);
            startActivity(intent);
        }
    }

    public void managePlayer(View view){

    }
}
