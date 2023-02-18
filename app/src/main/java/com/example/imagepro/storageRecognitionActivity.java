package com.example.imagepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class storageRecognitionActivity extends AppCompatActivity {

    private Button select_image;
    private ImageView image_view;
    private ImageView scan_button;
    private TextView text_view;
    int Selected_Picture=200;

    //TextRecognizer
    private TextRecognizer textRecognizer;

    private String show_image_or_text="image";
    Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_recognition);

        image_view=findViewById(R.id.image_view);
        //selectImage function
        select_image=findViewById(R.id.select_image);
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when button gets pressed this function runes
                image_chooser();
            }
        });
        // This will load model
        textRecognizer= TextRecognition.getClient(new TextRecognizerOptions.Builder().build());
        // adding textview
        text_view=findViewById(R.id.text_view);
        text_view.setVisibility(View.GONE);

        //when scan_button is clicked and show_image_or_text is "image" show text
        scan_button=findViewById(R.id.scan_button);
        //when show_image_or_text=="image" show textview and remove imageView
        //when show_image_or_text=="text" remove textview and show imageView
        scan_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    //change color to white
                    scan_button.setColorFilter(Color.WHITE);
                    return true;
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    //change color to black
                    scan_button.setColorFilter(Color.BLACK);
                    if (show_image_or_text=="text"){
                        text_view.setVisibility(View.GONE);
                        image_view.setVisibility(View.VISIBLE);
                        show_image_or_text="image";
                    }
                    else {
                        text_view.setVisibility(View.VISIBLE);
                        image_view.setVisibility(View.GONE);
                        show_image_or_text="text";

                    }
                    return true;
                }
                return false;
            }
        });

    }

    void image_chooser() {
        //creating a new intent to navigate to gallery
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // This will open image chooser
        //selected image will return path url
        startActivityForResult(Intent.createChooser(i, "Selected Picture"), Selected_Picture);
    }

    //This function will listen to when image is selected it will run

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==Selected_Picture){
                //this means picture is successfully selected

                Uri selectedImageUri=data.getData();
                if (selectedImageUri != null);{
                    Log.d("storage_Activity", "Output Uri"+selectedImageUri);

                    //convert uri to Bitmap
                    try {
                        bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    //convert this image to InputImage which is supported my ML Kit script libary
                    InputImage image=InputImage.fromBitmap(bitmap, 0);
                    //pass this image to textRecognizer
                    Task<Text> result=textRecognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            // text contain all recgnize text
                            text_view.setText(text.getText());
                            Log.d("Storage_activity", "Out:"+text.getText());

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        }
    }
}