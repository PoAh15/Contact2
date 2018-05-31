package com.example.piero.mypersonalcontacts.presentation.main;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.piero.mypersonalcontacts.R;
import com.example.piero.mypersonalcontacts.model.entity.Contact;

/**
 * Created by piero on 07/05/18.
 */

public class ContactViewHolder extends RecyclerView.ViewHolder {

    private TextView firstName;
    private TextView secondName;
    private TextView number;
    private ImageView contactImage;
    private Uri imageUri;

    public ContactViewHolder(View itemView) {
        super(itemView);
        firstName = itemView.findViewById(R.id.firstName);
        number = itemView.findViewById(R.id.number);
        contactImage = itemView.findViewById(R.id.image);
    }

    public void bind(Contact contact){
        firstName.setText(contact.getFirstName());
        number.setText(contact.getPhone());

        try {
            if(contact.getPictures() != null )
                contactImage.setImageURI(imageUri.parse(contact.getPictures()));
            else contactImage.setImageResource(R.drawable.vd_android);

        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }


}
