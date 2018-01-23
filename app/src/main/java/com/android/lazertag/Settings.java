package com.android.lazertag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EditText nameSpace;
        nameSpace = (EditText)findViewById(R.id.editText);
        final SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        nameSpace.setText(prefs.getString("Name", "Player"));

        final SharedPreferences crossType = this.getSharedPreferences("Hair", MODE_PRIVATE);
        final SharedPreferences.Editor crossEditor = crossType.edit();
        final SharedPreferences Target = this.getSharedPreferences("Target", MODE_PRIVATE);
        final SharedPreferences.Editor targetEdit = Target.edit();

        //Spinner Stuff
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.crosshairarray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                crossEditor.putString("Hair", parent.getItemAtPosition(position) + "");
                crossEditor.apply();
                setImageView(crossType.getString("Hair", "nope"));
                //Toast.makeText(getBaseContext(), crossType.getString("Hair", "nope") ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.targetArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetEdit.putString("Target", parent.getItemAtPosition(position) + "");
                targetEdit.apply();
                setImageView2(Target.getString("Target", "nope"));
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
        Player self = Player.getOwnPlayer();
        self.changeName(nameSpace.getText().toString());
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void setImageView(String Hair) {
        ImageView Preview = (ImageView) findViewById(R.id.hairPreview);
        if (Hair.equals("GLogo")) {
            Preview.setImageDrawable(getResources().getDrawable(R.drawable.gisforgitgud, getTheme()));
        } else if (Hair.equals("Pentacle")) {
            Preview.setImageDrawable(getResources().getDrawable(R.drawable.pentacle, getTheme()));
        } else if (Hair.equals("Tryangle")) {
            Preview.setImageDrawable(getResources().getDrawable(R.drawable.tryangle, getTheme()));
        } else if (Hair.equals("Zelda")) {
            Preview.setImageDrawable(getResources().getDrawable(R.drawable.zelda, getTheme()));
        } else {
            Preview.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_info, getTheme()));
        }
    }

    public void setImageView2(String Target) {
        ImageView Preview = (ImageView) findViewById(R.id.imageView);
        if (Target.equals("Pentacle")) {
            Preview.setImageDrawable(getResources().getDrawable(R.drawable.pentacle, getTheme()));
        } else if (Target.equals("Tryangle")) {
            Preview.setImageDrawable(getResources().getDrawable(R.drawable.tryangle, getTheme()));
        } else if (Target.equals("Zelda")) {
            Preview.setImageDrawable(getResources().getDrawable(R.drawable.zelda, getTheme()));
        }
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
}
