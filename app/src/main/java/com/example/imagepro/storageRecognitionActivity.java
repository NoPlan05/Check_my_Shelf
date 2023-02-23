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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class storageRecognitionActivity extends AppCompatActivity {

    private Button select_image;
    private ImageView image_view;
    private ImageView scan_button;
    private TextView text_view;
    int Selected_Picture=200;

    //TextRecognizer
    private TextRecognizer textRecognizer;

    private String show_image_or_text="image";

    private String resultString;

    private String filteroutput;
    List<List<String>> topLevelList = new ArrayList<>();
    Bitmap bitmap=null;
    MainActivity mainac = new MainActivity();

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
                                    text_view.setText(text.getText());
                                    saveTextAsCSV("Lagerbestand", text);

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

    //public static void saveTextAsCSV() { ? }
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
}