package com.example.piero.mypersonalcontacts.presentation.addContact;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piero.mypersonalcontacts.R;
import com.example.piero.mypersonalcontacts.model.entity.Contact;
import com.example.piero.mypersonalcontacts.presentation.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by piero on 17/05/18.
 */

public class AddContactActivity extends AppCompatActivity{

    private TextView firstName;
    private TextView number;
    private TextView email;
    private TextView company;
    private TextView address;
    private ImageView takePicture;
    private String uriString;
    private BottomNavigationView bottomBar;

    private  final int MY_PERMISSIONS_REQUEST_GALLERY = 10, MY_PERMISSIONS_REQUEST_CAMERA = 20,MY_PERMISSIONS_REQUEST_WRITE = 30;

    private AddContactViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_contact);

        viewModel = ViewModelProviders.of(this).get(AddContactViewModel.class);


        number = findViewById(R.id.number);
        email = findViewById(R.id.email);
        company = findViewById(R.id.company);
        firstName = findViewById(R.id.firstName);
        address = findViewById(R.id.address);
        takePicture = findViewById(R.id.addPhoto);
        bottomBar = findViewById(R.id.AddBottomBar);

    }




    @Override
    protected void onResume() {
        super.onResume();
        takePicture.setOnClickListener(view -> showPictureDialog());

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save:
                        addContact();
                        return true;

                    case R.id.cancel:
                        cancel();
                        return true;

                }

                return true;
            }
        });

    }



    @Override
    protected void onPause() {
        super.onPause();
        bottomBar.setOnNavigationItemSelectedListener(null);
        takePicture.setOnClickListener(null);
    }


    private void addContact() {

        Contact contact = new Contact(
                firstName.getText().toString(),
                number.getText().toString(),
                email.getText().toString(),
                company.getText().toString(),
                address.getText().toString(),
                uriString

        );
        viewModel.addContact(contact);

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

    }

    private void cancel(){

    Intent goBack = new Intent(this, MainActivity.class);
    startActivity(goBack);


    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog;
        pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            choosePhotoFromGallary();
                            break;
                        case 1:
                            takePhotoFromCamera();
                            break;
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_GALLERY);
        }

        else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent,  MY_PERMISSIONS_REQUEST_GALLERY);
        }
    }

    private void takePhotoFromCamera() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE);
        }

        else {Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_CAMERA);}



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap mediaPhoto =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    takePicture.setImageBitmap(mediaPhoto);
                    uriString = contentURI.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            Bitmap cameraPhoto = (Bitmap) data.getExtras().get("data");
            takePicture.setImageBitmap(cameraPhoto);
            uriString = getImageUri(this,cameraPhoto).toString();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GALLERY: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    choosePhotoFromGallary();
                }
                else {

                }
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        takePhotoFromCamera();
                    }
                    else {

                    }
                }
            case MY_PERMISSIONS_REQUEST_WRITE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    takePhotoFromCamera();
                }
                else {

                }
            }
            }
    }

}