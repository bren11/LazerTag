package com.android.lazertag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EditText nameSpace;
        nameSpace = (EditText)findViewById(R.id.editText);
        SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        nameSpace.setText(prefs.getString("Name", "Player"));

    }

    public void goToMain(View view){
        EditText nameSpace;
        nameSpace = (EditText)findViewById(R.id.editText);
        SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Name", nameSpace.getText().toString());
        editor.apply();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
