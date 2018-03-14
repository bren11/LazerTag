package com.android.lazertag;

import android.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.opencv.core.Mat;
import com.google.firebase.database.DatabaseReference;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    RectangleFindr recrec = new RectangleFindr();

    static{ System.loadLibrary("opencv_java3"); }
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getPermissions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        /*String default_file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/orig2.png";
        //Log.d("filetogoto", default_file);
        Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_COLOR);
        //recrec.saveFile(src, "my thingy");
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- "
                    + default_file +"] \n");
            System.exit(-1);
        }
        recrec.FindRect(src);*/
        final SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        Player.getLocalPlayer().setName(prefs.getString("Name", "Player"));
        /*Network test = Network.getInstance();
        DatabaseReference testRef = test.database.getReference("test");
        ArrayList<String> testList = new ArrayList<String>();
        testList.add("bob");
        testList.add("stop");
        testRef.setValue(testList);
        */

        //final SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        //Player.getLocalPlayer().setName(prefs.getString("Name", "Player"));
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

    private void getPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            if(checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //Requesting permission.
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override //Override from ActivityCompat.OnRequestPermissionsResultCallback Interface
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                }
                return;
            }
        }
    }
}
