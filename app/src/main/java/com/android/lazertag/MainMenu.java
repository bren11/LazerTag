package com.android.lazertag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void goToJoin(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void goToCreate(View view){
        Intent intent = new Intent(this, CreateScreen.class);
        startActivity(intent);
    }

    public void goToSettings(View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
