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

    MainActivity mainac = new MainActivity();



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

    public static int[] search(String text, List<List<String>> topLevelList) {
        for (int i = 0; i < topLevelList.size(); i++) {
            for (int j = 0; j < topLevelList.get(i).size(); j++) {
                if (topLevelList.get(i).get(j).contains(text)) {
                    return new int[] {i, j};
                }
            }
        }
        return null;
    }
    public void saveTextAsCSV(String fileName, Text text) {
        List<String> lines = new ArrayList<>();
        List<String> row;
        String line;

        resultString = text.getText();
        if (resultString.length()<10) {
            Toast.makeText(this, "Bitte nochmal versuchen", Toast.LENGTH_SHORT).show();
        } else {
            resultString = resultString.toLowerCase(); //alles klein
            Log.d("CameraActivity", "Der gescannte Text aber klein:\n" + resultString);

            //in verschachtelte Liste
            String[] topLevelElements = resultString.split("\n");  // bei Absätzen neues Element

            for (String topLevelElement : topLevelElements) {
                List<String> subList = new ArrayList<>();
                String[] subListElements = topLevelElement.split(" ");

                for (String subListElement : subListElements) {
                    subList.add(subListElement);
                }

                topLevelList.add(subList);
            }
            Log.d("BonFilter", "verschachtelte Liste erstellt:" + topLevelList);

            //Bonart wird festgestellt
            String art = "";
            for (List<String> subList : topLevelList) {
                for (String element : subList) {
                    if (element.toLowerCase().contains("rewe") || element.toLowerCase().contains("812706034") || element.toLowerCase().contains("325633917")) {
                        art = "rewe";
                    }
                    else if (element.toLowerCase().contains("aldi") || element.toLowerCase().contains("127135543") || element.toLowerCase().contains("-k-u-n") || element.toLowerCase().contains("k-u-n-") || element.toLowerCase().contains("-u-n-d") || element.toLowerCase().contains("u-n-d-") || element.toLowerCase().contains("-n-d-e") || element.toLowerCase().contains("n-d-e-") || element.toLowerCase().contains("-d-e-n") || element.toLowerCase().contains("d-e-n-") || element.toLowerCase().contains("-e-n-b") || element.toLowerCase().contains("e-n-b-") || element.toLowerCase().contains("-n-b-e") || element.toLowerCase().contains("n-b-e-") || element.toLowerCase().contains("-b-e-l") || element.toLowerCase().contains("b-e-l-") || element.toLowerCase().contains("-e-l-e") || element.toLowerCase().contains("e-l-e-") || element.toLowerCase().contains("-l-e-g") || element.toLowerCase().contains("l-e-g-")) {
                        art = "aldi"; // -k-u-n-d-e-n-b-e-l-e-g-
                    }
                    else if (element.toLowerCase().contains("netto-online") || element.toLowerCase().contains("marken-discount") || element.toLowerCase().contains("213413670")) {
                        art = "netto";
                    }
                    else if (element.toLowerCase().contains("lidl")) {
                        art = "lidl";
                    }
                    else if (element.toLowerCase().contains("kaufland")) {
                        art = "kaufland";
                    }
                }
            }
            Log.d("BonFilter", "Bonart:" + art);

            //Ausgaben
            if (art.equals("")) {
                Toast.makeText(this, "Bon nicht erkannt", Toast.LENGTH_SHORT).show();
            } else if (art.equals("rewe") || art.equals("aldi")) {
                Toast.makeText(this, "Bon erkannt:" + art, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wir unterstützen leider nur Bons von Rewe und Aldi", Toast.LENGTH_SHORT).show();
            }

            //bei Rewe Bons:
            if (art.equals("rewe")) {

                //anfang kürzen
                int[] gefundenIndex = search("eur", topLevelList); //eur vor wichtige und 1 dahinter, wenn nicht eur bei mind. 11
                if (gefundenIndex != null && gefundenIndex[0]<7) {
                    topLevelList = topLevelList.subList(gefundenIndex[0] + 1, topLevelList.size());
                    Log.d("BonFilter", "gefunden: _eur_");
                } else {
                    topLevelList = topLevelList.subList(4, topLevelList.size());
                    Log.d("BonFilter", "eur nicht gefunden");
                }

                //Ende wird gekürzt
                int[] gefundenIndex2 = search("summe", topLevelList);
                if (gefundenIndex2 != null) {
                    topLevelList = topLevelList.subList(0, gefundenIndex2[0]);
                    Log.d("BonFilter", "gefunden: _summe_");
                }
                Log.d("BonFilter", "Liste gekürzt:" + topLevelList);

                //unnötiges raus
                for (int i = 0; i < topLevelList.size(); i++) {
                    for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                        if (topLevelList.get(i).get(j).equals("a") || topLevelList.get(i).get(j).equals("b") || topLevelList.get(i).get(j).contains("*") || topLevelList.get(i).get(j).contains("eur") || topLevelList.get(i).get(j).equals("x")  || topLevelList.get(i).get(j).equals("4a") ) {
                            topLevelList.get(i).remove(j);
                        }
                    }
                }
                //.equals, .contains

                //Leere Listen entfernen
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).isEmpty()) {
                        topLevelList.remove(i);
                    }
                }
                Log.d("BonFilter", "entschlackt:" + topLevelList);

                List<Integer> protectedindizes = new ArrayList<>();
                //sucht stückzahlen um diese zu schützen
                for (int i = 0; i < topLevelList.size(); i++) {
                    for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                        if (topLevelList.get(i).get(j).contains("stk")) {
                            protectedindizes.add(i);
                        }
                    }
                }
                Log.d("BonFilter", "geschützt sind:" + protectedindizes);

                //Elemente ohne Schrift werden entfernt
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (!protectedindizes.contains(i)) {// Prüfen, ob i in protectedindizes enthalten ist
                        for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                            if (!topLevelList.get(i).get(j).matches(".*[a-zA-Z].*")) {// Prüfen, ob das Element an der Stelle (i,j) keine Buchstaben enthält
                                topLevelList.get(i).remove(j);
                            }
                        }
                    }
                }

                // Bei Pfand und wiegen die ganze liste löschen
                for (int i = 0; i < topLevelList.size(); i++) {
                    for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                        if (topLevelList.get(i).get(j).contains("pfand") || topLevelList.get(i).get(j).contains("kg")) {
                            topLevelList.remove(i);
                        }
                    }
                }

                //Leere Listen entfernen
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).isEmpty()) {
                        topLevelList.remove(i);
                    }
                }

                Log.d("BonFilter", "ohne Preise:" + topLevelList);

                //Bei stk nur index 0
                for (int i = 0; i < topLevelList.size(); i++) {
                    for (int j = 0; j < topLevelList.get(i).size(); j++) {
                        if (topLevelList.get(i).get(j).contains("stk")) {
                            for (int k = topLevelList.get(i).size() - 1; k >= 1; k--) {
                                topLevelList.get(i).remove(k);
                            }
                            break;
                        }
                    }
                }

                //Leere Listen entfernen
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).isEmpty()) {
                        topLevelList.remove(i);
                    }
                }
                Log.d("BonFilter", "Sückzahlen richtig:" + topLevelList);

                //Produktnamen wieder zusammen
                List<String> result = new ArrayList<>();
                for (List<String> innerList : topLevelList) {
                    String joined = String.join(" ", innerList);
                    result.add(joined);
                }
                Log.d("BonFilter", "Produktnamen richtig:" + result);

                //Stückzahl zu Produkt (davor) (was wenn kein produkt davor? dann auch hinweis)
                List<List<String>> nestedList = new ArrayList<>();
                topLevelList.clear();
                List<String> currentList = new ArrayList<>();
                for (String str : result) {
                    if (str.matches("\\d+")) { // Prüfen, ob das Element nur aus Zahlen besteht
                        currentList.add(str); // Zum aktuellen Element hinzufügen
                    } else {
                        if (!currentList.isEmpty()) { // Prüfen, ob es bereits Zahlen-Elemente gibt
                            topLevelList.add(currentList); // Zur verschachtelten Liste hinzufügen
                            currentList = new ArrayList<>(); // Neues leeres Element erstellen
                        }
                        currentList.add(str); // Zum aktuellen Element hinzufügen
                    }
                }
                if (!currentList.isEmpty()) { // Prüfen, ob das letzte Element eine Zahlenliste ist
                    topLevelList.add(currentList);
                }
                Log.d("BonFilter", "zuordnung Stückzahlen:" + topLevelList);

                //leerg (leerg./leergut) komplett raus mit stk
                for (int i = topLevelList.size() - 1; i >= 0; i--) {
                    for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                        if (topLevelList.get(i).get(j).contains("eerg")) {
                            topLevelList.remove(i);
                            break; // Wenn ein Element entfernt wurde, muss die äußere Schleife nicht fortgesetzt werden
                        }
                    }
                }
                Log.d("BonFilter", "leergut entfehrnt:" + topLevelList);

                //wenn len name nur 2 löschen
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).get(0).length()<4) {
                        topLevelList.remove(i);
                    }
                }
                Log.d("BonFilter", "Namen < 4 gelöscht:" + topLevelList);

                //bei keiner Anzahl 1
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).size()==1){
                        topLevelList.get(i).add("1");
                    }
                }
                Log.d("BonFilter", "Anzahl hinzugefügt:" + topLevelList);

                //wenn anzahl kein int = 2
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (!topLevelList.get(i).get(1).matches("\\d+")){
                        topLevelList.get(i).set(1, "2");
                    } else if (topLevelList.get(i).get(1)=="0") { //Null unzulässig
                        topLevelList.get(i).set(1, "1");
                    }
                }
                Log.d("BonFilter", "Anzahl hinzugefügt:" + topLevelList);

                //[108, kg.x0,99, eur/kg]???

                // ALDI =============================================================================================================================================
            } else if (art.equals("aldi")) {

                //anfang kürzen
                int[] gefundenIndex = search("zeiten", topLevelList);
                if (gefundenIndex != null && gefundenIndex[0]<7) {
                    topLevelList = topLevelList.subList(gefundenIndex[0] + 2, topLevelList.size());
                    Log.d("BonFilter", "gefunden: öffnungszeiten");
                } else {int[] gefundenIndex2 = search("ffnung", topLevelList);
                    if (gefundenIndex2 != null && gefundenIndex2[0]<7) {
                        topLevelList = topLevelList.subList(gefundenIndex2[0] + 2, topLevelList.size());
                        Log.d("BonFilter", "gefunden: öffnungszeiten");
                    } else {
                        topLevelList = topLevelList.subList(4, topLevelList.size());
                        Log.d("BonFilter", "öffnungszeiten nicht gefunden");
                    }
                }

                //Ende wird gekürzt
                int[] gefundenIndex3 = search("anzahl", topLevelList);
                if (gefundenIndex3 != null && gefundenIndex3[0]>9) {
                    topLevelList = topLevelList.subList(0, gefundenIndex3[0]);
                    Log.d("BonFilter", "gefunden: _anzahl_");
                } else {int[] gefundenIndex4 = search("artikel", topLevelList);
                    if (gefundenIndex4 != null && gefundenIndex4[0]>9) {
                        topLevelList = topLevelList.subList(topLevelList.size(), gefundenIndex4[0] - 1);
                        Log.d("BonFilter", "gefunden: _artikel_");
                    } else { Log.d("BonFilter", "anzahl artikel nicht gefunden");}
                }
                Log.d("BonFilter", "Liste gekürzt:" + topLevelList);

                //unnötiges raus
                for (int i = 0; i < topLevelList.size(); i++) {
                    for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                        if (topLevelList.get(i).get(j).equals("b") || topLevelList.get(i).get(j).contains("€") || topLevelList.get(i).get(j).equals("e")) {
                            topLevelList.get(i).remove(j);
                        }
                    }
                }

                //Leere Listen entfernen
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).isEmpty()) {
                        topLevelList.remove(i);
                    }
                }
                Log.d("BonFilter", "entschlackt:" + topLevelList);

                List<Integer> protectedindizes = new ArrayList<>();
                //sucht stückzahlen um diese zu schützen
                for (int i = 0; i < topLevelList.size(); i++) {
                    for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                        if (topLevelList.get(i).get(j).contains("x")) {
                            protectedindizes.add(i);
                        }
                    }
                }
                Log.d("BonFilter", "geschützt sind:" + protectedindizes);

                // Bei Pfand und wiegen die ganze liste löschen
                for (int i = 0; i < topLevelList.size(); i++) {
                    for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                        if (topLevelList.get(i).get(j).contains("pfand") || topLevelList.get(i).get(j).contains("kg")) {
                            topLevelList.remove(i);
                        }
                    }
                }

                //Elemente ohne Schrift werden entfernt
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (!protectedindizes.contains(i)) {// Prüfen, ob i in protectedindizes enthalten ist
                        for (int j = topLevelList.get(i).size() - 1; j >= 0; j--) {
                            if (!topLevelList.get(i).get(j).matches(".*(?![e])[a-z].*")) {// Prüfen, ob das Element an der Stelle (i,j) keine Buchstaben enthält (außer e)
                                topLevelList.get(i).remove(j);
                            }
                        }
                    }
                }

                //Leere Listen entfernen
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).isEmpty()) {
                        topLevelList.remove(i);
                    }
                }
                Log.d("BonFilter", "ohne Preise:" + topLevelList);

                //Produktnamen wieder zusammen
                List<String> result = new ArrayList<>();
                for (List<String> innerList : topLevelList) {
                    String joined = String.join(" ", innerList);
                    result.add(joined);
                }
                Log.d("BonFilter", "Produktnamen richtig:" + result);

                //Stückzahlen wirklich nur die zahl am Anfang (funktioniert das?)
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).charAt(0) == 'x') {
                        result.remove(i);
                        result.add(i, "2");
                    } else if (Character.isDigit(result.get(i).charAt(0))) {
                        if (Character.isDigit(result.get(i).charAt(1))) {
                            String numberStr = "";
                            numberStr = numberStr + result.get(i).charAt(0) + result.get(i).charAt(1);
                            result.remove(i);
                            result.add(i, numberStr);
                        } else {
                            result.add(i, String.valueOf(result.get(i).charAt(0)));
                            result.remove(i+1);
                        }
                    }
                }
                Log.d("BonFilter", "Stückzahlen richtig:" + result);

                //Stückzahl zu Produkt (dahinter)
                topLevelList.clear();
                String numberStr = "";
                for (String element : result) {
                    if (element.matches("\\d+")) { // Wenn das Element nur aus Zahlen besteht
                        numberStr = element;
                    } else { // Wenn das Element nicht nur aus Zahlen besteht
                        ArrayList<String> subList = new ArrayList<>();
                        subList.add(element);
                        topLevelList.add(subList);
                        if (numberStr != "") {
                            topLevelList.get(topLevelList.size()-1).add(numberStr); // Füge Zahlen zur verschachtelten Liste hinzu
                            numberStr = ""; // Setze die String für Zahlen zurück
                        }
                    }
                }
                Log.d("BonFilter", "zuordnung Stückzahlen:" + topLevelList);

                //wenn len name nur 3 löschen
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).get(0).length()<4) { // && !topLevelList.get(i).get(0).matches(".*[a-z].*")
                        topLevelList.remove(i);
                    }
                }
                Log.d("BonFilter", "Namen < 4 gelöscht:" + topLevelList);

                //bei keiner Anzahl 1
                for (int i = topLevelList.size() - 1; i >= 0; i--) { //In diesem Beispiel beginnt die Schleife mit dem letzten Element in der Liste und geht dann rückwärts bis zum ersten Element. Auf diese Weise wird sichergestellt, dass das Entfernen eines Elements keine Auswirkungen auf die Indizes der folgenden Elemente hat.
                    if (topLevelList.get(i).size()<2){
                        topLevelList.get(i).add("1");
                    } else if (topLevelList.get(i).get(1)=="0") { //Null unzulässig
                        topLevelList.get(i).set(1, "1");
                    }
                }
                Log.d("BonFilter", "Anzahl hinzugefügt:" + topLevelList);
            }
        }
        mainac.adddata(topLevelList);
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