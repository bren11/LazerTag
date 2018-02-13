package com.android.lazertag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    GeneralPreferences genPref = GeneralPreferences.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EditText nameSpace;
        nameSpace = (EditText)findViewById(R.id.editText);
        final SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        nameSpace.setText(prefs.getString("Name", "Player"));
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.crosshairarray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.targetArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner.setSelection(genPref.getCrosshair());
        spinner2.setSelection(genPref.getTarget());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genPref.setCrosshair(position);
                setImageView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genPref.setTarget(position);
                setImageView2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void goToMain(View view){
        EditText nameSpace;
        nameSpace = (EditText)findViewById(R.id.editText);
        SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Name", nameSpace.getText().toString());
        editor.apply();
        Player self = Player.getLocalPlayer();
        self.setName(nameSpace.getText().toString());
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void setImageView() {
        ImageView Preview = (ImageView) findViewById(R.id.hairPreview);
        int[] crosshairs = genPref.getCrosshairs();
        Preview.setImageResource(crosshairs[genPref.getCrosshair()]);
    }

    public void setImageView2() {
        ImageView Preview = (ImageView) findViewById(R.id.imageView);
        int[] targets = genPref.getTargets();
        Preview.setImageResource(targets[genPref.getTarget()]);
    }

    public void left1(View view) {
        if (genPref.getCrosshair() != 0) {
            genPref.setCrosshair(genPref.getCrosshair() - 1);
        } else {
           genPref.setCrosshair(genPref.getCrosshairs().length - 1);
        }
        setImageView();
    }

    public void right1(View view) {
        if (genPref.getCrosshair() != genPref.getCrosshairs().length - 1) {
           genPref.setCrosshair(genPref.getCrosshair() + 1);
        } else {
            genPref.setCrosshair(0);
        }
        setImageView();
    }

    public void left2(View view) {
        if (genPref.getTarget() != 0) {
            genPref.setTarget(genPref.getTarget() - 1);
        } else {
            genPref.setTarget(genPref.getTargets().length - 1);
        }
        setImageView2();
    }

    public void right2(View view) {
        if (genPref.getTarget() != genPref.getTargets().length - 1) {
            genPref.setTarget(genPref.getTarget() + 1);
        } else {
            genPref.setTarget(0);
        }
        setImageView2();
    }
}
