package com.android.lazertag;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InLobby extends AppCompatActivity {
    ValueEventListener playerListner;
    ValueEventListener startListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_screen);
        (findViewById(R.id.button2)).setVisibility(View.GONE);
        final int[] ids = new int[]{R.id.n0, R.id.n1 , R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7};
        Network database = Network.getInstance();
        playerListner = new ValueEventListener() {
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
        };
        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("players").addValueEventListener(playerListner);
        final InLobby thisLobby = this;
        startListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    int value = dataSnapshot.getValue(Integer.class);
                    if(value == 1){
                        if (android.os.Build.VERSION.SDK_INT < 23){
                            Toast.makeText(thisLobby, "Your operating system is not compatible with our proprietary BLASTING technology", Toast.LENGTH_SHORT).show();
                        } else if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(thisLobby, "The app needs certain permissions to run. To give permissions, go to settings>apps>laser tag>permissions.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(thisLobby, Screen2.class);
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("state").addValueEventListener(startListner);

        final InLobby _this = this;
        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("toDelete").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    if(dataSnapshot.getValue(boolean.class)){
                        _this.goToMainTrue();
                        Network database = Network.getInstance();
                        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("state").removeEventListener(startListner);
                        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("players").removeEventListener(playerListner);
                        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("toDelete").removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void goToMain(View view){
        /*
        Network database = Network.getInstance();
        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("players").child(Player.getLocalPlayer().getName()).removeValue();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        */
        goToMainTrue();
    }

    public void goToMainTrue(){
        Network database = Network.getInstance();
        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("players").child(Player.getLocalPlayer().getName()).removeValue();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void goToScreen(View view){
        Toast.makeText(this, "You aren't hosting this lobby, only the host can start the game", Toast.LENGTH_SHORT).show();
    }

    public void managePlayer(View view){

    }
}
