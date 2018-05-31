package com.example.piero.mypersonalcontacts.presentation.detail;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.piero.mypersonalcontacts.R;
import com.example.piero.mypersonalcontacts.model.entity.Contact;
import com.example.piero.mypersonalcontacts.presentation.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class DetailContactActivity extends AppCompatActivity {

    private static final int  MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private static final String TAG = "DetailContactActivity";
    public static final String EXTRA_CONTACT_ID = TAG + ".contact_id";
    private  final int MY_PERMISSIONS_REQUEST_GALLERY = 10, MY_PERMISSIONS_REQUEST_CAMERA = 20,MY_PERMISSIONS_REQUEST_WRITE = 30;

    public static void newInstance(Context context, int id){
        Intent intent = new Intent(context, DetailContactActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, id);
        context.startActivity(intent);
    }

    private DetailContactActivityViewModel viewModel;
    private EditText name;
    private EditText phone;
    private EditText email;
    private EditText company;
    private EditText address;
    private ImageView call;
    private ImageView map;
    private ImageView contactImage;
    private Uri imageUri;
    private String uriString;
    private BottomNavigationView bottomBar;
    private int contactId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

         contactId = getIntent().getIntExtra(EXTRA_CONTACT_ID, -1);
        if (contactId == -1){
            throw new IllegalArgumentException("invalid contact id=" + contactId);
        }


        viewModel = ViewModelProviders.of(this)
                .get(DetailContactActivityViewModel.class);

        name = findViewById(R.id.accountName);
        phone = findViewById(R.id.accountPhone);
        email = findViewById(R.id.accountEmail);
        company = findViewById(R.id.accountCompany);
        address = findViewById(R.id.accountAddress);
        call = findViewById(R.id.call);
        contactImage = findViewById(R.id.accountImage);
        map = findViewById(R.id.show);
        bottomBar = findViewById(R.id.DetailBottomBar);


        name.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        company.setEnabled(false);
        address.setEnabled(false);
        bottomBar.setVisibility(View.INVISIBLE);



            viewModel.observeContact(contactId).observe(this, (Contact contact) -> {
                if(contact == null){
                    Intent goBack = new Intent(this, MainActivity.class);
                    startActivity(goBack);}
                else {
                    name.setText(contact.getFirstName());
                    phone.setText(contact.getPhone());
                    email.setText(contact.getEmail());
                    company.setText(contact.getCompany());
                    address.setText(contact.getAddress());
                    try {
                        if (contact.getPictures() != null){
                            uriString = contact.getPictures();
                            contactImage.setImageURI(imageUri.parse(uriString));}
                        else contactImage.setImageDrawable(getDrawable(R.drawable.vd_photo_camera));


                    } catch (Exception e) {
                        e.printStackTrace();
                        if (contactImage == null)
                            contactImage.setImageAlpha(R.drawable.vd_photo_camera);
                    }
                }

            });
    }


    @Override
    protected void onResume() {
        super.onResume();

        call.setOnClickListener(view -> callThatNumber());
        map.setOnClickListener(view -> showOnTheMap());
    }

    private void showOnTheMap() {

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address.getText().toString()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void callThatNumber() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }

        else{
            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone.getText()));
            startActivity(i);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    callThatNumber();
                } else {

                    call.setOnClickListener(null);
                }
            }

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
                return;
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteContact();
                return true;
            case R.id.modify:
                modify();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void modify() {

        name.setEnabled(true);
        phone.setEnabled(true);
        email.setEnabled(true);
        company.setEnabled(true);
        address.setEnabled(true);
        bottomBar.setVisibility(View.VISIBLE);

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save:
                        saveContact();
                        return true;

                    case R.id.cancel:
                        cancelModification();
                        return true;

                }

                return true;
            }
        });
        contactImage.setOnClickListener(view -> showPictureDialog());



    }

    private void deleteContact() {

        viewModel.deleteContact(contactId);



    }

    private void saveContact(){

        Contact contact = new Contact(name.getText().toString(),
                phone.getText().toString(),
                email.getText().toString(),
                company.getText().toString(),
                address.getText().toString(),
                uriString);

        contact.setId(contactId);

        viewModel.updateContact(contact);

        name.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        company.setEnabled(false);
        address.setEnabled(false);
        bottomBar.setVisibility(View.INVISIBLE);

    }

    private void showPictureDialog() {
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
                    contactImage.setImageBitmap(mediaPhoto);
                    uriString = contentURI.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            Bitmap cameraPhoto = (Bitmap) data.getExtras().get("data");
            contactImage.setImageBitmap(cameraPhoto);
            uriString = getImageUri(this,cameraPhoto).toString();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void cancelModification(){


        viewModel.observeContact(contactId).observe(this,(Contact contact) ->{
            name.setText(contact.getFirstName());
            phone.setText(contact.getPhone());
            email.setText(contact.getEmail());
            company.setText(contact.getCompany());
            address.setText(contact.getAddress());
            try {
                if (contact.getPictures() != null)
                    contactImage.setImageURI(imageUri.parse(contact.getPictures()));
                else contactImage.setImageDrawable(getDrawable(R.drawable.vd_photo_camera));


            } catch (Exception e) {
                e.printStackTrace();
                if (contactImage == null)
                    contactImage.setImageAlpha(R.drawable.vd_photo_camera);
            }

        });

        name.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        company.setEnabled(false);
        address.setEnabled(false);
        bottomBar.setVisibility(View.INVISIBLE);


    }

}
