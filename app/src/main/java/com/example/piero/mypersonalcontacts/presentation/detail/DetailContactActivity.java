package com.example.piero.mypersonalcontacts.presentation.detail;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.piero.mypersonalcontacts.R;
import com.example.piero.mypersonalcontacts.model.entity.Contact;




public class DetailContactActivity extends AppCompatActivity {

    private static final int  MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private static final String TAG = "DetailContactActivity";
    public static final String EXTRA_CONTACT_ID = TAG + ".contact_id";

    public static void newInstance(Context context, int id){
        Intent intent = new Intent(context, DetailContactActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, id);
        context.startActivity(intent);
    }

    private DetailContactActivityViewModel viewModel;
    private TextView name;
    private TextView surname;
    private TextView phone;
    private TextView email;
    private TextView company;
    private TextView address;
    private ImageView call;
    private ImageView contactImage;
    private Uri imageUri;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int contactId = getIntent().getIntExtra(EXTRA_CONTACT_ID, -1);
        if (contactId == -1){
            throw new IllegalArgumentException("invalid contact id=" + contactId);
        }

        viewModel = ViewModelProviders.of(this)
                .get(DetailContactActivityViewModel.class);

        name = findViewById(R.id.accountName);
        surname = findViewById(R.id.accountSecondName);
        phone = findViewById(R.id.accountPhone);
        email = findViewById(R.id.accountEmail);
        company = findViewById(R.id.accountCompany);
        address = findViewById(R.id.accountAddress);
        call = findViewById(R.id.call);
        contactImage = findViewById(R.id.accountImage);



            viewModel.observeContact(contactId)
                .observe(this, (Contact contact) -> {
                    name.setText(contact.getFirstName());
                    surname.setText(contact.getLastName());
                    phone.setText(contact.getPhone());
                    email.setText(contact.getEmail());
                    company.setText(contact.getCompany());
                    address.setText(contact.getAddress());
                    try {
                        if(contact.getPictures() != null )
                        contactImage.setImageURI(imageUri.parse(contact.getPictures()));
                        else contactImage.setImageDrawable(getDrawable(R.drawable.vd_photo_camera));


                    } catch (Exception e) {
                        e.printStackTrace();
                        if(contactImage == null)contactImage.setImageAlpha(R.drawable.vd_photo_camera);
                    }

                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        call.setOnClickListener(view -> callThatNumber());
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
                return;
            }

        }

    }
}
