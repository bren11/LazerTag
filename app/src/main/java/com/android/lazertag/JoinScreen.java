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
    ArrayList<String> lobbyNames;
    int lobbySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Network database = Network.getInstance();
        database.getLobbies().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> lobbyNames = new ArrayList<String>();
                final int[] ids = new int[]{R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7};
                for (DataSnapshot lobbyName: dataSnapshot.getChildren()){
                    String name = lobbyName.getValue(String.class);
                    lobbyNames.add(name);

                }
                Network database = Network.getInstance();
                for(int i = 0; i < lobbyNames.size() && i < 8; i++){
                    final Button button = (Button) findViewById(ids[i]);
                    DatabaseReference lobby = database.getLobby(lobbyNames.get(i));
                    final String name = lobbyNames.get(i);
                    lobby.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Player> players = dataSnapshot.getValue(ArrayList.class);
                            String textToDisplay = "" + name + " (" + players.size() + ")";
                            button.setText(textToDisplay);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setContentView(R.layout.activity_join_screen);
        Button test = (Button)findViewById(R.id.button);
        test.setBackgroundColor(Color.BLUE);
        /*final String[] games = new String[]{"", "", "", "", "", "", "", ""};
        final int[] ids = new int[]{R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7};
        final int[] nums = new int[]{1,1,1,1,1,1,1,1};

        final Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                for (int i = 0; i < games.length; i++){
                    Button button;
                    button = (Button) findViewById(ids[i]);
                    button.setText("" + games[i] + " (" + nums[i] + ")");
                }
                handler.postDelayed(this,500); // set time here to refresh textView
            }
        });*/
    }
    public void goToMain(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

}
