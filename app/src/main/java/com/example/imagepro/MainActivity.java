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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static {
        if (OpenCVLoader.initDebug()) {
            Log.d("MainActivity: ", "Opencv is loaded");
        } else {
            Log.d("MainActivity: ", "Opencv failed to load");
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
        camera_button = findViewById(R.id.camera_button);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        // to go in to storage
        storage_Image_button = findViewById(R.id.storage_Image_button);
        storage_Image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, storageRecognitionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        button_plus = findViewById(R.id.button_plus);
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
        button_minus = findViewById(R.id.button_minus);
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
                } else {
                    Toast.makeText(MainActivity.this, "Sie haben keine Produkte mehr, möchten sie sie vielleicht auf ihre Einkaufliste setzen?", Toast.LENGTH_LONG).show();
                }
            }
        });
        readlagerbestand();

    }
    private List<List> wholeList = new ArrayList<>();


    private void readlagerbestand() {
        InputStream is = getResources().openRawResource(R.raw.lagerbestand);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";

        try {
            // Step over header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                // Split by ","
                String[] tokens = line.split(",");
                // Read data
                int nummer = Integer.parseInt(tokens[0]);
                String produkt = tokens[1];
                int anzahl = Integer.parseInt(tokens[2]);

                // resett Lists
                List<Object> innerList = new ArrayList<>();
                List<Integer> nummerList = new ArrayList<>();
                List<String> produktList = new ArrayList<>();
                List<Integer> anzahlList = new ArrayList<>();
                // Add elements to separate lists
                nummerList.add(nummer);
                produktList.add(produkt);
                anzahlList.add(anzahl);
                innerList.add(nummerList);
                innerList.add(produktList);
                innerList.add(anzahlList);

                // Add inner list to outer list
                wholeList.add(innerList);

                Log.d("Check my Shelf", "Just created: " + innerList);
            }
        } catch (IOException e) {
            Log.wtf("Check my Shelf", "Error reading data file on line " + line, e);
            throw new RuntimeException(e);
        }

        Log.d("Alles Ausgeben", "Alles: " + wholeList);
        Log.d("spezielles ausgeben", "test: " + wholeList.get(2).get(1));
    }


}
