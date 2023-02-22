package com.example.imagepro;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class csvWriting {
    public static void writeCsv(Context context) {
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
            writer.write("Column1,Column2,Column3\n");
            writer.write("Value1,Value2,Value3\n");
            writer.write("Value4,Value5,Value6\n");
            Log.d("csvWriting", "Seeeeeeeehrrrrrrrrr laaaaaaaaaannnnnnnnnggggggeeeeerrrrrrrr tttteeeeeexxxxxxxttttttt");

            // Close the writer and output stream
            writer.close();
            fos.close();
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }
}
