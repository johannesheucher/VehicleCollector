package com.jh.vehiclecollector;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @remark Parts taken from "https://developer.android.com/training/camera/photobasics.html"
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button takePictureButton = (Button)findViewById(R.id.button_take_picture);
        takePictureButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        EditText makeText = (EditText)findViewById(R.id.input_make);
        EditText modelText = (EditText)findViewById(R.id.input_model);
        dispatchTakePictureIntent(makeText.getText().toString(), modelText.getText().toString());
    }


    private void dispatchTakePictureIntent(String make, String model) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(make, model);
            } catch (IOException ex) {
                // Error occurred while creating the File
                new AlertDialog.Builder(this).setTitle("IO Error").setMessage("File couldn't be created").show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createImageFile(String make, String model) throws IOException {
        // Create an image file name
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String subDir = "/jh_vehiclecollector";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = make + "_" + model + "_" + timeStamp + "_";
        String suffix = ".png";
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".png",         /* suffix */
//                storageDir      /* directory */
//        );
        File image = new File(storageDir + subDir, imageFileName + suffix);

        File f = new File(storageDir + subDir);
        f.mkdirs();

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
