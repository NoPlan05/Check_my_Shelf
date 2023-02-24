package com.example.imagepro;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.opencv.android.OpenCVLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
    private Button button_plus_4;
    private Button button_minus_4;
    private Button button_plus_5;
    private Button button_minus_5;
    private Button button_plus_6;
    private Button button_minus_6;
    private Button button_plus_7;
    private Button button_minus_7;
    private Button button_plus_8;
    private Button button_minus_8;
    private Button button_plus_9;
    private Button button_minus_9;
    private Button button_plus_10;
    private Button button_minus_10;
    private Button button_plus_11;
    private Button button_minus_11;
    private Button button_plus_12;
    private Button button_minus_12;


    private ImageView cancel_button;
    private ImageView apply_button;
    private ConstraintLayout container_new_product;
    private ConstraintLayout container_buttons_new_product;

    private EditText entry_field_product_name;
    private EditText entry_field_product_number;
    private ImageView button_update;
    private ImageView add_button;
    private ImageView stopp_button;
    private ConstraintLayout container_buttons_edit_product;
    private ImageView change_button;
    private Button button_new_product_minus;
    private Button button_new_product_plus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readlagerbestand();
        sortlagerbestand();

        ColorStateList colorStateList = getResources().getColorStateList(R.color.dark_gray);
        button_minus_1 = findViewById(R.id.button_minus_1);
        Drawable defaultBackground = button_minus_1.getBackground();


        // to go into camera
        camera_button = findViewById(R.id.camera_button);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readlagerbestand();
                sortlagerbestand();
                writeCsv();
                startActivity(new Intent(MainActivity.this, CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        // to go in to storage
        storage_Image_button = findViewById(R.id.storage_Image_button);
        storage_Image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, storageRecognitionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                readlagerbestand();
                sortlagerbestand();
                writeCsv();
            }
        });

        add_button = findViewById(R.id.add_button);
        cancel_button = findViewById(R.id.cancel_button);
        container_new_product = findViewById(R.id.container_new_product);
        container_buttons_new_product = findViewById(R.id.container_buttons_new_product);
        entry_field_product_name = findViewById(R.id.entry_field_product_name);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container_new_product.setVisibility(View.VISIBLE);
                container_buttons_new_product.setVisibility(View.VISIBLE);
                storage_Image_button.setVisibility(View.GONE);
                camera_button.setVisibility(View.GONE);
                add_button.setVisibility(View.GONE);
                entry_field_product_name.requestFocus();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(entry_field_product_name, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        cancel_button = findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container_new_product.setVisibility(View.GONE);
                container_buttons_new_product.setVisibility(View.GONE);
                storage_Image_button.setVisibility(View.VISIBLE);
                camera_button.setVisibility(View.VISIBLE);
                add_button.setVisibility(View.VISIBLE);
            }

        });
        entry_field_product_name = findViewById(R.id.entry_field_product_name);
        entry_field_product_number = findViewById(R.id.entry_field_product_number);

        apply_button = findViewById(R.id.apply_button);
        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "langer text um aufzufallen test "+product_1.getText().toString());
                List<List<String>> topLevelList = new ArrayList<>(Arrays.asList());
                List<String> notTopLevelList = new ArrayList<>();
                topLevelList.clear();
                notTopLevelList.clear();
                for (int plus1counter = 0; plus1counter < wholeList.size(); plus1counter++) {
                    if (wholeList.get(plus1counter).get(0).equals(product_1.getText().toString())) {
                        notTopLevelList.add(entry_field_product_name.getText().toString());
                        notTopLevelList.add(entry_field_product_number.getText().toString());
                        topLevelList.add(notTopLevelList);
                        Log.d("test topLevelList", "topLevelList="+ topLevelList);
                        adddata(topLevelList);
                        topLevelList.clear();
                        break;
                    }
                }
                container_new_product.setVisibility(View.GONE);
                container_buttons_new_product.setVisibility(View.GONE);
                storage_Image_button.setVisibility(View.VISIBLE);
                camera_button.setVisibility(View.VISIBLE);
                add_button.setVisibility(View.VISIBLE);


            }
        });
        button_new_product_minus = findViewById(R.id.button_new_product_minus);
        button_new_product_plus = findViewById(R.id.button_new_product_plus);
        button_new_product_minus = findViewById(R.id.button_new_product_minus);
        button_new_product_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countString = entry_field_product_number.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;
                // Display the new value in the text view.
                entry_field_product_number.setText(String.valueOf(count));
            }
        });
        button_new_product_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countString = entry_field_product_number.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                if (count >= 1){
                    count--;
                }

                // Display the new value in the text view.
                entry_field_product_number.setText(String.valueOf(count));
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

                Log.d("test", "langer text um aufzufallen test "+product_1.getText().toString());
                for (int plus1counter = 0; plus1counter < wholeList.size(); plus1counter++) {
                    if (wholeList.get(plus1counter).get(0).equals(product_1.getText().toString())) {
                        wholeList.get(plus1counter).set(1, count);
                        //Log.d("Klick", " erfolkreich geaendert?");
                        break;
                    }
                }


                // Display the new value in the text view.
                number_1.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_1.setBackground(defaultBackground);
                    button_minus_1.setBackgroundTintList(colorStateList);
                    button_minus_1.setText("-");
                }
            }
        });

        number_1 = findViewById(R.id.number_1);
        button_minus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_1.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus1counter = 0; minus1counter < wholeList.size(); minus1counter++) {
                        if (wholeList.get(minus1counter).get(0).equals(product_1.getText().toString())) {
                            wholeList.get(minus1counter).set(1, count);
                            //Log.d("Klick", " erfolkreich geaendert?");
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_1.setText(String.valueOf(count));
                    //save the cange
                    //wholeList.get(0).set(1, count);
                    writeCsv();
                    if (count == 0){
                        button_minus_1.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_1.setBackgroundTintList(colorStateList);
                        button_minus_1.setText("");
                    }

                } else {
                    //Toast.makeText(MainActivity.this, "Sie haben keine Produkte mehr, möchten sie sie vielleicht auf ihre Einkaufliste setzen?", Toast.LENGTH_LONG).show();
                    //Log.d("Klick", "Button erfolkreich geklickt");
                    for (int counter1 = 0; counter1 < wholeList.size(); counter1++) {
                        //Log.d("Klick", "For schleife ausgefürt");
                        if (wholeList.get(counter1).get(0).equals(product_1.getText())) {
                            wholeList.get(counter1).set(0, "Produkt");
                            wholeList.get(counter1).set(1, 0);
                            section_1.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            //Log.d("Klick", " erfolkreich geaendert?");
                            button_minus_1.setBackground(defaultBackground);
                            button_minus_1.setBackgroundTintList(colorStateList);
                            button_minus_1.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        product_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_button = findViewById(R.id.change_button);
                stopp_button = findViewById(R.id.stopp_button);
                container_new_product = findViewById(R.id.container_new_product);
                container_buttons_edit_product = findViewById(R.id.container_buttons_edit_product);
                entry_field_product_name = findViewById(R.id.entry_field_product_name);

                        container_new_product.setVisibility(View.VISIBLE);
                        container_buttons_edit_product.setVisibility(View.VISIBLE);
                        storage_Image_button.setVisibility(View.GONE);
                        camera_button.setVisibility(View.GONE);
                        add_button.setVisibility(View.GONE);
                        entry_field_product_name.requestFocus();
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(entry_field_product_name, InputMethodManager.SHOW_IMPLICIT);

                entry_field_product_name = findViewById(R.id.entry_field_product_name);
                entry_field_product_number = findViewById(R.id.entry_field_product_number);
            }
        });
        change_button = findViewById(R.id.change_button);
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<List<String>> topLevelList = new ArrayList<>(Arrays.asList());
                List<String> notTopLevelList = new ArrayList<>();
                topLevelList.clear();
                Log.d("test enty fiel", "entry fielt:" + entry_field_product_name.getText().toString());
                for (int plus2counter = 0; plus2counter < wholeList.size(); plus2counter++) {
                    if (wholeList.get(plus2counter).get(0).equals(product_1.getText().toString())) {
                        wholeList.get(plus2counter).set(0,"Produkt");
                        Log.d("test enty fiel", "entry fielt:" + entry_field_product_name.getText().toString());
                        wholeList.get(plus2counter).set(1, 0);
                        notTopLevelList.add(entry_field_product_name.getText().toString());
                        notTopLevelList.add(entry_field_product_number.getText().toString());
                        topLevelList.add(notTopLevelList);
                        Log.d("test topLevelList", "topLevelList="+ topLevelList);

                        section_1.setVisibility(View.GONE);

                        writeCsv();
                        adddata(topLevelList);
                        topLevelList.clear();
                        wholeList.clear();
                        sortlagerbestand();
                        readlagerbestand();
                        sortlagerbestand();
                        break;
                    }
                }
                container_new_product.setVisibility(View.GONE);
                container_buttons_edit_product.setVisibility(View.GONE);
                storage_Image_button.setVisibility(View.VISIBLE);
                camera_button.setVisibility(View.VISIBLE);
                add_button.setVisibility(View.VISIBLE);
            }
        });
        stopp_button = findViewById(R.id.stopp_button);
        stopp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container_new_product.setVisibility(View.GONE);
                container_buttons_edit_product.setVisibility(View.GONE);
                storage_Image_button.setVisibility(View.VISIBLE);
                camera_button.setVisibility(View.VISIBLE);
                change_button.setVisibility(View.VISIBLE);
            }
        });
        button_new_product_minus = findViewById(R.id.button_new_product_minus);
        button_new_product_plus = findViewById(R.id.button_new_product_plus);
        button_new_product_minus = findViewById(R.id.button_new_product_minus);
        button_new_product_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countString = entry_field_product_number.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;
                // Display the new value in the text view.
                entry_field_product_number.setText(String.valueOf(count));
            }
        });
        button_new_product_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countString = entry_field_product_number.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                if (count >= 1){
                    count--;
                }

                // Display the new value in the text view.
                entry_field_product_number.setText(String.valueOf(count));
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

                Log.d("test", "langer text um aufzufallen test "+product_2.getText().toString());
                for (int plus2counter = 0; plus2counter < wholeList.size(); plus2counter++) {
                    if (wholeList.get(plus2counter).get(0).equals(product_2.getText().toString())) {
                        wholeList.get(plus2counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_2.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_2.setBackground(defaultBackground);
                    button_minus_2.setBackgroundTintList(colorStateList);
                    button_minus_2.setText("-");
                }
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
                    for (int minus2counter = 0; minus2counter < wholeList.size(); minus2counter++) {
                        if (wholeList.get(minus2counter).get(0).equals(product_2.getText().toString())) {
                            wholeList.get(minus2counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_2.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_2.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_2.setBackgroundTintList(colorStateList);
                        button_minus_2.setText("");
                    }

                } else {
                    for (int counter2 = 0; counter2 < wholeList.size(); counter2++) {
                        if (wholeList.get(counter2).get(0).equals(product_2.getText())) {
                            wholeList.get(counter2).set(0, "Produkt");
                            wholeList.get(counter2).set(1, 0);
                            section_2.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_2.setBackground(defaultBackground);
                            button_minus_2.setBackgroundTintList(colorStateList);
                            button_minus_2.setText("-");
                            break;
                        }
                    }
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

                Log.d("test", "langer text um aufzufallen test "+product_3.getText().toString());
                for (int plus3counter = 0; plus3counter < wholeList.size(); plus3counter++) {
                    if (wholeList.get(plus3counter).get(0).equals(product_3.getText().toString())) {
                        wholeList.get(plus3counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_3.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_3.setBackground(defaultBackground);
                    button_minus_3.setBackgroundTintList(colorStateList);
                    button_minus_3.setText("-");
                }
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
                    for (int minus3counter = 0; minus3counter < wholeList.size(); minus3counter++) {
                        if (wholeList.get(minus3counter).get(0).equals(product_3.getText().toString())) {
                            wholeList.get(minus3counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_3.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_3.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_3.setBackgroundTintList(colorStateList);
                        button_minus_3.setText("");
                    }

                } else {
                    for (int counter3 = 0; counter3 < wholeList.size(); counter3++) {
                        if (wholeList.get(counter3).get(0).equals(product_3.getText())) {
                            wholeList.get(counter3).set(0, "Produkt");
                            wholeList.get(counter3).set(1, 0);
                            section_3.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_3.setBackground(defaultBackground);
                            button_minus_3.setBackgroundTintList(colorStateList);
                            button_minus_3.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_4 = findViewById(R.id.button_plus_4);
        number_4 = findViewById(R.id.number_4);
        button_plus_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_4.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                Log.d("test", "langer text um aufzufallen test "+product_4.getText().toString());
                for (int plus4counter = 0; plus4counter < wholeList.size(); plus4counter++) {
                    if (wholeList.get(plus4counter).get(0).equals(product_4.getText().toString())) {
                        wholeList.get(plus4counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_4.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_4.setBackground(defaultBackground);
                    button_minus_4.setBackgroundTintList(colorStateList);
                    button_minus_4.setText("-");
                }
            }
        });
        button_minus_4 = findViewById(R.id.button_minus_4);
        number_4 = findViewById(R.id.number_4);
        button_minus_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_4.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus4counter = 0; minus4counter < wholeList.size(); minus4counter++) {
                        if (wholeList.get(minus4counter).get(0).equals(product_4.getText().toString())) {
                            wholeList.get(minus4counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_4.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_4.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_4.setBackgroundTintList(colorStateList);
                        button_minus_4.setText("");
                    }

                } else {
                    for (int counter4 = 0; counter4 < wholeList.size(); counter4++) {
                        if (wholeList.get(counter4).get(0).equals(product_4.getText())) {
                            wholeList.get(counter4).set(0, "Produkt");
                            wholeList.get(counter4).set(1, 0);
                            section_4.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_4.setBackground(defaultBackground);
                            button_minus_4.setBackgroundTintList(colorStateList);
                            button_minus_4.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_5 = findViewById(R.id.button_plus_5);
        number_5 = findViewById(R.id.number_5);
        button_plus_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_5.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                Log.d("test", "langer text um aufzufallen test "+product_5.getText().toString());
                for (int plus5counter = 0; plus5counter < wholeList.size(); plus5counter++) {
                    if (wholeList.get(plus5counter).get(0).equals(product_5.getText().toString())) {
                        wholeList.get(plus5counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_5.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_5.setBackground(defaultBackground);
                    button_minus_5.setBackgroundTintList(colorStateList);
                    button_minus_5.setText("-");
                }
            }
        });
        button_minus_5 = findViewById(R.id.button_minus_5);
        number_5 = findViewById(R.id.number_5);
        button_minus_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_5.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus5counter = 0; minus5counter < wholeList.size(); minus5counter++) {
                        if (wholeList.get(minus5counter).get(0).equals(product_5.getText().toString())) {
                            wholeList.get(minus5counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_5.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_5.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_5.setBackgroundTintList(colorStateList);
                        button_minus_5.setText("");
                    }

                } else {
                    for (int counter5 = 0; counter5 < wholeList.size(); counter5++) {
                        if (wholeList.get(counter5).get(0).equals(product_5.getText())) {
                            wholeList.get(counter5).set(0, "Produkt");
                            wholeList.get(counter5).set(1, 0);
                            section_5.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_5.setBackground(defaultBackground);
                            button_minus_5.setBackgroundTintList(colorStateList);
                            button_minus_5.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_6 = findViewById(R.id.button_plus_6);
        number_6 = findViewById(R.id.number_6);
        button_plus_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_6.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                Log.d("test", "langer text um aufzufallen test "+product_6.getText().toString());
                for (int plus6counter = 0; plus6counter < wholeList.size(); plus6counter++) {
                    if (wholeList.get(plus6counter).get(0).equals(product_6.getText().toString())) {
                        wholeList.get(plus6counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_6.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_6.setBackground(defaultBackground);
                    button_minus_6.setBackgroundTintList(colorStateList);
                    button_minus_6.setText("-");
                }
            }
        });
        button_minus_6 = findViewById(R.id.button_minus_6);
        number_6 = findViewById(R.id.number_6);
        button_minus_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_6.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus6counter = 0; minus6counter < wholeList.size(); minus6counter++) {
                        if (wholeList.get(minus6counter).get(0).equals(product_6.getText().toString())) {
                            wholeList.get(minus6counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_6.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_6.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_6.setBackgroundTintList(colorStateList);
                        button_minus_6.setText("");
                    }

                } else {
                    for (int counter6 = 0; counter6 < wholeList.size(); counter6++) {
                        if (wholeList.get(counter6).get(0).equals(product_6.getText())) {
                            wholeList.get(counter6).set(0, "Produkt");
                            wholeList.get(counter6).set(1, 0);
                            section_6.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_6.setBackground(defaultBackground);
                            button_minus_6.setBackgroundTintList(colorStateList);
                            button_minus_6.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_7 = findViewById(R.id.button_plus_7);
        number_7 = findViewById(R.id.number_7);
        button_plus_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_7.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                for (int plus7counter = 0; plus7counter < wholeList.size(); plus7counter++) {
                    if (wholeList.get(plus7counter).get(0).equals(product_7.getText().toString())) {
                        wholeList.get(plus7counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_7.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_7.setBackground(defaultBackground);
                    button_minus_7.setBackgroundTintList(colorStateList);
                    button_minus_7.setText("-");
                }
            }
        });
        button_minus_7 = findViewById(R.id.button_minus_7);
        number_7 = findViewById(R.id.number_7);
        button_minus_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_7.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus7counter = 0; minus7counter < wholeList.size(); minus7counter++) {
                        if (wholeList.get(minus7counter).get(0).equals(product_7.getText().toString())) {
                            wholeList.get(minus7counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_7.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_7.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_7.setBackgroundTintList(colorStateList);
                        button_minus_7.setText("");
                    }

                } else {
                    for (int counter7 = 0; counter7 < wholeList.size(); counter7++) {
                        if (wholeList.get(counter7).get(0).equals(product_7.getText())) {
                            wholeList.get(counter7).set(0, "Produkt");
                            wholeList.get(counter7).set(1, 0);
                            section_7.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_7.setBackground(defaultBackground);
                            button_minus_7.setBackgroundTintList(colorStateList);
                            button_minus_7.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_8 = findViewById(R.id.button_plus_8);
        number_8 = findViewById(R.id.number_8);
        button_plus_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_8.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                for (int plus8counter = 0; plus8counter < wholeList.size(); plus8counter++) {
                    if (wholeList.get(plus8counter).get(0).equals(product_8.getText().toString())) {
                        wholeList.get(plus8counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_8.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_8.setBackground(defaultBackground);
                    button_minus_8.setBackgroundTintList(colorStateList);
                    button_minus_8.setText("-");
                }
            }
        });
        button_minus_8 = findViewById(R.id.button_minus_8);
        number_8 = findViewById(R.id.number_8);
        button_minus_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_8.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus8counter = 0; minus8counter < wholeList.size(); minus8counter++) {
                        if (wholeList.get(minus8counter).get(0).equals(product_8.getText().toString())) {
                            wholeList.get(minus8counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_8.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_8.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_8.setBackgroundTintList(colorStateList);
                        button_minus_8.setText("");
                    }

                } else {
                    for (int counter8 = 0; counter8 < wholeList.size(); counter8++) {
                        if (wholeList.get(counter8).get(0).equals(product_8.getText())) {
                            wholeList.get(counter8).set(0, "Produkt");
                            wholeList.get(counter8).set(1, 0);
                            section_8.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_8.setBackground(defaultBackground);
                            button_minus_8.setBackgroundTintList(colorStateList);
                            button_minus_8.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_9 = findViewById(R.id.button_plus_9);
        number_9 = findViewById(R.id.number_9);
        button_plus_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_9.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                for (int plus9counter = 0; plus9counter < wholeList.size(); plus9counter++) {
                    if (wholeList.get(plus9counter).get(0).equals(product_9.getText().toString())) {
                        wholeList.get(plus9counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_9.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_9.setBackground(defaultBackground);
                    button_minus_9.setBackgroundTintList(colorStateList);
                    button_minus_9.setText("-");
                }
            }
        });
        button_minus_9 = findViewById(R.id.button_minus_9);
        number_9 = findViewById(R.id.number_9);
        button_minus_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_9.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus9counter = 0; minus9counter < wholeList.size(); minus9counter++) {
                        if (wholeList.get(minus9counter).get(0).equals(product_9.getText().toString())) {
                            wholeList.get(minus9counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_9.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_9.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_9.setBackgroundTintList(colorStateList);
                        button_minus_9.setText("");
                    }

                } else {
                    for (int counter9 = 0; counter9 < wholeList.size(); counter9++) {
                        if (wholeList.get(counter9).get(0).equals(product_9.getText())) {
                            wholeList.get(counter9).set(0, "Produkt");
                            wholeList.get(counter9).set(1, 0);
                            section_9.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_9.setBackground(defaultBackground);
                            button_minus_9.setBackgroundTintList(colorStateList);
                            button_minus_9.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_10 = findViewById(R.id.button_plus_10);
        number_10 = findViewById(R.id.number_10);
        button_plus_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_10.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                for (int plus10counter = 0; plus10counter < wholeList.size(); plus10counter++) {
                    if (wholeList.get(plus10counter).get(0).equals(product_10.getText().toString())) {
                        wholeList.get(plus10counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_10.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_10.setBackground(defaultBackground);
                    button_minus_10.setBackgroundTintList(colorStateList);
                    button_minus_10.setText("-");
                }
            }
        });
        button_minus_10 = findViewById(R.id.button_minus_10);
        number_10 = findViewById(R.id.number_10);
        button_minus_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_10.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus10counter = 0; minus10counter < wholeList.size(); minus10counter++) {
                        if (wholeList.get(minus10counter).get(0).equals(product_10.getText().toString())) {
                            wholeList.get(minus10counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_10.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_10.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_10.setBackgroundTintList(colorStateList);
                        button_minus_10.setText("");
                    }

                } else {
                    for (int counter10 = 0; counter10 < wholeList.size(); counter10++) {
                        if (wholeList.get(counter10).get(0).equals(product_10.getText())) {
                            wholeList.get(counter10).set(0, "Produkt");
                            wholeList.get(counter10).set(1, 0);
                            section_10.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_10.setBackground(defaultBackground);
                            button_minus_10.setBackgroundTintList(colorStateList);
                            button_minus_10.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_11 = findViewById(R.id.button_plus_11);
        number_11 = findViewById(R.id.number_11);
        button_plus_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_11.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                for (int plus11counter = 0; plus11counter < wholeList.size(); plus11counter++) {
                    if (wholeList.get(plus11counter).get(0).equals(product_11.getText().toString())) {
                        wholeList.get(plus11counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_11.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_11.setBackground(defaultBackground);
                    button_minus_11.setBackgroundTintList(colorStateList);
                    button_minus_11.setText("-");
                }
            }
        });
        button_minus_11 = findViewById(R.id.button_minus_11);
        number_11 = findViewById(R.id.number_11);
        button_minus_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_11.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus11counter = 0; minus11counter < wholeList.size(); minus11counter++) {
                        if (wholeList.get(minus11counter).get(0).equals(product_11.getText().toString())) {
                            wholeList.get(minus11counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_11.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_11.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_11.setBackgroundTintList(colorStateList);
                        button_minus_11.setText("");
                    }

                } else {
                    for (int counter11 = 0; counter11 < wholeList.size(); counter11++) {
                        if (wholeList.get(counter11).get(0).equals(product_11.getText())) {
                            wholeList.get(counter11).set(0, "Produkt");
                            wholeList.get(counter11).set(1, 0);
                            section_11.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_11.setBackground(defaultBackground);
                            button_minus_11.setBackgroundTintList(colorStateList);
                            button_minus_11.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_plus_12 = findViewById(R.id.button_plus_12);
        number_12 = findViewById(R.id.number_12);
        button_plus_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_12.getText().toString();
                // Convert value to a number and increment it
                Integer count = Integer.parseInt(countString);
                count++;

                for (int plus12counter = 0; plus12counter < wholeList.size(); plus12counter++) {
                    if (wholeList.get(plus12counter).get(0).equals(product_12.getText().toString())) {
                        wholeList.get(plus12counter).set(1, count);
                        break;
                    }
                }


                // Display the new value in the text view.
                number_12.setText(String.valueOf(count));
                //save the cange
                writeCsv();
                if (count == 1){
                    button_minus_12.setBackground(defaultBackground);
                    button_minus_12.setBackgroundTintList(colorStateList);
                    button_minus_12.setText("-");
                }
            }
        });
        button_minus_12 = findViewById(R.id.button_minus_12);
        number_12 = findViewById(R.id.number_12);
        button_minus_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = number_12.getText().toString();
                // Convert value to a number and decrement it
                int count = Integer.parseInt(countString);
                if (count > 0) {
                    count--;
                    for (int minus12counter = 0; minus12counter < wholeList.size(); minus12counter++) {
                        if (wholeList.get(minus12counter).get(0).equals(product_12.getText().toString())) {
                            wholeList.get(minus12counter).set(1, count);
                            break;
                        }
                    }
                    // Display the new value in the text view.
                    number_12.setText(String.valueOf(count));
                    //save the cange
                    writeCsv();
                    if (count == 0){
                        button_minus_12.setBackgroundResource(R.drawable.delete_button_image);
                        button_minus_12.setBackgroundTintList(colorStateList);
                        button_minus_12.setText("");
                    }

                } else {
                    for (int counter12 = 0; counter12 < wholeList.size(); counter12++) {
                        if (wholeList.get(counter12).get(0).equals(product_12.getText())) {
                            wholeList.get(counter12).set(0, "Produkt");
                            wholeList.get(counter12).set(1, 0);
                            section_12.setVisibility(View.GONE);
                            writeCsv();
                            readlagerbestand();
                            sortlagerbestand();
                            button_minus_12.setBackground(defaultBackground);
                            button_minus_12.setBackgroundTintList(colorStateList);
                            button_minus_12.setText("-");
                            break;
                        }
                    }
                }
            }

        });
        button_update = findViewById(R.id.button_update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readlagerbestand();
                sortlagerbestand();
            }
        });


    }

    private List<List> wholeList = new ArrayList<>();


    private void readlagerbestand() {
        InputStream is = null;

        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "Documents");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e("writeCsv", "Failed to create directory");
                    return;
                }
            }
            Log.d("Witert test", "context ist" + this);
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

            wholeList.clear();
            while ((line = reader.readLine()) != null) {
                // Split by ","
                String[] tokens = line.split(",");
                // Read data
                String produkt = tokens[0];
                int anzahl = Integer.parseInt(tokens[1]);

                // resett Lists and create list with values of current line
                List<Object> innerList = new ArrayList<>();
                innerList.add(produkt);
                innerList.add(anzahl);

                // Add inner list to outer list
                wholeList.add(innerList);

                Log.d("Camera", "Just created: " + innerList);
            }
        } catch (IOException e) {
            Log.wtf("Check my Shelf", "Error reading data file on line " + line, e);
            throw new RuntimeException(e);
        } finally {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                Log.e("readlagerbestand", "Error closing input stream", e);
            }
        }
    }


    }

    private TextView no_product;
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
    private TextView product_11;
    private TextView product_12;
    ////////////////////////
    private TextView number_1;
    private TextView number_2;
    private TextView number_3;
    private TextView number_4;
    private TextView number_5;
    private TextView number_6;
    private TextView number_7;
    private TextView number_8;
    private TextView number_9;
    private TextView number_10;
    private TextView number_11;
    private TextView number_12;
    /////////////////////////
    private ConstraintLayout section_1;
    private ConstraintLayout section_2;
    private ConstraintLayout section_3;
    private ConstraintLayout section_4;
    private ConstraintLayout section_5;
    private ConstraintLayout section_6;
    private ConstraintLayout section_7;
    private ConstraintLayout section_8;
    private ConstraintLayout section_9;
    private ConstraintLayout section_10;
    private ConstraintLayout section_11;
    private ConstraintLayout section_12;


    private void sortlagerbestand() {
        no_product = findViewById(R.id.no_product);
        product_1 = findViewById(R.id.product_1);
        product_2 = findViewById(R.id.product_2);
        product_3 = findViewById(R.id.product_3);
        product_4 = findViewById(R.id.product_4);
        product_5 = findViewById(R.id.product_5);
        product_6 = findViewById(R.id.product_6);
        product_7 = findViewById(R.id.product_7);
        product_8 = findViewById(R.id.product_8);
        product_9 = findViewById(R.id.product_9);
        product_10 = findViewById(R.id.product_10);
        product_11 = findViewById(R.id.product_11);
        product_12 = findViewById(R.id.product_12);
        ///////////////////
        section_1 = findViewById(R.id.section_1);
        section_2 = findViewById(R.id.section_2);
        section_3 = findViewById(R.id.section_3);
        section_4 = findViewById(R.id.section_4);
        section_5 = findViewById(R.id.section_5);
        section_6 = findViewById(R.id.section_6);
        section_7 = findViewById(R.id.section_7);
        section_8 = findViewById(R.id.section_8);
        section_9 = findViewById(R.id.section_9);
        section_10 = findViewById(R.id.section_10);
        section_11 = findViewById(R.id.section_11);
        section_12 = findViewById(R.id.section_12);
        ///////////////////
        number_1 = findViewById(R.id.number_1);
        number_2 = findViewById(R.id.number_2);
        number_3 = findViewById(R.id.number_3);
        number_4 = findViewById(R.id.number_4);
        number_5 = findViewById(R.id.number_5);
        number_6 = findViewById(R.id.number_6);
        number_7 = findViewById(R.id.number_7);
        number_8 = findViewById(R.id.number_8);
        number_9 = findViewById(R.id.number_9);
        number_10 = findViewById(R.id.number_10);
        number_11 = findViewById(R.id.number_11);
        number_12 = findViewById(R.id.number_12);
        ///////////////////


        // assign the other TextViews to their respective ids as needed

        int productCounter = 1; // start with the first product TextView



        for (int counter = 0; counter < wholeList.size(); counter++) {
            String productName = wholeList.get(counter).get(0).toString();
            String productquantity = wholeList.get(counter).get(1).toString();



            if (!productName.equals("Produkt")) {
                Log.d("Check my Shelf", "Produktname:" + productName);

                switch (productCounter) {
                    case 1:
                        product_1.setText(productName);
                        section_1.setVisibility(View.VISIBLE);
                        number_1.setText(productquantity);
                        no_product.setVisibility(View.GONE);
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
                    case 4:
                        product_4.setText(productName);
                        section_4.setVisibility(View.VISIBLE);
                        number_4.setText(productquantity);
                        break;
                    case 5:
                        product_5.setText(productName);
                        section_5.setVisibility(View.VISIBLE);
                        number_5.setText(productquantity);
                        break;
                    case 6:
                        product_6.setText(productName);
                        section_6.setVisibility(View.VISIBLE);
                        number_6.setText(productquantity);
                        break;
                    case 7:
                        product_7.setText(productName);
                        section_7.setVisibility(View.VISIBLE);
                        number_7.setText(productquantity);
                        break;
                    case 8:
                        product_8.setText(productName);
                        section_8.setVisibility(View.VISIBLE);
                        number_8.setText(productquantity);
                        break;
                    case 9:
                        product_9.setText(productName);
                        section_9.setVisibility(View.VISIBLE);
                        number_9.setText(productquantity);
                        break;
                    case 10:
                        product_10.setText(productName);
                        section_10.setVisibility(View.VISIBLE);
                        number_10.setText(productquantity);
                        break;
                    case 11:
                        product_11.setText(productName);
                        section_11.setVisibility(View.VISIBLE);
                        number_11.setText(productquantity);
                        break;
                    case 12:
                        product_12.setText(productName);
                        section_12.setVisibility(View.VISIBLE);
                        number_12.setText(productquantity);
                        break;

                    // add more cases for each product as needed
                }

                productCounter++; // increment to the next product TextView
            }
        }


    }

    public void adddata(List<List<String>> topLevelList) {
        readlagerbestand();
        Map<String, Integer> dataMap = new HashMap<>();

        // Fill the dataMap with values from wholeList
        for (List<Object> item : wholeList) {
            String key = item.get(0).toString();
            int value = Integer.parseInt(item.get(1).toString());
            dataMap.put(key, value);
        }

        // Update the dataMap with values from topLevelList
        for (List<String> item : topLevelList) {
            String key = item.get(0);
            int value = Integer.parseInt(item.get(1));

            if (!key.equals("Produkt")) {
                Integer oldValue = dataMap.get(key);
                if (oldValue == null) {
                    oldValue = 0;
                }
                String replaceKey = null;
                for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
                    if (!entry.getKey().equals("Produkt") && entry.getValue() == value) {
                        replaceKey = entry.getKey();
                        break;
                    }
                }
                if (replaceKey != null) {
                    int replaceValue = dataMap.get(replaceKey);
                    dataMap.put(replaceKey, 0);
                    dataMap.put(key, oldValue + replaceValue);
                } else {
                    dataMap.put(key, oldValue + value);
                }
            }
        }

        // Replace one of the keys with "Produkt" if there are duplicate values
        Set<Integer> uniqueValues = new HashSet<>(dataMap.values());
        if (uniqueValues.size() != dataMap.size()) {
            for (int i = 0; i < wholeList.size(); i++) {
                String key = wholeList.get(i).get(0).toString();
                int value = Integer.parseInt(wholeList.get(i).get(1).toString());
                if (value != 0 && !key.equals("Produkt") && Collections.frequency(dataMap.values(), value) > 1) {
                    dataMap.put("Produkt", 0);
                    wholeList.set(i, Arrays.asList("Produkt", 0));
                    break;
                }
            }
        }

        // Update wholeList with values from dataMap
        wholeList.clear();
        for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            List<Object> item = new ArrayList<>();
            item.add(key);
            if (value < 1){
                value = 1;
            }
            item.add(value);
            wholeList.add(item);
        }

        // Write the updated data to CSV file
        Log.d("whole List new", "List:" + wholeList);
        writeCsv();
    }






    public void writeCsv() {
        File dir = new File(Environment.getExternalStorageDirectory(), "Documents");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e("writeCsv", "Failed to create directory");
                return;
            }
        }
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
        readlagerbestand();
        sortlagerbestand();
    }
    @Override
    protected void onPause() {
        super.onPause();
        EditText editText = findViewById(R.id.entry_field_product_number);
        EditText editText1 = findViewById(R.id.entry_field_product_name);
        editText.setText("0");
        editText1.setText("");
    }
}




