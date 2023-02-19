package com.example.imagepro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.google.mlkit.nl.languageid.IdentifiedLanguage;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
//import com.google.mlkit.samples.nl.languageid.R;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
///////////
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;
import java.util.Collections;
//////////

public class CameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2{
    private static final String TAG="MainActivity";

    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private ImageView scan_button;
    private ImageView take_picture_button;
    private ImageView show_image_button;
    private ImageView current_image;
    private TextView textview;
    private TextRecognizer textRecognizer;
    private String Camera_or_recognizeText="camera";
    // defining bitmap to store current image in bitmap format
    private Bitmap bitmap=null;

    private String resultString;

    private String filteroutput;
    List<List<String>> topLevelList = new ArrayList<>();
    //List<String> subList= new ArrayList<>();

    //List<RecognizedLanguage> languages = new ArrayList<>();



    private BaseLoaderCallback mLoaderCallback =new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:{
                    Log.i(TAG,"OpenCv Is loaded");
                    mOpenCvCameraView.enableView();
                }
                default:
                {
                    super.onManagerConnected(status);

                }
                break;
            }
        }
    };

    public CameraActivity(){
        Log.i(TAG,"Instantiated new "+this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //requesting permission for storage if needet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);

        }


        int MY_PERMISSIONS_REQUEST_CAMERA = 0;
        // if camera permission is not given it will ask for it on device
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        setContentView(R.layout.activity_camera);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.frame_Surface);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        //load text recognition model
        textRecognizer = TextRecognition.getClient(new TextRecognizerOptions.Builder().build());

        textview = findViewById(R.id.textview);
        //setting textview hidden
        textview.setVisibility(View.GONE);

        //capture image function
        take_picture_button = findViewById(R.id.take_picture_button);
        // setOnTouchListener instead setOnClickListener to change color on click
        // if take_picture_button is pressed do nothing
        // when button released change color of take_picture_button

        take_picture_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                }
                //if using camera and click take_picture_button
                //1. change color to white
                // convert current mRGBA frame to bitmap and store
                //rotate frame to get portrait mode image
                // When take_picture_button is clicked and we have saved bitmap stop camera view
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Camera_or_recognizeText == "camera") {
                        take_picture_button.setColorFilter(Color.WHITE);
                        Mat a = mRgba.t();
                        Core.flip(a, mRgba, 1);
                        a.release();
                        bitmap = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(mRgba, bitmap);
                        mOpenCvCameraView.disableView();

                        Camera_or_recognizeText = "recognizeText";
                    } else {
                        take_picture_button.setColorFilter(Color.BLACK);
                        textview.setVisibility(View.GONE);
                        current_image.setVisibility(View.GONE);
                        mOpenCvCameraView.enableView();
                        textview.setText("");
                        Camera_or_recognizeText = "camera";
                    }
                    return true;
                }
                return false;
            }
        });
        //now when scan button is pressed
        // when released change color to black for click effect
        // if Camera_or_recognizeText is recognizeText then recognize Text from bitmap we stored
        // Before that we have to convert bitmap to InputImage used by ML kit
        // onSuccess image recognition add text to textview
        // When translate_button ist clicked add textview visible
        scan_button = findViewById(R.id.scan_button);
        scan_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    scan_button.setColorFilter(Color.WHITE);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scan_button.setColorFilter(Color.BLACK);
                    if (Camera_or_recognizeText == "recognizeText") {
                        textview.setVisibility(View.VISIBLE);

                        InputImage image = InputImage.fromBitmap(bitmap, 0);

                        // recognize text
                        Task<Text> result = textRecognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                                    @Override
                                    public void onSuccess(Text text) {
                                        saveTextAsCSV("Lagerbestand", text);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    } else {
                        Toast.makeText(CameraActivity.this, "Bitte machen sie zu erst ein Foto", Toast.LENGTH_LONG).show();
                        ;
                    }
                    return true;
                }
                return false;
            }
        });
        //to change back to the image
        // same on press change for colors as in scan_button
        // first we check if Camera_or_recognizeText is recognizeText
        // if it is camera pressing this Button will do nothing because photo was not taken
        // if this button is clicked make textview invisible and add capture image to current_image
        show_image_button = findViewById(R.id.show_image_button);
        show_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CameraActivity.this,storageRecognitionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


    };
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void saveTextAsCSV(String fileName, Text text) {

        resultString = text.getText();
        resultString = resultString.toLowerCase(); //alles klein
        System.out.print(resultString); //Kontrollausgabe

        String[] topLevelElements = resultString.split("\n");  // bei Absätzen neues Element

        for (String topLevelElement : topLevelElements) {
            List subList = new ArrayList<>();
            topLevelList.add(subList);
        }

        // Convert the List object to a String object
        StringBuilder stringBuilder = new StringBuilder();
        for (List<String> subList : topLevelList) {
            for (String element : subList) {
                stringBuilder.append(element).append(" "); // append each element to the StringBuilder
            }
            stringBuilder.append("\n"); // append a newline character to separate the sublists
        }
        String finalString = stringBuilder.toString().trim(); // convert the StringBuilder to a String and remove leading/trailing whitespace

        // Set the String object to the TextView
        textview.setText(finalString);
        Log.d("CameraActivity", "Out" + finalString);
        String[] lines = finalString.split("\\r?\\n"); // split the text into lines
/*
        List<String> row = new ArrayList<>();
        for (String line : lines) {
            row = new ArrayList<>();

            for (Text.Element element : finalString.split(",")) {
                if (element.getBoundingBox().top >= boundingBox.top &&
                        element.getBoundingBox().bottom <= boundingBox.bottom &&
                        element.getBoundingBox().left >= boundingBox.left &&
                        element.getBoundingBox().right <= boundingBox.right) {
                    row.add(element.getText());
                }
            }

            line = String.join(",", row);
            lines.add(line);
        }

        String content = String.join("\n", lines);

        try {
            File file = new File(getExternalFilesDir(null), fileName + ".csv");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(finalString);
            fileWriter.close();
            Toast.makeText(this, "Text wurde gescannet", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "File saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Bitte versuche sie es Erneut", Toast.LENGTH_SHORT).show();
        }

 */
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()){
            //if load success
            Log.d(TAG,"Opencv initialization is done");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            //if not loaded
            Log.d(TAG,"Opencv is not loaded. try again");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,mLoaderCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView !=null){
            mOpenCvCameraView.disableView();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if(mOpenCvCameraView !=null){
            mOpenCvCameraView.disableView();
        }

    }

    public void onCameraViewStarted(int width ,int height){
        mRgba=new Mat(height,width, CvType.CV_8UC4);
        mGray =new Mat(height,width,CvType.CV_8UC1);
    }
    public void onCameraViewStopped(){
        mRgba.release();
    }
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba=inputFrame.rgba();
        mGray=inputFrame.gray();

        return mRgba;

    }

}