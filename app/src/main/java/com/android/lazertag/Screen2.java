package com.android.lazertag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.android.lazertag.Player.getLocalPlayer;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Screen2 extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    static{ System.loadLibrary("opencv_java3"); }
    private BFImage imageRec = new BFImage(FeatureDetector.ORB, DescriptorExtractor.ORB, DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
    private Network network;


    CameraControllerV2WithPreview ccv2WithPreview;

    AutoFitTextureView textureView;

    public File mCurrentPhotoPath = null;
    boolean newPic = false;

    GeneralPreferences genPref = GeneralPreferences.getInstance();

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

        SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);

        textureView = (AutoFitTextureView)findViewById(R.id.textureview);
        textureView.setAspectRatio(9,16);

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
                    compareImage(match.name());
                    newPic = false;
                }
            }
        }
        handler.post(new MyRunnable(handler, imageRec, mCurrentPhotoPath));

        network = Network.getInstance();
        String key = Player.getLocalPlayer().getCurrentLobby();
        network.getLobby(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                network.currentLobby = dataSnapshot;

                ArrayList<Hit> value = dataSnapshot.child("hitReg").getValue(new GenericTypeIndicator<ArrayList<Hit>>(){});
                if (value != null) {
                    Hit currentHit = value.get(value.size() - 1);
                    if (currentHit.getReceiver().equals(getLocalPlayer())) {
                        showToast("You got Blasted by " + currentHit.getSender().getName() + "!");
                    } else if (currentHit.getSender().equals(getLocalPlayer())) {
                        showToast("You Blasted " + currentHit.getReceiver().getName() + "!");
                    } else {
                        showToast(currentHit.getSender().getName() + " Blasted " + currentHit.getReceiver().getName() + "!");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("ScreenError", "Failed to read value.", error.toException());
            }
        });

        final MediaPlayer blastNoise = MediaPlayer.create(this, R.raw.blastnoise);

        findViewById(R.id.getpicture).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(ccv2WithPreview != null) {
                    ccv2WithPreview.takePicture(createImageFile());
                    newPic = true;
                    //Toast.makeText(getApplicationContext(), mCurrentPhotoPath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    handler.post(new MyRunnable(handler, imageRec, mCurrentPhotoPath));
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    blastNoise.start();
                }
                //network.getTarget().setValue(match.name());
                //Toast.makeText(getApplicationContext(), "Picture Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.getpicture).setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/pentacle.jpg", 1);
        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/tryangle.jpg", 1);
        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/zelda.jpg", 1);

        final ImageView crossHair = (ImageView) findViewById(R.id.CrosshairView);
        int[] crossHairs = genPref.getCrosshairs();
        int crossHairPosition = prefs.getInt("Crosshair", 0);
        crossHair.setImageResource(crossHairs[crossHairPosition]);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        final float[] timestamp = new float[1];
        boolean isSpinner;
        if (prefs.getInt("Crosshair", 0) == 4) {
            isSpinner = true;
        } else {
            isSpinner = false;
        }
        if (isSpinner == true) {
            SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    if ((Math.abs(sensorEvent.values[2]) > 0.5f) && (timestamp[0] != 0)) {
                        float rot = (float) (crossHair.getRotation() + ((Math.toDegrees(sensorEvent.values[2]) * ((sensorEvent.timestamp - timestamp[0]) / 100000000f))));
                        crossHair.setRotation(rot);
                    }
                    timestamp[0] = sensorEvent.timestamp;
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };


            sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

    public void goback(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void compareImage(final String image){
        final DatabaseReference lobby = network.getLobby(Player.getLocalPlayer().getCurrentLobby());
        lobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot x : dataSnapshot.child("players").getChildren()){
                    Player player = (Player)x.getValue();
                    if(player.getImage().equals(image)){
                        ArrayList<Hit> value = dataSnapshot.child("hitReg").getValue(new GenericTypeIndicator<ArrayList<Hit>>(){});
                        value.add(new Hit(player,Player.getLocalPlayer()));
                        lobby.child("hitreg").setValue(value);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast("Image Comparison Error");
            }
        });
    }
}