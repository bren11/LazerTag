package com.android.lazertag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        /*Network test = Network.getInstance();
        DatabaseReference testRef = test.database.getReference("test");
        ArrayList<String> testList = new ArrayList<String>();
        testList.add("bob");
        testList.add("stop");
        testRef.setValue(testList);
        */
    }

    public void goToJoin(View view){
        Intent intent = new Intent(this, JoinScreen.class);
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
