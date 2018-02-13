package com.android.lazertag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InLobby extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_screen);
        ( findViewById(R.id.button2)).setVisibility(View.GONE);
        final int[] ids = new int[]{R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7};
        Network database = Network.getInstance();
        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).addValueEventListener(new ValueEventListener() {
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
        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("Players").child(Player.getLocalPlayer().getName()).removeValue();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void goToScreen(View view){

    }
}
