package com.example.imagepro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.opencv.android.OpenCVLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private Button button_plus_1;
    private Button button_minus_1;
    private Button button_plus_2;
    private Button button_minus_2;
    private Button button_plus_3;
    private Button button_minus_3;
    private ImageView button_delete_1;
    private ImageView button_delete_2;
    private ImageView button_delete_3;
    private ImageView button_update;

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
        button_plus_1 = findViewById(R.id.button_plus_1);
        number_1 = findViewById(R.id.number_1);
        button_plus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_1.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;
                for(int plusonecounter = 0; plusonecounter < wholeList.size(); plusonecounter++){
                    if (wholeList.get(plusonecounter).get(1).equals(countString)) {
                        wholeList.get(plusonecounter).set(1, count);
                        writeCsv(MainActivity.this);
                        readlagerbestand();
                        sortlagerbestand();
                        //Log.d("Klick", " erfolkreich geaendert?");
                        break;
                    }
                }
                // Display the new value in the text view.
                number_1.setText(String.valueOf(count));
                //save the cange
                //wholeList.get(0).set(1, count);
                writeCsv(MainActivity.this);
            }
        });
        button_minus_1 = findViewById(R.id.button_minus_1);
        number_1 = findViewById(R.id.number_1);
        button_minus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_1.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for(int minusonecounter = 0; minusonecounter < wholeList.size(); minusonecounter++){
                        if (wholeList.get(minusonecounter).get(1).equals(countString)) {
                            wholeList.get(minusonecounter).set(1, count);
                            writeCsv(MainActivity.this);
                            readlagerbestand();
                            sortlagerbestand();
                            //Log.d("Klick", " erfolkreich geaendert?");
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_1.setText(String.valueOf(count));
                    //save the cange
                    //wholeList.get(0).set(1, count);
                    writeCsv(MainActivity.this);

                    } else {
                        Toast.makeText(MainActivity.this, "Sie haben keine Produkte mehr, möchten sie sie vielleicht auf ihre Einkaufliste setzen?", Toast.LENGTH_LONG).show();
                    }
                }

        });
        button_plus_2 = findViewById(R.id.button_plus_2);
        number_2 = findViewById(R.id.number_2);
        button_plus_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_2.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;
                for(int plustwocounter = 0; plustwocounter < wholeList.size(); plustwocounter++){
                    if (wholeList.get(plustwocounter).get(1).equals(countString)) {
                        wholeList.get(plustwocounter).set(1, count);
                        writeCsv(MainActivity.this);
                        readlagerbestand();
                        sortlagerbestand();
                        //Log.d("Klick", " erfolkreich geaendert?");
                        break;
                    }
                }
                // Display the new value in the text view.
                number_2.setText(String.valueOf(count));
                //save the cange
                //wholeList.get(1).set(1, count);
                writeCsv(MainActivity.this);
            }
        });
        button_minus_2 = findViewById(R.id.button_minus_2);
        number_2 = findViewById(R.id.number_2);
        button_minus_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_2.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for(int minustwocounter = 0; minustwocounter < wholeList.size(); minustwocounter++){
                        if (wholeList.get(minustwocounter).get(1).equals(countString)) {
                            wholeList.get(minustwocounter).set(1, count);
                            writeCsv(MainActivity.this);
                            readlagerbestand();
                            sortlagerbestand();
                            //Log.d("Klick", " erfolkreich geaendert?");
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_2.setText(String.valueOf(count));
                    //save the cange
                    //wholeList.get(1).set(1, count);
                    writeCsv(MainActivity.this);

                } else {
                    Toast.makeText(MainActivity.this, "Sie haben keine Produkte mehr, möchten sie sie vielleicht auf ihre Einkaufliste setzen?", Toast.LENGTH_LONG).show();
                }
            }

        });
        button_plus_3 = findViewById(R.id.button_plus_3);
        number_3 = findViewById(R.id.number_3);
        button_plus_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_3.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;
                for(int plusthreecounter = 0; plusthreecounter < wholeList.size(); plusthreecounter++){
                    Log.d("Klick", " for schleife gestartet");
                    if (wholeList.get(plusthreecounter).get(1).equals(countString)) {
                        wholeList.get(plusthreecounter).set(1, count);
                        writeCsv(MainActivity.this);
                        readlagerbestand();
                        sortlagerbestand();
                        Log.d("Klick", " erfolkreich geaendert?");
                        break;
                    }
                }
                // Display the new value in the text view.
                number_3.setText(String.valueOf(count));
                //save the cange
                //wholeList.get(2).set(1, count);
                writeCsv(MainActivity.this);
            }
        });
        button_minus_3 = findViewById(R.id.button_minus_3);
        number_3 = findViewById(R.id.number_3);
        button_minus_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_3.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for(int minusthreecounter = 0; minusthreecounter < wholeList.size(); minusthreecounter++){
                        Log.d("Klick", " for schleife gestartet");
                        if (wholeList.get(minusthreecounter).get(1).equals(count)) {
                            wholeList.get(minusthreecounter).set(1, count);
                            readlagerbestand();
                            sortlagerbestand();
                            writeCsv(MainActivity.this);
                            Log.d("Klick", " erfolkreich geaendert?");
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_3.setText(String.valueOf(count));
                    //save the cange
                    //wholeList.get(2).set(1, count);
                    writeCsv(MainActivity.this);

                } else {
                    Toast.makeText(MainActivity.this, "Sie haben keine Produkte mehr, möchten sie sie vielleicht auf ihre Einkaufliste setzen?", Toast.LENGTH_LONG).show();
                }
            }

        });
        button_delete_1 = findViewById(R.id.button_delete_1);
        button_delete_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("Klick", "Button erfolkreich geklickt");
                for(int onecounter = 0; onecounter < wholeList.size(); onecounter++){
                    //Log.d("Klick", "For schleife ausgefürt");
                    if (wholeList.get(onecounter).get(0).equals(product_1.getText())){
                        wholeList.get(onecounter).set(0, "Produkt");
                        wholeList.get(onecounter).set(1, 0);
                        section_1.setVisibility(View.GONE);
                        writeCsv(MainActivity.this);
                        readlagerbestand();
                        sortlagerbestand();
                        //Log.d("Klick", " erfolkreich geaendert?");
                        break;
                    }
                }

                }
        });
        button_delete_2 = findViewById(R.id.button_delete_2);
        button_delete_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int twocounter = 0; twocounter < wholeList.size(); twocounter++){
                    if (wholeList.get(twocounter).get(0).equals(product_2.getText())){
                        wholeList.get(twocounter).set(0, "Produkt");
                        wholeList.get(twocounter).set(1, 0);
                        section_2.setVisibility(View.GONE);
                        writeCsv(MainActivity.this);
                        readlagerbestand();
                        sortlagerbestand();
                        break;
                    }
                }

            }
        });
        button_delete_3 = findViewById(R.id.button_delete_3);
        button_delete_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int threecounter = 0; threecounter < wholeList.size(); threecounter++){
                    if (wholeList.get(threecounter).get(0).equals(product_3.getText())){
                        wholeList.get(threecounter).set(0, "Produkt");
                        wholeList.get(threecounter).set(1, 0);
                        section_3.setVisibility(View.GONE);
                        writeCsv(MainActivity.this);
                        readlagerbestand();
                        sortlagerbestand();
                        break;
                    }
                }

            }
        });
        button_update = findViewById(R.id.button_update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeCsv(MainActivity.this);
                readlagerbestand();
                sortlagerbestand();
            }
        });

        readlagerbestand();
        sortlagerbestand();
        writeCsv(this);
    }

    private List<List> wholeList = new ArrayList<>();



    private void readlagerbestand() {
        InputStream is = null;

        try {
            Context context = this;
            File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(dir, "data.csv");
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            is = getResources().openRawResource(R.raw.lagerbestand);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";

        try {
            // Step over header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                // Split by ","
                String[] tokens = line.split(",");
                // Read data
                String produkt = tokens[0];
                int anzahl;
                try {
                    anzahl = Integer.parseInt(tokens[1]);
                } catch (NumberFormatException e) {
                    // handle non-numeric input here (e.g. log an error, skip the line, etc.)
                    continue;
                }

                // resett Lists and create list with values of current line
                List<Object> innerList = new ArrayList<>();
                innerList.add(produkt);
                innerList.add(anzahl);

                // Add inner list to outer list
                wholeList.add(innerList);
                Log.d("Check my Shelf", "Just created: " + innerList);
            }
        } catch (IOException e) {
            Log.wtf("Check my Shelf", "Error reading data file on line " + line, e);
            throw new RuntimeException(e);
        }


        Log.d("Alles Ausgeben", "Alles: " + wholeList);
        Log.d("spezielles ausgeben", "test: " + wholeList.get(0).get(1));
        //wholeList.get(0).set(2, 55);
        //Log.d("spezielles ausgeben", "55?: " + wholeList.get(0).get(2));
    }

    private TextView noproduct;
    private TextView product_1;
    private TextView product_2;
    private TextView product_3;
    private TextView product_4;
    private TextView product_5;
    private TextView product_6;
    private TextView product_7;
    private TextView product_8;
    private TextView product_9;
    private TextView product_10;
    ////////////////////////
    private TextView number_1;
    private TextView number_2;
    private TextView number_3;
    /////////////////////////
    private LinearLayout section_1;
    private LinearLayout section_2;
    private LinearLayout section_3;
    private LinearLayout section_4;
    private LinearLayout section_5;
    private LinearLayout section_6;
    private LinearLayout section_7;
    private LinearLayout section_8;
    private LinearLayout section_9;
    private LinearLayout section_10;




    private void sortlagerbestand() {
        noproduct = findViewById(R.id.noproduct);
        product_1 = findViewById(R.id.product_1);
        product_2 = findViewById(R.id.product_2);
        product_3 = findViewById(R.id.product_3);
        //product_4 = findViewById(R.id.product_4);
        //product_5 = findViewById(R.id.product_5);
        //product_6 = findViewById(R.id.product_6);
        //product_7 = findViewById(R.id.product_7);
        //product_8 = findViewById(R.id.product_8);
        //product_9 = findViewById(R.id.product_9);
        //product_10 = findViewById(R.id.product_10);
        ///////////////////
        section_1 = findViewById(R.id.section_1);
        section_2 = findViewById(R.id.section_2);
        section_3 = findViewById(R.id.section_3);
        ///////////////////
        number_1 = findViewById(R.id.number_1);
        number_2 = findViewById(R.id.number_2);
        number_3 = findViewById(R.id.number_3);
        ///////////////////


        // assign the other TextViews to their respective ids as needed

        int productCounter = 1; // start with the first product TextView

        for (int counter = 0; counter < wholeList.size(); counter++) {
            String productName = wholeList.get(counter).get(0).toString();
            //productName = productName.substring(1, productName.length() - 1);
            String productquantity = wholeList.get(counter).get(1).toString();
            //productquantity = productquantity.substring(1, productquantity.length() - 1);


            if (!productName.equals("Produkt")) {
                Log.d("Check my Shelf", "Produktname:" + productName);

                switch (productCounter) {
                    case 1:
                        product_1.setText(productName);
                        section_1.setVisibility(View.VISIBLE);
                        number_1.setText(productquantity);
                        noproduct.setVisibility(View.GONE);
                        break;
                    case 2:
                        product_2.setText(productName);
                        section_2.setVisibility(View.VISIBLE);
                        number_2.setText(productquantity);
                        break;
                    case 3:
                        product_3.setText(productName);
                        section_3.setVisibility(View.VISIBLE);
                        number_3.setText(productquantity);
                        break;
                    // add more cases for each product as needed
                }

                productCounter++; // increment to the next product TextView
            }
        }



    }
        private void writeCsv(Context context) {
            // Get the directory where the file will be saved
            File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            // Create the CSV file
            File file = new File(dir, "data.csv");

            try {
                // Open a file output stream
                FileOutputStream fos = new FileOutputStream(file);

                // Create an output writer
                OutputStreamWriter writer = new OutputStreamWriter(fos);

                // Write some data to the file
                writer.write("Produkt,Anzahl\n");
                for (int counter = 0; counter < wholeList.size(); counter++) {
                    writer.write("" + wholeList.get(counter).get(0) + "," + wholeList.get(counter).get(1) + "\n");
                }
                Log.d("csvWriting", "test");

                // Close the writer and output stream
                writer.close();
                fos.close();
            } catch (IOException e) {
                // Handle the exception
                e.printStackTrace();
            }
        }

}




