package com.android.lazertag;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Screen extends Activity implements CvCameraViewListener2,PictureCapturingListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private Network network;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;

    static{ System.loadLibrary("opencv_java3"); }

    private BFImage imageRec = new BFImage(FeatureDetector.ORB, DescriptorExtractor.ORB, DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;

    //service
    private APictureCapturingService pictureService;

        //@Override
        private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public Screen() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    public String mCurrentPhotoPath;

    @Override
    public File createImageFile(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        File photoFile = null;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }*/

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);


        /* I am probably going to take this out later

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ImageView crosshair = (ImageView) findViewById(R.id.imageview2);
        int width = displayMetrics.widthPixels;
        */

        //imageRec;

        network = Network.getInstance();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_screen);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.screen_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        checkPermissions();

        pictureService = PictureCapturingServiceImpl.getInstance(this);

        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/pentacle.jpg", 1);
        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/tryangle.jpg", 1);
        imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/zelda.jpg", 1);
        //System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());

        /*InputStream ins = getResources().openRawResource(R.raw.pentacle);
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        StringBuffer sb = new StringBuffer();
        String line;
        try {

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            System.out.print("error" + e);
        }

        File f = new File(sb.toString());
        imageRec.addToLibrary(f.getAbsolutePath(), 1);*/
        //imageRec.loadImageDescriptors(new File("R/drawable.tryangle"));
        //imageRec.loadImageDescriptors(new File("R.drawable.zelda"));

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
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


    public void TakePic(View view){
        //dispatchTakePictureIntent();
        //System.out.println(mCurrentPhotoPath);
        /*while (new File(mCurrentPhotoPath).length() < 1000000){}
        TrainingImage match = imageRec.detectPhoto(mCurrentPhotoPath);
        System.out.println(match.name());
        final Button mTextView = (Button) findViewById(R.id.screenButton);
        if (match.name().equals("zelda.jpg")){
            mTextView.setText(R.string.zelda);
        }else if (match.name().equals("pentacle.jpg")){
            mTextView.setText(R.string.pentacle);
        }else if (match.name().equals("tryangle.jpg")){
            mTextView.setText(R.string.tryangle);
        }else {
            mTextView.setText("none");
        }
        mTextView.setText(match.name());
        //this.onResume();*/

        showToast("Starting capture!");
        pictureService.startCapturing(this);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
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

    /**
     * We've finished taking pictures from all phone's cameras
     */
    @Override
    public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {
        if (picturesTaken != null && !picturesTaken.isEmpty()) {

            if (pictureService.mFile.length() < 10000) {
                System.out.println(pictureService.mFile);
                TrainingImage match = imageRec.detectPhoto(pictureService.mFile);
                System.out.println(match.name());
                network.getTarget().setValue(match.name());
            }
            return;
        }
        showToast("No camera detected!");
    }

    /**
     * Displaying the pictures taken.
     */
    @Override
    public void onCaptureDone(final String pictureUrl, final byte[] pictureData) {
        if (pictureData != null && pictureUrl != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
                    final int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                    final Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                    /*if (pictureUrl.contains("0_pic.jpg")) {
                        uploadBackPhoto.setImageBitmap(scaled);
                    } else if (pictureUrl.contains("1_pic.jpg")) {
                        uploadFrontPhoto.setImageBitmap(scaled);
                    }*/}});}
            showToast("Picture saved to " + pictureUrl);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();
                }
            }
        }
    }

    /**
     * checking  permissions at Runtime.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        final String[] requiredPermissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
        }
    }
}
