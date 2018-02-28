package com.android.lazertag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

public class MainMenu extends AppCompatActivity {

    RectangleFindr recrec = new RectangleFindr();

    static{ System.loadLibrary("opencv_java3"); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        String default_file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/orig2.png";
        //Log.d("filetogoto", default_file);
        Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_COLOR);
        //recrec.saveFile(src, "my thingy");
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- "
                    + default_file +"] \n");
            System.exit(-1);
        }
        recrec.FindRect(src);
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
}
