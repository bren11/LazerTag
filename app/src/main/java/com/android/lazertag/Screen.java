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
import org.yaml.snakeyaml.util.UriEncoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screen extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;

    //static{ System.loadLibrary("opencv_java3"); }

    //private BFImage imageRec = new BFImage(FeatureDetector.ORB, DescriptorExtractor.ORB, DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);

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

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
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
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        //imageRec;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_screen);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.screen_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        //imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/pentacle.jpg", 1);
        //imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/tryangle.jpg", 1);
        //imageRec.addToLibrary(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/zelda.jpg", 1);
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
}
