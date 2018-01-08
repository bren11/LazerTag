package com.android.lazertag;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Screen2 extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    static{ System.loadLibrary("opencv_java3"); }
    private BFImage imageRec = new BFImage(FeatureDetector.ORB, DescriptorExtractor.ORB, DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
    private Network network;

    CameraControllerV2WithPreview ccv2WithPreview;

    AutoFitTextureView textureView;

    public File mCurrentPhotoPath = null;
    boolean newPic = false;

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;

    public File createImageFile(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image;
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final Intent intent = getIntent();

        textureView = (AutoFitTextureView)findViewById(R.id.textureview);

        ccv2WithPreview = new CameraControllerV2WithPreview(Screen2.this, textureView, createImageFile());

        final Handler handler = new Handler();
        class MyRunnable implements Runnable {
            private Handler handler;
            private BFImage imageRec;
            private File file;

            public MyRunnable(Handler handler, BFImage imageRec, File file) {
                this.handler = handler;
                this.imageRec = imageRec;
                this.file = file;
            }
            @Override
            public void run() {
                this.handler.postDelayed(this, 500);

                if(newPic && file.length() > 1000){
                    TrainingImage match = imageRec.detectPhoto(file.getAbsolutePath());
                    network.getTarget().setValue(match.name());
                    newPic = false;
                }
            }
        }
        handler.post(new MyRunnable(handler, imageRec, mCurrentPhotoPath));

        network = Network.getInstance();
        network.getTarget().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                showToast(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        findViewById(R.id.getpicture).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(ccv2WithPreview != null) {
                    ccv2WithPreview.takePicture(createImageFile());
                    newPic = true;
                    //Toast.makeText(getApplicationContext(), mCurrentPhotoPath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    handler.post(new MyRunnable(handler, imageRec, mCurrentPhotoPath));
                }

                    //network.getTarget().setValue(match.name());
                //Toast.makeText(getApplicationContext(), "Picture Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        getPermissions();

        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/pentacle.jpg", 1);
        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/tryangle.jpg", 1);
        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/zelda.jpg", 1);


        //Nick was here. code is bad. sry.
        ImageView crossHair = (ImageView) findViewById(R.id.CrosshairView);
        SharedPreferences crossType = this.getSharedPreferences("Hair", MODE_PRIVATE);
        String Hair = crossType.getString("Hair", "nope");
        //Toast.makeText(getBaseContext(), Hair ,Toast.LENGTH_SHORT).show();
        if (Hair.equals("GLogo")) {
            crossHair.setImageDrawable(getResources().getDrawable(R.drawable.gisforgitgud, getTheme()));
        } else if (Hair.equals("Pentacle")) {
            crossHair.setImageDrawable(getResources().getDrawable(R.drawable.pentacle, getTheme()));
        } else if (Hair.equals("Tryangle")) {
            crossHair.setImageDrawable(getResources().getDrawable(R.drawable.tryangle, getTheme()));
        } else if (Hair.equals("Zelda")) {
            crossHair.setImageDrawable(getResources().getDrawable(R.drawable.zelda, getTheme()));
        } else if (Hair.equals("nope")) {
            crossHair.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_info, getTheme()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(ccv2WithPreview != null) {
//            ccv2WithPreview.closeCamera();
//        }
//        if(ccv2WithoutPreview != null) {
//            ccv2WithoutPreview.closeCamera();
//        }
    }

    private void getPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //Requesting permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}