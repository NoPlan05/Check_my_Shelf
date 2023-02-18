package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    static {
        if(OpenCVLoader.initDebug()){
            Log.d("MainActivity: ","Opencv is loaded");
        }
        else {
            Log.d("MainActivity: ","Opencv failed to load");
        }
    }

    private ImageView camera_button;
    private ImageView storage_Image_button;
    private TextView number;
    private Button button_plus;
    private Button button_minus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // to go into camera
        camera_button=findViewById(R.id.camera_button);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        // to go in to storage
        storage_Image_button=findViewById(R.id.storage_Image_button);
        storage_Image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,storageRecognitionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        button_plus=findViewById(R.id.button_plus);
        number = findViewById(R.id.number);
        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;
                // Display the new value in the text view.
                number.setText(count.toString());
            }
        });
        button_minus=findViewById(R.id.button_minus);
        number = findViewById(R.id.number);
        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    String countString = number.getText().toString();
                    // Convert value to a number and increment it
                    Integer count = Integer.parseInt(countString);
                if (count != 0) {
                    count--;
                    // Display the new value in the text view.
                    number.setText(count.toString());
                }
                else {
                    Toast.makeText(MainActivity.this, "Sie haben keine Produkte mehr, möchten sie sie vielleicht auf ihre Einkaufliste setzen?", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}