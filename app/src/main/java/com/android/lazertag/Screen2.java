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
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.opencv.core.Mat;
import org.opencv.dnn.Net;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.android.lazertag.Player.getLocalPlayer;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Screen2 extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    static{ System.loadLibrary("opencv_java3"); }
    private BFImage imageRec = new BFImage(FeatureDetector.ORB, DescriptorExtractor.ORB, DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
    private RectangleFindr recrec = new RectangleFindr();
    private Network network;

    boolean isPaused = false;

    CameraControllerV2WithPreview ccv2WithPreview;

    AutoFitTextureView textureView;

    public File mCurrentPhoto = null;
    boolean newPic = false;
    ChildEventListener hitlogListner;
    ValueEventListener pauseListner;

    GeneralPreferences genPref = GeneralPreferences.getInstance();
    final Screen2 thisThing = this;
    public File createImageFile(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhoto = image;
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Player player = Player.getLocalPlayer();
        SharedPreferences prefs = this.getSharedPreferences("nameData", MODE_PRIVATE);
        Button leaveAndEnd = (Button) findViewById(R.id.leaveAndEnd);
        if (prefs.getString("Name", "Player").equals(player.getCurrentLobby())){
            leaveAndEnd.setText("End Game");
        }

        textureView = (AutoFitTextureView)findViewById(R.id.textureview);
        textureView.setAspectRatio(9,16);

        ccv2WithPreview = new CameraControllerV2WithPreview(Screen2.this, textureView);

        final Handler handler = new Handler();
        class MyRunnable implements Runnable {
            private Handler handler;
            private BFImage imageRec;

            public MyRunnable(Handler handler, BFImage imageRec) {
                this.handler = handler;
                this.imageRec = imageRec;
            }
            @Override
            public void run() {
                this.handler.postDelayed(this, 500);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/test/img num: 0.png");


                //System.out.println(file.length());
                if(newPic && file.length() > 1000){
                    //String default_file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/orig2.png";
                    //Log.d("filetogoto", default_file);
                    /*Mat src = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
                    //recrec.saveFile(src, "my thingy");
                    if( src.empty() ) {
                        System.out.println("Error opening image!");
                        System.out.println("Program Arguments: [image_name -- "
                                + file.getAbsolutePath() +"] \n");
                        System.exit(-1);
                    }
                    recrec.FindRect(src);*/
                    TrainingImage match = imageRec.detectPhoto(file.getAbsolutePath());
                    try {
                        PrintWriter writer = new PrintWriter(file);
                        writer.print("");
                        writer.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    if (match != null) {
                        compareImage(match.name());
                    }
                    newPic = false;
                }
                Button getPicture = (Button) findViewById(R.id.getpicture);
                getPicture.setVisibility(View.VISIBLE);
            }
        }
        handler.post(new MyRunnable(handler, imageRec));


        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                        while(true) {
                    sleep(1000);
                            if(Player.getLocalPlayer().getTimeDisabled() > 0){

                                Player.getLocalPlayer().setTimeDisabled(Player.getLocalPlayer().getTimeDisabled() - 0.5);
                                System.out.println(Player.getLocalPlayer().getTimeDisabled() + "Seconds");
                                if(Player.getLocalPlayer().getTimeDisabled() < 0)
                                    Player.getLocalPlayer().setTimeDisabled(0.0);
                            }
                }
                     }
            catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        network = Network.getInstance();
        String key = Player.getLocalPlayer().getCurrentLobby();
        hitlogListner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                network.currentLobby = dataSnapshot;

                if (dataSnapshot != null) {
                    Hit currentHit = dataSnapshot.getValue(Hit.class);
                    if (currentHit.getReceiver().getName().equals(getLocalPlayer().getName())) {
                        showToast("You got Blasted by " + currentHit.getSender().getName() + "!");
                        if(Player.getLocalPlayer().getTimeDisabled() == 0)
                            Player.getLocalPlayer().setTimeDisabled(3.0);
                    } else if (currentHit.getSender().getName().equals(getLocalPlayer().getName())) {
                        showToast("You Blasted " + currentHit.getReceiver().getName() + "!");
                    } else {
                        showToast(currentHit.getSender().getName() + " Blasted " + currentHit.getReceiver().getName() + "!");
                    }
                }
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot){

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("ScreenError", "Failed to read value.", error.toException());
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName){

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName){

            }
        };
        network.getLobby(key).child("hitreg").addChildEventListener(hitlogListner);

        final MediaPlayer blastNoise = MediaPlayer.create(this, R.raw.blastnoise);

        findViewById(R.id.getpicture).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Button getPicture = (Button) findViewById(R.id.getpicture);
                getPicture.setVisibility(View.GONE);
                if(ccv2WithPreview != null && Player.getLocalPlayer().getTimeDisabled() == 0) {
                    //thisThing.compareImage("tryangle.jpg");
                    //createImageFile();
                    ccv2WithPreview.takePicture();
                    newPic = true;

                    //Toast.makeText(getApplicationContext(), mCurrentPhoto.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    //blastNoise.start();
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

        //getPermissions();

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
        final int[] ids = new int[]{R.id.n0, R.id.n1 , R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n7};
        Network database = Network.getInstance();
        pauseListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                ArrayList<String> names = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Player player = data.getValue(Player.class);
                    names.add(player.getName());
                    i++;
                }
                for(int j = 0; j < i && j < 8; j++){
                    TextView button = (TextView) findViewById(ids[j]);
                    button.setText(names.get(j));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("players").addValueEventListener(pauseListner);

        database.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("toDelete").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getValue(boolean.class)) {
                        network.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("players").removeEventListener(pauseListner);
                        network.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("hitreg").removeEventListener(hitlogListner);
                        network.getLobby(Player.getLocalPlayer().getCurrentLobby()).child("toDelete").removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        Network database = Network.getInstance();
        Player player = Player.getLocalPlayer();
        if (player.getCurrentLobby().equals(player.getName())) {
            database.getLobby(player.getCurrentLobby()).child("toDelete").setValue(true);
            database.getLobby(player.getName()).removeValue();
            database.getLobbies().child(player.getName()).removeValue();
        } else {
            database.getLobby(player.getCurrentLobby()).child("players").child(GeneralPreferences.getInstance().getCurrentKey()).removeValue();
            GeneralPreferences.getInstance().setCurrentKey("");
        }
    }
//        if(ccv2WithPreview != null) {
//            ccv2WithPreview.closeCamera();
//        }
//        if(ccv2WithoutPreview != null) {
//            ccv2WithoutPreview.closeCamera();b
//

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
        Network database = Network.getInstance();
        Player player = Player.getLocalPlayer();
        if(player.getCurrentLobby().equals(player.getName())) {
            database.getLobby(player.getCurrentLobby()).child("toDelete").setValue(true);
            database.getLobby(player.getName()).removeValue();
            database.getLobbies().child(player.getName()).removeValue();
        }
        else{
            database.getLobby(player.getCurrentLobby()).child("players").child(GeneralPreferences.getInstance().getCurrentKey()).removeValue();
            GeneralPreferences.getInstance().setCurrentKey("");
        }
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }


    public void compareImage(final String image) {
        final DatabaseReference lobby = network.getLobby(Player.getLocalPlayer().getCurrentLobby());
        lobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.child("players").getChildren()) {
                    Player player = x.getValue(Player.class);
                    if (player.getImage().equals(image)) {
                        DatabaseReference ref = lobby.child("hitreg").push();
                        ref.setValue(new Hit(player, Player.getLocalPlayer()));
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

    public void onPause(View view) {
        //ImageButton pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        ImageView crossHairView = (ImageView) findViewById(R.id.CrosshairView);
        Button leaveAndEnd = (Button) findViewById(R.id.leaveAndEnd);
        TextView pauseView = (TextView) findViewById(R.id.pauseView);
        TextView n0 = (TextView) findViewById(R.id.n0);
        TextView n1 = (TextView) findViewById(R.id.n1);
        TextView n2 = (TextView) findViewById(R.id.n1);
        TextView n3 = (TextView) findViewById(R.id.n2);
        TextView n4 = (TextView) findViewById(R.id.n3);
        TextView n5 = (TextView) findViewById(R.id.n4);
        TextView n6 = (TextView) findViewById(R.id.n5);
        TextView n7 = (TextView) findViewById(R.id.n7);
        Button getPicture = (Button) findViewById(R.id.getpicture);

        if (isPaused == false) {
            crossHairView.setVisibility(View.INVISIBLE);
            leaveAndEnd.setVisibility(View.VISIBLE);
            pauseView.setVisibility(View.VISIBLE);
            //getPicture.setClickable(false);
            getPicture.setVisibility(View.GONE);
            n0.setVisibility(View.VISIBLE);
            n1.setVisibility(View.VISIBLE);
            n2.setVisibility(View.VISIBLE);
            n3.setVisibility(View.VISIBLE);
            n4.setVisibility(View.VISIBLE);
            n5.setVisibility(View.VISIBLE);
            n6.setVisibility(View.VISIBLE);
            n7.setVisibility(View.VISIBLE);
            isPaused = true;
        } else {
            crossHairView.setVisibility(View.VISIBLE);
            leaveAndEnd.setVisibility(View.INVISIBLE);
            pauseView.setVisibility(View.INVISIBLE);
            //getPicture.setClickable(true);
            getPicture.setVisibility(View.VISIBLE);
            n0.setVisibility(View.INVISIBLE);
            n1.setVisibility(View.INVISIBLE);
            n2.setVisibility(View.INVISIBLE);
            n3.setVisibility(View.INVISIBLE);
            n4.setVisibility(View.INVISIBLE);
            n5.setVisibility(View.INVISIBLE);
            n6.setVisibility(View.INVISIBLE);
            n7.setVisibility(View.INVISIBLE);
            isPaused = false;
        }
    }
}