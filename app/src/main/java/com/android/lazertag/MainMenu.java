package com.android.lazertag;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    ConnectivityManager connMgr;
    NetworkInfo netInfo;
    boolean connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //netInfo = connMgr.getActiveNetworkInfo();
        //connection = netInfo.isConnected();
    }

    public void goToJoin(View view){
        Intent intent = new Intent(this, Screen.class);
        startActivity(intent);
    }
}
