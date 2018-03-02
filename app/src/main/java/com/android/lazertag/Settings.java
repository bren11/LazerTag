package com.android.lazertag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Settings extends AppCompatActivity {

    GeneralPreferences genPref = GeneralPreferences.getInstance();
    int crosshair;
    int target;
    int[] targets;
    int[] crosshairs;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EditText nameSpace;
        nameSpace = (EditText)findViewById(R.id.editText);
        final SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        nameSpace.setText(prefs.getString("Name", "Player"));
        final String blocked = "/h.#$[]";
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && blocked.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        nameSpace.setFilters(new InputFilter[] { filter });
        crosshair = prefs.getInt("Crosshair", 0);
        target = prefs.getInt("Target", 0);
        targets = genPref.getTargets();
        crosshairs = genPref.getCrosshairs();
        setImageView();
        setImageView2();
    }

    public void goToMain(View view){
        EditText nameSpace = (EditText)findViewById(R.id.editText);
        SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Name", nameSpace.getText().toString());
        editor.putInt("Crosshair", crosshair);
        editor.putInt("Target", target);
        editor.apply();
        Player self = Player.getLocalPlayer();
        self.setName(nameSpace.getText().toString());
        //self.setImage(target);
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
        Preview.setImageResource(crosshairs[crosshair]);
    }

    public void setImageView2() {
        ImageView Preview = (ImageView) findViewById(R.id.targPreview);
        Preview.setImageResource(targets[target]);
    }

    public void left1(View view) {
        if (crosshair != 0) {
            crosshair -= 1;
        } else {
            crosshair = crosshairs.length - 1;
        }
        setImageView();
    }

    public void right1(View view) {
        if (crosshair != crosshairs.length - 1) {
           crosshair += 1;
        } else {
            crosshair = 0;
        }
        setImageView();
    }

    public void left2(View view) {
        if (target != 0) {
            target -= 1;
        } else {
            target = targets.length - 1;
        }
        setImageView2();
    }

    public void right2(View view) {
        if (target != targets.length - 1) {
            target += 1;
        } else {
            target = 0;
        }
        setImageView2();
    }
}
