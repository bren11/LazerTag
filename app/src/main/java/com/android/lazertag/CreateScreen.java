package com.android.lazertag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class CreateScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_screen);
        final String[] players = new String[]{"bob", "", "", "", "", "", "", ""};
        final int[] ids = new int[]{R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7};
        final Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                for (int i = 0; i < players.length; i++){
                    Button button;
                    button = (Button) findViewById(ids[i]);
                    button.setText(players[i]);
                }
                handler.postDelayed(this,500); // set time here to refresh textView
            }
        });
    }
    public void goToMain(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
